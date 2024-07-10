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
import org.yaml.snakeyaml.Yaml;
import team2.elearningapplication.Enum.ResponseCode;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.admin.answer.AnswerData;
import team2.elearningapplication.dto.request.admin.answer.DeleteAnswerRequest;
import team2.elearningapplication.dto.request.admin.answer.GetAnswerByIdRequest;
import team2.elearningapplication.dto.request.admin.answer.UpdateAnswerRequest;
import team2.elearningapplication.dto.response.admin.answer.AddAnswerResponse;
import team2.elearningapplication.dto.response.admin.answer.DeleteAnswerResponse;
import team2.elearningapplication.dto.response.admin.answer.GetAnswerByIdResponse;
import team2.elearningapplication.dto.response.admin.answer.UpdateAnswerResponse;
import team2.elearningapplication.entity.Answer;
import team2.elearningapplication.entity.Question;
import team2.elearningapplication.entity.User;
import team2.elearningapplication.repository.IAnswerRepository;
import team2.elearningapplication.repository.IQuestionRepository;
import team2.elearningapplication.repository.IUserRepository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AnswerServiceImplTest extends Mockito {

    // responses
    private final ResponseCommon<GetAnswerByIdResponse> ANSWER_NOT_EXIST_GET = new ResponseCommon<>(ResponseCode.ANSWER_NOT_EXIST.getCode(), "Answer not exist", null);
    private final ResponseCommon<DeleteAnswerResponse> ANSWER_NOT_EXIST_DELETE = new ResponseCommon<>(ResponseCode.ANSWER_NOT_EXIST.getCode(), "Answer not exist in question", null);
    private final ResponseCommon<GetAnswerByIdResponse> GET_EXCEPTION = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get answer fail", null);
    private final ResponseCommon<DeleteAnswerResponse> DELETE_ANSWER_FAIL = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Delete answer fail", null);
    private final ResponseCommon<AddAnswerResponse> QUESTION_NOT_EXIST = new ResponseCommon<AddAnswerResponse>(ResponseCode.QUESTION_NOT_EXIST.getCode(), "Question not exist, cannot add answer to question", null);
    private final ResponseCommon<AddAnswerResponse> ADD_EXCEPTION = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add answer fail", null);
    private final ResponseCommon<UpdateAnswerResponse> ANSWER_NOT_EXIST_UPDATE = new ResponseCommon<>(ResponseCode.ANSWER_NOT_EXIST.getCode(), "Answer not exist in question", null);

    // response of deleteAnswer

    // mock list to replace database
    private static ArrayList<Answer> answers;
    private static ArrayList<Question> questions;
    private static ArrayList<User> users;

    // mock service
    @InjectMocks
    AnswerServiceImpl answerService;

    @Mock
    IAnswerRepository answerRepository;

    @Mock
    IQuestionRepository questionRepository;

    @Mock
    IUserRepository userRepository;

    // test setup methods
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setData() {
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

            questions = new ArrayList<Question>();
            List<Map<String, Object>> yamlQuestions = (List<Map<String, Object>>) yamlData.get("questions");
            for (Map<String, Object> yamlQuestion : yamlQuestions) {
                Question question = new Question();
                question.setId((Integer) yamlQuestion.get("id"));
                question.setQuestionName((String) yamlQuestion.get("questionName"));
                questions.add(question);
            }

            answers = new ArrayList<Answer>();
            List<Map<String, Object>> yamlAnswers = (List<Map<String, Object>>) yamlData.get("answers");
            for (Map<String, Object> yamlAnswer : yamlAnswers) {
                Answer answer = new Answer();
                answer.setId((Integer) yamlAnswer.get("id"));
                answer.setAnswerContent((String) yamlAnswer.get("answerContent"));
                answer.setUserCreated(users.get((int) yamlAnswer.get("createdBy")));
                answer.setDeleted((Boolean) yamlAnswer.get("deleted"));
                answer.setUserUpdated(users.get((int) yamlAnswer.get("updatedBy")));
                answer.setCorrect((Boolean) yamlAnswer.get("correct"));
                answer.setQuestionId((Integer) yamlAnswer.get("questionId"));
                answers.add(answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // provide test cases
    static Stream<Integer> provideIDs() {
        Yaml yaml = new Yaml();
        List<Integer> listIDs = new ArrayList<>();
        try (InputStream in = AnswerServiceImplTest.class.getClassLoader().getResourceAsStream("test-cases.yml")) {
            Map<String, Object> yamlData = yaml.load(in);
            Map<String, List<Integer>> yamlTestCases = (Map<String, List<Integer>>) yamlData.get("getAnswerById");
            listIDs = yamlTestCases.get("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listIDs.stream();
    }

    static Stream<Integer> provideQuestionIDs() {
        Yaml yaml = new Yaml();
        List<Integer> listQIDs = new ArrayList<>();
        try (InputStream in = AnswerServiceImplTest.class.getClassLoader().getResourceAsStream("test-cases.yml")) {
            Map<String, Object> yamlData = yaml.load(in);
            Map<String, List<Integer>> yamlTestCases = (Map<String, List<Integer>>) yamlData.get("addAnswer");
            listQIDs = yamlTestCases.get("questionID");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listQIDs.stream();
    }

    static Stream<Boolean> provideCorrect() {
        Yaml yaml = new Yaml();
        List<Boolean> listCorrect = new ArrayList<>();
        try (InputStream in = AnswerServiceImplTest.class.getClassLoader().getResourceAsStream("test-cases.yml")) {
            Map<String, Object> yamlData = yaml.load(in);
            Map<String, List<Boolean>> yamlTestCases = (Map<String, List<Boolean>>) yamlData.get("addAnswer");
            listCorrect = yamlTestCases.get("correct");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listCorrect.stream();
    }

    static Stream<String> provideAnswerName() {
        Yaml yaml = new Yaml();
        List<String> listAnswerName = new ArrayList<>();
        try (InputStream in = AnswerServiceImplTest.class.getClassLoader().getResourceAsStream("test-cases.yml")) {
            Map<String, Object> yamlData = yaml.load(in);
            Map<String, List<String>> yamlTestCases = (Map<String, List<String>>) yamlData.get("addAnswer");
            listAnswerName = yamlTestCases.get("answerName");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listAnswerName.stream();
    }

    static Stream<String> provideUsername() {
        Yaml yaml = new Yaml();
        List<String> listUsername = new ArrayList<>();
        try (InputStream in = AnswerServiceImplTest.class.getClassLoader().getResourceAsStream("test-cases.yml")) {
            Map<String, Object> yamlData = yaml.load(in);
            Map<String, List<String>> yamlTestCases = (Map<String, List<String>>) yamlData.get("addAnswer");
            listUsername = yamlTestCases.get("username");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listUsername.stream();
    }


    // base test method for getAnswerById()
    void getAnswerById(GetAnswerByIdRequest request, ResponseCommon<GetAnswerByIdResponse> expectedResponse) {

        // call stack: answerService.getAnswerById(request) -> answerRepository.findAnswerById(id) -> answers.get(id)
        final int id = request.getId();

        reset(answerRepository);

        when(answerRepository.findAnswerById(id)).then(invocation -> {
            if (id > 0 && id <= answers.size()) {
                // db answer id start with 1, but array index start with 0, offsetting by 1
                return Optional.of(answers.get(id - 1));
            } else {
                return Optional.empty();
            }
        });


        var response = answerService.getAnswerById(request);

        // verify that the mocked findAnswerById method is called once
        verify(answerRepository, times(1)).findAnswerById(id);

        // has response
        assertNotNull(response);

        // code
        assertEquals(expectedResponse.getCode(), response.getCode());

        // message
        assertEquals(expectedResponse.getMessage(), response.getMessage());

        // data: Answer
        if (response.getData() != null && expectedResponse.getData() != null) {
            GetAnswerByIdResponse expectedData = (GetAnswerByIdResponse) expectedResponse.getData();
            GetAnswerByIdResponse actualData = (GetAnswerByIdResponse) response.getData();

            assertEquals(expectedData.getId(), actualData.getId());
            assertEquals(expectedData.getAnswerContent(), actualData.getAnswerContent());
            assertEquals(expectedData.isDeleted(), actualData.isDeleted());
            assertEquals(expectedData.isCorrect(), actualData.isCorrect());
            assertEquals(expectedData.getQuestionId(), actualData.getQuestionId());
            assertEquals(expectedData.getCreatedBy(), actualData.getCreatedBy());
            assertEquals(expectedData.getUpdatedBy(), actualData.getUpdatedBy());
        }

    }

    void deleteAnswer(DeleteAnswerRequest request, ResponseCommon<DeleteAnswerResponse> expectedResponse) {

        // call stack: answerService.getAnswerById(request) -> answerRepository.findAnswerById(id) -> answers.get(id)
        final int answerID = request.getAnswerID();
        final int questionID = request.getQuestionID();
        final String username = request.getUsername();

        reset(answerRepository);
        reset(userRepository);

        ArrayList<Answer> answers2 = new ArrayList<>();
        for (Answer answer : answers) {
            answers2.add(answer);
        }

        when(answerRepository.findAnswerByQuestionIdAndId(questionID, answerID)).then(invocation -> {
            System.out.println("findAnswerByQuestionIdAndId " + questionID + " " + answerID);
            for (Answer answer : answers2) {
                if (answer.getQuestionId() == questionID && answer.getId() == answerID) {
                    return Optional.of(answer);
                }
            }
            return Optional.empty();
        });

        when(userRepository.findByUsername(username)).then(invocation -> {
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    return Optional.of(user);
                }
            }
            return Optional.empty();
        });

        var response = answerService.deleteAnswer(request);

        // verify that the mocked findAnswerById method is called once
        verify(answerRepository, times(1)).findAnswerByQuestionIdAndId(questionID, answerID);
        verify(userRepository, times(1)).findByUsername(username);

        // has response
        assertNotNull(response);

        // code
        assertEquals(expectedResponse.getCode(), response.getCode());

        // message
        assertEquals(expectedResponse.getMessage(), response.getMessage());

        // data: Answer
        if (response.getData() != null && expectedResponse.getData() != null) {

            DeleteAnswerResponse expectedData = expectedResponse.getData();
            DeleteAnswerResponse actualData = response.getData();

            assertEquals(expectedData.getAnswerID(), actualData.getAnswerID());
            assertEquals(expectedData.getAnswerContent(), actualData.getAnswerContent());
            assertEquals(expectedData.getQuestionID(), actualData.getQuestionID());
            assertEquals(expectedData.isCorrect(), actualData.isCorrect());
            assertEquals(expectedData.getCreatedBy(), actualData.getCreatedBy());
            assertEquals(expectedData.getUpdatedBy(), actualData.getUpdatedBy());
        }

    }


    // generate expected response for getAnswerById()
    private ResponseCommon<GetAnswerByIdResponse> expectedGetResponse(Answer answer) {
        GetAnswerByIdResponse expectedGetResponse = new GetAnswerByIdResponse();

        expectedGetResponse.setId(answer.getId());
        expectedGetResponse.setAnswerContent(answer.getAnswerContent());
        expectedGetResponse.setDeleted(answer.isDeleted());
        expectedGetResponse.setCorrect(answer.isCorrect());
        expectedGetResponse.setQuestionId(answer.getQuestionId());
        expectedGetResponse.setCreatedBy(answer.getUserCreated().getUsername());
        expectedGetResponse.setUpdatedBy(answer.getUserUpdated().getUsername());

        return new ResponseCommon<>(expectedGetResponse);
    }


    // parameterized test method for getAnswerById()
    @ParameterizedTest
    @MethodSource("provideIDs")
    void getAnswerByID(int id) {
        GetAnswerByIdRequest request = new GetAnswerByIdRequest();
        request.setId(id);
        if (id < 1 || id > answers.size()) {
            getAnswerById(request, ANSWER_NOT_EXIST_GET);
        } else {
            Answer answer = answers.get(id - 1);
            ResponseCommon<GetAnswerByIdResponse> expectedResponse = expectedGetResponse(answer);
            getAnswerById(request, expectedResponse);
        }
    }

    // single test method for exception
    @Test
    void getAnswerByIDException() {
        GetAnswerByIdRequest request = new GetAnswerByIdRequest();
        request.setId(1);
        when(answerRepository.findAnswerById(request.getId())).thenThrow(new RuntimeException());
        assertEquals(answerService.getAnswerById(request).getCode(), GET_EXCEPTION.getCode());
    }

    // parameterized test method for addAnswer() by questionID
    @ParameterizedTest
    @MethodSource("provideQuestionIDs")
    void addAnswerParamQuestionID(int questionID) {
        AnswerData answerData = new AnswerData();
        answerData.setQuestionID(questionID);
        answerData.setAnswerName(answers.get(0).getAnswerContent());
        answerData.setCorrect(false);
        answerData.setUsername(users.get(0).getUsername());

        if (questionID < 1 || questionID > questions.size()) {
            addAnswer(answerData, QUESTION_NOT_EXIST);
        } else {
            User u = users.get(0);
            ResponseCommon<AddAnswerResponse> expectedResponse = expectedAddResponse(answerData, u);
            addAnswer(answerData, expectedResponse);
        }
    }

    @ParameterizedTest
    @MethodSource("provideAnswerName")
    void addAnswerParamAnswerName(String answerName) {
        AnswerData answerData = new AnswerData();
        answerData.setQuestionID(1);
        answerData.setAnswerName(answerName);
        answerData.setCorrect(false);
        answerData.setUsername(users.get(0).getUsername());

        if (answerName == null || answerName.isEmpty()) {
            addAnswer(answerData, ADD_EXCEPTION);
        } else {
            User u = users.get(0);
            ResponseCommon<AddAnswerResponse> expectedResponse = expectedAddResponse(answerData, u);
            addAnswer(answerData, expectedResponse);
        }
    }

    @ParameterizedTest
    @MethodSource("provideCorrect")
    void addAnswerParamCorrect(boolean correct) {
        AnswerData answerData = new AnswerData();
        answerData.setQuestionID(1);
        answerData.setAnswerName(answers.get(0).getAnswerContent());
        answerData.setCorrect(correct);
        answerData.setUsername(users.get(0).getUsername());

        User u = users.get(0);
        ResponseCommon<AddAnswerResponse> expectedResponse = expectedAddResponse(answerData, u);
        addAnswer(answerData, expectedResponse);
    }
//
//    @ParameterizedTest
//    @MethodSource("provideUsername")
//    void addAnswerParamUsername(String username) {
//        AnswerData answerData = new AnswerData();
//        answerData.setQuestionID(1);
//        answerData.setAnswerName(answers.get(0).getAnswerContent());
//        answerData.setCorrect(false);
//        answerData.setUsername(username);
//
//        if (username == null || username.isEmpty()) {
//            addAnswer(answerData, ADD_EXCEPTION);
//        } else {
//            User u = users.get(0);
//            ResponseCommon<AddAnswerResponse> expectedResponse = expectedAddResponse(answerData, u);
//            addAnswer(answerData, expectedResponse);
//        }
//    }

    // parameterized test method for updateAnswer()


    private ResponseCommon<DeleteAnswerResponse> expectedDeleteResponse(Answer answer) {
        DeleteAnswerResponse expectedDeleteResponse = new DeleteAnswerResponse();

        expectedDeleteResponse.setAnswerID(answer.getId());
        expectedDeleteResponse.setAnswerContent(answer.getAnswerContent());
        expectedDeleteResponse.setQuestionID(answer.getQuestionId());
        expectedDeleteResponse.setCorrect(answer.isCorrect());

        return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Delete answer success", expectedDeleteResponse);
    }

    // parameterized test method for deleteAnswer()
    @ParameterizedTest
    @MethodSource("provideIDs")
    void deleteAnswer(int id) {
        DeleteAnswerRequest request = new DeleteAnswerRequest();
        request.setAnswerID(id);

        if (id < 1 || id > answers.size()) {
            // id = 1 -> 4 => qID = 1; id = 5 -> 8 => qID = 2, ...
            deleteAnswer(request, ANSWER_NOT_EXIST_DELETE);
        } else if (id > 0 && id <= answers.size()) {
            request.setQuestionID((id - 1) / 4 + 1);
            Answer answer = answers.get(id - 1);
            deleteAnswer(request, expectedDeleteResponse(answer));

        } else {
            Answer answer = answers.get(id - 1);
            ResponseCommon<GetAnswerByIdResponse> expectedResponse = expectedGetResponse(answer);
            deleteAnswer(request, DELETE_ANSWER_FAIL);
        }
    }


    void addAnswer(AnswerData answerData, ResponseCommon<AddAnswerResponse> expectedResponse) {

        // reset mocks
        reset(answerRepository);
        reset(questionRepository);
        reset(userRepository);

        // call stack: answerService.addAnswer(answerData) -> questionRepository.findQuestionById(id) -> questions.get(id)
        final int questionId = answerData.getQuestionID();
        final String username = answerData.getUsername();
        final String answerName = answerData.getAnswerName();
        final boolean correct = answerData.isCorrect();


        when(questionRepository.findQuestionById(questionId)).then(invocation -> {
            if (questionId > 0 && questionId <= questions.size()) {
                // Offset by 1
                return Optional.of(questions.get(questionId - 1));
            } else {
                return Optional.empty();
            }
        });

        // call stack: answerService.addAnswer(answerData) -> userRepository.findByUsername(username) -> users.get(id)
        when(userRepository.findByUsername(answerData.getUsername())).then(invocation -> {
            for (User user : users) {
                if (user.getUsername().equals(answerData.getUsername())) {
                    return Optional.of(user);
                }

            }
            return Optional.empty();
        });


        User u = userRepository.findByUsername(answerData.getUsername()).get();

        // call stack: answerService.addAnswer(answerData) -> answerRepository.save(answer) -> answerRepository.findAnswerById(id) -> answers.get(id)
        // mock answer
        Answer answer = new Answer().setAnswerContent(answerData.getAnswerName()).setCorrect(answerData.isCorrect()).setQuestionId(questionId).setUserCreated(u).setUserUpdated(u).setDeleted(false);

        // mock save answer
        when(answerRepository.save(answer)).then(invocation -> {
            answers.add(answer);
            return answer;
        });

        var response = answerService.addAnswer(answerData);


        assertNotNull(response);

        assertEquals(expectedResponse.getCode(), response.getCode());
        assertEquals(expectedResponse.getMessage(), response.getMessage());
    }

    private ResponseCommon<AddAnswerResponse> expectedAddResponse(AnswerData answerData, User u) {
        AddAnswerResponse expectedAddResponse = new AddAnswerResponse();
        expectedAddResponse.setId(answerData.getQuestionID());
        expectedAddResponse.setAnswerContent(answerData.getAnswerName());
        expectedAddResponse.setCorrect(answerData.isCorrect());
        expectedAddResponse.setCreatedBy(u.getUsername());
        expectedAddResponse.setUpdatedBy(u.getUsername());
        return new ResponseCommon<AddAnswerResponse>(ResponseCode.SUCCESS.getCode(), "Add answer success", expectedAddResponse);
    }

    void updateAnswer(UpdateAnswerRequest request, ResponseCommon<UpdateAnswerResponse> expectedResponse) {
        when(answerRepository.findAnswerByQuestionIdAndId(request.getQuestionID(), request.getAnswerID())).then(invocation -> {
            int questionId = request.getQuestionID();
            int answerId = request.getAnswerID();

            for (Answer answer : answers) {
                if (questionId == answer.getQuestionId() && answerId == answer.getId()) {
                    return Optional.of(answer);
                }
            }
            return Optional.empty();
        });

        when(userRepository.findByUsername(request.getUsername())).then(invocation -> {
            for (User user : users) {
                if (user.getUsername().equals(request.getUsername())) {
                    return Optional.of(user);
                }
            }
            return Optional.empty();
        });

        var response = answerService.updateAnswer(request);

        verify(answerRepository, times(1)).findAnswerByQuestionIdAndId(request.getQuestionID(), request.getAnswerID());

        // has response
        assertNotNull(response);

        // code
        assertEquals(expectedResponse.getCode(), response.getCode());

        // message
        assertEquals(expectedResponse.getMessage(), response.getMessage());

        // data: UpdateAnswerResponse
        if (response.getData() != null && expectedResponse.getData() != null) {
            UpdateAnswerResponse expectedData = (UpdateAnswerResponse) expectedResponse.getData();
            UpdateAnswerResponse actualData = (UpdateAnswerResponse) response.getData();

            assertEquals(expectedData.getQuestionID(), actualData.getQuestionID());
            assertEquals(expectedData.getAnswerID(), actualData.getAnswerID());
            assertEquals(expectedData.getAnswerContent(), actualData.getAnswerContent());
            assertEquals(expectedData.isCorrect(), actualData.isCorrect());
            assertEquals(expectedData.getCreatedBy(), actualData.getCreatedBy());
            assertEquals(expectedData.getUpdatedBy(), actualData.getUpdatedBy());
        }

    }

    private ResponseCommon<UpdateAnswerResponse> expectedUpdateResponse(Answer answer) {
        UpdateAnswerResponse expectedUpdateResponse = new UpdateAnswerResponse();

        expectedUpdateResponse.setQuestionID(answer.getQuestionId());
        expectedUpdateResponse.setAnswerID(answer.getId());
        expectedUpdateResponse.setAnswerContent(answer.getAnswerContent());
        expectedUpdateResponse.setCorrect(answer.isCorrect());
        expectedUpdateResponse.setCreatedBy(answer.getUserCreated().getUsername());
        expectedUpdateResponse.setUpdatedBy(answer.getUserUpdated().getUsername());

        return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Update answer success", expectedUpdateResponse);
    }

    @Test
    void addAnswerException() {
        AnswerData answerData = new AnswerData();
        answerData.setQuestionID(1);
        answerData.setAnswerName(answers.get(0).getAnswerContent());
        answerData.setCorrect(false);
        answerData.setUsername(users.get(0).getUsername());

        when(questionRepository.findQuestionById(answerData.getQuestionID())).thenThrow(new RuntimeException());
        assertEquals(answerService.addAnswer(answerData).getCode(), ADD_EXCEPTION.getCode());
    }

}