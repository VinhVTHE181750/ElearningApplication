package team2.elearningapplication.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team2.elearningapplication.Enum.ResponseCode;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.admin.quiz.*;
import team2.elearningapplication.dto.request.user.quiz.*;
import team2.elearningapplication.dto.response.admin.quiz.*;
import team2.elearningapplication.dto.response.user.quiz.*;
import team2.elearningapplication.service.IQuizService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/quiz")
@AllArgsConstructor
public class QuizController {
    private final IQuizService quizService;

    private final Logger log = LoggerFactory.getLogger(QuizController.class);

    @PostMapping("/add-quiz")
    public ResponseEntity<ResponseCommon<AddQuizResponse>> addQuiz(@Valid @RequestBody AddQuizRequest addQuizRequest) {
        ResponseCommon<AddQuizResponse> response = quizService.addQuiz(addQuizRequest);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            log.debug("addQuiz: Quiz added successfully.");
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.QUIZ_EXIST.getCode()) {
            log.debug("addQuiz: Quiz already exists.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Quiz already exists", null));
        } else {
            log.error("addQuiz: Add quiz failed.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add quiz failed", null));
        }
    }

    @PutMapping("/update-quiz")
    public ResponseEntity<ResponseCommon<UpdateQuizResponse>> updateQuiz(@Valid @RequestBody UpdateQuizRequest updateQuizRequest) {
        ResponseCommon<UpdateQuizResponse> response = quizService.updateQuiz(updateQuizRequest);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            log.debug("updateQuiz: Quiz updated successfully.");
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.QUIZ_NOT_EXIST.getCode()) {
            log.debug("updateQuiz: Quiz not found.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Quiz not found", null));
        } else {
            log.error("updateQuiz: Update quiz failed.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Update quiz failed", null));
        }
    }

    @DeleteMapping("/delete-quiz")
    public ResponseEntity<ResponseCommon<DeleteQuizResponse>> deleteQuiz(@Valid @RequestBody DeleteQuizRequest deleteQuizRequest) {
        ResponseCommon<DeleteQuizResponse> response = quizService.deleteQuiz(deleteQuizRequest);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            log.debug("deleteQuiz: Quiz deleted successfully.");
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.QUIZ_NOT_EXIST.getCode()) {
            log.debug("deleteQuiz: Quiz not found.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Quiz not found", null));
        } else {
            log.error("deleteQuiz: Delete quiz failed.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Delete quiz failed", null));
        }
    }

    @GetMapping("/find-all-quiz")
    public ResponseEntity<ResponseCommon<FindAllQuizResponse>> findAllQuiz() {
        ResponseCommon<FindAllQuizResponse> response = quizService.findAllQuiz();
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            log.debug("findAllQuiz: Found all quizzes successfully.");
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.QUIZ_LIST_IS_EMPTY.getCode()) {
            log.debug("findAllQuiz: Quiz list is empty.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Quiz list is empty", null));
        } else {
            log.error("findAllQuiz: Find all quizzes failed.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Find all quizzes failed", null));
        }
    }


    @GetMapping("/find-all-quiz-by-deleted")
    public ResponseEntity<ResponseCommon<FindAllQuizResponse>> findAllQuizByDeleted(FindQuizByDeletedRequest findQuizByDeletedRequest) {
        ResponseCommon<FindAllQuizResponse> response = quizService.findAllQuizByDeleted(findQuizByDeletedRequest);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            log.debug("findAllQuiz: Found all quizzes successfully.");
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.QUIZ_LIST_IS_EMPTY.getCode()) {
            log.debug("findAllQuiz: Quiz list is empty.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Quiz list is empty", null));
        } else {
            log.error("findAllQuiz: Find all quizzes failed.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Find all quizzes failed", null));
        }
    }

    @GetMapping("/get-quiz-by-id")
    public ResponseEntity<ResponseCommon<GetQuizByIdResponse>> getQuizById(GetQuizByIdRequest request) {
        ResponseCommon<GetQuizByIdResponse> response = quizService.getQuizById(request);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            log.debug("Get quiz by id successfully.");
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.QUIZ_NOT_EXIST.getCode()) {
            log.debug("Quiz not exist.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Quiz not exist", null));
        } else {
            log.error("Get quiz by id failed");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get quiz by id failed", null));
        }
    }

    @GetMapping("/start-quiz")
    public ResponseEntity<ResponseCommon<StartQuizResponse>> startQuiz(@ParameterObject StartQuizRequest startQuizRequest) {
        ResponseCommon<StartQuizResponse> response = quizService.startQuiz(startQuizRequest);

        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            log.debug("Quiz started successfully.");
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.QUIZ_NOT_EXIST.getCode()) {
            log.debug("Quiz not exist.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Quiz not exist", null));
        } else {
            log.error("Start quiz by ID failed");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Start quiz by ID failed", null));
        }
    }

//    @PostMapping("/next-question")
//    public ResponseEntity<ResponseCommon<NextQuestionResponse>> nextQuestion(@Valid @RequestBody NextQuestionRequest nextQuestionRequest) {
//        ResponseCommon<NextQuestionResponse> response = quizService.nextQuestion(nextQuestionRequest);
//
//        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
//            log.debug("Next question retrieved successfully.");
//            return ResponseEntity.ok(response);
//        } else if (response.getCode() == ResponseCode.FAIL.getCode()) {
//            log.error("Next question retrieval failed");
//            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Next question retrieval failed", null));
//        } else {
//            log.error("Next question retrieval failed");
//            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Next question retrieval failed", null));
//        }
//    }

    @PostMapping("/finish-quiz")
    public ResponseEntity<ResponseCommon<FinishQuizResponse>> finishQuiz(@Valid @RequestBody FinishQuizRequest finishQuizRequest) {
        ResponseCommon<FinishQuizResponse> response = quizService.finishQuiz(finishQuizRequest);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            log.debug("Quiz finished successfully.");
            return ResponseEntity.ok(response);
        } else {
            log.error("Quiz finishing failed");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Quiz finishing failed", null));
        }
    }

    @PostMapping("/reset-quiz")
    public ResponseEntity<ResponseCommon<ResetQuizResponse>> resetQuiz(@Valid @RequestBody ResetQuizRequest resetQuizRequest) {
        ResponseCommon<ResetQuizResponse> response = quizService.resetQuiz(resetQuizRequest);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            log.debug("Quiz reset successfully.");
            return ResponseEntity.ok(response);
        } else {
            log.error("Quiz reset failed");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Quiz reset failed", null));
        }
    }

    @GetMapping("/get-all-session-quiz")
    public ResponseEntity<ResponseCommon<GetAllSessionQuizByUserResponse>> getAllSessionQuiz(
            @ParameterObject GetAllSessionQuizByUserRequest getAllSessionQuizByUserRequest
    ) {
        ResponseCommon<GetAllSessionQuizByUserResponse> response = quizService.getAllSessionQuiz(getAllSessionQuizByUserRequest);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Get all session quiz failed", null));
        }
    }

    @GetMapping("/get-correct-answers-by-session")
    public ResponseEntity<ResponseCommon<GetCorrectAnswerBySessionId>> getCorrectAnswersBySession(
            @RequestParam("sessionId") int sessionId
    ) {
        GetAnswerCorrectBySessionIdRequest request = new GetAnswerCorrectBySessionIdRequest();
        request.setSessionId(sessionId);

        ResponseCommon<GetCorrectAnswerBySessionId> response = quizService.getAnswerCorrectBySessionId(request);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Get correct answers by session failed", null));
        }
    }
}
