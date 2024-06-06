package team2.elearningapplication.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team2.elearningapplication.Enum.ResponseCode;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.admin.answer.*;
import team2.elearningapplication.dto.request.user.answer.GetAnswerByQuestionIdRequest;
import team2.elearningapplication.dto.response.admin.answer.*;
import team2.elearningapplication.dto.response.user.answer.GetAnswerByQuestionIdResponse;
import team2.elearningapplication.service.IAnswerService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/answer")
@AllArgsConstructor
public class AnswerController {
    private final IAnswerService answerService;
    private final Logger log = LoggerFactory.getLogger(AnswerController.class);


    @PostMapping("/add-answer")
    public ResponseEntity<ResponseCommon<AddAnswerResponse>> addAnswer(@Valid @RequestBody AnswerData answerData) {
        ResponseCommon<AddAnswerResponse> response = answerService.addAnswer(answerData);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            log.debug("addAnswer: Answer added successfully.");
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.QUESTION_NOT_EXIST.getCode()) {
            log.debug("addAnswer: Question not found.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Question not found, cannot add answer", null));
        } else {
            log.error("addAnswer: Add answer failed.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add answer failed", null));
        }
    }

    @PutMapping("/update-answer")
    public ResponseEntity<ResponseCommon<UpdateAnswerResponse>> updateAnswer(@Valid @RequestBody UpdateAnswerRequest updateAnswerRequest) {
        ResponseCommon<UpdateAnswerResponse> response = answerService.updateAnswer(updateAnswerRequest);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            log.debug("updateAnswer: Answer updated successfully.");
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.ANSWER_NOT_EXIST.getCode()) {
            log.debug("updateAnswer: Answer not found in the question.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Answer not found in the question", null));
        } else {
            log.error("updateAnswer: Update answer failed.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Update answer failed", null));
        }
    }

    @DeleteMapping("/delete-answer")
    public ResponseEntity<ResponseCommon<DeleteAnswerResponse>> deleteAnswer(@Valid @RequestBody DeleteAnswerRequest deleteAnswerRequest) {
        ResponseCommon<DeleteAnswerResponse> response = answerService.deleteAnswer(deleteAnswerRequest);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            log.debug("deleteAnswer: Answer deleted successfully.");
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.ANSWER_NOT_EXIST.getCode()) {
            log.debug("deleteAnswer: Answer not found in the question.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Answer not found in the question", null));
        } else {
            log.error("deleteAnswer: Delete answer failed.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Delete answer failed", null));
        }
    }

    @GetMapping("/find-all-answer")
    public ResponseEntity<ResponseCommon<List<FindAllAnswerResponse>>> findAllAnswer() {
        ResponseCommon<List<FindAllAnswerResponse>> response = answerService.findAllAnswer();
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            log.debug("findAllAnswer: Found all answers successfully.");
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.ANSWER_LIST_IS_EMPTY.getCode()) {
            log.debug("findAllAnswer: Answer list is empty.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Answer list is empty", null));
        } else {
            log.error("findAllAnswer: Find all answers failed.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Find all answers failed", null));
        }
    }

    @GetMapping("/get-answer-by-id")
    public ResponseEntity<ResponseCommon<GetAnswerByIdResponse>> getAnswerById(GetAnswerByIdRequest request) {
        ResponseCommon<GetAnswerByIdResponse> response = answerService.getAnswerById(request);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            log.debug("Get answers by id successfully.");
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.ANSWER_NOT_EXIST.getCode()) {
            log.debug("Answer not exist.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Answer not exist", null));
        } else {
            log.error("Get answers by id failed.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get answers by id failed", null));
        }
    }

    @GetMapping("/find-all-answer-by-deleted")
    public ResponseEntity<ResponseCommon<List<FindAllAnswerResponse>>> findAllAnswerByDeleted(FindAllAnswerByDeletedRequest findAllAnswerByDeletedRequest) {
        ResponseCommon<List<FindAllAnswerResponse>> response = answerService.findAllAnswerByDeleted(findAllAnswerByDeletedRequest);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            log.debug("findAllAnswer: Found all answers successfully.");
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.ANSWER_LIST_IS_EMPTY.getCode()) {
            log.debug("findAllAnswer: Answer list is empty.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Answer list is empty", null));
        } else {
            log.error("findAllAnswer: Find all answers failed.");
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Find all answers failed", null));
        }
    }

    @GetMapping("/get-answer-by-question-id")
    public ResponseEntity<ResponseCommon<List<GetAnswerByQuestionIdResponse>>> getAnswerByQuestionId(@ParameterObject GetAnswerByQuestionIdRequest getAnswerByQuestionIdRequest) {
        try {

            ResponseCommon<List<GetAnswerByQuestionIdResponse>> response = answerService.getAnswerByQuestionId(getAnswerByQuestionIdRequest);
            if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
                return ResponseEntity.ok(response);
            } else if (response.getCode() == ResponseCode.FAIL.getCode()) {
                return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get answer by question ID failed", null));
            } else {
                return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.ANSWER_LIST_IS_EMPTY.getCode(), "Answer list is empty", null));
            }
        } catch (Exception e) {
            log.error("getAnswerByQuestionId: An error occurred - " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get answer by question ID failed - " + e.getMessage(), null));
        }
    }

}
