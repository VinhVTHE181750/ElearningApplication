package team2.elearningapplication.service;

import team2.elearningapplication.dto.common.PageRequestDTO;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.admin.question.*;
import team2.elearningapplication.dto.request.user.question.GetQuestionByQuizIDRequest;
import team2.elearningapplication.dto.response.admin.question.AddQuestionResponse;
import team2.elearningapplication.dto.response.admin.question.DeleteQuestionResponse;
import team2.elearningapplication.dto.response.admin.question.GetQuestionByIdResponse;
import team2.elearningapplication.dto.response.admin.question.UpdateQuestionResponse;
import team2.elearningapplication.dto.response.user.question.GetQuestionByQuizIdResponse;
import team2.elearningapplication.dto.response.user.question.GetQuestionPageResponse;
import team2.elearningapplication.entity.Question;

import java.util.List;

public interface IQuestionService {

    ResponseCommon<AddQuestionResponse> addQuestion(QuestionData questionData);

    ResponseCommon<UpdateQuestionResponse> updateQuestion(UpdateQuestionRequest updateQuestionRequest);

    ResponseCommon<DeleteQuestionResponse> deleteQuestion(DeleteQuestionRequest deleteQuestionRequest);

    ResponseCommon<List<Question>> findAllQuestion();

    ResponseCommon<List<Question>> findAllQuestionByDeleted(FindQuestionByDeletedRequest findQuestionByDeletedRequest);

    ResponseCommon<GetQuestionByIdResponse> getQuestionById(GetQuestionByIdRequest getQuestionByIdRequest);

    ResponseCommon<GetQuestionPageResponse> getQuestionPage(PageRequestDTO pageRequestDTO);

    ResponseCommon<GetQuestionByQuizIdResponse> getQuestionByQuizId(GetQuestionByQuizIDRequest getQuestionByQuizIDRequest);
}
