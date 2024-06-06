package team2.elearningapplication.service;

import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.admin.answer.*;
import team2.elearningapplication.dto.request.user.answer.GetAnswerByQuestionIdRequest;
import team2.elearningapplication.dto.response.admin.answer.*;
import team2.elearningapplication.dto.response.user.answer.GetAnswerByQuestionIdResponse;

import java.util.List;

public interface IAnswerService {
    ResponseCommon<AddAnswerResponse> addAnswer(AnswerData answerData);

    ResponseCommon<UpdateAnswerResponse> updateAnswer(UpdateAnswerRequest updateAnswerRequest);

    ResponseCommon<DeleteAnswerResponse> deleteAnswer(DeleteAnswerRequest deleteAnswerRequest);

    ResponseCommon<List<FindAllAnswerResponse>> findAllAnswer();

    ResponseCommon<GetAnswerByIdResponse> getAnswerById(GetAnswerByIdRequest getAnswerByIdRequest);

    ResponseCommon<List<FindAllAnswerResponse>> findAllAnswerByDeleted(FindAllAnswerByDeletedRequest findAllAnswerByDeletedRequest);

    ResponseCommon<List<GetAnswerByQuestionIdResponse>> getAnswerByQuestionId(GetAnswerByQuestionIdRequest getAnswerByQuestionIdRequest);

}
