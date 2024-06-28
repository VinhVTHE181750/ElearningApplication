package team2.elearningapplication.service.implement;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void createUser() {
    }

    @Test
    void updateUser() {
    }

    void getUserByUsername() {

    }
}