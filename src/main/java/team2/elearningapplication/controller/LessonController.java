package team2.elearningapplication.controller;

import lombok.AllArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team2.elearningapplication.Enum.ResponseCode;
import team2.elearningapplication.dto.common.PageRequestDTO;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.admin.lesson.*;
import team2.elearningapplication.dto.request.user.lesson.GetLessonByCourseIdRequest;
import team2.elearningapplication.dto.request.user.lesson.GetTrackingCourseRequest;
import team2.elearningapplication.dto.request.user.lesson.LessonCompletedRequest;
import team2.elearningapplication.dto.response.admin.lesson.*;
import team2.elearningapplication.dto.response.user.lesson.GetLessonByCourseIdResponse;
import team2.elearningapplication.dto.response.user.lesson.GetLessonPageResponse;
import team2.elearningapplication.dto.response.user.lesson.GetTrackingCourse;
import team2.elearningapplication.dto.response.user.lesson.LessonCompletedResponse;
import team2.elearningapplication.service.ILessonService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/lesson")
@AllArgsConstructor
public class LessonController {
    private ILessonService lessonService;

    @PostMapping("/add-lesson")
    public ResponseEntity<ResponseCommon<AddLessonResponse>> addLesson(@Valid @RequestBody AddLessonRequest addLessonRequest){
        ResponseCommon<AddLessonResponse> response = lessonService.addLesson(addLessonRequest);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.LESSON_EXIST.getCode()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseCommon<>(response.getCode(), "Lesson already exists", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add lesson failed", null));
        }
    }

    @PutMapping("/update-lesson")
    public ResponseEntity<ResponseCommon<UpdateLessonResponse>> updateLesson(@Valid @RequestBody UpdateLessonRequest updateLessonRequest){
        ResponseCommon<UpdateLessonResponse> response = lessonService.updateLesson(updateLessonRequest);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.LESSON_NOT_EXIST.getCode()) {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Lesson does not exist", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Update lesson failed", null));
        }
    }

    @DeleteMapping("/delete-lesson")
    public ResponseEntity<ResponseCommon<DeleteLessonResponse>> deleteLesson(@Valid @RequestBody DeleteLessonRequest deleteLessonRequest){
        ResponseCommon<DeleteLessonResponse> response = lessonService.deleteLesson(deleteLessonRequest);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.LESSON_NOT_EXIST.getCode()) {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Lesson does not exist", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Delete lesson failed", null));
        }
    }

    @GetMapping("/find-all-lesson")
    public ResponseEntity<ResponseCommon<FindAllLessonResponse>> findAllLesson(){
        ResponseCommon<FindAllLessonResponse> response = lessonService.findAllLesson();
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.LESSON_LIST_IS_EMPTY.getCode()) {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Lesson list is empty", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(),"Get All lesson fail", null));
        }
    }
    @GetMapping("/find-all-lesson-by-deleted")
    public ResponseEntity<ResponseCommon<FindAllLessonResponse>> findAllLessonByDeleted(FindLessonByDeletedRequest findLessonByDeletedRequest){
        ResponseCommon<FindAllLessonResponse> response = lessonService.findLessonByDeleted(findLessonByDeletedRequest);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.LESSON_LIST_IS_EMPTY.getCode()) {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Lesson list is empty", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(),"Get All lesson fail", null));
        }
    }

    @GetMapping("/get-lesson-by-id")
    public ResponseEntity<ResponseCommon<GetLessonByIdResponse>> getLessonById(@ParameterObject GetLessonByIdRequest request){
        ResponseCommon<GetLessonByIdResponse> response = lessonService.getLessonById(request);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.LESSON_NOT_EXIST.getCode()) {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Lesson not exist", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(),"Get lesson by id fail", null));
}
    }

    @GetMapping("/page-lesson")
    public ResponseEntity<ResponseCommon<GetLessonPageResponse>> coursePage(@Valid @RequestBody PageRequestDTO pageRequestDTO){
        ResponseCommon<GetLessonPageResponse> response = lessonService.getLessonPage(pageRequestDTO);
        // if response code quals empty list code -> tell user
        if(response.getCode() == ResponseCode.LESSON_LIST_IS_EMPTY.getCode()){
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.COURSE_LIST_IS_EMPTY.getCode(),"Lesson list is empty",null));
        } else if(response.getCode() == ResponseCode.SUCCESS.getCode()){
            return ResponseEntity.ok().body(new ResponseCommon<>(ResponseCode.SUCCESS.getCode(),"Get  lesson page success",response.getData()));
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(),"Get lesson page fail",null));
        }
    }

    @GetMapping("/get-lessons-by-course")
    public ResponseEntity<ResponseCommon<GetLessonByCourseIdResponse>> getLessonsByCourseId(@ParameterObject GetLessonByCourseIdRequest getLessonByCourseIdRequest) {
        try {
            ResponseCommon<GetLessonByCourseIdResponse> response = lessonService.getLessonByCourseId(getLessonByCourseIdRequest);

            if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
                return ResponseEntity.ok(response);
            } else if (response.getCode() == ResponseCode.COURSE_NOT_EXIST.getCode()) {
                return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Course not exist", null));
            } else {
                return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get lessons by course id fail", null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get lessons by course id fail", null));
        }
    }

    @PostMapping("/complete-lesson")
    public ResponseEntity<LessonCompletedResponse> completeLesson(@Valid @RequestBody LessonCompletedRequest completeLessonRequest) {
        try {
            ResponseCommon<LessonCompletedResponse> response = lessonService.completeLesson(completeLessonRequest);

            if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
                return ResponseEntity.ok(response.getData());
            } else {
                return ResponseEntity.badRequest().body(response.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new LessonCompletedResponse());
        }
    }

    @GetMapping("/get-tracking-course")
    public ResponseEntity<GetTrackingCourse> getTrackingCourse(@ParameterObject GetTrackingCourseRequest getTrackingCourseRequest) {
        try {
            ResponseCommon<GetTrackingCourse> response = lessonService.trackingCourse(getTrackingCourseRequest);

            if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
                return ResponseEntity.ok(response.getData());
            } else {
                return ResponseEntity.badRequest().body(new GetTrackingCourse());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new GetTrackingCourse());
        }
    }
}
