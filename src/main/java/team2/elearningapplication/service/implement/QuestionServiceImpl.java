package team2.elearningapplication.service.implement;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team2.elearningapplication.Enum.ResponseCode;
import team2.elearningapplication.dto.common.PageRequestDTO;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.admin.question.*;
import team2.elearningapplication.dto.request.user.question.GetQuestionByQuizIDRequest;
import team2.elearningapplication.dto.response.admin.question.AddQuestionResponse;
import team2.elearningapplication.dto.response.admin.question.DeleteQuestionResponse;
import team2.elearningapplication.dto.response.admin.question.GetQuestionByIdResponse;
import team2.elearningapplication.dto.response.admin.question.UpdateQuestionResponse;
import team2.elearningapplication.dto.response.user.question.GetQuestionByQuizIdResponse;
import team2.elearningapplication.dto.response.user.question.GetQuestionPageResponse;
import team2.elearningapplication.entity.Answer;
import team2.elearningapplication.entity.Question;
import team2.elearningapplication.entity.Quiz;
import team2.elearningapplication.entity.User;
import team2.elearningapplication.repository.*;
import team2.elearningapplication.service.IQuestionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements IQuestionService {
    private final IQuestionDataRepository questionDataRepository;
    private final IQuestionRepository questionRepository;
    private final IQuizRepository iQuizRepository;
    private final IAnswerRepository iAnswerRepository;
    private final IUserRepository userRepository;


    private final Logger log = LoggerFactory.getLogger(QuestionServiceImpl.class);

    @Override
    public ResponseCommon<AddQuestionResponse> addQuestion(QuestionData questionData) {
        try {
            // Find the question by name to check if it already exists
            Quiz quiz = iQuizRepository.findQuizById(questionData.getQuizID()).orElse(null);
            User user = userRepository.findByUsername(questionData.getUsername()).orElse(null);
            if (quiz == null) {
                log.debug("addQuestion: Quiz not exists.");
                return new ResponseCommon<>(ResponseCode.QUIZ_NOT_EXIST.getCode(), "Quizz not exist", null);
            }
//                List<Answer> answerList =  questionData.getListAnswer();
            // Create and save the question
            Question questionAdd = new Question();
            questionAdd.setQuestionName(questionData.getQuestionName());
            questionAdd.setQuestionType(questionData.getQuestionType());
            questionAdd.setQuizID(questionData.getQuizID());
            questionAdd.setUserCreated(user);
            questionAdd = questionRepository.save(questionAdd);

            for (Answer answer : questionData.getListAnswer()) {
                answer.setQuestionId(questionAdd.getId());
                iAnswerRepository.save(answer);
            }

            log.debug("addQuestion: Question added successfully.");
            return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Add question success", new AddQuestionResponse());

        } catch (Exception e) {
            log.error("addQuestion: An error occurred - " + e.getMessage(), e);
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add question fail", null);
        }
    }

    @Override
    @Transactional
    public ResponseCommon<UpdateQuestionResponse> updateQuestion(UpdateQuestionRequest updateQuestionRequest) {
        try {
            // Find the question to update
            Question questionToUpdate = questionRepository.findQuestionById(updateQuestionRequest.getQuestionID()).orElse(null);
            User user = userRepository.findByUsername(updateQuestionRequest.getUsername()).orElse(null);
            // Check if the question exists
            if (Objects.isNull(questionToUpdate)) {
                log.debug("updateQuestion: Question not found.");
                return new ResponseCommon<>(ResponseCode.QUESTION_NOT_EXIST.getCode(), "Question not exist", null);
            }

            // Update the question
            questionToUpdate.setQuestionName(updateQuestionRequest.getQuestionName());
            questionToUpdate.setQuestionType(updateQuestionRequest.getQuestionType());
            questionToUpdate.setUpdatedAt(LocalDateTime.now());
            questionToUpdate.setDeleted(updateQuestionRequest.isDeleted());
            questionToUpdate.setUserUpdated(user);
            // Save the updated question
            questionRepository.save(questionToUpdate);
            for (Answer answer : updateQuestionRequest.getAnswers()) {
                Answer answerUpdate = iAnswerRepository.findAnswerById(answer.getId()).orElse(null);
                if (answerUpdate == null) {
                    log.debug("updateQuestion: Answer not found.");
                    return new ResponseCommon<>(ResponseCode.ANSWER_NOT_EXIST.getCode(), "Answer not exist", null);
                }
                answerUpdate.setAnswerContent(answer.getAnswerContent());
                answerUpdate.setCorrect(answer.isCorrect());
                answerUpdate.setUpdatedAt(LocalDateTime.now());
                iAnswerRepository.save(answerUpdate);
            }

            log.debug("updateQuestion: Question updated successfully.");
            return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Update question success", new UpdateQuestionResponse());
        } catch (Exception e) {
            log.error("updateQuestion: An error occurred - " + e.getMessage(), e);
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Update question fail", null);
        }
    }

    @Override
    public ResponseCommon<DeleteQuestionResponse> deleteQuestion(DeleteQuestionRequest deleteQuestionRequest) {
        try {
            // Find the question to delete
            Question questionToDelete = questionRepository.findQuestionById(deleteQuestionRequest.getQuestionID()).orElse(null);
            User user = userRepository.findByUsername(deleteQuestionRequest.getUsername()).orElse(null);
            // Check if the question exists
            if (Objects.isNull(questionToDelete)) {
                log.debug("deleteQuestion: Question not found.");
                return new ResponseCommon<>(ResponseCode.QUESTION_NOT_EXIST.getCode(), "Question not exist", null);
            }

            // Set the question as deleted
            questionToDelete.setDeleted(true);
            questionToDelete.setUpdatedAt(LocalDateTime.now());
            questionToDelete.setUserUpdated(user);

            // Save the deleted question
            questionRepository.save(questionToDelete);

            // Create and return a success response
            DeleteQuestionResponse deleteQuestionResponse = new DeleteQuestionResponse();
            deleteQuestionResponse.setQuizID(questionToDelete.getQuizID());
            deleteQuestionResponse.setQuestionID(questionToDelete.getId());

            log.debug("deleteQuestion: Question deleted successfully.");
            return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Delete question success", deleteQuestionResponse);
        } catch (Exception e) {
            log.error("deleteQuestion: An error occurred - " + e.getMessage(), e);
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Delete question fail", null);
        }
    }

    @Override
    public ResponseCommon<List<Question>> findAllQuestion() {
        try {
            // Find all questions
            List<Question> questions = questionRepository.findAll();

            // Check if the question list is empty
            if (questions.isEmpty()) {
                log.debug("findAllQuestion: Question list is empty.");
                return new ResponseCommon<>(ResponseCode.QUESTION_LIST_IS_EMPTY.getCode(), "Question list is empty", null);
            }
            log.debug("findAllQuestion: Found all questions successfully.");
            return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Find all question success", questions);
        } catch (Exception e) {
            log.error("findAllQuestion: An error occurred - " + e.getMessage(), e);
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Find all question fail", null);
        }
    }

    @Override
    public ResponseCommon<List<Question>> findAllQuestionByDeleted(FindQuestionByDeletedRequest findQuestionByDeletedRequest) {
        try {
            // Find all questions
            List<Question> questions = questionRepository.findQuestionByDeleted(findQuestionByDeletedRequest.isDeleted());

            // Check if the question list is empty
            if (questions.isEmpty()) {
                log.debug("findAllQuestion: Question list is empty.");
                return new ResponseCommon<>(ResponseCode.QUESTION_LIST_IS_EMPTY.getCode(), "Question list is empty", null);
            }
            log.debug("findAllQuestion: Found all questions successfully.");
            return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Find all question success", questions);
        } catch (Exception e) {
            log.error("findAllQuestion: An error occurred - " + e.getMessage(), e);
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Find all question fail", null);
        }
    }

    @Override
    public ResponseCommon<GetQuestionByIdResponse> getQuestionById(GetQuestionByIdRequest getQuestionByIdRequest) {
        try {
            Question question = questionRepository.findQuestionById(getQuestionByIdRequest.getId()).orElse(null);
            // If question not exist -> tell user
            if (Objects.isNull(question)) {
                log.debug("Get question by id: Question not exits.");
                return new ResponseCommon<>(ResponseCode.QUESTION_NOT_EXIST.getCode(), "Question not exits", null);
            } else {
                GetQuestionByIdResponse response = new GetQuestionByIdResponse();
                List<Answer> answers = iAnswerRepository.findAnswerByQuestionId(question.getId());

                response.setId(question.getId());
                response.setQuizID(question.getQuizID());
                response.setQuestionType(question.getQuestionType());
                response.setQuestionName(question.getQuestionName());
                response.setAnswerList(answers);
                response.setDeleted(question.isDeleted());

                log.debug("Get question by id successfully.");
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Get question by id success", response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Get question by id: An error occurred - " + e.getMessage(), e);
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get question by id fail", null);
        }
    }

    @Override
    public ResponseCommon<GetQuestionPageResponse> getQuestionPage(PageRequestDTO pageRequestDTO) {
        try {
            int pageNo = pageRequestDTO.getPageNo();
            int pageSize = pageRequestDTO.getPageSize();
            String sortBy = pageRequestDTO.getSortBy();
            String sortDir = pageRequestDTO.getSortDir();

            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.fromString(sortDir), sortBy));

            Page<Question> questionPage = questionRepository.findAllByDeleted(false, pageable);
            if (questionPage.isEmpty()) {
                return new ResponseCommon<>(ResponseCode.QUESTION_LIST_IS_EMPTY, null);
            }
            GetQuestionPageResponse response = new GetQuestionPageResponse();
            response.setQuestionList(questionPage.getContent());
            response.setPageNo(pageNo);
            response.setPageSize(pageSize);
            response.setTotalElements((int) questionPage.getTotalElements());
            response.setTotalPages(questionPage.getTotalPages());
            response.setLast(questionPage.isLast());
            return new ResponseCommon<>(ResponseCode.SUCCESS, response);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Get question page An error occurred - " + e.getMessage(), e);
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get question page fail", null);
        }
    }

    @Override
    public ResponseCommon<GetQuestionByQuizIdResponse> getQuestionByQuizId(GetQuestionByQuizIDRequest getQuestionByQuizIDRequest) {
        try {
            List<Question> questions = questionRepository.getQuestionByQuizID(getQuestionByQuizIDRequest.getQuizId());
            GetQuestionByQuizIdResponse getQuestionByQuizIdResponse = new GetQuestionByQuizIdResponse();
            getQuestionByQuizIdResponse.setQuestionList(questions);
            return new ResponseCommon<>(ResponseCode.SUCCESS,getQuestionByQuizIdResponse);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Get question by id An error occurred - " + e.getMessage(), e);
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get question by id  fail", null);
        }
    }
}
