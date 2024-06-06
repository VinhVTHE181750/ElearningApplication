package team2.elearningapplication.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import team2.elearningapplication.Enum.ResponseCode;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.admin.course.AddCourseRequest;
import team2.elearningapplication.dto.response.admin.course.AddCourseResponse;
import team2.elearningapplication.service.ICourseService;
import team2.elearningapplication.service.IPaymentService;

import javax.validation.Valid;

public class CommentController {
    private final int TOP_COURSE = 10;
    private ICourseService courseService;
    private IPaymentService paymentService;

    @PostMapping("/add-course")
    public ResponseEntity<ResponseCommon<AddCourseResponse>> addCourse(@Valid @RequestBody AddCourseRequest addCourseRequest) {
        ResponseCommon<AddCourseResponse> response = courseService.addCourse(addCourseRequest);
        // if code of response equal code SUCCESS  -> return ok
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.COURSE_EXIST.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseCommon<>(response.getCode(), "Course already exsit", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add course fail", null));
        }
        public ResponseEntity<ResponseCommon<AddCourseResponse>> addCourse (@Valid @RequestBody AddCourseRequest
        addCourseRequest){
            ResponseCommon<AddCourseResponse> response = courseService.addCourse(addCourseRequest);
            // if code of response equal code SUCCESS  -> return ok
            if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
                return ResponseEntity.ok(response);
            } else if (response.getCode() == ResponseCode.COURSE_EXIST.getCode()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseCommon<>(response.getCode(), "Course already exsit", null));
            } else {
                return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add course fail", null));
            }
            public ResponseEntity<ResponseCommon<AddCourseResponse>> addCourse (@Valid @RequestBody AddCourseRequest
            addCourseRequest){
                ResponseCommon<AddCourseResponse> response = courseService.addCourse(addCourseRequest);
                // if code of response equal code SUCCESS  -> return ok
                if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
                    return ResponseEntity.ok(response);
                } else if (response.getCode() == ResponseCode.COURSE_EXIST.getCode()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseCommon<>(response.getCode(), "Course already exsit", null));
                } else {
                    return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add course fail", null));
                }
            }
            public ResponseEntity<ResponseCommon<AddCourseResponse>> addCourse (@Valid @RequestBody AddCourseRequest
            addCourseRequest){
                ResponseCommon<AddCourseResponse> response = courseService.addCourse(addCourseRequest);
                // if code of response equal code SUCCESS  -> return ok
                if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
                    return ResponseEntity.ok(response);
                } else if (response.getCode() == ResponseCode.COURSE_EXIST.getCode()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseCommon<>(response.getCode(), "Course already exsit", null));
                } else {
                    return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add course fail", null));
                }
            }
            public ResponseEntity<ResponseCommon<AddCourseResponse>> addCourse (@Valid @RequestBody AddCourseRequest
            addCourseRequest){
                ResponseCommon<AddCourseResponse> response = courseService.addCourse(addCourseRequest);
                // if code of response equal code SUCCESS  -> return ok
                if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
                    return ResponseEntity.ok(response);
                } else if (response.getCode() == ResponseCode.COURSE_EXIST.getCode()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseCommon<>(response.getCode(), "Course already exsit", null));
                } else {
                    return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add course fail", null));
                }
            }
            public ResponseEntity<ResponseCommon<AddCourseResponse>> addCourse (@Valid @RequestBody AddCourseRequest
            addCourseRequest){
                ResponseCommon<AddCourseResponse> response = courseService.addCourse(addCourseRequest);
                // if code of response equal code SUCCESS  -> return ok
                if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
                    return ResponseEntity.ok(response);
                } else if (response.getCode() == ResponseCode.COURSE_EXIST.getCode()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseCommon<>(response.getCode(), "Course already exsit", null));
                } else {
                    return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add course fail", null));
                }
            }
            el

        }
