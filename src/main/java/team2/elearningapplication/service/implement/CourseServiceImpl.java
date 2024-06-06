package team2.elearningapplication.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team2.elearningapplication.Enum.*;
import team2.elearningapplication.config.VnPayConfig;
import team2.elearningapplication.dto.common.PageRequestDTO;
import team2.elearningapplication.dto.common.PaymentRes;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.admin.course.*;
import team2.elearningapplication.dto.request.user.course.CheckEnrollCourseRequest;
import team2.elearningapplication.dto.request.user.course.EnrollCourseRequest;
import team2.elearningapplication.dto.request.user.course.PaymentConfirmRequest;
import team2.elearningapplication.dto.request.user.course.SearchCourseByNameAndCategoryRequest;
import team2.elearningapplication.dto.response.admin.course.*;
import team2.elearningapplication.dto.response.user.course.*;
import team2.elearningapplication.entity.*;
import team2.elearningapplication.repository.*;
import team2.elearningapplication.service.ICourseService;
import team2.elearningapplication.service.email.EmailService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements ICourseService {
    private final ICourseRepository courseRepository;
    private final ICategoryRepository categoryRepository;
    private final IUserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final IPaymentRepository paymentRepository;
    private final IHistoryResgisterCourseRepository historyRegisterCourseRepository;
    private final EmailService emailService;

    @Override
    public ResponseCommon<AddCourseResponse> addCourse(AddCourseRequest addCourseRequest) {
        try {
            Course course = courseRepository.findCourseByName(addCourseRequest.getName()).orElse(null);
            User user = userRepository.findByUsername(addCourseRequest.getUsername()).orElse(null);
            // if course not null -> tell the user
            if (!Objects.isNull(course)) {
                log.debug("Add Course failed: Course already exists");
                return new ResponseCommon<>(ResponseCode.COURSE_EXIST, null);
            }
            // if course is null -> new course
            if (Objects.isNull(course)) {
                course = new Course();
            }
            course.setName(addCourseRequest.getName());
            course.setDescription(addCourseRequest.getDescription());
            course.setLinkThumnail(addCourseRequest.getLink_thumnail());
            course.setPrice(addCourseRequest.getPrice());
            course.setCreatedAt(LocalDateTime.now());
            Category category = categoryRepository.findCategoryByName(addCourseRequest.getCategory()).orElse(null);
            System.out.println(category);
            course.setCategory(category);
            course.setUserCreated(user);

            // Save course to the database
            Course savedCourse = courseRepository.save(course);

            // If the course is not saved successfully, return a FAIL response
            if (savedCourse == null) {
                log.debug("Add Course failed: Unable to save the course");
                return new ResponseCommon<>(ResponseCode.FAIL, null);
            }

            AddCourseResponse addCourseResponse = new AddCourseResponse();
            addCourseResponse.setCourseID(course.getId());
            addCourseResponse.setCourseName(course.getName());
            addCourseResponse.setDescription(course.getDescription());
            addCourseResponse.setPrice(course.getPrice());
            addCourseResponse.setCategory(course.getCategory());
            addCourseResponse.setLinkThumail(course.getLinkThumnail());
            addCourseResponse.setCreatedAt(course.getCreatedAt());
            addCourseResponse.setCreatedBy(user.getUsername());
            log.debug("Add Course successful");
            return new ResponseCommon<>(ResponseCode.SUCCESS, addCourseResponse);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Add Course failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<UpdateCourseResponse> updateCourse(UpdateCourseRequest updateCourseRequest) {
        try {
            Course courseExist = courseRepository.findCourseById(updateCourseRequest.getCourseID()).orElse(null);
            User user = userRepository.findByUsername(updateCourseRequest.getUsername()).orElse(null);
            // if courseExist is null -> tell the user
            if (Objects.isNull(courseExist)) {
                log.debug("Update Course failed: Course does not exist");
                return new ResponseCommon<>(ResponseCode.COURSE_NOT_EXIST, null);
            } else {
                Category category = categoryRepository.findCategoryById(updateCourseRequest.getCategoryID()).orElse(null);
                Course courseUpdate = courseExist;
                courseUpdate.setCategory(category);
                courseUpdate.setName(updateCourseRequest.getName());
                courseUpdate.setDescription(updateCourseRequest.getDescription());
                courseUpdate.setPrice(updateCourseRequest.getPrice());
                courseUpdate.setLinkThumnail(updateCourseRequest.getLink_thumnail());
                courseUpdate.setCreatedAt(LocalDateTime.now());
                courseUpdate.setUpdatedAt(LocalDateTime.now());
                courseUpdate.setDeleted(updateCourseRequest.isDeleted());
                courseUpdate.setUserUpdated(user);
                courseRepository.save(courseUpdate);
                UpdateCourseResponse updateCourseResponse = new UpdateCourseResponse();
                updateCourseResponse.setCourseID(courseUpdate.getId());
                updateCourseResponse.setCourseName(courseUpdate.getName());
                updateCourseResponse.setDescription(courseUpdate.getDescription());
                updateCourseResponse.setPrice(courseUpdate.getPrice());
                updateCourseResponse.setCategory(courseUpdate.getCategory());
                updateCourseResponse.setLinkThumail(courseUpdate.getLinkThumnail());
                LocalDateTime localDateTime = LocalDateTime.now();
                updateCourseResponse.setUpdateAt(localDateTime);
                updateCourseResponse.setCreateAt(courseUpdate.getCreatedAt());
                updateCourseResponse.setUpdatedBy(courseUpdate.getUserUpdated().getUsername());
                updateCourseResponse.setCreatedBy(courseUpdate.getUserCreated().getUsername());
                log.debug("Update Course successful");
                return new ResponseCommon<>(ResponseCode.SUCCESS, updateCourseResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Update Course failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<DeleteCourseResponse> deleteCourse(DeleteCourseRequest deleteCourseRequest) {
        try {
            Course courseExist = courseRepository.findCourseById(deleteCourseRequest.getCourseID()).orElse(null);
            User user = userRepository.findByUsername(deleteCourseRequest.getUsername()).orElse(null);
            // if courseExist is null -> tell the user
            if (Objects.isNull(courseExist)) {
                log.debug("Delete Course failed: Course does not exist");
                return new ResponseCommon<>(ResponseCode.COURSE_NOT_EXIST, null);
            } else {
                Course courseDelete = courseExist;
                courseDelete.setDeleted(true);
                courseDelete.setUpdatedAt(LocalDateTime.now());
                courseDelete.setUserUpdated(user);
                courseRepository.save(courseDelete);
                DeleteCourseResponse deleteCourseResponse = new DeleteCourseResponse();
                deleteCourseResponse.setCourseID(courseDelete.getId());
                deleteCourseResponse.setCourseName(courseDelete.getName());
                deleteCourseResponse.setDescription(courseDelete.getDescription());
                deleteCourseResponse.setPrice(courseDelete.getPrice());
                deleteCourseResponse.setCategory(courseDelete.getCategory());
                deleteCourseResponse.setLinkThumail(courseDelete.getLinkThumnail());
                deleteCourseResponse.setCreatedAt(courseDelete.getCreatedAt());
                deleteCourseResponse.setDeleted(courseDelete.isDeleted());
                deleteCourseResponse.setCreatedBy(courseDelete.getUserCreated().getUsername());
                deleteCourseResponse.setUpdatedBy(courseDelete.getUserUpdated().getUsername());
                log.debug("Delete Course successful");
                return new ResponseCommon<>(ResponseCode.SUCCESS, deleteCourseResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Delete Course failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    public ResponseCommon<FindAllCourseResponse> findAllCourse() {
        try {
            // Get all courses with isDeleted is false
            List<Course> listCourse = courseRepository.findAllByIsDeleted(false);

            // if listCourse is empty -> tell the user
            if (listCourse.isEmpty()) {
                log.debug("Get all Course failed: Course list is empty");
                return new ResponseCommon<>(ResponseCode.COURSE_LIST_IS_EMPTY, null);
            } // else -> return the list of courses
            else {
                FindAllCourseResponse response = new FindAllCourseResponse("Get all success", listCourse);
                log.debug("Get all Course successful");
                return new ResponseCommon<>(ResponseCode.SUCCESS, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Get all Course failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<FindAllCourseResponse> findAllCourseByDeleted(FindCourseByDeletedRequest findCourseByDeletedRequest) {
        try {
            // Get all courses with isDeleted is false
            List<Course> listCourse = courseRepository.findAllByIsDeleted(findCourseByDeletedRequest.isDeleted());

            // if listCourse is empty -> tell the user
            if (listCourse.isEmpty()) {
                log.debug("Get all Course failed: Course list is empty");
                return new ResponseCommon<>(ResponseCode.COURSE_LIST_IS_EMPTY, null);
            } // else -> return the list of courses
            else {
                FindAllCourseResponse response = new FindAllCourseResponse("Get all success", listCourse);
                log.debug("Get all Course successful");
                return new ResponseCommon<>(ResponseCode.SUCCESS, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Get all Course failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<GetCourseByIdResponse> getCourseById(GetCourseByIdRequest getCourseByIdRequest) {
        try {
            Course course = courseRepository.findCourseById(getCourseByIdRequest.getId()).orElse(null);
            // If course does not exist
            if (Objects.isNull(course)) {
                log.debug("Get Course By Id failed: Course does not exist");
                return new ResponseCommon<>(ResponseCode.COURSE_NOT_EXIST, null);
            } else {
                GetCourseByIdResponse response = new GetCourseByIdResponse();
                response.setId(course.getId());
                response.setName(course.getName());
                response.setDescription(course.getDescription());
                response.setCategory(course.getCategory());
                response.setPrice(course.getPrice());
                response.setLinkThumail(course.getLinkThumnail());
                response.setCreateAt(course.getCreatedAt());
                response.setDeleted(course.isDeleted());

                log.debug("Get Course by id successful");
                return new ResponseCommon<>(ResponseCode.SUCCESS, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Get Course By Id failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<GetTopCourseResponse> getTopCourse(int numberCourse) {
        try {
            List<Course> topCourse = courseRepository.getTopCourses(numberCourse);
            // if topCourse is empty -> tell user
            if (topCourse.isEmpty()) {
                return new ResponseCommon<>(ResponseCode.COURSE_LIST_IS_EMPTY.getCode(), "Course list is empty", null);
            } else {
                GetTopCourseResponse getTopCourseResponse = new GetTopCourseResponse(topCourse);
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Get top course success", getTopCourseResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Get Top Course failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<GetNewestCourseResponse> getNewestCourse(int numberCourse) {
        try {
            List<Course> topNewestCourse = courseRepository.getTopNewCourse(numberCourse);
            // if topCourse is empty -> tell user
            if (topNewestCourse.isEmpty()) {
                return new ResponseCommon<>(ResponseCode.COURSE_LIST_IS_EMPTY.getCode(), "Course list is empty", null);
            } else {
                GetNewestCourseResponse getNewestCourseResponse = new GetNewestCourseResponse(topNewestCourse);
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Get newest course success", getNewestCourseResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Get Newest Course failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<GetCourseByUserResponse> getCourseByUser(String username) {
        try {
            User user = userRepository.findByUsername(username).orElse(null);
            List<Course> courseList = courseRepository.getCoursesByUserId(user.getId());
            // if courseList is empty -> tell user
            if (courseList.isEmpty()) {
                return new ResponseCommon<>(ResponseCode.COURSE_LIST_IS_EMPTY.getCode(), "User not enroll any course", null);
            } else {
                GetCourseByUserResponse getCourseByUserResponse = new GetCourseByUserResponse(courseList);
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Get course by user success", getCourseByUserResponse);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Get Newest Course failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<GetTotalCourseResponse> getTotalCourse() {
        try {
            int totalCourse = courseRepository.getTotalCourse();
            GetTotalCourseResponse getTotalCourseResponse = new GetTotalCourseResponse(totalCourse);
            return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Get total course success", getTotalCourseResponse);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Get Total Course failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<SearchCourseByNameAndCategoryResponse> searchCourse(SearchCourseByNameAndCategoryRequest searchCourseByNameAndCategoryRequest) {
        try {
            List<Course> courseList = courseRepository.searchCoursesByNameOrCategory(searchCourseByNameAndCategoryRequest.getKeyword());
            // if courseList is empty -> tell user
            if (courseList.isEmpty()) {
                return new ResponseCommon<>(ResponseCode.COURSE_LIST_IS_EMPTY.getCode(), "Not course match with search", null);
            } else {
                SearchCourseByNameAndCategoryResponse searchCourseByNameAndCategoryResponse = new SearchCourseByNameAndCategoryResponse(courseList);
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Search success", searchCourseByNameAndCategoryResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Get search Course failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<PageCourseResponse> getAllCoursePage(PageRequestDTO pageRequestDTO) {
        try {
            int pageNo = pageRequestDTO.getPageNo();
            int pageSize = pageRequestDTO.getPageSize();
            String sortBy = pageRequestDTO.getSortBy();
            String sortDir = pageRequestDTO.getSortDir();

            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.fromString(sortDir), sortBy));

            Page<Course> coursePage = courseRepository.findAllByIsDeleted(false, pageable);
            if (coursePage.isEmpty()) {
                return new ResponseCommon<>(ResponseCode.COURSE_LIST_IS_EMPTY, null);
            }
            PageCourseResponse response = new PageCourseResponse();
            response.setCourseList(coursePage.getContent());
            response.setPageNo(pageNo);
            response.setPageSize(pageSize);
            response.setTotalElements((int) coursePage.getTotalElements());
            response.setTotalPages(coursePage.getTotalPages());
            response.setLast(coursePage.isLast());
            return new ResponseCommon<>(ResponseCode.SUCCESS, response);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Get  Course page  failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<EnrollCourseResponse> enrollCourse(EnrollCourseRequest enrollCourseRequest) {
        try {
            EnrollCourseResponse enrollCourseResponse = new EnrollCourseResponse();
            Course courseBuy = courseRepository.findCourseById(enrollCourseRequest.getCourseId()).orElse(null);
            System.out.println(courseBuy);
            Order order = new Order();
            order.setCreated_at(LocalDateTime.now());
            order.setUser(userRepository.findByUsername(enrollCourseRequest.getUsername()).orElse(null));
            order.setCourse(courseBuy);
            order.setEnumTypeProcessPayment(EnumTypeProcessPayment.INPROCESS);
            order.setAmount(courseBuy.getPrice());
            orderRepository.save(order);

            ResponseCommon<PaymentRes> paymentResponse = paymentService.addPayment(courseBuy.getPrice());
            if (paymentResponse.getCode() == ResponseCode.SUCCESS.getCode()) {
                enrollCourseResponse.setOrderId(order.getId());
                enrollCourseResponse.setUrlPayment(paymentResponse.getData().getUrl());
                order.setChecksum(paymentResponse.getData().getVnp_TxnRef());
                orderRepository.save(order);
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Send url done", enrollCourseResponse);
            } else {
                log.debug("Enroll course response faile because paymentResponse not success.");
                return new ResponseCommon<>(ResponseCode.SEND_URL_PAYMENT_FAIL.getCode(), "Send url fail", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Enroll Course failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<PaymentConfirmResponse> paymentConfirm(PaymentConfirmRequest paymentConfirmRequest) {
        try {
            Order order = orderRepository.getOrderById(paymentConfirmRequest.getOrderId()).orElse(null);
            if (!validateOrder(order)) {
                return new ResponseCommon<>(ResponseCode.ORDER_NOT_EXIST, null);
            }
            String signValue = generateAndHashQueryString(paymentConfirmRequest);
            String vnPaySecureHash = paymentConfirmRequest.getVnp_SecureHash();
            if (!validateSignValue(signValue, vnPaySecureHash)) {
                return new ResponseCommon<>(ResponseCode.CHANGE_PARAM.getCode(), "Param is hacker", null);
            }
            String checksum = order.getChecksum();
            String vnPayTnxRef = paymentConfirmRequest.getVnp_TxnRef();
            if (!validateChecksum(checksum, vnPayTnxRef)) {
                return new ResponseCommon<>(ResponseCode.ORDER_NOT_FOUND.getCode(), "Order not found", null);
            }
            double amountDB = order.getAmount();
            double amountReturn = Double.parseDouble(paymentConfirmRequest.getVnp_Amount());
            if (!validateAmount(amountDB, amountReturn)) {
                return new ResponseCommon<>(ResponseCode.INVALID_AMOUNT.getCode(), "Invalid Amount", null);
            }
            if (!validateOrderStatus(order.getEnumTypeProcessPayment())) {
                return new ResponseCommon<>(ResponseCode.ORDER_ALREADY_CONFIRM.getCode(), "Order already confirm", null);
            }
            if (!validateResponseCode(paymentConfirmRequest.getVnp_ResponseCode())) {
                return new ResponseCommon<>(ResponseCode.USER_CANCEL_BILL.getCode(), "User cancel bill", null);
            }
            return processPayment(order);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Enroll Course failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    private boolean validateOrder(Order order) {
        return !Objects.isNull(order);
    }

    private boolean validateSignValue(String signValue, String vnPaySecureHash) {
        return signValue.equals(vnPaySecureHash);
    }

    private boolean validateChecksum(String checksum, String vnPayTnxRef) {
        return vnPayTnxRef.equals(checksum);
    }

    private boolean validateAmount(double amountDB, double amountReturn) {
        return amountReturn == amountDB;
    }

    private boolean validateOrderStatus(EnumTypeProcessPayment enumTypeProcessPayment) {
        return enumTypeProcessPayment.equals(EnumTypeProcessPayment.INPROCESS);
    }

    private boolean validateResponseCode(String vnp_ResponseCode) {
        return vnp_ResponseCode.equals("00");
    }

    private ResponseCommon<PaymentConfirmResponse> processPayment(Order order) {
        Payment payment = new Payment();
        HistoryRegisterCourse historyRegisterCourse = new HistoryRegisterCourse();
        payment.setUser(order.getUser());
        payment.setCourse(order.getCourse());
        payment.setPaymentGateway(EnumPaymentGateway.VN_PAY);
        payment.setTransaction_id(payment.getTransaction_id());
        payment.setAmount(order.getAmount());
        payment.setEnumPaymentProcess(EnumPaymentProcess.SUCCESS);
        payment.setTransaction_id(order.getChecksum());
        payment.setCreated_at(LocalDateTime.now());
        paymentRepository.save(payment);
        order.setPayment(payment);
        order.setEnumTypeProcessPayment(EnumTypeProcessPayment.DONE);
        orderRepository.save(order);
        historyRegisterCourse.setCourse(payment.getCourse());
        historyRegisterCourse.setUser(payment.getUser());
        historyRegisterCourse.setSttLessonCurrent(1);
        historyRegisterCourse.setPayment(payment);
        historyRegisterCourse.setProcess(EnumTypeProcessAccount.NOT_READY);
        historyRegisterCourse.setCreatedAt(LocalDateTime.now());
        historyRegisterCourse.setOrder(order);
        historyRegisterCourseRepository.save(historyRegisterCourse);
        PaymentConfirmResponse paymentConfirmResponse = new PaymentConfirmResponse();
        paymentConfirmResponse.setStatus("Payment done");
        String mailTo = payment.getUser().getEmail();
        String fullname = payment.getUser().getFullName();
        String coursename = payment.getCourse().getName();
        double amount = payment.getAmount();
        String transaction_id = payment.getTransaction_id();
        LocalDateTime createdAt = payment.getCreated_at();
        log.info("START... Sending email");
        emailService.sendEmail(setUpMailPayment(mailTo, fullname, coursename, amount, transaction_id, createdAt));
        log.info("END... Email sent success");
        return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Confirm success", null);
    }

    private Mail setUpMailPayment(String mailTo, String fullname, String courseName, double amount, String transactionId, LocalDateTime createdAt) {
        Mail mail = new Mail();
        mail.setFrom("elearningapplicationsystem@gmail.com");
        mail.setTo(mailTo);
        mail.setSubject("Information Your Payment Elearning Application");
        Map<String, Object> model = new HashMap<>();
        model.put("fullname", fullname);
        model.put("coursename", courseName);
        model.put("amount", amount);
        model.put("transaction_id", transactionId);
        model.put("created_at", createdAt);
        mail.setPros(model);
        mail.setTemplate("payment");
        return mail;
    }

    public String generateAndHashQueryString(PaymentConfirmRequest paymentConfirmRequest) {
        Map<String, String> fields = new HashMap<>();
        fields.put("vnp_Amount", paymentConfirmRequest.getVnp_Amount());
        fields.put("vnp_BankCode", paymentConfirmRequest.getVnp_BankCode());
        fields.put("vnp_BankTranNo", paymentConfirmRequest.getVnp_BankTranNo());
        fields.put("vnp_CardType", paymentConfirmRequest.getVnp_CardType());
        fields.put("vnp_OrderInfo", paymentConfirmRequest.getVnp_OrderInfo());
        fields.put("vnp_PayDate", paymentConfirmRequest.getVnp_PayDate());
        fields.put("vnp_ResponseCode", paymentConfirmRequest.getVnp_ResponseCode());
        fields.put("vnp_TmnCode", paymentConfirmRequest.getVnp_TmnCode());
        fields.put("vnp_TransactionNo", paymentConfirmRequest.getVnp_TransactionNo());
        fields.put("vnp_TransactionStatus", paymentConfirmRequest.getVnp_TransactionStatus());
        fields.put("vnp_TxnRef", paymentConfirmRequest.getVnp_TxnRef());

        String queryString = VnPayConfig.hashAllFields(fields);

        return queryString;
    }

    @Override
    public ResponseCommon<CheckEnrollCourseResponse> isEnrollCourse(CheckEnrollCourseRequest checkEnrollCourseRequest) {
        try {
            CheckEnrollCourseResponse checkEnrollCourseResponse = new CheckEnrollCourseResponse();
            User user = userRepository.findByUsername(checkEnrollCourseRequest.getUsername()).orElse(null);
            Course course = courseRepository.findCourseById(checkEnrollCourseRequest.getCourseId()).orElse(null);
            HistoryRegisterCourse historyRegisterCourse = historyRegisterCourseRepository.findHistoryRegisterCourseByCourseIdAndUser(course, user).orElse(null);
            checkEnrollCourseResponse.setEnrollCourse(!Objects.isNull(historyRegisterCourse));
            return new ResponseCommon<>(ResponseCode.SUCCESS, checkEnrollCourseResponse);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Enroll Course failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }
}

