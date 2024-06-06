package team2.elearningapplication.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team2.elearningapplication.Enum.ResponseCode;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.user.post.*;
import team2.elearningapplication.dto.response.user.post.*;
import team2.elearningapplication.service.IPostService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/post")
@AllArgsConstructor
public class PostController {
    private final IPostService postService;

    @PostMapping("/add-post")
    public ResponseEntity<ResponseCommon<AddPostResponse>> addPost(@Valid @RequestBody AddPostRequest addPostRequest) {
        ResponseCommon<AddPostResponse> response = postService.addPost(addPostRequest);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.POST_CONTENT_IS_EMPTY.getCode()) {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Post content is empty", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add post failed", null));
        }
    }

    @PutMapping("/update-post")
    public ResponseEntity<ResponseCommon<UpdatePostResponse>> updatePost(@Valid @RequestBody UpdatePostRequest updatePostRequest) {
        ResponseCommon<UpdatePostResponse> response = postService.updatePost(updatePostRequest);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.POST_NOT_EXIST.getCode()) {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Post does not exist", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Update post failed", null));
        }
    }

    @DeleteMapping("/delete-post")
    public ResponseEntity<ResponseCommon<DeletePostResponse>> deletePost(@Valid @RequestBody DeletePostRequest deletePostRequest) {
        ResponseCommon<DeletePostResponse> response = postService.deletePost(deletePostRequest);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.POST_NOT_EXIST.getCode()) {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Post does not exist", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Delete post failed", null));
        }
    }

    @GetMapping("/find-all-post")
    public ResponseEntity<ResponseCommon<FindAllPostResponse>> findAllPost(GetPostByDeleted getPostByDeleted) {
        ResponseCommon<FindAllPostResponse> response = postService.findAllPost(getPostByDeleted);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.POST_LIST_IS_EMPTY.getCode()) {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Post list is empty", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get all post failed", null));
        }
    }

    @GetMapping("/get-post-by-id")
    public ResponseEntity<ResponseCommon<GetPostByIdResponse>> getPostById(GetPostByIdRequest request) {
        ResponseCommon<GetPostByIdResponse> response = postService.getPostById(request);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.POST_NOT_EXIST.getCode()) {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Post not exist", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get post by id failed", null));
        }
    }

    @GetMapping("/get-post-by-course-id")
    public ResponseEntity<ResponseCommon<GetPostByCourseIdResponse>> getPostById(GetPostByCourseIdRequest request) {
        ResponseCommon<GetPostByCourseIdResponse> response = postService.getPostByCourseId(request);
        if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
            return ResponseEntity.ok(response);
        } else if (response.getCode() == ResponseCode.POST_NOT_EXIST.getCode()) {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(response.getCode(), "Post not exist", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get post by id failed", null));
        }
    }


}
