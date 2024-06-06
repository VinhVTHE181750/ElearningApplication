package team2.elearningapplication.service;

import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.user.post.*;
import team2.elearningapplication.dto.response.user.post.*;

public interface IPostService {
    ResponseCommon<AddPostResponse> addPost(AddPostRequest addPostRequest);

    ResponseCommon<UpdatePostResponse> updatePost(UpdatePostRequest updatePostRequest);

    ResponseCommon<DeletePostResponse> deletePost(DeletePostRequest deletePostRequest);

    ResponseCommon<FindAllPostResponse> findAllPost(GetPostByDeleted getPostByDeleted);

    ResponseCommon<GetPostByIdResponse> getPostById(GetPostByIdRequest getPostByIdRequest);

    ResponseCommon<GetPostByCourseIdResponse> getPostByCourseId(GetPostByCourseIdRequest getPostByCourseIdRequest);
}
