package team2.elearningapplication.service;

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

public interface ILessonService {
    ResponseCommon<AddLessonResponse> addLesson(AddLessonRequest addLessonRequest);
    ResponseCommon<UpdateLessonResponse> updateLesson(UpdateLessonRequest updateLessonRequest);
    ResponseCommon<DeleteLessonResponse> deleteLesson(DeleteLessonRequest deleteLessonRequest);

    ResponseCommon<FindAllLessonResponse> findAllLesson();
    ResponseCommon<FindAllLessonResponse> findLessonByDeleted(FindLessonByDeletedRequest findLessonByDeletedRequest);
    ResponseCommon<GetLessonByIdResponse> getLessonById(GetLessonByIdRequest getLessonByIdRequest);

    ResponseCommon<GetLessonPageResponse> getLessonPage(PageRequestDTO pageRequestDTO);

    ResponseCommon<GetLessonByCourseIdResponse> getLessonByCourseId(GetLessonByCourseIdRequest getLessonByCourseIdRequest);
    ResponseCommon<LessonCompletedResponse> completeLesson(LessonCompletedRequest completeLessonRequest);

    ResponseCommon<GetTrackingCourse> trackingCourse(GetTrackingCourseRequest getTrackingCourseRequest);
}
