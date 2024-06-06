package team2.elearningapplication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team2.elearningapplication.Enum.EnumUserStatus;
import team2.elearningapplication.Enum.ResponseCode;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.admin.SetRoleUserRequest;
import team2.elearningapplication.dto.request.admin.SetRoleUserResponse;
import team2.elearningapplication.dto.request.user.authen.*;
import team2.elearningapplication.dto.response.admin.GetUserResponse;
import team2.elearningapplication.dto.response.admin.dashboard.GetTotalUserResponse;
import team2.elearningapplication.dto.response.user.authen.*;
import team2.elearningapplication.entity.User;
import team2.elearningapplication.repository.IUserRepository;
import team2.elearningapplication.security.SecurityUtils;
import team2.elearningapplication.security.UserDetailsImpl;
import team2.elearningapplication.security.jwt.JWTResponse;
import team2.elearningapplication.security.jwt.JWTUtils;
import team2.elearningapplication.service.IUserService;
import team2.elearningapplication.service.implement.PasswordService;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
@Log4j2

public class UserController {

    private IUserService userService;
    private final JWTUtils jwtUtils;
    private final PasswordService passwordService;
    private IUserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    public ResponseEntity<ResponseCommon<CreateUserResponseDTO>> createUser(@Valid @RequestBody CreateUserRequest requestDTO) {
        log.debug("Handle request create user with email {}", requestDTO.getEmail());
        ResponseCommon<CreateUserResponseDTO> responseDTO = userService.createUser(requestDTO);
        if (responseDTO.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(responseDTO);
        } else if(responseDTO.getCode() == ResponseCode.USER_EXIST.getCode()){
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.USER_EXIST.getCode(),"Email already register account",null));
        }
        else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(responseDTO.getCode(),"Resgister fail",null));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ResponseCommon<VerifyOtpResponse>> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        log.debug("Handle verify otp with id {}", request.getEmail());
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        ResponseCommon<VerifyOtpResponse> response = userService.verifyOtp(request);
        if (response.getCode()==ResponseCode.EXPIRED_OTP.getCode()) {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.EXPIRED_OTP.getCode(),"Expried otp",null));
        }
        else  if(response.getCode() == ResponseCode.OTP_INCORRECT.getCode()){

            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.OTP_INCORRECT.getCode(),"OTP incorrect",null));

        } else if(response.getCode() == ResponseCode.SUCCESS.getCode()){
            user.setStatus(EnumUserStatus.ACTIVE);
            userService.updateUser(user);
            return ResponseEntity.ok(response);
        }
        else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(),"verify otp fail",null));
        }
    }

    @PostMapping("/getOTP")
    public ResponseEntity<ResponseCommon<GetOTPResponse>> getOTP(@Valid @RequestBody GetOTPRequest request) {
        ResponseCommon<GetOTPResponse> responseDTO = userService.getOtp(request);
        if (responseDTO.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/resendOTP")
    public ResponseEntity<ResponseCommon<ResendOTPResponse>> resendOTP(@Valid @RequestBody ResendOTPRequest request) {
        ResponseCommon<ResendOTPResponse> responseDTO = userService.resendOTP(request);
        if (responseDTO.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseCommon<JWTResponse>> login(@RequestBody LoginRequest loginRequest) {
        ResponseCommon<JWTResponse> response = userService.login(loginRequest);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else if(response.getCode()==ResponseCode.USER_NOT_FOUND.getCode()){
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.USER_NOT_FOUND.getCode(),"Account not register in system",null));
        } else if(response.getCode() ==ResponseCode.PASSWORD_INCORRECT.getCode()){
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.PASSWORD_INCORRECT.getCode(),"Username or password incorrect",null));
        }
        else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(),"Login fail",null));
        }
    }

    @PostMapping("/send-otp-forgot-password")
    public ResponseEntity<ResponseCommon<ForgotPasswordResponse>> sendOTPForgotPass(@Valid @RequestBody SendOTPForgotPasswordRequest sendOTPForgotPasswordRequest) {
        User user = userRepository.findByEmail(sendOTPForgotPasswordRequest.getEmail()).orElse(null);
        if (Objects.isNull(user)) {
            return new ResponseEntity<>(new ResponseCommon<>(ResponseCode.USER_NOT_FOUND, null), HttpStatus.BAD_REQUEST);
        } else {
            GetOTPRequest request = new GetOTPRequest(sendOTPForgotPasswordRequest.getEmail(), false);
            userService.getOtp(request);
            return new ResponseEntity<>(new ResponseCommon<>(ResponseCode.SUCCESS, null), HttpStatus.OK);
        }
    }

    @PostMapping("/verify-otp-forgotPass")
    public ResponseEntity<ResponseCommon<VerifyOtpResponse>> verifyOtpForgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        User user = userRepository.findByEmail(forgotPasswordRequest.getEmail()).orElse(null);
        if (Objects.isNull(user)) {
            return new ResponseEntity<>(new ResponseCommon<>(ResponseCode.USER_NOT_FOUND, null), HttpStatus.BAD_REQUEST);
        }
        VerifyOtpRequest request = new VerifyOtpRequest(forgotPasswordRequest.getOtp(), user.getEmail());
        ResponseCommon<VerifyOtpResponse> response = userService.verifyOtp(request);
        if(response.getCode()==ResponseCode.EXPIRED_OTP.getCode()){
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.EXPIRED_OTP.getCode(),"Expried otp",null));
        } else if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            String hassPass = passwordService.hashPassword(forgotPasswordRequest.getPassword());
            user.setPassword(hassPass);
            userService.updateUser(user);
            return ResponseEntity.ok(response);
        }
        else if(response.getCode() == ResponseCode.OTP_INCORRECT.getCode()){
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.OTP_INCORRECT.getCode(),"OTP incorrect",null));
        }
        else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/changePassword")
    public ResponseEntity<ResponseCommon<ChangePasswordResponse>> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        ResponseCommon<ChangePasswordResponse> response = userService.changePassword(changePasswordRequest);
        String username = SecurityUtils.getUsernameAuth();
        System.out.println(username);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        }
        else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/refresh-access-token")
    public ResponseEntity<JWTResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (refreshToken.isEmpty() || refreshToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            UserDetailsImpl userDetails = UserDetailsImpl.build(user);
            String accessToken = jwtUtils.generateAccessToken(userDetails);
            String newRefreshToken = jwtUtils.generateRefreshToken(userDetails);
            JWTResponse response = new JWTResponse();
            response.setAccessToken(accessToken);
            response.setRefreshToken(newRefreshToken);
            return ResponseEntity.ok(response);
        }
    }
    @Operation(
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/change-profile")
    public ResponseEntity<ResponseCommon<ChangeProfileResponse>> changeProfile(@Valid @RequestBody ChangeProfileRequest changeProfileRequest) {
        ResponseCommon<ChangeProfileResponse> response = userService.changeProfile(changeProfileRequest);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            logger.info("Change profile success");
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.USER_NOT_FOUND.getCode()) {
            logger.error("User not found");
            return ResponseEntity.status(404).body(new ResponseCommon<>(response.getCode(), "User not found", null));
        } else if (response.getCode() == ResponseCode.INVALID_DATA.getCode()) {
            logger.error("Invalid data");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Invalid data", null));
        } else {
            logger.error("Change profile failed");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Change profile failed", null));
        }
    }

    @GetMapping("/get-user-by-email")
    public ResponseEntity<ResponseCommon<GetUserByEmailResponse>> getUserByEmail(GetUserByEmailRequest request) {
        ResponseCommon<GetUserByEmailResponse> response = userService.getUserByEmail(request);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            log.debug("Get user by email successfully.");
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.USER_NOT_FOUND.getCode()) {
            log.debug("User not exist.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "User not exist", null));
        } else {
            log.error("Get user by email failed");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get user by email failed", null));
        }
    }

    @PostMapping("/log-out")
    public ResponseEntity<ResponseCommon<LogOutResponse>> logOut(LogOutRequest logOutRequest){
        ResponseCommon<LogOutResponse> response = userService.logOut(logOutRequest);
        if(response.getCode()==ResponseCode.FAIL.getCode()){
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(),"LogOut fail",null));
        } else {
            return ResponseEntity.ok().body(new ResponseCommon<>(ResponseCode.SUCCESS.getCode(),"LogOut success",response.getData()));
        }
    }

    @GetMapping("/total-user")
    public ResponseEntity<ResponseCommon<GetTotalUserResponse>> getTotalUser(){
        ResponseCommon<GetTotalUserResponse> response = userService.getTotalUser();
        if(response.getCode()==ResponseCode.FAIL.getCode()){
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(),"Get total user  fail",null));
        } else {
            return ResponseEntity.ok().body(new ResponseCommon<>(ResponseCode.SUCCESS.getCode(),"Get total user success",response.getData()));
        }
    }
    @GetMapping("/get-user-by-username")
    public ResponseEntity<ResponseCommon<GetUserByUsernameResponse>> getUserByUsername(@Valid @ParameterObject GetUserByUsernameRequest getUserByUsernameRequest){
        ResponseCommon<GetUserByUsernameResponse> response = userService.getUserByUsername(getUserByUsernameRequest);
        if(response.getCode() == ResponseCode.USER_NOT_FOUND.getCode()){
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.USER_NOT_FOUND,null));
        } else if(response.getCode()==ResponseCode.FAIL.getCode()){
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL,null));
        } else {
            return ResponseEntity.ok().body(new ResponseCommon<>(ResponseCode.SUCCESS,response.getData()));
        }
    }

    @PostMapping("/set-role-user")
    public ResponseEntity<ResponseCommon<SetRoleUserResponse>> setRoleUser(@RequestBody SetRoleUserRequest setRoleUserRequest) {
        try {
            ResponseCommon<SetRoleUserResponse> response = userService.setRole(setRoleUserRequest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseCommon<>(ResponseCode.FAIL, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/get-all-user")
    public ResponseEntity<ResponseCommon<GetUserResponse>> getUsers() {
        try {
            ResponseCommon<GetUserResponse> response = userService.getUser();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseCommon<>(ResponseCode.FAIL, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
