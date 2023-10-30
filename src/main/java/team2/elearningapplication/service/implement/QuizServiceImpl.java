package team2.elearningapplication.service.implement;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import team2.elearningapplication.Enum.ResponseCode;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.admin.quiz.*;
import team2.elearningapplication.dto.request.user.quiz.FinishQuizRequest;
import team2.elearningapplication.dto.request.user.quiz.NextQuestionRequest;
import team2.elearningapplication.dto.request.user.quiz.ResetQuizRequest;
import team2.elearningapplication.dto.request.user.quiz.StartQuizRequest;
import team2.elearningapplication.dto.response.admin.quiz.*;
import team2.elearningapplication.dto.response.user.quiz.FinishQuizResponse;
import team2.elearningapplication.dto.response.user.quiz.NextQuestionResponse;
import team2.elearningapplication.dto.response.user.quiz.ResetQuizResponse;
import team2.elearningapplication.dto.response.user.quiz.StartQuizResponse;
import team2.elearningapplication.entity.*;
import team2.elearningapplication.repository.*;
import team2.elearningapplication.service.ILessonService;
import team2.elearningapplication.service.IQuizService;
import team2.elearningapplication.service.email.EmailService;
import team2.elearningapplication.utils.CommonUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements IQuizService {
    private final IQuizRepository quizRepository;
    private final ILessonRespository lessonRespository;
    private  final IQuestionRepository questionRepository;
    private final IAnswerRepository answerRepository;
    private final IUserRepository userRepository;
    private final EmailService emailService;
    private final ICourseRepository courseRepository;
    private final IHistoryQuizRepository historyQuizRepository;
    private static final double BASE_MARK = 0.8;

    private final Logger log = LoggerFactory.getLogger(QuestionServiceImpl.class);

    @Override
    public ResponseCommon<AddQuizResponse> addQuiz(AddQuizRequest addQuizRequest) {
        try {
            Quiz quiz = quizRepository.findQuizByName(addQuizRequest.getQuizName()).orElse(null);
            User user = userRepository.findByUsername(addQuizRequest.getUsername()).orElse(null);

            // if quiz not null -> tell user
            if(!Objects.isNull(quiz)) return new ResponseCommon<>(ResponseCode.QUIZ_EXIST.getCode(),"Quiz already exist",null);
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
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(),"Add Quiz success",addQuizResponse);
            }
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(),"Add Quiz fail",null);
        }
    }

    @Override
    public ResponseCommon<UpdateQuizResponse> updateQuiz(UpdateQuizRequest updateQuizRequest) {
        try {
            Quiz quiz = quizRepository.findQuizById(updateQuizRequest.getQuizID()).orElse(null);
            User user = userRepository.findByUsername(updateQuizRequest.getUsername()).orElse(null);
            // if quiz is null -> tell user
            if(Objects.isNull(quiz)) return  new ResponseCommon<>(ResponseCode.QUIZ_NOT_EXIST.getCode(),"Quiz not exist",null);
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
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(),"Update quiz success",updateQuizResponse);
            }
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(),"Update quiz fail",null);
        }

    }

    @Override
    public ResponseCommon<DeleteQuizResponse> deleteQuiz(DeleteQuizRequest deleteQuizRequest) {
        try {
            Quiz quiz = quizRepository.findQuizById(deleteQuizRequest.getQuizID()).orElse(null);
            User user = userRepository.findByUsername(deleteQuizRequest.getUsername()).orElse(null);
            // if quiz is null -> tell user
            if(Objects.isNull(quiz))
                return  new ResponseCommon<>(ResponseCode.QUIZ_NOT_EXIST.getCode(),"Quiz not exist",null);
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
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(),"Delete quiz success",deleteQuizResponse);
            }
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(),"Delete quiz fail",null);
        }
    }

    @Override
    public ResponseCommon<FindAllQuizResponse> findAllQuiz() {
        try {
            List<Quiz> quizList = quizRepository.findAllByIsDeleted(false);
            if(quizList.isEmpty()) return new ResponseCommon<>(ResponseCode.QUIZ_LIST_IS_EMPTY.getCode(),"Quiz list is empty",null);
            else {
                FindAllQuizResponse findAllQuizResponse = new FindAllQuizResponse();
                findAllQuizResponse.setQuizList(quizList);
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(),"Find all quiz success",findAllQuizResponse);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(),"Find all quiz fail",null);
        }
    }

    @Override
    public ResponseCommon<FindAllQuizResponse> findAllQuizByDeleted(FindQuizByDeletedRequest findQuizByDeletedRequest) {
        try {
            List<Quiz> quizList = quizRepository.findAllByIsDeleted(findQuizByDeletedRequest.isDeleted());
            if(quizList.isEmpty()) return new ResponseCommon<>(ResponseCode.QUIZ_LIST_IS_EMPTY.getCode(),"Quiz list is empty",null);
            else {
                FindAllQuizResponse findAllQuizResponse = new FindAllQuizResponse();
                findAllQuizResponse.setQuizList(quizList);
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(),"Find all quiz success",findAllQuizResponse);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(),"Find all quiz fail",null);
        }
    }

    @Override
    public ResponseCommon<GetQuizByIdResponse> getQuizById(GetQuizByIdRequest getQuizByIdRequest) {
        try {
            Quiz quiz = quizRepository.findQuizById(getQuizByIdRequest.getId()).orElse(null);
            // If quiz not exist -> tell user
            if ( Objects.isNull(quiz) ) {
                log.debug("Quiz not exist");
                return  new ResponseCommon<>(ResponseCode.QUIZ_NOT_EXIST.getCode(),"Quiz not exist",null);
            }
            else {
                GetQuizByIdResponse response = new GetQuizByIdResponse();

                response.setId(quiz.getId());
                response.setName(quiz.getName());
                response.setLesson(quiz.getLesson());
                response.setLesson(quiz.getLesson());
                response.setDeleted(quiz.isDeleted());

                log.debug("Get quiz by id successfully");
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Get quiz by id success", response);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("Get quiz by id failed");
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(),"Get quiz by id failed",null);
        }
    }

    @Override
    public ResponseCommon<StartQuizResponse> startQuiz(StartQuizRequest startQuizRequest) {
        try {
            Question question = questionRepository.findQuestionByQuizIDAndAndOrdQuestion(startQuizRequest.getQuizId(),1);
            int sessionId = CommonUtils.getSessionID();
            int totalQuestion = questionRepository.countQuestionsByQuizId(startQuizRequest.getQuizId());
            StartQuizResponse startQuizResponse = new StartQuizResponse();
            startQuizResponse.setQuestion(question);
            startQuizResponse.setSessionId(sessionId);
            startQuizResponse.setTotalQuestion(totalQuestion);
            return new ResponseCommon<>(ResponseCode.SUCCESS,startQuizResponse);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("start  quiz by id failed");
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(),"start quiz by id failed",null);
        }
    }

