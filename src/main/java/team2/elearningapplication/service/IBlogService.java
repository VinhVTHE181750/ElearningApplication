package team2.elearningapplication.service;

import team2.elearningapplication.dto.common.GetAllBlogRequest;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.user.blog.AddBlogRequest;
import team2.elearningapplication.dto.request.user.blog.DeleteBlogRequest;
import team2.elearningapplication.dto.request.user.blog.GetBlogByIdRequest;
import team2.elearningapplication.dto.request.user.blog.UpdateBlogRequest;
import team2.elearningapplication.dto.response.admin.blog.AddBlogResponse;
import team2.elearningapplication.dto.response.admin.blog.DeleteBlogResponse;
import team2.elearningapplication.dto.response.admin.blog.GetBlogByIdResponse;
import team2.elearningapplication.dto.response.admin.blog.UpdateBlogResponse;
import team2.elearningapplication.dto.response.common.GetAllBlogResponse;

public interface IBlogService {
    ResponseCommon<AddBlogResponse> addBlog(AddBlogRequest addBlogRequest);

    ResponseCommon<UpdateBlogResponse> updateBlog(UpdateBlogRequest updateBlogRequest);

    ResponseCommon<DeleteBlogResponse> deleteBlog(DeleteBlogRequest deleteBlogRequest);

    ResponseCommon<GetAllBlogResponse> findAllBlog(GetAllBlogRequest getAllBlogRequest);

    ResponseCommon<GetBlogByIdResponse> findById(GetBlogByIdRequest getBlogByIdRequest);
}
