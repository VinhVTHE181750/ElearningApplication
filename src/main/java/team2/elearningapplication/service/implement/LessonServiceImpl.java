package team2.elearningapplication.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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
import team2.elearningapplication.entity.Course;
import team2.elearningapplication.entity.Lesson;
import team2.elearningapplication.entity.LessonCompleted;
import team2.elearningapplication.entity.User;
import team2.elearningapplication.repository.ICourseRepository;
import team2.elearningapplication.repository.ILessonCompletedRepository;
import team2.elearningapplication.repository.ILessonRespository;
import team2.elearningapplication.repository.IUserRepository;
import team2.elearningapplication.service.ILessonService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class LessonServiceImpl implements ILessonService {
    private final ILessonRespository lessonRespository;
    private final ICourseRepository courseRepository;
    private final IUserRepository userRepository;
    private final ILessonCompletedRepository lessonCompletedRespository;

    @Override
    public ResponseCommon<AddLessonResponse> addLesson(AddLessonRequest addLessonRequest) {
        try {
            Lesson lesson = lessonRespository.findLessonByOrdNumberAndCourse(addLessonRequest.getOrdNumber(), addLessonRequest.getCourseID()).orElse(null);
            User user = userRepository.findByUsername(addLessonRequest.getUsername()).orElse(null);
            // if lesson not null -> lesson exists -> tell the user
            if (!Objects.isNull(lesson)) {
                log.debug("Add Lesson failed: Lesson already exists");
                return new ResponseCommon<>(ResponseCode.LESSON_EXIST, null);
            }
            // if lesson is null -> create a new lesson
            else {
                Lesson addLesson = new Lesson();
                addLesson.setName(addLessonRequest.getLessonName());
                addLesson.setCourse(courseRepository.findCourseById(addLessonRequest.getCourseID()).orElse(null));
                addLesson.setOrdNumber(addLessonRequest.getOrdNumber());
                addLesson.setLinkContent(addLessonRequest.getLinkContent());
                addLesson.setDescription(addLessonRequest.getDescription());
                LocalDateTime createdAt = LocalDateTime.now();
                addLesson.setCreatedAt(createdAt);
                addLesson.setUserCreated(user);
                lessonRespository.save(addLesson);
                AddLessonResponse addLessonResponse = new AddLessonResponse();
                addLessonResponse.setLessonName(addLesson.getName());
                addLessonResponse.setLessonID(addLesson.getId());
                addLessonResponse.setOrdNumber(addLesson.getOrdNumber());
                addLessonResponse.setCourseID(addLesson.getCourse().getId());
                addLessonResponse.setLinkContent(addLesson.getLinkContent());
                addLessonResponse.setDescription(addLesson.getDescription());
                addLessonResponse.setCreatedAt(addLesson.getCreatedAt());
                addLessonResponse.setCreatedBy(user.getUsername());
                log.debug("Add Lesson successful");
                return new ResponseCommon<>(ResponseCode.SUCCESS, addLessonResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Add Lesson failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add Lesson Fail", null);
        }
    }

    @Override
    public ResponseCommon<UpdateLessonResponse> updateLesson(UpdateLessonRequest updateLessonRequest) {
        try {
            Lesson lesson = lessonRespository.findLessonById(updateLessonRequest.getLessonID()).orElse(null);
            User user = userRepository.findByUsername(updateLessonRequest.getUsername()).orElse(null);
            // if lesson is null -> lesson does not exist -> tell the user
            if (Objects.isNull(lesson)) {
                log.debug("Update Lesson failed: Lesson does not exist");
                return new ResponseCommon<>(ResponseCode.LESSON_NOT_EXIST, null);
            }
            // if lesson not null -> update the lesson
            else {
                Lesson updateLesson = lesson;
                updateLesson.setName(updateLessonRequest.getLessonName());
                updateLesson.setCourse(courseRepository.findCourseById(updateLessonRequest.getCourseID()).orElse(null));
                updateLesson.setOrdNumber(updateLessonRequest.getOrdNumber());
                updateLesson.setLinkContent(updateLessonRequest.getLinkContent());
                updateLesson.setDescription(updateLessonRequest.getDescription());
                LocalDateTime updateAt = LocalDateTime.now();
                updateLesson.setUpdatedAt(updateAt);
                updateLesson.setDeleted(updateLessonRequest.isDeleted());
                updateLesson.setUserUpdated(user);
                lessonRespository.save(updateLesson);
                UpdateLessonResponse updateLessonResponse = new UpdateLessonResponse();
                updateLessonResponse.setLessonID(updateLesson.getId());
                updateLessonResponse.setLessonName(updateLesson.getName());
                updateLessonResponse.setOrdNumber(updateLesson.getOrdNumber());
                updateLessonResponse.setCourseID(updateLesson.getCourse().getId());
                updateLessonResponse.setLinkContent(updateLesson.getLinkContent());
                updateLessonResponse.setDescription(updateLesson.getDescription());
                updateLessonResponse.setCreatedAt(updateLesson.getCreatedAt());
                updateLessonResponse.setUpdateAt(updateAt);
                updateLessonResponse.setUpdatedBy(updateLesson.getUserUpdated().getUsername());
                updateLessonResponse.setCreatedBy(updateLesson.getUserCreated().getUsername());

                log.debug("Update Lesson successful");
                return new ResponseCommon<>(ResponseCode.SUCCESS, updateLessonResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Update Lesson failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Update Lesson Fail", null);
        }
    }

    @Override
    public ResponseCommon<DeleteLessonResponse> deleteLesson(DeleteLessonRequest deleteLessonRequest) {
        try {
            Lesson lesson = lessonRespository.findLessonById(deleteLessonRequest.getLessonID()).orElse(null);
            User user = userRepository.findByUsername(deleteLessonRequest.getUsername()).orElse(null);
            // if lesson is null -> lesson does not exist -> tell the user
            if (Objects.isNull(lesson)) {
                log.debug("Delete Lesson failed: Lesson does not exist");
                return new ResponseCommon<>(ResponseCode.LESSON_NOT_EXIST, null);
            }
            // if lesson not null -> update the lesson
            else {
                Lesson deleteLesson = lesson;
                deleteLesson.setDeleted(true);
                deleteLesson.setUpdatedAt(LocalDateTime.now());
                deleteLesson.setUserUpdated(user);
                lessonRespository.save(deleteLesson);
                DeleteLessonResponse deleteLessonResponse = new DeleteLessonResponse();
                deleteLessonResponse.setLessonName(deleteLesson.getName());
                deleteLessonResponse.setLessonID(deleteLesson.getId());
                deleteLessonResponse.setOrdNumber(deleteLesson.getOrdNumber());
                deleteLessonResponse.setCourseID(deleteLesson.getCourse().getId());
                deleteLessonResponse.setLinkContent(deleteLesson.getLinkContent());
                deleteLessonResponse.setDescription(deleteLesson.getDescription());
                deleteLessonResponse.setCreatedAt(deleteLesson.getCreatedAt());
                deleteLessonResponse.setUpdatedBy(deleteLesson.getUserUpdated().getUsername());
                deleteLessonResponse.setCreatedBy(deleteLesson.getUserCreated().getUsername());
                LocalDateTime updateAt = LocalDateTime.now();
                deleteLessonResponse.setUpdateAt(updateAt);
                log.debug("Delete Lesson successful");
                return new ResponseCommon<>(ResponseCode.SUCCESS, deleteLessonResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Delete Lesson failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Delete Lesson Fail", null);
        }
    }

    @Override
    public ResponseCommon<FindAllLessonResponse> findAllLesson() {
        try {
            List<Lesson> listLesson = lessonRespository.findAllByIsDeleted(false);
            // if the list of lessons is empty -> tell the user
            if (listLesson.isEmpty()) {
                log.debug("Get all Lesson failed: Lesson list is empty");
                return new ResponseCommon<>(ResponseCode.LESSON_LIST_IS_EMPTY, null);
            } else {
                FindAllLessonResponse response = new FindAllLessonResponse("Get all lesson success", listLesson);
                log.debug("Get all Lesson successful");
                return new ResponseCommon<>(ResponseCode.SUCCESS, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Get all Lesson failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<FindAllLessonResponse> findLessonByDeleted(FindLessonByDeletedRequest findLessonByDeletedRequest) {
        try {
            List<Lesson> listLesson = lessonRespository.findAllByIsDeleted(findLessonByDeletedRequest.isDeleted());
            // if the list of lessons is empty -> tell the user
            if (listLesson.isEmpty()) {
                log.debug("Get all Lesson failed: Lesson list is empty");
                return new ResponseCommon<>(ResponseCode.LESSON_LIST_IS_EMPTY, null);
            } else {
                FindAllLessonResponse response = new FindAllLessonResponse("Get all lesson success", listLesson);
                log.debug("Get all Lesson successful");
                return new ResponseCommon<>(ResponseCode.SUCCESS, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Get all Lesson failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<GetLessonByIdResponse> getLessonById(GetLessonByIdRequest getLessonByIdRequest) {
        try {
            Lesson lesson = lessonRespository.findLessonById(getLessonByIdRequest.getId()).orElse(null);
            // If lesson not exist -> tell user
            if (Objects.isNull(lesson)) {
                log.debug("Get Lesson by id failed: Lesson list not exist");
                return new ResponseCommon<>(ResponseCode.LESSON_NOT_EXIST, null);
            } else {
                GetLessonByIdResponse response = new GetLessonByIdResponse();

                response.setId(lesson.getId());
                response.setName(lesson.getName());
                response.setDescription(lesson.getDescription());
                response.setCourse(lesson.getCourse());
                response.setOrdNumber(lesson.getOrdNumber());
                response.setLinkContent(lesson.getLinkContent());
                response.setCreatedAt(lesson.getCreatedAt());
                response.setDeleted(lesson.isDeleted());

                log.debug("Get Lesson by id successful");
                return new ResponseCommon<>(ResponseCode.SUCCESS, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Get Lesson by id failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<GetLessonPageResponse> getLessonPage(PageRequestDTO pageRequestDTO) {
        try {
            int pageNo = pageRequestDTO.getPageNo();
            int pageSize = pageRequestDTO.getPageSize();
            String sortBy = pageRequestDTO.getSortBy();
            String sortDir = pageRequestDTO.getSortDir();

            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.fromString(sortDir), sortBy));

            Page<Lesson> lessonPage = lessonRespository.findAllByIsDeleted(false, pageable);
            if (lessonPage.isEmpty()) {
                return new ResponseCommon<>(ResponseCode.LESSON_LIST_IS_EMPTY, null);
            }
            GetLessonPageResponse response = new GetLessonPageResponse();
            response.setLessonsList(lessonPage.getContent());
            response.setPageNo(pageNo);
            response.setPageSize(pageSize);
            response.setTotalElements((int) lessonPage.getTotalElements());
            response.setTotalPages(lessonPage.getTotalPages());
            response.setLast(lessonPage.isLast());
            return new ResponseCommon<>(ResponseCode.SUCCESS, response);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Get lesson page An error occurred - " + e.getMessage(), e);
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get lesson page fail", null);
        }
    }

    @Override
    public ResponseCommon<GetLessonByCourseIdResponse> getLessonByCourseId(GetLessonByCourseIdRequest getLessonByCourseIdRequest) {
        try {
            Course course = courseRepository.findCourseById(getLessonByCourseIdRequest.getCourseId()).orElse(null);
            if (Objects.isNull(course)) {
                return new ResponseCommon<>(ResponseCode.COURSE_NOT_EXIST, null);
            } else {
                List<Lesson> lessonList = lessonRespository.findAllByCourse(course);
                GetLessonByCourseIdResponse response = new GetLessonByCourseIdResponse();
                response.setLessonList(lessonList);
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Get lesson by course id success", response);

            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Get lesson by course id An error occurred - " + e.getMessage(), e);
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get lesson by course id fail", null);
        }
    }

    @Override
    public ResponseCommon<LessonCompletedResponse> completeLesson(LessonCompletedRequest completeLessonRequest) {
        try {
            LessonCompletedResponse response = new LessonCompletedResponse();

            // Lấy thông tin người dùng và bài học từ cơ sở dữ liệu
            Lesson lesson = lessonRespository.findById(completeLessonRequest.getLessonId()).orElse(null);
            User user = userRepository.findByUsername(completeLessonRequest.getUsername()).orElse(null);
            Course course = courseRepository.findCourseById(lesson.getCourse().getId()).orElse(null);
            // Kiểm tra xem bài học đã hoàn thành chưa
            LessonCompleted checkLesson = lessonCompletedRespository.findLessonsByUserAndLesson(user, lesson).orElse(null);
            if (checkLesson != null) {
                response.setDone(true);
                response.setMessage("Lesson is done after");
                return new ResponseCommon<>(ResponseCode.FAIL, response);
            }

            LessonCompleted completed = new LessonCompleted();
            completed.setDone(true);
            completed.setUser(user);
            completed.setLesson(lesson);
            completed.setCourse(course);
            lessonCompletedRespository.save(completed);
            response.setDone(true);
            response.setMessage("Done make lesson completed2");
            return new ResponseCommon<>(ResponseCode.SUCCESS,response);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("make lesson completed An error occurred - " + e.getMessage(), e);
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get lesson by course id fail", null);
        }
    }

    @Override
    public ResponseCommon<GetTrackingCourse> trackingCourse(GetTrackingCourseRequest getTrackingCourseRequest) {
        try {
            User user = userRepository.findByUsername(getTrackingCourseRequest.getUsername()).orElse(null);
            int totalLesson = lessonRespository.countLessonsByCourseId(getTrackingCourseRequest.getCourseId());
            List<LessonCompleted> lessonCompleted = lessonCompletedRespository.findCompletedLessonsByUserAndCourse(user.getUsername(), getTrackingCourseRequest.getCourseId());
            int totalCompleted = lessonCompleted.size();
            GetTrackingCourse getTrackingCourse = new GetTrackingCourse();
            getTrackingCourse.setLessonCompleted(totalCompleted);
            getTrackingCourse.setTotalLesson(totalLesson);
            return new ResponseCommon<>(ResponseCode.SUCCESS,getTrackingCourse);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("tracking course An error occurred - " + e.getMessage(), e);
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get lesson by course id fail", null);
        }
    }
}
