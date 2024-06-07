package team2.elearningapplication.service.implement;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team2.elearningapplication.Enum.EnumUserStatus;
import team2.elearningapplication.Enum.ResponseCode;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.admin.SetRoleUserRequest;
import team2.elearningapplication.dto.request.admin.SetRoleUserResponse;
import team2.elearningapplication.dto.request.user.authen.*;
import team2.elearningapplication.dto.response.admin.GetUserResponse;
import team2.elearningapplication.dto.response.admin.dashboard.GetTotalUserResponse;
import team2.elearningapplication.dto.response.user.authen.*;
import team2.elearningapplication.entity.Mail;
import team2.elearningapplication.entity.User;
import team2.elearningapplication.repository.IUserRepository;
import team2.elearningapplication.security.SecurityUtils;
import team2.elearningapplication.security.UserDetailsImpl;
import team2.elearningapplication.security.jwt.JWTResponse;
import team2.elearningapplication.security.jwt.JWTUtils;
import team2.elearningapplication.service.IUserService;
import team2.elearningapplication.service.email.EmailService;
import team2.elearningapplication.utils.CommonUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private static final String START_MAIL = START_MAIL;
    private static final String END_MAIL = END_MAIL;
    private static final String VALUE_OF_EXPIRED = "Value of expired {}";
    private final IUserRepository userRepository;
    private final EmailService emailService;
    private final PasswordService passwordService;
    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${mail.from}")
    private String mailFrom;
    @Value("${otp.valid.minutes}")
    private int otpValid;

    @Override
    public ResponseCommon<GetUserByUsernameResponse> getUserByUsername(GetUserByUsernameRequest getUserByUsernameRequest) {
        try {
            User user = userRepository.findByUsername(getUserByUsernameRequest.getUsername()).orElse(null);
            if (Objects.isNull(user)) {
                return new ResponseCommon<>(ResponseCode.USER_EXIST, null);
            } else {
                GetUserByUsernameResponse getUserByUsernameResponse = new GetUserByUsernameResponse();
                getUserByUsernameResponse.setUser(user);
                return new ResponseCommon<>(ResponseCode.SUCCESS, getUserByUsernameResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public String genUserFromEmail(String email) {
        String username = email.substring(0, email.indexOf("@"));
        Random random = new Random();
        StringBuilder randomNumber = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10); // Số ngẫu nhiên từ 0 đến 9
            randomNumber.append(digit);
        }
        return username + randomNumber;
    }

    @Override
    public ResponseCommon<CreateUserResponseDTO> createUser(CreateUserRequest requestDTO) {
        try {
            User user = userRepository.findByEmail(requestDTO.getEmail()).orElse(null);
            if (Objects.nonNull(user) && user.getStatus() != EnumUserStatus.IN_PROCESS) {
                return new ResponseCommon<>(ResponseCode.USER_EXIST, null);
            }
            // if user is null -> new user
            log.debug("check user is null{}", user);
            if (Objects.isNull(user)) {
                user = new User();
            }
            user.setUsername(genUserFromEmail(requestDTO.getEmail()));
            String hassPass = passwordService.hashPassword(requestDTO.getPassword());
            user.setPassword(hassPass);
            user.setEmail(requestDTO.getEmail());
            user.setPhone(requestDTO.getPhone());
            user.setRole(requestDTO.getRole());
            user.setFullName(requestDTO.getFullName());
            user.setGender(requestDTO.getGender());
            user.setDate_of_birth(requestDTO.getDateOfBirth());
            LocalDateTime localDateTime = LocalDateTime.now();
            LocalDateTime expired = localDateTime.plusMinutes(otpValid);
            user.setStatus(EnumUserStatus.IN_PROCESS);
            log.debug(VALUE_OF_EXPIRED, expired);
            user.setExpiredOTP(expired);
            user.setCreatedAt(LocalDateTime.now());
            user.setOtp(CommonUtils.getOTP());
            User createdUser = userRepository.save(user);
            log.info(START_MAIL);
            emailService.sendEmail(setUpMail(user.getEmail(), user.getOtp()));
            log.info(END_MAIL);
            CreateUserResponseDTO responseDTO = new CreateUserResponseDTO();
            responseDTO.setUsername(createdUser.getUsername());
            responseDTO.setEmail(createdUser.getEmail());
            responseDTO.setCreatedAt(createdUser.getCreatedAt());

            return new ResponseCommon<>(ResponseCode.SUCCESS, responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    private Mail setUpMail(String mailTo, String otp) {
        Mail mail = new Mail();
        mail.setFrom(mailFrom);
        mail.setTo(mailTo);
        mail.setSubject("OTP ELEARNING APPLICATION!");
        Map<String, Object> model = new HashMap<>();
        model.put("otp_value", otp);
        mail.setPros(model);
        mail.setTemplate("index");
        return mail;
    }

    @Override
    public User updateUser(User user) {
        try {
            Optional<User> existingUser = userRepository.findById(user.getId());
            if (existingUser.isPresent()) {
                User updatedUser = existingUser.get();
                updatedUser.setUsername(user.getUsername());
                updatedUser.setPassword(user.getPassword());
                updatedUser.setEmail(user.getEmail());
                updatedUser.setPhone(user.getPhone());
                updatedUser.setRole(user.getRole());
                updatedUser.setCreatedAt(user.getCreatedAt());
                updatedUser.setFullName(user.getFullName());
                updatedUser.setGender(user.getGender());
                updatedUser.setDate_of_birth(user.getDate_of_birth());
                updatedUser.setUpdatedAt(LocalDateTime.now());
                return userRepository.save(updatedUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public ResponseCommon<GetOTPResponse> getOtp(GetOTPRequest request) {
        try {
            User user = userRepository.findByEmailAndStatus(request.getEmail(), EnumUserStatus.ACTIVE).orElse(null);
            // if  user is null ->throw error
            if (Objects.isNull(user)) {
                return new ResponseCommon<>(ResponseCode.USER_NOT_FOUND, null);
            }
            // step1: gen otp
            // if otp of user expried
            LocalDateTime localDateTime = LocalDateTime.now();
            String otp = CommonUtils.getOTP();
            //step2: send email
            log.info(START_MAIL);
            emailService.sendEmail(setUpMail(user.getEmail(), otp));
            log.info(END_MAIL);
            if (request.isCreate()) {
                user.setStatus(EnumUserStatus.IN_PROCESS);
            }
            LocalDateTime expired = localDateTime.plusMinutes(otpValid);
            log.debug(VALUE_OF_EXPIRED, expired);
            user.setExpiredOTP(expired);
            user.setOtp(otp);
            GetOTPResponse response = new GetOTPResponse(user.getUsername(), user.getEmail());
            return new ResponseCommon<>(ResponseCode.SUCCESS, response);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }


    @Override
    public ResponseCommon<JWTResponse> login(LoginRequest loginRequest) {
        try {
            Optional<User> user = userRepository.findByEmail(loginRequest.getUsername());
            // if username request not found in database -> tell user
            if (user.isEmpty()) {
                return new ResponseCommon<>(ResponseCode.USER_NOT_FOUND, null);
            } // else -> check password
            else {
                String hashPass = passwordService.hashPassword(loginRequest.getPassword());
                // if password not equals password in database -> return fail
                if (!user.orElse(null).getPassword().equals(hashPass)) {
                    return new ResponseCommon<>(ResponseCode.PASSWORD_INCORRECT, null);
                } // else -> verify otp
                else {
                    JWTUtils utils = new JWTUtils();
                    UserDetailsImpl userDetails = UserDetailsImpl.build(user.get());
                    String accessToken = utils.generateAccessToken(userDetails);
                    String refreshToken = utils.generateRefreshToken(userDetails);
                    user.orElse(null).setSession_id(CommonUtils.getSessionID());
                    userRepository.save(user.get());
                    return new ResponseCommon<>(new JWTResponse(accessToken, refreshToken, ResponseCode.SUCCESS.getMessage()));
//
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }


    @Override
    public ResponseCommon<VerifyOtpResponse> verifyOtp(VerifyOtpRequest verifyOtpRequest) {
        try {
            User user = userRepository.findByEmail(verifyOtpRequest.getEmail()).orElse(null);

            if (Objects.isNull(user)) {
                return new ResponseCommon<>(ResponseCode.USER_NOT_FOUND, null);
            }

            LocalDateTime localDateTime = LocalDateTime.now();
            LocalDateTime expiredOTP = user.getExpiredOTP();
            String otp = user.getOtp();

            if (localDateTime.isAfter(expiredOTP)) {
                // Trường hợp OTP đã hết hạn
                return new ResponseCommon<>(ResponseCode.EXPIRED_OTP, null);
            } else if (verifyOtpRequest.getOtp().equals(otp)) {
                // Trường hợp OTP đúng
                return new ResponseCommon<>(ResponseCode.SUCCESS, null);
            } else {
                // Trường hợp OTP sai
                return new ResponseCommon<>(ResponseCode.OTP_INCORRECT, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }


    @Override
    public ResponseCommon<ChangePasswordResponse> changePassword(ChangePasswordRequest changePasswordRequest) {
        try {
            String username = SecurityUtils.getUsernameAuth();
            log(username);
            User user = userRepository.findByUsername(username).orElse(null);
            // if user is null -> tell error
            log.debug("change passsword with username{}", username);
            if (Objects.isNull(user)) {
                return new ResponseCommon<>(ResponseCode.USER_NOT_FOUND, null);
            } else {
                String hassPass = passwordService.hashPassword(changePasswordRequest.getNewPassword());
                // if oldPassword not correct -> tell user
                if (!passwordService.hashPassword(changePasswordRequest.getOldPassword()).equals(user.getPassword())) {
                    return new ResponseCommon<>(ResponseCode.PASSWORD_INCORRECT, null);
                } else {
                    user.setPassword(hassPass);
                    userRepository.save(user);
                    return new ResponseCommon<>(ResponseCode.SUCCESS, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseCommon<>(new ChangePasswordResponse("Error"));
        }
    }

    @Override
    public ResponseCommon<ChangeProfileResponse> changeProfile(ChangeProfileRequest changeProfileRequest) {
        try {
            User user = userRepository.findByUsername(changeProfileRequest.getUsername()).orElse(null);
            if (user == null) {
                log.debug("User not found for email: {}", changeProfileRequest.getUsername());
                return new ResponseCommon<>(ResponseCode.USER_NOT_FOUND, null);
            }

            user.setFullName(changeProfileRequest.getFullName());
            user.setPhone(changeProfileRequest.getPhoneNum());
            user.setGender(changeProfileRequest.getGender());
            user.setDate_of_birth(changeProfileRequest.getDateOfBirth());

            userRepository.save(user);

            ChangeProfileResponse changeProfileResponse = new ChangeProfileResponse();
            changeProfileResponse.setFullName(user.getFullName());
            changeProfileResponse.setPhoneNum(user.getPhone());
            changeProfileResponse.setGender(user.getGender());

            log.debug("User profile updated successfully for email: {}", changeProfileRequest.getUsername());
            return new ResponseCommon<>(ResponseCode.SUCCESS, changeProfileResponse);
        } catch (Exception e) {
            log.error("Error while updating user profile for email: {}", changeProfileRequest.getUsername(), e);
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<GetUserByEmailResponse> getUserByEmail(GetUserByEmailRequest getUserByEmailRequest) {
        try {
            User user = userRepository.findByEmail(getUserByEmailRequest.getEmail()).orElse(null);
            // If user in database not exist -> tell user
            if (Objects.isNull(user)) {
                log.debug("User not exist");
                return new ResponseCommon<>(ResponseCode.USER_NOT_FOUND.getCode(), "User not exist", null);
            } else {
                GetUserByEmailResponse response = new GetUserByEmailResponse();

                response.setId(user.getId());
                response.setUsername(user.getUsername());
                response.setEmail(user.getEmail());
                response.setPhone(user.getPhone());
                response.setRole(user.getRole());
                response.setCreatedAt(user.getCreatedAt());
                response.setFullName(user.getFullName());
                response.setGender(user.getGender());
                response.setDate_of_birth(user.getDate_of_birth());
                response.setStatus(user.getStatus());

                log.debug("Get user by email successfully");
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Get user by email success", response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Get user by email failed");
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get user by email failed", null);
        }
    }

    @Override
    public ResponseCommon<LogOutResponse> logOut(LogOutRequest logOutRequest) {
        try {
            User user = userRepository.findByUsername(logOutRequest.getUsername()).orElse(null);
            if (!Objects.isNull(user)) {
                user.setSession_id(null);
                userRepository.save(user);
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Logout successful", null);
            } else {
                return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Logout failed", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Log out failed");
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Log out failed", null);
        }
    }

    @Override
    public ResponseCommon<GetTotalUserResponse> getTotalUser() {
        try {
            int totalUser = userRepository.getTotalUser();
            GetTotalUserResponse getTotalUserResponse = new GetTotalUserResponse(totalUser);
            return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Get total user success", getTotalUserResponse);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Get total user failed");
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get total user failed", null);
        }
    }

    @Override
    public ResponseCommon<ResendOTPResponse> resendOTP(ResendOTPRequest request) {
        try {
            User user = userRepository.findByEmail(request.getEmail()).orElse(null);
            LocalDateTime localDateTime = LocalDateTime.now();
            String otp = CommonUtils.getOTP();
            //step2: send email
            log.info(START_MAIL);
            assert user != null;
            emailService.sendEmail(setUpMail(user.getEmail(), otp));
            log.info(END_MAIL);
            user.setUsername(genUserFromEmail(request.getEmail()));

            LocalDateTime expired = localDateTime.plusMinutes(otpValid);
            log.debug(VALUE_OF_EXPIRED, expired);
            user.setExpiredOTP(expired);
            user.setOtp(otp);
            userRepository.save(user);
            return new ResponseCommon<>(ResponseCode.SUCCESS, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<SetRoleUserResponse> setRole(SetRoleUserRequest setRoleUserRequest) {
        try {
            User user = userRepository.findByUsername(setRoleUserRequest.getUsername()).orElse(null);
            assert user != null;
            user.setRole(setRoleUserRequest.getTypeRole());
            userRepository.save(user);
            SetRoleUserResponse setRoleUserResponse = new SetRoleUserResponse();
            setRoleUserResponse.setUsername(user.getUsername());
            setRoleUserResponse.setTypeRole(user.getRole());
            return new ResponseCommon<>(ResponseCode.SUCCESS, setRoleUserResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<GetUserResponse> getUser() {
        try {
            List<User> users = userRepository.findAll();
            GetUserResponse getUserResponse = new GetUserResponse();
            getUserResponse.setUserList(users);
            return new ResponseCommon<>(ResponseCode.SUCCESS, getUserResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }
}
