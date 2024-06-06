package team2.elearningapplication.controller;

import lombok.AllArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team2.elearningapplication.Enum.ResponseCode;
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
import team2.elearningapplication.service.IBlogService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/blog")
@AllArgsConstructor
public class BlogController {
    private IBlogService blogService;

    @PostMapping("/add-blog")
    public ResponseEntity<ResponseCommon<AddBlogResponse>> addBlog(@Valid @RequestBody AddBlogRequest addBlogRequest) {
        ResponseCommon<AddBlogResponse> response = blogService.addBlog(addBlogRequest);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add Blog failed", null));
        }
    }

    @PostMapping("/update-blog")
    public ResponseEntity<ResponseCommon<UpdateBlogResponse>> updateBlog(@Valid @RequestBody UpdateBlogRequest updateBlogRequest) {
        ResponseCommon<UpdateBlogResponse> response = blogService.updateBlog(updateBlogRequest);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.BLOG_NOT_EXIST.getCode()) {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.BLOG_NOT_EXIST.getCode(), "Blog not exist", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Update Blog failed", null));
        }
    }

    @PutMapping("/delete-blog")
    public ResponseEntity<ResponseCommon<DeleteBlogResponse>> deleteBlog(@Valid @RequestBody DeleteBlogRequest deleteBlogRequest) {
        ResponseCommon<DeleteBlogResponse> response = blogService.deleteBlog(deleteBlogRequest);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.BLOG_NOT_EXIST.getCode()) {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.BLOG_NOT_EXIST.getCode(), "Blog not exist", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Delete Blog failed", null));
        }
    }

    @GetMapping("/find-all-blog")
    public ResponseEntity<ResponseCommon<GetAllBlogResponse>> findAllBlog(@ParameterObject GetAllBlogRequest getAllBlogRequest) {
        ResponseCommon<GetAllBlogResponse> response = blogService.findAllBlog(getAllBlogRequest);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.BLOG_LIST_IS_EMPTY.getCode()) {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.BLOG_LIST_IS_EMPTY.getCode(), "List Blog is Empty", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Find All Blog failed", null));
        }
    }

    @GetMapping("/get-blog-by-id")
    public ResponseEntity<ResponseCommon<GetBlogByIdResponse>> getBlogById(@ParameterObject GetBlogByIdRequest request) {
        ResponseCommon<GetBlogByIdResponse> response = blogService.findById(request);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.BLOG_NOT_EXIST.getCode()) {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.BLOG_NOT_EXIST.getCode(), "Blog not exist", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get Blog by ID failed", null));
        }
    }
}