//    @Override
//    public ResponseCommon<NextQuestionResponse> nextQuestion(NextQuestionRequest nextQuestionRequest) {
//        try {
//            HistoryAnswer historyAnswer = new HistoryAnswer();
//            Question question = questionRepository.findQuestionByQuizIDAndAndOrdQuestion(nextQuestionRequest.getQuizId(), nextQuestionRequest.getOrdQuestion());
//            log.error("next question: " + question.toString());
//            List<Answer> totalAnswer = answerRepository.findAll();
////            List<Answer> answerList = new ArrayList<>();
//            // handle when multiple choice
////            List<Integer> answerId = nextQuestionRequest.getAnswerId();
////            for (int i = 0; i < totalAnswer.size(); i++) {
////                for (int j = 0; j < answerId.size(); j++) {
////                    if(totalAnswer.get(i).getId() == answerId.get(j)){
////                        answerList.add(totalAnswer.get(i));
////                    }
////                }
////            }
////            for (Answer answer: answerList) {
////                historyAnswer.setUserAnswerId(answer.getId());
////                historyAnswerRepository.save(historyAnswer);
////            }
////            historyAnswer.setUserAnswerId(nextQuestionRequest.getAnswerId());
//            Answer answerCorrect = answerRepository.findCorrectAnswer(nextQuestionRequest.getPreQuestionId());
//            log.error("answer correct" + answerCorrect);
////            historyAnswer.setQuestionId(nextQuestionRequest.getPreQuestionId());
//            historyAnswer.setSessionId(nextQuestionRequest.getSessionId());
//            historyAnswer.setUser(userRepository.findByUsername(nextQuestionRequest.getUsername()).orElse(null));
//            historyAnswerRepository.save(historyAnswer);
////            historyAnswer.setAnswerIdCorrect(answerCorrect.getId());
//            historyAnswerRepository.save(historyAnswer);
//            NextQuestionResponse nextQuestionResponse = new NextQuestionResponse();
//            nextQuestionResponse.setQuestion(question);
//            return new ResponseCommon<>(ResponseCode.SUCCESS,nextQuestionResponse);
//        }catch (Exception e) {
//            e.printStackTrace();
//            log.error("next question failed");
//            return new ResponseCommon<>(ResponseCode.FAIL.getCode(),"next question failed",null);
//        }
//    }

    @Override
    public ResponseCommon<FinishQuizResponse> finishQuiz(FinishQuizRequest finishQuizRequest) {
        try {
            User user = userRepository.findByUsername(finishQuizRequest.getUsername()).orElse(null);
            Course course = courseRepository.findCourseById(finishQuizRequest.getCourseId()).orElse(null);
            List<Integer> answerByUser = finishQuizRequest.getAnswerIdList();
//            List<Integer> answerCorrect = answerRepository.findMatchingAnswerIdsByQuizId(finishQuizRequest.getQuizId());
//            System.out.println(answerCorrect);
//            List<Integer> answerByUserCopy = new ArrayList<>(answerByUser);
//            List<Integer> answerCorrectCopy = new ArrayList<>(answerCorrect);
//            answerByUserCopy.retainAll(answerCorrectCopy);
//            int commonCount = answerByUserCopy.size();
//            System.out.println(commonCount);
//            int differentCount = answerByUser.size() + answerCorrect.size() - 2 * commonCount;
//            System.out.println(differentCount);
//            int totalCorrect = commonCount;
//            int totalIncorrect = differentCount;
//            List<HistoryAnswer> listCorrectAnswer = historyAnswerRepository.findMatchingAnswers(finishQuizRequest.getSessionId());
//            List<HistoryAnswer> listIncorrectAnswer = historyAnswerRepository.findNonMatchingAnswers(finishQuizRequest.getSessionId());
//            int totalCorrect = listCorrectAnswer.size();
//            int totalIncorrect = listIncorrectAnswer.size();
            HistoryQuiz historyQuiz = new HistoryQuiz();
            int totalCorrect = 0;
            int totalQuestion = questionRepository.countQuestionsByQuizId(finishQuizRequest.getQuizId());
            int totalIncorrect = totalQuestion - totalCorrect;
            for (int i = 0; i < answerByUser.size(); i++) {
                boolean isCorrect = answerRepository.checkIsCorrect(answerByUser.get(i));
                log.debug("isCorrect: " + isCorrect);
                if(isCorrect){
                    totalCorrect++;
                    historyQuiz.setUser(user);
                    historyQuiz.setSessionId(finishQuizRequest.getSessionId());
                    historyQuiz.setAnswerId(answerByUser.get(i));
                    historyQuiz.setCorrect(isCorrect);
                    historyQuizRepository.save(historyQuiz);
                } else {
                    totalIncorrect++;
                    historyQuiz.setUser(user);
                    historyQuiz.setSessionId(finishQuizRequest.getSessionId());
                    historyQuiz.setAnswerId(answerByUser.get(i));
                    historyQuiz.setCorrect(isCorrect);
                    historyQuizRepository.save(historyQuiz);
                }
            }
            double mark = totalCorrect/totalQuestion;
            if(mark >= BASE_MARK){
                log.info("START... Sending email");
                emailService.sendEmail(setUpMail(user.getEmail(), course.getName()));
                log.info("END... Email sent success");
            }
            FinishQuizResponse finishQuizResponse = new FinishQuizResponse();
            finishQuizResponse.setTotalCorrect(totalCorrect);
            finishQuizResponse.setTotalInCorrect(totalIncorrect);
            finishQuizResponse.setPercent(mark);
            return new ResponseCommon<>(ResponseCode.SUCCESS,finishQuizResponse);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("finish quiz  failed");
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(),"finish quiz  failed",null);
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
            int sessionId = resetQuizRequest.getSessionId();
            String username = resetQuizRequest.getUsername();
            User user = userRepository.findByUsername(username).orElse(null);
//            historyAnswerRepository.deleteBySessionIdAndUser(sessionId,user);
            int newSessionId = CommonUtils.getSessionID();
            ResetQuizResponse resetQuizResponse = new ResetQuizResponse();
            resetQuizResponse.setNewSessionId(newSessionId);
            return new ResponseCommon<>(ResponseCode.SUCCESS,resetQuizResponse);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("reset quiz  failed");
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(),"reset quiz  failed",null);
        }
    }
}
