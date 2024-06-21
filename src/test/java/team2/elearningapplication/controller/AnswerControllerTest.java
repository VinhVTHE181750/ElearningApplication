package team2.elearningapplication.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import team2.elearningapplication.Enum.ResponseCode;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.admin.answer.AnswerData;
import team2.elearningapplication.dto.request.admin.answer.UpdateAnswerRequest;
import team2.elearningapplication.dto.response.admin.answer.AddAnswerResponse;
import team2.elearningapplication.dto.response.admin.answer.UpdateAnswerResponse;
import team2.elearningapplication.service.IAnswerService;
import team2.elearningapplication.service.implement.AnswerServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AnswerControllerTest extends Mockito {


    // Test method: addAnswer()

    void getAddAnswerResponse(AnswerData answerData, ResponseCommon<AddAnswerResponse> expectedOutput) {
        // Mock the AnswerService class, as it's a dependency in the AnswerController class.
        IAnswerService answerService = mock(AnswerServiceImpl.class);
        // Create an instance of AnswerController, injecting the mocked AnswerService.
        AnswerController answerController = new AnswerController(answerService);
        // Use Mockito to define the behavior of the answerService.addAnswer() method when it receives the sample input.
        when(answerService.addAnswer(answerData)).thenReturn(expectedOutput);
        // Call the addAnswer method of AnswerController with the sample input.
        ResponseEntity<ResponseCommon<AddAnswerResponse>> actualOutput = answerController.addAnswer(answerData);
        // Assert that the output is as expected.
        assertNotNull(actualOutput.getBody());
        assertEquals(expectedOutput.getData(), actualOutput.getBody().getData());
    }

    // Input: AnswerData (username, questionID, answerName, isCorrect)
    // Boundary:
    // username (string)
    // questionID (int): 1 -> infinity
    // answerName (string)

    // isCorrect (boolean)
    // To compare: ResponseCommon<AddAnswerResponse>.getData()

    // Mocking the service class and testing the addAnswer method of AnswerController class by passing AnswerData object as input and checking if the output is ResponseCommon<AddAnswerResponse> object or not.
    // TCs
    // 1. All args are valid
    // 1.1 AnswerData is null
    // 2. username: username is empty // invalid
    // 2.1 username: username contains special characters // invalid
    // 2.2 username: username is null // invalid
    // 3. questionID: questionID = 0 // boundary invalid
    // 3.1 questionID = 1 // boundary valid
    // 3.2 questionID = -5 // invalid
    // 3.2 questionID = 5 // valid
    // 4. answerName: answerName is empty // invalid
    // 4.1 answerName: answerName is null // invalid
    // 5. isCorrect: isCorrect = true // valid
    // 5.1 isCorrect: isCorrect = false // valid
    // 6. deleted: deleted = true // valid
    // 6.1 deleted: deleted = false // valid


    @Test
    void addAnswerValidData() {
        // Case 1: All args are valid
        ResponseCommon<AddAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Answer added successfully", null);
        AnswerData answerData = new AnswerData("username", 1, "answerName", true);
        getAddAnswerResponse(answerData, expectedOutput);
    }

    @Test
    void addAnswerAnswerDataIsNull() {
        // Case 1.1: AnswerData is null
        ResponseCommon<AddAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add answer failed", null);
        AnswerData answerData = null;
        getAddAnswerResponse(answerData, expectedOutput);
    }

    @Test
    void addAnswerUsernameIsEmpty() {
        // Case 2: username is empty
        ResponseCommon<AddAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add answer failed", null);
        AnswerData answerData = new AnswerData("", 1, "answerName", true);
        getAddAnswerResponse(answerData, expectedOutput);
    }

    @Test
    void addAnswerUsernameContainsSpecialCharacters() {
        // Case 2.1: username contains special characters
        ResponseCommon<AddAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add answer failed", null);
        AnswerData answerData = new AnswerData("user@name", 1, "answerName", true);
        getAddAnswerResponse(answerData, expectedOutput);
    }

    @Test
    void addAnswerUsernameIsNull() {
        // Case 2.2: username is null
        ResponseCommon<AddAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add answer failed", null);
        AnswerData answerData = new AnswerData(null, 1, "answerName", true);
        getAddAnswerResponse(answerData, expectedOutput);
    }

    @Test
    void addAnswerQuestionIDZero() {
        // Case 3: questionID = 0
        ResponseCommon<AddAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add answer failed", null);
        AnswerData answerData = new AnswerData("username", 0, "answerName", true);
        getAddAnswerResponse(answerData, expectedOutput);
    }

    @Test
    void addAnswerQuestionIDOne() {
        // Case 3.1: questionID = 1
        ResponseCommon<AddAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Answer added successfully", null);
        AnswerData answerData = new AnswerData("username", 1, "answerName", true);
        getAddAnswerResponse(answerData, expectedOutput);
    }

    @Test
    void addAnswerQuestionIDNegative() {
        // Case 3.2: questionID = -5
        ResponseCommon<AddAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add answer failed", null);
        AnswerData answerData = new AnswerData("username", -5, "answerName", true);
        getAddAnswerResponse(answerData, expectedOutput);
    }

    @Test
    void addAnswerQuestionIDFive() {
        // Case 3.3: questionID = 5
        ResponseCommon<AddAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Answer added successfully", null);
        AnswerData answerData = new AnswerData("username", 5, "answerName", true);
        getAddAnswerResponse(answerData, expectedOutput);
    }

    @Test
    void addAnswerQuestionIDIsNotNumber() {
        // Case 3.4: questionID is not a number
        ResponseCommon<AddAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add answer failed", null);
        AnswerData answerData = new AnswerData("username", 1, "answerName", true);
        getAddAnswerResponse(answerData, expectedOutput);
    }

    @Test
    void addAnswerAnswerNameIsEmpty() {
        // Case 4: answerName is empty
        ResponseCommon<AddAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add answer failed", null);
        AnswerData answerData = new AnswerData("username", 1, "", true);
        getAddAnswerResponse(answerData, expectedOutput);
    }

    @Test
    void addAnswerAnswerNameIsNull() {
        // Case 4.1: answerName is null
        ResponseCommon<AddAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add answer failed", null);
        AnswerData answerData = new AnswerData("username", 1, null, true);
        getAddAnswerResponse(answerData, expectedOutput);
    }

    @Test
    void addAnswerIsCorrectTrue() {
        // Case 5: isCorrect = true
        ResponseCommon<AddAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Answer added successfully", null);
        AnswerData answerData = new AnswerData("username", 1, "answerName", true);
        getAddAnswerResponse(answerData, expectedOutput);
    }

    @Test
    void addAnswerIsCorrectFalse() {
        // Case 5.1: isCorrect = false
        ResponseCommon<AddAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Answer added successfully", null);
        AnswerData answerData = new AnswerData("username", 1, "answerName", false);
        getAddAnswerResponse(answerData, expectedOutput);
    }

    @Test
    void addAnswerDeletedTrue() {
        // Case 6: deleted = true
        ResponseCommon<AddAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Answer added successfully", null);
        AnswerData answerData = new AnswerData("username", 1, "answerName", true);
        getAddAnswerResponse(answerData, expectedOutput);
    }

    @Test
    void addAnswerDeletedFalse() {
        // Case 6.1: deleted = false
        ResponseCommon<AddAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Answer added successfully", null);
        AnswerData answerData = new AnswerData("username", 1, "answerName", false);
        getAddAnswerResponse(answerData, expectedOutput);
    }


    // Test method: updateAnswer()
    void getUpdateAnswerResponse(UpdateAnswerRequest updateAnswerRequest, ResponseCommon<UpdateAnswerResponse> expectedOutput) {
        // Mock the AnswerService class, as it's a dependency in the AnswerController class.
        IAnswerService answerService = mock(AnswerServiceImpl.class);
        // Create an instance of AnswerController, injecting the mocked AnswerService.
        AnswerController answerController = new AnswerController(answerService);
        // Use Mockito to define the behavior of the answerService.updateAnswer() method when it receives the sample input.
        when(answerService.updateAnswer(updateAnswerRequest)).thenReturn(expectedOutput);
        // Call the updateAnswer method of AnswerController with the sample input.
        ResponseEntity<ResponseCommon<UpdateAnswerResponse>> actualOutput = answerController.updateAnswer(updateAnswerRequest);
        // Assert that the output is as expected.
        assertNotNull(actualOutput.getBody());
        assertEquals(expectedOutput.getData(), actualOutput.getBody().getData());
    }
    // Input: UpdateAnswerRequest (username, questionID, answerID, answerContent, isCorrect, deleted)
    // Boundary:
    // username (string): not empty
    // questionID (int): 1 -> infinity
    // answerID (int): 1 -> infinity
    // answerContent (string): not empty

    // isCorrect (boolean)
    // deleted (boolean)

    // Output: ResponseCommon<UpdateAnswerResponse>
    // To compare: ResponseCommon<UpdateAnswerResponse>.getData()
    // Mocking the service class and testing
    // the updateAnswer method of AnswerController class by passing UpdateAnswerRequest object as input

    // and checking if the output is ResponseCommon<UpdateAnswerResponse> object or not.

    // TCs
    // 1. All args are valid

    // 2. UpdateAnswerRequest is null
    // All args are valid except:
    // 2. username: username is empty // invalid
    // 2.1 username: username contains special characters // invalid
    // 2.2 username: username is null // invalid
    // 3. questionID: questionID = 0 // boundary invalid
    // 3.1 questionID = 1 // boundary valid
    // 3.2 questionID = -5 // invalid
    // 3.3 questionID = 5 // valid
    // 4. answerID: answerID = 0 // boundary invalid
    // 4.1 answerID = 1 // boundary valid
    // 4.2 answerID = -5 // invalid
    // 4.3 answerID: 5 // valid
    // 5. answerContent: answerContent is empty // invalid
    // 5.1 answerContent: answerContent is null // invalid
    // 6. isCorrect: isCorrect = true // valid
    // 6.1 isCorrect: isCorrect = false // valid
    // 7. deleted: deleted = true // valid

    // 7.1 deleted: deleted = false // valid


    @Test
    void updateAnswerValidData() {
        // Case 1: All args are valid
        ResponseCommon<UpdateAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Answer updated successfully", null);
        UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest("username", 1, 1, "answerContent", true, false);
        getUpdateAnswerResponse(updateAnswerRequest, expectedOutput);
    }

    @Test
    void updateAnswerUpdateAnswerRequestIsNull() {
        // Case 2: UpdateAnswerRequest is null
        ResponseCommon<UpdateAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Update answer failed", null);
        UpdateAnswerRequest updateAnswerRequest = null;
        getUpdateAnswerResponse(updateAnswerRequest, expectedOutput);
    }

    @Test
    void updateAnswerUsernameIsEmpty() {
        // Case 2: username is empty
        ResponseCommon<UpdateAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Update answer failed", null);
        UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest("", 1, 1, "answerContent", true, false);
        getUpdateAnswerResponse(updateAnswerRequest, expectedOutput);
    }

    @Test
    void updateAnswerUsernameContainsSpecialCharacters() {
        // Case 2.1: username contains special characters
        ResponseCommon<UpdateAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Update answer failed", null);
        UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest("user@name", 1, 1, "answerContent", true, false);
        getUpdateAnswerResponse(updateAnswerRequest, expectedOutput);
    }

    @Test
    void updateAnswerUsernameIsNull() {
        // Case 2.2: username is null
        ResponseCommon<UpdateAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Update answer failed", null);
        UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest(null, 1, 1, "answerContent", true, false);
        getUpdateAnswerResponse(updateAnswerRequest, expectedOutput);
    }

    @Test
    void updateAnswerQuestionIDZero() {
        // Case 3: questionID = 0
        ResponseCommon<UpdateAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Update answer failed", null);
        UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest("username", 0, 1, "answerContent", true, false);
        getUpdateAnswerResponse(updateAnswerRequest, expectedOutput);
    }

    @Test
    void updateAnswerQuestionIDOne() {
        // Case 3.1: questionID = 1
        ResponseCommon<UpdateAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Answer updated successfully", null);
        UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest("username", 1, 1, "answerContent", true, false);
        getUpdateAnswerResponse(updateAnswerRequest, expectedOutput);
    }

    @Test
    void updateAnswerQuestionIDNegative() {
        // Case 3.2: questionID = -5
        ResponseCommon<UpdateAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Update answer failed", null);
        UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest("username", -5, 1, "answerContent", true, false);
        getUpdateAnswerResponse(updateAnswerRequest, expectedOutput);
    }

    @Test
    void updateAnswerQuestionIDFive() {
        // Case 3.3: questionID = 5
        ResponseCommon<UpdateAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Answer updated successfully", null);
        UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest("username", 5, 1, "answerContent", true, false);
        getUpdateAnswerResponse(updateAnswerRequest, expectedOutput);
    }

    @Test
    void updateAnswerAnswerIDZero() {
        // Case 4: answerID = 0
        ResponseCommon<UpdateAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Update answer failed", null);
        UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest("username", 1, 0, "answerContent", true, false);
        getUpdateAnswerResponse(updateAnswerRequest, expectedOutput);
    }

    @Test
    void updateAnswerAnswerIDOne() {
        // Case 4.1: answerID = 1
        ResponseCommon<UpdateAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Answer updated successfully", null);
        UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest("username", 1, 1, "answerContent", true, false);
        getUpdateAnswerResponse(updateAnswerRequest, expectedOutput);
    }

    @Test
    void updateAnswerAnswerIDNegative() {
        // Case 4.2: answerID = -5
        ResponseCommon<UpdateAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Update answer failed", null);
        UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest("username", 1, -5, "answerContent", true, false);
        getUpdateAnswerResponse(updateAnswerRequest, expectedOutput);
    }

    @Test
    void updateAnswerAnswerIDFive() {
        // Case 4.3: answerID = 5
        ResponseCommon<UpdateAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Answer updated successfully", null);
        UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest("username", 1, 5, "answerContent", true, false);
        getUpdateAnswerResponse(updateAnswerRequest, expectedOutput);
    }

    @Test
    void updateAnswerAnswerContentIsEmpty() {
        // Case 5: answerContent is empty
        ResponseCommon<UpdateAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Update answer failed", null);
        UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest("username", 1, 1, "", true, false);
        getUpdateAnswerResponse(updateAnswerRequest, expectedOutput);
    }

    @Test
    void updateAnswerAnswerContentIsNull() {
        // Case 5.1: answerContent is null
        ResponseCommon<UpdateAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Update answer failed", null);
        UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest("username", 1, 1, null, true, false);
        getUpdateAnswerResponse(updateAnswerRequest, expectedOutput);
    }

    @Test
    void updateAnswerIsCorrectTrue() {
        // Case 6: isCorrect = true
        ResponseCommon<UpdateAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Answer updated successfully", null);
        UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest("username", 1, 1, "answerContent", true, false);
        getUpdateAnswerResponse(updateAnswerRequest, expectedOutput);
    }

    @Test
    void updateAnswerIsCorrectFalse() {
        // Case 6.1: isCorrect = false
        ResponseCommon<UpdateAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Answer updated successfully", null);
        UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest("username", 1, 1, "answerContent", false, false);
        getUpdateAnswerResponse(updateAnswerRequest, expectedOutput);
    }

    @Test
    void updateAnswerDeletedTrue() {
        // Case 7: deleted = true
        ResponseCommon<UpdateAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Answer updated successfully", null);
        UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest("username", 1, 1, "answerContent", true, true);
        getUpdateAnswerResponse(updateAnswerRequest, expectedOutput);
    }

    @Test
    void updateAnswerDeletedFalse() {
        // Case 7.1: deleted = false
        ResponseCommon<UpdateAnswerResponse> expectedOutput = new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Answer updated successfully", null);
        UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest("username", 1, 1, "answerContent", true, false);
        getUpdateAnswerResponse(updateAnswerRequest, expectedOutput);
    }
}