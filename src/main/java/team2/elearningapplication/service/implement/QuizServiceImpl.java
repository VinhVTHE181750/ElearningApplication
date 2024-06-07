package team2.elearningapplication.service.implement;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import team2.elearningapplication.Enum.ResponseCode;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.admin.quiz.*;
import team2.elearningapplication.dto.request.user.quiz.*;
import team2.elearningapplication.dto.response.admin.quiz.*;
import team2.elearningapplication.dto.response.user.answer.AnswerCorrectDTO;
import team2.elearningapplication.dto.response.user.quiz.*;
import team2.elearningapplication.entity.*;
import team2.elearningapplication.repository.*;
import team2.elearningapplication.service.IQuizService;
import team2.elearningapplication.service.email.EmailService;
import team2.elearningapplication.utils.CommonUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements IQuizService {
    private static final double BASE_MARK = 0.8;
    private static final String QUIZ_NOT_EXIST = "Quiz not exist";

    private final IQuizRepository quizRepository;
    private final ILessonRespository lessonRespository;
    private final IQuestionRepository questionRepository;
    private final IAnswerRepository answerRepository;
    private final IUserRepository userRepository;
    private final EmailService emailService;
    private final ICourseRepository courseRepository;
    private final IHistoryQuizRepository historyQuizRepository;

    private final Logger log = LoggerFactory.getLogger(QuizServiceImpl.class);

    @Override
    public ResponseCommon<AddQuizResponse> addQuiz(AddQuizRequest addQuizRequest) {
        try {
            Quiz quiz = quizRepository.findQuizByName(addQuizRequest.getQuizName()).orElse(null);
            User user = userRepository.findByUsername(addQuizRequest.getUsername()).orElse(null);

            // if quiz not null -> tell user
            if (!Objects.isNull(quiz))
                return new ResponseCommon<>(ResponseCode.QUIZ_EXIST.getCode(), "Quiz already exist", null);
            else {
                Quiz quizAdd = new Quiz();
                quizAdd.setLesson(lessonRespository.findLessonById(addQuizRequest.getLessonID()).orElse(null));
                quizAdd.setName(addQuizRequest.getQuizName());
                quizAdd.setUserCreated(user);
                quizRepository.save(quizAdd);
                AddQuizResponse addQuizResponse = new AddQuizResponse();
                addQuizResponse.setLessonName(quizAdd.getName());
                addQuizResponse.setQuizID(quizAdd.getId());
                addQuizResponse.setLessonID(quizAdd.getLesson().getId());
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Add Quiz success", addQuizResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add Quiz fail", null);
        }
    }

    @Override
    public ResponseCommon<UpdateQuizResponse> updateQuiz(UpdateQuizRequest updateQuizRequest) {
        try {
            Quiz quiz = quizRepository.findQuizById(updateQuizRequest.getQuizID()).orElse(null);
            User user = userRepository.findByUsername(updateQuizRequest.getUsername()).orElse(null);
            // if quiz is null -> tell user
            if (Objects.isNull(quiz))
                return new ResponseCommon<>(ResponseCode.QUIZ_NOT_EXIST.getCode(), QUIZ_NOT_EXIST, null);
            else {
                quiz.setName(updateQuizRequest.getQuizName());
                quiz.setLesson(lessonRespository.findLessonById(updateQuizRequest.getLessonID()).orElse(null));
                quiz.setDeleted(updateQuizRequest.isDeleted());
                quiz.setUserUpdated(user);
                quizRepository.save(quiz);
                UpdateQuizResponse updateQuizResponse = new UpdateQuizResponse();
                updateQuizResponse.setUpdateAt(LocalDateTime.now());
                updateQuizResponse.setQuizName(quiz.getName());
                updateQuizResponse.setLessonID(quiz.getLesson().getId());
                updateQuizResponse.setLessonName(quiz.getLesson().getName());
                updateQuizResponse.setQuizName(quiz.getName());
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Update quiz success", updateQuizResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Update quiz fail", null);
        }

    }

    @Override
    public ResponseCommon<DeleteQuizResponse> deleteQuiz(DeleteQuizRequest deleteQuizRequest) {
        try {
            Quiz quiz = quizRepository.findQuizById(deleteQuizRequest.getQuizID()).orElse(null);
            User user = userRepository.findByUsername(deleteQuizRequest.getUsername()).orElse(null);
            // if quiz is null -> tell user
            if (Objects.isNull(quiz))
                return new ResponseCommon<>(ResponseCode.QUIZ_NOT_EXIST.getCode(), QUIZ_NOT_EXIST, null);
            else {
                quiz.setDeleted(true);
                quiz.setUpdatedAt(LocalDateTime.now());
                quiz.setUserUpdated(user);
                quizRepository.save(quiz);
                DeleteQuizResponse deleteQuizResponse = new DeleteQuizResponse();
                deleteQuizResponse.setUpdateAt(LocalDateTime.now());
                deleteQuizResponse.setQuizName(quiz.getName());
                deleteQuizResponse.setLessonID(quiz.getLesson().getId());
                deleteQuizResponse.setLessonName(quiz.getLesson().getName());
                deleteQuizResponse.setQuizName(quiz.getName());
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Delete quiz success", deleteQuizResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Delete quiz fail", null);
        }
    }

    @Override
    public ResponseCommon<FindAllQuizResponse> findAllQuiz() {
        try {
            List<Quiz> quizList = quizRepository.findAllByIsDeleted(false);
            if (quizList.isEmpty())
                return new ResponseCommon<>(ResponseCode.QUIZ_LIST_IS_EMPTY.getCode(), "Quiz list is empty", null);
            else {
                FindAllQuizResponse findAllQuizResponse = new FindAllQuizResponse();
                findAllQuizResponse.setQuizList(quizList);
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Find all quiz success", findAllQuizResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Find all quiz fail", null);
        }
    }

    @Override
    public ResponseCommon<FindAllQuizResponse> findAllQuizByDeleted(FindQuizByDeletedRequest findQuizByDeletedRequest) {
        try {
            List<Quiz> quizList = quizRepository.findAllByIsDeleted(findQuizByDeletedRequest.isDeleted());
            if (quizList.isEmpty())
                return new ResponseCommon<>(ResponseCode.QUIZ_LIST_IS_EMPTY.getCode(), "Quiz list is empty", null);
            else {
                FindAllQuizResponse findAllQuizResponse = new FindAllQuizResponse();
                findAllQuizResponse.setQuizList(quizList);
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Find all quiz success", findAllQuizResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Find all quiz fail", null);
        }
    }

    @Override
    public ResponseCommon<GetQuizByIdResponse> getQuizById(GetQuizByIdRequest getQuizByIdRequest) {
        try {
            Quiz quiz = quizRepository.findQuizById(getQuizByIdRequest.getId()).orElse(null);
            // If quiz not exist -> tell user
            if (Objects.isNull(quiz)) {
                log.debug(QUIZ_NOT_EXIST);
                return new ResponseCommon<>(ResponseCode.QUIZ_NOT_EXIST.getCode(), QUIZ_NOT_EXIST, null);
            } else {
                GetQuizByIdResponse response = new GetQuizByIdResponse();

                response.setId(quiz.getId());
                response.setName(quiz.getName());
                response.setLesson(quiz.getLesson());
                response.setLesson(quiz.getLesson());
                response.setDeleted(quiz.isDeleted());

                log.debug("Get quiz by id successfully");
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Get quiz by id success", response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Get quiz by id failed");
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get quiz by id failed", null);
        }
    }

    @Override
    public ResponseCommon<StartQuizResponse> startQuiz(StartQuizRequest startQuizRequest) {
        try {
            Question question = questionRepository.findQuestionByQuizIDAndAndOrdQuestion(startQuizRequest.getQuizId(), 1);
            int sessionId = CommonUtils.getSessionID();
            int totalQuestion = questionRepository.countQuestionsByQuizId(startQuizRequest.getQuizId());
            StartQuizResponse startQuizResponse = new StartQuizResponse();
            startQuizResponse.setQuestion(question);
            startQuizResponse.setSessionId(sessionId);
            startQuizResponse.setTotalQuestion(totalQuestion);
            return new ResponseCommon<>(ResponseCode.SUCCESS, startQuizResponse);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("start  quiz by id failed");
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "start quiz by id failed", null);
        }
    }

    @Override
    public ResponseCommon<FinishQuizResponse> finishQuiz(FinishQuizRequest finishQuizRequest) {
        try {
            User user = userRepository.findByUsername(finishQuizRequest.getUsername()).orElse(null);
            Course course = courseRepository.findCourseById(finishQuizRequest.getCourseId()).orElse(null);
            List<Integer> answerByUser = finishQuizRequest.getAnswerIdList();
            HistoryQuiz historyQuiz = new HistoryQuiz();
            int totalCorrect = 0;
            int totalQuestion = questionRepository.countQuestionsByQuizId(finishQuizRequest.getQuizId());
            int totalIncorrect = totalQuestion - totalCorrect;
            for (int i = 0; i < answerByUser.size(); i++) {
                boolean isCorrect = answerRepository.checkIsCorrect(answerByUser.get(i));
                log.debug(Level.DeString.format("isCorrect: %s", isCorrect));
                if (isCorrect) {
                    totalCorrect++;
                    historyQuiz.setUser(user);
                    historyQuiz.setSessionId(finishQuizRequest.getSessionId());
                    historyQuiz.setAnswerId(answerByUser.get(i));
                    historyQuiz.setCorrect(isCorrect);
                    historyQuiz.setCreatedAt(LocalDateTime.now());
                    historyQuizRepository.save(historyQuiz);
                } else {
                    totalIncorrect++;
                    historyQuiz.setUser(user);
                    historyQuiz.setSessionId(finishQuizRequest.getSessionId());
                    historyQuiz.setAnswerId(answerByUser.get(i));
                    historyQuiz.setCorrect(isCorrect);
                    historyQuiz.setCreatedAt(LocalDateTime.now());
                    historyQuizRepository.save(historyQuiz);
                }
            }
            double mark = (double) totalCorrect / totalQuestion;
            if (mark >= BASE_MARK) {
                log.info("START... Sending email");
                assert user != null;
                assert course != null;
                emailService.sendEmail(setUpMail(user.getEmail(), course.getName()));
                log.info("END... Email sent successfully");
            }
            FinishQuizResponse finishQuizResponse = new FinishQuizResponse();
            finishQuizResponse.setTotalCorrect(totalCorrect);
            finishQuizResponse.setTotalInCorrect(totalIncorrect);
            finishQuizResponse.setPercent(mark);
            return new ResponseCommon<>(ResponseCode.SUCCESS, finishQuizResponse);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("finish quiz  failed");
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "finish quiz  failed", null);
        }
    }

    private Mail setUpMail(String mailTo, String courseName) {
        Mail mail = new Mail();
        mail.setTo(mailTo);
        mail.setSubject("Congratulations on earning your " + courseName);
        Map<String, Object> model = new HashMap<>();
        model.put("course_name", courseName);
        mail.setPros(model);
        mail.setTemplate("certificate");
        return mail;
    }

    @Override
    public ResponseCommon<ResetQuizResponse> resetQuiz(ResetQuizRequest resetQuizRequest) {
        try {

            int newSessionId = CommonUtils.getSessionID();
            ResetQuizResponse resetQuizResponse = new ResetQuizResponse();
            resetQuizResponse.setNewSessionId(newSessionId);
            return new ResponseCommon<>(ResponseCode.SUCCESS, resetQuizResponse);
        } catch (Exception e) {
            log.error("reset quiz  failed");
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "reset quiz  failed", null);
        }
    }

    @Override
    public ResponseCommon<GetAllSessionQuizByUserResponse> getAllSessionQuiz(GetAllSessionQuizByUserRequest getAllSessionQuizByUserRequest) {
        try {
            List<HistoryQuizUser> historyQuizUsers = new ArrayList<>();

            User user = userRepository.findByUsername(getAllSessionQuizByUserRequest.getUsername()).orElse(null);
            List<Integer> listSessionId = historyQuizRepository.findDistinctSessionIdsByUser(user);

            for (int i = 0; i < listSessionId.size(); i++) {
                historyQuizUsers.add(new HistoryQuizUser(
                        listSessionId.get(i),
                        historyQuizRepository.countCorrectAnswersBySessionId(listSessionId.get(i)),
                        historyQuizRepository.countIncorrectAnswersBySessionId(listSessionId.get(i)),
                        historyQuizRepository.findLatestCreatedAtBySessionId(listSessionId.get(i))
                ));
            }
            GetAllSessionQuizByUserResponse response = new GetAllSessionQuizByUserResponse();
            response.setListQuiz(historyQuizUsers);
            return new ResponseCommon<>(ResponseCode.SUCCESS, response);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("get all session quiz  failed");
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "get all session quiz  failed", null);
        }
    }

    @Override
    public ResponseCommon<GetCorrectAnswerBySessionId> getAnswerCorrectBySessionId(GetAnswerCorrectBySessionIdRequest getAnswerCorrectBySessionIdRequest) {
        try {
            List<AnswerCorrectDTO> answerListCorrect = new ArrayList<>();
            List<Answer> answerLists = new ArrayList<>();
            List<Integer> listAnswerId = historyQuizRepository.findAnswerIdsBySessionIdAndCorrect(getAnswerCorrectBySessionIdRequest.getSessionId());
            for (int i = 0; i < listAnswerId.size(); i++) {
                answerLists.add(answerRepository.findAnswerById(listAnswerId.get(i)).orElse(null));
            }
            for (Answer answer : answerLists) {
                AnswerCorrectDTO answerDTO = new AnswerCorrectDTO();
                answerDTO.setId(answer.getId());
                answerDTO.setAnswerContent(answer.getAnswerContent());
                answerDTO.setCorrect(answerDTO.isCorrect());
                answerDTO.setQuestionId(answer.getQuestionId());
                answerListCorrect.add(answerDTO);
            }
            GetCorrectAnswerBySessionId response = new GetCorrectAnswerBySessionId();
            response.setAnswerList(answerListCorrect);
            return new ResponseCommon<>(ResponseCode.SUCCESS, response);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("get correct answer by  session quiz  failed");
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "get correct answer  session quiz  failed", null);
        }
    }
}
