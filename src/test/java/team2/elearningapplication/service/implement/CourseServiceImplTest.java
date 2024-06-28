package team2.elearningapplication.service.implement;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.yaml.snakeyaml.Yaml;
import team2.elearningapplication.Enum.ResponseCode;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.admin.course.GetCourseByIdRequest;
import team2.elearningapplication.dto.request.admin.course.UpdateCourseRequest;
import team2.elearningapplication.dto.response.admin.course.AddCourseResponse;
import team2.elearningapplication.dto.response.admin.course.GetCourseByIdResponse;
import team2.elearningapplication.dto.response.admin.course.UpdateCourseResponse;
import team2.elearningapplication.entity.*;
import team2.elearningapplication.repository.ICourseRepository;
import team2.elearningapplication.repository.IUserRepository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CourseServiceImplTest extends Mockito {
    // respones
    private final ResponseCommon<GetCourseByIdResponse> COURSE_NOT_EXIST = new ResponseCommon<>(ResponseCode.COURSE_NOT_EXIST.getCode(), "Course not found", null);
    // mock list to replace database
    private static ArrayList<Course> courses = new ArrayList<>();
    private static ArrayList<Category> categorys = new ArrayList<>();
    private static ArrayList<User> users = new ArrayList<>();
    // mock service
    @InjectMocks
    CourseServiceImpl courseService;
    @Mock
    ICourseRepository courseRepository;
    @Mock
    IUserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @BeforeAll
    static void setData() {
        Yaml yaml = new Yaml();
        try (InputStream in = AnswerServiceImplTest.class.getClassLoader().getResourceAsStream("data.yml")) {
            Map<String, Object> yamlData = yaml.load(in);
            categorys = new ArrayList<Category>();
            List<Map<String, Object>> yamlCategory = (List<Map<String, Object>>) yamlData.get("category");
            for (Map<String, Object> c : yamlCategory) {
                Category category = new Category();
                category.setId((Integer) c.get("id"));
                category.setName((String) c.get("categoryName"));
                categorys.add(category);
            }
            courses = new ArrayList<Course>();

            List<Map<String, Object>> yamlCourse = (List<Map<String, Object>>) yamlData.get("course");

            for (Map<String, Object> c : yamlCourse) {
                Course course = new Course();
                course.setId((Integer) c.get("id"));
                course.setName((String) c.get("courseName"));
                course.setDescription((String) c.get("description"));
                course.setPrice((Integer) c.get("price"));
                course.setCategory(categorys.get((Integer) c.get("id")));
                course.setLinkThumnail((String) c.get("link_image"));
                // bo qua cot created_atcreated_by, updated_by
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void addCourse(CourseData courseData, ResponseCommon<AddCourseResponse> expectedResponse) {
        reset(courseRepository);
        when(courseRepository.save(any(Course.class))).then(invocation -> {
            Course course = invocation.getArgument(0);
            course.setId(courses.size() + 1);
            courses.add(course);
            return course;
        });
        var response = courseService.addCourse(courseData);
        verify(courseRepository, times(1)).save(any(Course.class));
        assertNotNull(response);
        assertEquals(expectedResponse.getCode(), response.getCode());
        assertEquals(expectedResponse.getMessage(), response.getMessage());
        // data: AddCourseResponse
        if(response.getData() != null && expectedResponse.getData() != null){
            AddCourseResponse expectedData = (AddCourseResponse) expectedResponse.getData();
            AddCourseResponse actualData = (AddCourseResponse) response.getData();

            assertEquals(expectedData.getCourseID(), actualData.getCourseID());
            assertEquals(expectedData.getCourseName(), actualData.getCourseName());
            assertEquals(expectedData.getDescription(), actualData.getDescription());
            assertEquals(expectedData.getPrice(), actualData.getPrice());
            assertEquals(expectedData.getCategory(), actualData.getCategory());
            assertEquals(expectedData.getLinkThumail(), actualData.getLinkThumail());
        }
    };

    void updateCourse(UpdateCourseRequest request, ResponseCommon<UpdateCourseResponse> expectedResponse) {
        when(courseRepository.findCourseById(request.getCourseID())).then(innocation -> {
            int courseiID = request.getCourseID();
            for(Course course : courses){
                if(course.getId() == courseiID){
                    return Optional.of(course);
                }
            }
            return Optional.empty();
        });

        when(userRepository.findByUsername(request.getUsername())).then(invocation -> {
            for(User user: users){
                if(user.getUsername().equals(request.getUsername())){
                    return Optional.of(user);
                }
            }
            return Optional.empty();
        });
        var response = courseService.updateCourse(request);
        verify(courseRepository, times(1)).findCourseById(request.getCourseID());
        assertNotNull(response);
        assertEquals(expectedResponse.getCode(), response.getCode());
        assertEquals(expectedResponse.getMessage(), response.getMessage());
        // data: UpdateCourseResponse
        if(response.getData() != null && expectedResponse.getData() != null){
            UpdateCourseResponse expectedData = (UpdateCourseResponse) expectedResponse.getData();
            UpdateCourseResponse actualData = (UpdateCourseResponse) response.getData();

            assertEquals(expectedData.getCourseID(), actualData.getCourseID());
            assertEquals(expectedData.getCourseName(), actualData.getCourseName());
            assertEquals(expectedData.getDescription(), actualData.getDescription());
            assertEquals(expectedData.getPrice(), actualData.getPrice());
            assertEquals(expectedData.getCategory(), actualData.getCategory());
            assertEquals(expectedData.getLinkThumail(), actualData.getLinkThumail());
        }
    }
    private ResponseCommon<UpdateCourseResponse> expectedUpdateResponse(Course course){
        UpdateCourseResponse expectedResponse = new UpdateCourseResponse();

        expectedResponse.setCourseID(course.getId());
        expectedResponse.setCourseName(course.getName());
        expectedResponse.setDescription(course.getDescription());
        expectedResponse.setPrice(course.getPrice());
        expectedResponse.setCategory(course.getCategory());
        expectedResponse.setLinkThumail(course.getLinkThumnail());
        return new ResponseCommon<>(expectedResponse);
    }
    @Test
    void deleteCourse() {
    }
    private Course getCourse(int id){
        return courses.get(id);
    }

    void getCourseById(GetCourseByIdRequest request, ResponseCommon<GetCourseByIdResponse> expectedResponse) {
        final int id = request.getId();
        reset(courseRepository);
        when(courseRepository.findCourseById(id)).then(invocation -> {
            if(id > 0 && id <= courses.size()){
                return Optional.of(getCourse(id - 1));
            } else{
                return Optional.empty();
            }
        });
        var response = courseService.getCourseById(request);
        verify(courseRepository, times(1)).findCourseById(id);
        assertNotNull(response);
        assertEquals(expectedResponse.getCode(), response.getCode());
        assertEquals(expectedResponse.getMessage(), response.getMessage());
        // data: Course
        if(response.getData() != null && expectedResponse.getData() != null){
            GetCourseByIdResponse expectedData = (GetCourseByIdResponse) expectedResponse.getData();
            GetCourseByIdResponse actualData = (GetCourseByIdResponse) response.getData();

            assertEquals(expectedData.getId(), actualData.getId());
            assertEquals(expectedData.getName(), actualData.getName());
            assertEquals(expectedData.getDescription(), actualData.getDescription());
            assertEquals(expectedData.getPrice(), actualData.getPrice());
            assertEquals(expectedData.getCategory(), actualData.getCategory());
            assertEquals(expectedData.getLinkThumail(), actualData.getLinkThumail());

        }
    }

    // generate expected response
    private ResponseCommon<GetCourseByIdResponse> expectedGetResponse(Course course){
        GetCourseByIdResponse expectedResponse = new GetCourseByIdResponse();

        expectedResponse.setId(course.getId());
        expectedResponse.setName(course.getName());
        expectedResponse.setDescription(course.getDescription());
        expectedResponse.setPrice(course.getPrice());
        expectedResponse.setCategory(course.getCategory());
        expectedResponse.setLinkThumail(course.getLinkThumnail());
        return new ResponseCommon<>(expectedResponse);
    }

    static Stream<Integer> provideIds() {
        Yaml yaml = new Yaml();
        List<Integer> listIDs = new ArrayList<>();
        try (InputStream in = AnswerServiceImplTest.class.getClassLoader().getResourceAsStream("test-cases.yml")) {
            Map<String, Object> yamlData = yaml.load(in);
            Map<String, List<Integer>> yamlTestCases = (Map<String, List<Integer>>) yamlData.get("getCourseById");
            listIDs = yamlTestCases.get("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listIDs.stream();
    }
    @ParameterizedTest
    @MethodSource("provideIds")
    void getCourseByID(int id){
        GetCourseByIdRequest request = new GetCourseByIdRequest();
        request.setId(id);
        if (id < 1 || id > courses.size()) {
            getCourseById(request, COURSE_NOT_EXIST);
        } else {
            Course course = courses.get(id - 1);
            ResponseCommon<GetCourseByIdResponse> expectedResponse = expectedGetResponse(course);
            getCourseById(request, expectedResponse);
        }
    }
}