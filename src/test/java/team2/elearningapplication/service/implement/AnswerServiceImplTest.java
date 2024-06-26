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
import team2.elearningapplication.dto.response.admin.answer.AddAnswerResponse;
import team2.elearningapplication.dto.response.admin.answer.GetAnswerByIdResponse;
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
        MockitoAnnotations.initMocks(this);
    }

    @BeforeAll
    static void setAnswers() {
        answers = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            answers.add(new Answer().setId(i + 1).setAnswerContent("Test Answer " + (i + 1)).setUserCreated(users.get(i % 5)).setDeleted(false).setUserUpdated(users.get(i % 5)).setCorrect(i % 4 == 0).setQuestionId(i % 5));
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

    void getAnswerById(GetAnswerByIdRequest request, ResponseCommon expectedResponse) {

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
    private static final ResponseCommon ANSWER_NOT_EXIST = new ResponseCommon<>(ResponseCode.ANSWER_NOT_EXIST.getCode(), "Answer not exist", null);

    // on success return code + data


    // on exception return this
    private static final ResponseCommon EXCEPTION = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Delete answer fail", null);


    // create expected responses
    private ResponseCommon expectedGetResponse(Answer answer) {
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
        ResponseCommon expectedResponse = expectedGetResponse(answer);


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
        ResponseCommon expectedResponse = expectedGetResponse(answer);

        getAnswerById(request, expectedResponse);
    }

    // BVA (answer id): valid boundary (20)
    @Test
    void getAnswerByIdTWENTY() {
        GetAnswerByIdRequest request = new GetAnswerByIdRequest();
        request.setId(20);

        Answer answer = answers.get(request.getId() - 1);
        ResponseCommon expectedResponse = expectedGetResponse(answer);

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

    private final ResponseCommon QUESTION_NOT_EXIST = new ResponseCommon<>(ResponseCode.QUESTION_NOT_EXIST.getCode(), "Question not exist, cannot add answer to question", null);
    private final ResponseCommon ADD_EXCEPTION =  new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add answer fail", null);

    void addAnswer(AnswerData answerData, ResponseCommon expectedResponse) {
        reset(answerRepository);
        reset(questionRepository);

        // call stack: answerService.addAnswer(answerData) -> questionRepository.findQuestionById(id) -> questions.get(id)
        final int questionId = answerData.getQuestionID();

        when(questionRepository.findQuestionById(questionId)).then(invocation -> {
            if (questionId > 0 && questionId <= questions.size()) {
                return questions.get(questionId - 1);
            } else {
                return null;
            }
        });

        when(userRepository.findByUsername(answerData.getUsername())).then(invocation -> {
            for (User user : users) {
                if (user.getUsername().equals(answerData.getUsername())) {
                    return Optional.of(user);
                }

            }
            return Optional.empty();
        });
        // call stack: answerService.addAnswer(answerData) -> answerRepository.save(answer) -> answerRepository.findAnswerById(id) -> answers.get(id)

        User u = userRepository.findByUsername(answerData.getUsername()).get();
        // mock answer
        Answer answer = new Answer()
                .setAnswerContent(answerData.getAnswerName())
                .setCorrect(answerData.isCorrect())
                .setQuestionId(questionId)
                .setUserCreated(u)
                .setUserUpdated(u)
                .setDeleted(false);

        when(answerRepository.save(answer)).thenReturn(answer);
        // call stack: answerService.addAnswer(answerData) -> userRepository.findUserById(id) -> users.get(id)

        var response = answerService.addAnswer(answerData);


        assertNotNull(response);
        assertEquals(expectedResponse.getCode(), response.getCode());
        assertEquals(expectedResponse.getMessage(), response.getMessage());
//        assertEquals(expectedResponse.getData(), response.getData());
        if(response.getData() != null && expectedResponse.getData() != null) {
            AddAnswerResponse expectedData = (AddAnswerResponse) expectedResponse.getData();
            AddAnswerResponse actualData = (AddAnswerResponse) response.getData();

            assertEquals(expectedData.getId(), actualData.getId());
            assertEquals(expectedData.getAnswerContent(), actualData.getAnswerContent());
            assertEquals(expectedData.isCorrect(), actualData.isCorrect());
            assertEquals(expectedData.getCreatedBy(), actualData.getCreatedBy());
            assertEquals(expectedData.getUpdatedBy(), actualData.getUpdatedBy());
        }
    }

    private ResponseCommon<AddAnswerResponse> expectedAddResponse(AnswerData answerData, User u) {
        AddAnswerResponse expectedAddResponse = new AddAnswerResponse();

        expectedAddResponse.setId(answerData.getQuestionID());
        expectedAddResponse.setAnswerContent(answerData.getAnswerName());
        expectedAddResponse.setCorrect(answerData.isCorrect());
        expectedAddResponse.setCreatedBy(u.getUsername());
        expectedAddResponse.setUpdatedBy(u.getUsername());

        return new ResponseCommon<AddAnswerResponse>(expectedAddResponse);
    }

    // BVA (question): valid boundary (1)


    void updateAnswer() {
    }


    void deleteAnswer() {
    }
}