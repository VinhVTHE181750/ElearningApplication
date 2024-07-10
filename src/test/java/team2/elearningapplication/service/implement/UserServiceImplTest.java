package team2.elearningapplication.service.implement;

import io.swagger.v3.oas.annotations.Parameter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.yaml.snakeyaml.Yaml;
import team2.elearningapplication.Enum.ResponseCode;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.user.authen.CreateUserRequest;
import team2.elearningapplication.dto.request.user.authen.GetUserByEmailRequest;
import team2.elearningapplication.dto.request.user.authen.GetUserByUsernameRequest;
import team2.elearningapplication.dto.response.admin.answer.GetAnswerByIdResponse;
import team2.elearningapplication.dto.response.user.authen.GetUserByUsernameResponse;
import team2.elearningapplication.entity.User;
import team2.elearningapplication.repository.IUserRepository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


class UserServiceImplTest extends Mockito{

    private final ResponseCommon<GetUserByUsernameResponse> GETUSERFAIL = new ResponseCommon<>(ResponseCode.FAIL, null);
    private final ResponseCommon<GetUserByUsernameResponse> GETUSEREXIST = new ResponseCommon<>(ResponseCode.USER_EXIST, null);
    private static ArrayList<User> users;

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    IUserRepository userRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeAll
    static void setUpAll() {
        Yaml yaml = new Yaml();
        try (InputStream in = AnswerServiceImplTest.class.getClassLoader().getResourceAsStream("data.yml")) {
            Map<String, Object> yamlData = yaml.load(in);
            users = new ArrayList<User>();
            List<Map<String, Object>> yamlUsers = (List<Map<String, Object>>) yamlData.get("users");
            for (Map<String, Object> yamlUser : yamlUsers) {
                User user = new User();
                user.setId((Integer) yamlUser.get("id"));
                user.setUsername((String) yamlUser.get("username"));
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    void getUserByUsername(GetUserByUsernameRequest request, ResponseCommon<GetUserByUsernameResponse> expectedResponse) {

        final String username = request.getUsername();
        reset(userRepository);
        when(userRepository.findByUsername(username)).then(invocation -> {
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    return Optional.of(user);
                }
            }
            return Optional.empty();

        });
        var response = userService.getUserByUsername(request);
        verify(userRepository, times(1)).findByUsername(username);
        assertNotNull(response);
        assertEquals(expectedResponse.getCode(), response.getCode());
        assertEquals(expectedResponse.getMessage(), response.getMessage());
    }
    void genUserByEmail(String email) {

        String response = userService.genUserFromEmail(email);
        assertNotNull(response);
        assertTrue(validateGenUserFromEmail(email, response));
    }
    private boolean validateGenUserFromEmail(String email, String response) {
        String username = email.split("@")[0];
        if (!response.startsWith(username)) return false;
        if (response.length() != username.length() + 6) return false;
        String lastSixChars = response.substring(response.length() - 6);
        return lastSixChars.matches("\\d{6}");
    }

    private ResponseCommon<GetUserByUsernameResponse> expectedGetResponse(User user) {
        GetUserByUsernameResponse expectedGetResponse = new GetUserByUsernameResponse();
        User userResponse = new User();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setRole(user.getRole());
        userResponse.setEmail(user.getEmail());
        expectedGetResponse.setUser(userResponse);

        return new ResponseCommon<>(expectedGetResponse);
    }

    static Stream<String> provideUsernames(){
        Yaml yaml = new Yaml();
        List<String> listUsernames = new ArrayList<>();
        try (InputStream in = AnswerServiceImplTest.class.getClassLoader().getResourceAsStream("test-cases.yml")) {
            Map<String, Object> yamlData = yaml.load(in);
            Map<String, List<String>> yamlTestCases = (Map<String, List<String>>) yamlData.get("getUserByUsernames");
            listUsernames = yamlTestCases.get("usernames");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listUsernames.stream();
    }

    @ParameterizedTest
    @MethodSource("provideUsernames")
    void getUserByUsername(String username) {
        GetUserByUsernameRequest request = new GetUserByUsernameRequest();
        request.setUsername(username);
        User user = getUser(username);
        if (user != null) {
            getUserByUsername(request, expectedGetResponse(user));

        } else {
            getUserByUsername(request, GETUSERFAIL);
        }
    }


    @Test
    void createUser(String username, String password) {
        CreateUserRequest request = new CreateUserRequest();
        


    }
    //data email save in test-cases.yml.genUserFromEmail.email
    static Stream<String> provideEmails(){
        Yaml yaml = new Yaml();
        List<String> listEmails = new ArrayList<>();
        try (InputStream in = AnswerServiceImplTest.class.getClassLoader().getResourceAsStream("test-cases.yml")) {
            Map<String, Object> yamlData = yaml.load(in);
            Map<String, List<String>> yamlTestCases = (Map<String, List<String>>) yamlData.get("genUserFromEmail");
            listEmails = yamlTestCases.get("email");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listEmails.stream();
    }


    @ParameterizedTest
    @MethodSource("provideEmails")
    @Test
    void genUserFromEmail(String email) {
        genUserByEmail(email);
    }
}