package team2.elearningapplication.service;

import team2.elearningapplication.dto.common.PageRequestDTO;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.admin.course.*;
import team2.elearningapplication.dto.request.user.course.CheckEnrollCourseRequest;
import team2.elearningapplication.dto.request.user.course.EnrollCourseRequest;
import team2.elearningapplication.dto.request.user.course.PaymentConfirmRequest;
import team2.elearningapplication.dto.request.user.course.SearchCourseByNameAndCategoryRequest;
import team2.elearningapplication.dto.response.admin.course.*;
import team2.elearningapplication.dto.response.user.course.*;

public interface ICourseService {
    ResponseCommon<AddCourseResponse> addCourse(AddCourseRequest addCourseRequest);

    ResponseCommon<UpdateCourseResponse> updateCourse(UpdateCourseRequest updateCourseRequest);

    ResponseCommon<DeleteCourseResponse> deleteCourse(DeleteCourseRequest deleteCourseRequest);

    ResponseCommon<FindAllCourseResponse> findAllCourse();

    ResponseCommon<FindAllCourseResponse> findAllCourseByDeleted(FindCourseByDeletedRequest findCourseByDeletedRequest);
    ResponseCommon<GetCourseByIdResponse> getCourseById(GetCourseByIdRequest getCourseByIdRequest);

    ResponseCommon<GetTopCourseResponse> getTopCourse(int numberCourse);

    ResponseCommon<GetNewestCourseResponse> getNewestCourse(int numberCourse);

    ResponseCommon<GetCourseByUserResponse> getCourseByUser(String username);

    ResponseCommon<GetTotalCourseResponse> getTotalCourse();

    ResponseCommon<SearchCourseByNameAndCategoryResponse> searchCourse(SearchCourseByNameAndCategoryRequest searchCourseByNameAndCategoryRequest);

    ResponseCommon<PageCourseResponse> getAllCoursePage(PageRequestDTO pageRequestDTO);

    ResponseCommon<EnrollCourseResponse> enrollCourse(EnrollCourseRequest enrollCourseRequest);

    ResponseCommon<PaymentConfirmResponse> paymentConfirm(PaymentConfirmRequest paymentConfirmRequest);

    ResponseCommon<CheckEnrollCourseResponse> isEnrollCourse(CheckEnrollCourseRequest checkEnrollCourseRequest);
}
