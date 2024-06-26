package team2.elearningapplication.service.implement;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import team2.elearningapplication.Enum.ResponseCode;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.admin.answer.AnswerData;
import team2.elearningapplication.dto.request.admin.answer.GetAnswerByIdRequest;
import team2.elearningapplication.dto.request.admin.answer.UpdateAnswerRequest;
import team2.elearningapplication.dto.response.admin.answer.AddAnswerResponse;
import team2.elearningapplication.dto.response.admin.answer.GetAnswerByIdResponse;
import team2.elearningapplication.dto.response.admin.answer.UpdateAnswerResponse;
import team2.elearningapplication.entity.Answer;
import team2.elearningapplication.entity.Question;
import team2.elearningapplication.entity.User;
import team2.elearningapplication.repository.IAnswerRepository;
import team2.elearningapplication.repository.IQuestionRepository;
import team2.elearningapplication.repository.IUserRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AnswerServiceImplTest extends Mockito {


    private static ArrayList<Answer> answers;
    private static ArrayList<Question> questions;
    private static ArrayList<User> users;

    @InjectMocks
    AnswerServiceImpl answerService;

    @Mock
    IAnswerRepository answerRepository;

    @Mock
    IQuestionRepository questionRepository;

    @Mock
    IUserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeAll
    static void setAnswers() {
        answers = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            answers.add(new Answer().setId(i + 1).setAnswerContent("Test Answer " + (i + 1)).setUserCreated(users.get(i % 5)).setDeleted(false).setUserUpdated(users.get(i % 5)).setCorrect(i % 4 == 0).setQuestionId(i % 5 + 1));
        }
    }

    @BeforeAll
    static void setQuestions() {
        questions = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            questions.add(new Question().setId(i + 1).setQuestionName("Test Question " + (i + 1)));
        }
    }

    @BeforeAll
    static void setUsers() {
        users = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            users.add(new User().setId(i + 1).setUsername("Test User " + (i + 1)));
        }
    }


    private Answer getAnswer(int id) {
        return answers.get(id);
    }

    void getAnswerById(GetAnswerByIdRequest request, ResponseCommon<GetAnswerByIdResponse> expectedResponse) {

        // call stack: answerService.getAnswerById(request) -> answerRepository.findAnswerById(id) -> answers.get(id)
        final int id = request.getId();

        reset(answerRepository);

        when(answerRepository.findAnswerById(id)).then(invocation -> {
            if (id > 0 && id <= answers.size()) {
                // db answer id start with 1, but array index start with 0, offsetting by 1
                return Optional.of(getAnswer(id - 1));
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

    // on failure return this response
    private static final ResponseCommon<GetAnswerByIdResponse> ANSWER_NOT_EXIST = new ResponseCommon<>(ResponseCode.ANSWER_NOT_EXIST.getCode(), "Answer not exist", null);

    // on success return code + data


    // on exception return this
    private static final ResponseCommon<GetAnswerByIdResponse> EXCEPTION = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Delete answer fail", null);


    // create expected responses
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

    // BVA (answer): valid boundary (1)
    @Test
    void getAnswerByIdONE() {
        GetAnswerByIdRequest request = new GetAnswerByIdRequest();
        request.setId(1);

        // Offsetting by 1
        Answer answer = answers.get(request.getId() - 1);
        ResponseCommon<GetAnswerByIdResponse> expectedResponse = expectedGetResponse(answer);


        getAnswerById(request, expectedResponse);
    }

    // BVA (answer id): invalid boundary (0)
    @Test
    void getAnswerByIdZERO() {
        GetAnswerByIdRequest request = new GetAnswerByIdRequest();
        request.setId(0);

        getAnswerById(request, ANSWER_NOT_EXIST);
    }


    // EP (answer id): invalid (-5)
    @Test
    void getAnswerByIdNEGATIVE() {
        GetAnswerByIdRequest request = new GetAnswerByIdRequest();
        request.setId(-5);

        getAnswerById(request, ANSWER_NOT_EXIST);
    }

    // EP (answer id): valid (5)
    @Test
    void getAnswerByIdFIVE() {
        GetAnswerByIdRequest request = new GetAnswerByIdRequest();
        request.setId(5);

        Answer answer = answers.get(request.getId() - 1);
        ResponseCommon<GetAnswerByIdResponse> expectedResponse = expectedGetResponse(answer);

        getAnswerById(request, expectedResponse);
    }

    // BVA (answer id): valid boundary (20)
    @Test
    void getAnswerByIdTWENTY() {
        GetAnswerByIdRequest request = new GetAnswerByIdRequest();
        request.setId(20);

        Answer answer = answers.get(request.getId() - 1);
        ResponseCommon<GetAnswerByIdResponse> expectedResponse = expectedGetResponse(answer);

        getAnswerById(request, expectedResponse);
    }

    // BVA (answer id): invalid boundary (21)
    @Test
    void getAnswerByIdTWENTYONE() {
        GetAnswerByIdRequest request = new GetAnswerByIdRequest();
        request.setId(21);

        getAnswerById(request, ANSWER_NOT_EXIST);
    }

    // EP (answer id): invalid (25)
    @Test
    void getAnswerByIdTWENTYFIVE() {
        GetAnswerByIdRequest request = new GetAnswerByIdRequest();
        request.setId(25);

        getAnswerById(request, ANSWER_NOT_EXIST);
    }

    @Test
    void getAnswerById_ExceptionThrown() {
        GetAnswerByIdRequest request = new GetAnswerByIdRequest();
        request.setId(1);

        // mock the event: db error -> findAnswerById() throws exception
        when(answerRepository.findAnswerById(request.getId())).thenThrow(new RuntimeException("Test Exception"));

        var response = answerService.getAnswerById(request);

        assertEquals(EXCEPTION.getCode(), response.getCode());
        assertEquals(EXCEPTION.getMessage(), response.getMessage());
        assertNull(response.getData());
    }

    private final ResponseCommon<AddAnswerResponse> QUESTION_NOT_EXIST = new ResponseCommon<AddAnswerResponse>(ResponseCode.QUESTION_NOT_EXIST.getCode(), "Question not exist, cannot add answer to question", null);
    private final ResponseCommon<AddAnswerResponse> ADD_EXCEPTION = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add answer fail", null);

    void addAnswer(AnswerData answerData, ResponseCommon<AddAnswerResponse> expectedResponse) {

        // reset mocks
        reset(answerRepository);
        reset(questionRepository);
        reset(userRepository);

        // call stack: answerService.addAnswer(answerData) -> questionRepository.findQuestionById(id) -> questions.get(id)
        final int questionId = answerData.getQuestionID();

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
        Answer answer = new Answer()
                .setAnswerContent(answerData.getAnswerName())
                .setCorrect(answerData.isCorrect())
                .setQuestionId(questionId)
                .setUserCreated(u)
                .setUserUpdated(u)
                .setDeleted(false);

        // mock save answer
        when(answerRepository.save(answer)).then(invocation -> {
            answers.add(answer);
            return answer;
        });

        when(answerRepository.findAnswerById(answer.getId())).then(invocation -> {
            return Optional.of(answer);
        });
        // call stack: answerService.addAnswer(answerData) -> userRepository.findUserById(id) -> users.get(id)

        var response = answerService.addAnswer(answerData);


        assertNotNull(response);
        assertEquals(expectedResponse.getCode(), response.getCode());
        assertEquals(expectedResponse.getMessage(), response.getMessage());
//        assertEquals(expectedResponse.getData(), response.getData());
        if (response.getData() != null && expectedResponse.getData() != null) {

            AddAnswerResponse expectedData = expectedResponse.getData();
            AddAnswerResponse actualData = response.getData();

            assertEquals(expectedData.getId(), actualData.getId());
            assertEquals(expectedData.getAnswerContent(), actualData.getAnswerContent());
            assertEquals(expectedData.isCorrect(), actualData.isCorrect());
            assertEquals(expectedData.getCreatedBy(), actualData.getCreatedBy());
            assertEquals(expectedData.getUpdatedBy(), actualData.getUpdatedBy());
        }
    }

    private ResponseCommon<AddAnswerResponse> expectedAddResponse(AnswerData answerData, User u) {

        // create expected response
        AddAnswerResponse expectedAddResponse = new AddAnswerResponse();
        expectedAddResponse.setId(answerData.getQuestionID());
        expectedAddResponse.setAnswerContent(answerData.getAnswerName());
        expectedAddResponse.setCorrect(answerData.isCorrect());
        expectedAddResponse.setCreatedBy(u.getUsername());
        expectedAddResponse.setUpdatedBy(u.getUsername());

        return new ResponseCommon<AddAnswerResponse>(ResponseCode.SUCCESS.getCode(), "Add answer success", expectedAddResponse);
    }

    // BVA (question): valid boundary (1)
    @Test
    void addAnswerONE() {
        AnswerData answerData = new AnswerData();
        answerData.setUsername("Test User 1");
        answerData.setQuestionID(1);
        answerData.setAnswerName("Test Answer 1");
        answerData.setCorrect(true);

        User u = users.get(0);
        ResponseCommon<AddAnswerResponse> expectedResponse = expectedAddResponse(answerData, u);

        addAnswer(answerData, expectedResponse);
    }

    // BVA (question id): invalid boundary (0)
    @Test
    void addAnswerQuestionIDZERO() {
        AnswerData answerData = new AnswerData();
        answerData.setUsername("Test User 1");
        answerData.setQuestionID(0);
        answerData.setAnswerName("Test Answer 1");
        answerData.setCorrect(true);

        addAnswer(answerData, QUESTION_NOT_EXIST);
    }

    // EP (question id): invalid (-5)
    @Test
    void addAnswerQuestionIDNEGATIVE() {
        AnswerData answerData = new AnswerData();
        answerData.setUsername("Test User 1");
        answerData.setQuestionID(-5);
        answerData.setAnswerName("Test Answer 1");
        answerData.setCorrect(true);

        addAnswer(answerData, QUESTION_NOT_EXIST);
    }

    // EP (question id): valid (3)
    @Test
    void addAnswerQuestionIDTHREE() {
        AnswerData answerData = new AnswerData();
        answerData.setUsername("Test User 1");
        answerData.setQuestionID(3);
        answerData.setAnswerName("Test Answer 1");
        answerData.setCorrect(true);

        User u = users.get(0);
        ResponseCommon<AddAnswerResponse> expectedResponse = expectedAddResponse(answerData, u);

        addAnswer(answerData, expectedResponse);
    }

    // BVA (question id): valid boundary (5)
    @Test
    void addAnswerQuestionIDFIVE() {
        AnswerData answerData = new AnswerData();
        answerData.setUsername("Test User 1");
        answerData.setQuestionID(5);
        answerData.setAnswerName("Test Answer 1");
        answerData.setCorrect(true);

        User u = users.get(0);
        ResponseCommon<AddAnswerResponse> expectedResponse = expectedAddResponse(answerData, u);

        addAnswer(answerData, expectedResponse);
    }

    // BVA (question id): invalid boundary (6)
    @Test
    void addAnswerQuestionIDSIX() {
        AnswerData answerData = new AnswerData();
        answerData.setUsername("Test User 1");
        answerData.setQuestionID(6);
        answerData.setAnswerName("Test Answer 1");
        answerData.setCorrect(true);

        addAnswer(answerData, QUESTION_NOT_EXIST);
    }

    // EP (question id): invalid (10)
    @Test
    void addAnswerQuestionIDTEN() {
        AnswerData answerData = new AnswerData();
        answerData.setUsername("Test User 1");
        answerData.setQuestionID(10);
        answerData.setAnswerName("Test Answer 1");
        answerData.setCorrect(true);

        addAnswer(answerData, QUESTION_NOT_EXIST);
    }

    @Test
    void addAnswer_ExceptionThrown() {
        AnswerData answerData = new AnswerData();
        answerData.setUsername("Test User 1");
        answerData.setQuestionID(1);
        answerData.setAnswerName("Test Answer 1");
        answerData.setCorrect(true);

        // mock the event: db error -> findQuestionById() throws exception
        when(questionRepository.findQuestionById(answerData.getQuestionID())).thenThrow(new RuntimeException("Test Exception"));

        var response = answerService.addAnswer(answerData);

        assertEquals(ADD_EXCEPTION.getCode(), response.getCode());
        assertEquals(ADD_EXCEPTION.getMessage(), response.getMessage());
        assertNull(response.getData());
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

    private final ResponseCommon<UpdateAnswerResponse> ANSWER_NOT_EXIST_UPDATE = new ResponseCommon<>(ResponseCode.ANSWER_NOT_EXIST.getCode(), "Answer not exist in question", null);


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

    // BVA (answer): valid boundary (1)
    @Test
    void updateAnswerONE() {
        UpdateAnswerRequest request = new UpdateAnswerRequest();
        request.setUsername("Test User 1");
        request.setQuestionID(1);
        request.setAnswerID(1);
        request.setAnswerContent("Test Answer 1");
        request.setCorrect(true);

        Answer answer = answers.get(0);
        ResponseCommon<UpdateAnswerResponse> expectedResponse = expectedUpdateResponse(answer);
        System.out.println("Expected: a" + answer.getId() + " q" + answer.getQuestionId());


        updateAnswer(request, expectedResponse);
    }

    // BVA (answer): invalid boundary (0)
    @Test
    void updateAnswerZERO() {
        UpdateAnswerRequest request = new UpdateAnswerRequest();
        request.setUsername("Test User 1");
        request.setQuestionID(1);
        request.setAnswerID(0);
        request.setAnswerContent("Test Answer 1");
        request.setCorrect(true);

        updateAnswer(request, ANSWER_NOT_EXIST_UPDATE);
    }


    void deleteAnswer() {
    }


}