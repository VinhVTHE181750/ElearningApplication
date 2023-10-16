package team2.elearningapplication.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team2.elearningapplication.Enum.ResponseCode;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.admin.quiz.AddQuizRequest;
import team2.elearningapplication.dto.request.admin.quiz.DeleteQuizRequest;
import team2.elearningapplication.dto.request.admin.quiz.UpdateQuizRequest;
import team2.elearningapplication.dto.response.admin.quiz.AddQuizResponse;
import team2.elearningapplication.dto.response.admin.quiz.DeleteQuizResponse;
import team2.elearningapplication.dto.response.admin.quiz.FindAllQuizResponse;
import team2.elearningapplication.dto.response.admin.quiz.UpdateQuizResponse;
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
}