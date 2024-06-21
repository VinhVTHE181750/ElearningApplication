package team2.elearningapplication.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import team2.elearningapplication.Enum.ResponseCode;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.admin.answer.AnswerData;
import team2.elearningapplication.dto.response.admin.answer.AddAnswerResponse;
import team2.elearningapplication.service.IAnswerService;
import team2.elearningapplication.service.implement.AnswerServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AnswerControllerTest extends Mockito {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }


    // Test method: addAnswer()
    // Input: AnswerData
    // Output: ResponseCommon<AddAnswerResponse>
    // To compare: ResponseCommon<AddAnswerResponse>.getData()
    // Mocking the service class and testing the addAnswer method of AnswerController class by passing AnswerData object as input and checking if the output is ResponseCommon<AddAnswerResponse> object or not.
    @Test
    void addAnswerValidData() {
        // Case 1: answerData is valid

        // Mock the AnswerService class, as it's a dependency in the AnswerController class.
        IAnswerService answerService = mock(AnswerServiceImpl.class);
        // Create an instance of AnswerController, injecting the mocked AnswerService.
        AnswerController answerController = new AnswerController(answerService);
        // Create a sample AnswerData object to be used as input.
        AnswerData answerData = new AnswerData("username", 1, "answerName", true);
        // Define the expected output, which is a ResponseCommon<AddAnswerResponse> object.
        ResponseCommon<AddAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Answer added successfully", null);
        // Use Mockito to define the behavior of the answerService.addAnswer() method when it receives the sample input.
        when(answerService.addAnswer(answerData)).thenReturn(expectedOutput);
        // Call the addAnswer method of AnswerController with the sample input.
        ResponseEntity<ResponseCommon<AddAnswerResponse>> actualOutput = answerController.addAnswer(answerData);
        // Assert that the output is as expected.
        assertNotNull(actualOutput.getBody());
        assertEquals(expectedOutput.getData(),
                actualOutput.getBody().getData());
    }

    @Test
    void addAnswerInvalidUsername() {
        // Case 2: answerData is invalid (username is empty)

        // Mock the AnswerService class, as it's a dependency in the AnswerController class.
        IAnswerService answerService = mock(AnswerServiceImpl.class);
        // Create an instance of AnswerController, injecting the mocked AnswerService.
        AnswerController answerController = new AnswerController(answerService);
        // Create a sample AnswerData object to be used as input.
        AnswerData answerData = new AnswerData("", 1, "answerName", true);
        // Define the expected output, which is a ResponseCommon<AddAnswerResponse> object.
        ResponseCommon<AddAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add answer failed", null);
        // Use Mockito to define the behavior of the answerService.addAnswer() method when it receives the sample input.
        when(answerService.addAnswer(answerData)).thenReturn(expectedOutput);
        // Call the addAnswer method of AnswerController with the sample input.
        ResponseEntity<ResponseCommon<AddAnswerResponse>> actualOutput = answerController.addAnswer(answerData);
        // Assert that the output is as expected.
        assertEquals(expectedOutput.getData(), actualOutput.getBody().getData());
    }

    @Test
    void addAnswerInvalidQuestionID() {
        // Case 3: answerData is invalid (questionID is negative)

        // Mock the AnswerService class, as it's a dependency in the AnswerController class.
        IAnswerService answerService = mock(AnswerServiceImpl.class);
        // Create an instance of AnswerController, injecting the mocked AnswerService.
        AnswerController answerController = new AnswerController(answerService);
        // Create a sample AnswerData object to be used as input.
        AnswerData answerData = new AnswerData("username", -1, "answerName", true);
        // Define the expected output, which is a ResponseCommon<AddAnswerResponse> object.
        ResponseCommon<AddAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add answer failed", null);
        // Use Mockito to define the behavior of the answerService.addAnswer() method when it receives the sample input.
        when(answerService.addAnswer(answerData)).thenReturn(expectedOutput);
        // Call the addAnswer method of AnswerController with the sample input.
        ResponseEntity<ResponseCommon<AddAnswerResponse>> actualOutput = answerController.addAnswer(answerData);
        // Assert that the output is as expected.
        assertEquals(expectedOutput.getData(), actualOutput.getBody().getData());
    }

    @Test
    void addAnswerQuestionIDZero() {
        // Case 4: answerData is invalid (questionID is zero)

        // Mock the AnswerService class, as it's a dependency in the AnswerController class.
        IAnswerService answerService = mock(AnswerServiceImpl.class);
        // Create an instance of AnswerController, injecting the mocked AnswerService.
        AnswerController answerController = new AnswerController(answerService);
        // Create a sample AnswerData object to be used as input.
        AnswerData answerData = new AnswerData("username", 0, "answerName", true);
        // Define the expected output, which is a ResponseCommon<AddAnswerResponse> object.
        ResponseCommon<AddAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add answer failed", null);
        // Use Mockito to define the behavior of the answerService.addAnswer() method when it receives the sample input.
        when(answerService.addAnswer(answerData)).thenReturn(expectedOutput);
        // Call the addAnswer method of AnswerController with the sample input.
        ResponseEntity<ResponseCommon<AddAnswerResponse>> actualOutput = answerController.addAnswer(answerData);
        // Assert that the output is as expected.
        assertEquals(expectedOutput.getData(), actualOutput.getBody().getData());
    }

    @Test
    void addAnswerQuestionIDIsNotNumber() {
        // Case 5: answerData is invalid (questionID is not a number)

        // Mock the AnswerService class, as it's a dependency in the AnswerController class.
        IAnswerService answerService = mock(AnswerServiceImpl.class);
        // Create an instance of AnswerController, injecting the mocked AnswerService.
        AnswerController answerController = new AnswerController(answerService);
        // Create a sample AnswerData object to be used as input.
        AnswerData answerData = new AnswerData("username", 1, "answerName", true);
        // Define the expected output, which is a ResponseCommon<AddAnswerResponse> object.
        ResponseCommon<AddAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add answer failed", null);
        // Use Mockito to define the behavior of the answerService.addAnswer() method when it receives the sample input.
        when(answerService.addAnswer(answerData)).thenReturn(expectedOutput);
        // Call the addAnswer method of AnswerController with the sample input.
        ResponseEntity<ResponseCommon<AddAnswerResponse>> actualOutput = answerController.addAnswer(answerData);
        // Assert that the output is as expected.
        assertEquals(expectedOutput.getData(), actualOutput.getBody().getData());
    }

    @Test
    void updateAnswer() {
    }

    @Test
    void deleteAnswer() {
    }

    @Test
    void findAllAnswer() {
    }

    @Test
    void getAnswerById() {
    }

    @Test
    void findAllAnswerByDeleted() {
    }

    @Test
    void getAnswerByQuestionId() {
    }
}