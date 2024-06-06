package team2.elearningapplication.service.implement;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import team2.elearningapplication.Enum.ResponseCode;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.user.post.*;
import team2.elearningapplication.dto.response.user.post.*;
import team2.elearningapplication.entity.Post;
import team2.elearningapplication.entity.User;
import team2.elearningapplication.repository.ILessonRespository;
import team2.elearningapplication.repository.IPostRepository;
import team2.elearningapplication.repository.IUserRepository;
import team2.elearningapplication.service.IPostService;
import team2.elearningapplication.service.IUserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements IPostService {
    private static final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);
    private final ILessonRespository lessonRespository;
    private final IUserRepository userRepository;
    private final IPostRepository postRepository;
    private final IUserService userService;

    @Override
    public ResponseCommon<AddPostResponse> addPost(AddPostRequest addPostRequest) {
        try {
            // if addPostRequest is null -> tell user
            if (Objects.isNull(addPostRequest)) {
                log.debug("Add Post failed: addPostRequest is null");
                return new ResponseCommon<>(ResponseCode.POST_IS_EMPTY, null);
            }
            // if content is null -> tell user
            else if (addPostRequest.getContent().trim().isEmpty()) {
                log.debug("Add Post failed: Post content is empty");
                return new ResponseCommon<>(ResponseCode.POST_CONTENT_IS_EMPTY, null);
            } else {
                Post postAdd = new Post();
                postAdd.setUser(userRepository.findByUsername(addPostRequest.getUsername()).orElse(null));
                postAdd.setCourseId(addPostRequest.getCourseId());
                postAdd.setContent(addPostRequest.getContent());
                postAdd.setCreatedAt(LocalDateTime.now());
                postRepository.save(postAdd);
                AddPostResponse addPostResponse = new AddPostResponse();
                log.debug("Add Post successful");
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Add Post success", addPostResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Add Post failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Add post fail", null);
        }
    }

    @Override
    public ResponseCommon<UpdatePostResponse> updatePost(UpdatePostRequest updatePostRequest) {
        try {
            User user = userRepository.findByUsername(updatePostRequest.getUsername()).orElse(null);
//            Post post = postRepository.findPostByUserAndCourseId(user, updatePostRequest.getCourseId()).orElse(null);
            Post post = postRepository.findById(updatePostRequest.getPostId()).orElse(null);
            // if post not exist -> tell user
            if (Objects.isNull(post)) {
                log.debug("Update Post failed: Post does not exist");
                return new ResponseCommon<>(ResponseCode.POST_NOT_EXIST, null);
            } else {
                post.setContent(updatePostRequest.getContent());
                postRepository.save(post);
                UpdatePostResponse updatePostResponse = new UpdatePostResponse();
                updatePostResponse.setContent(post.getContent());
                updatePostResponse.setUpdateAt(LocalDateTime.now());
                updatePostResponse.setEmail(post.getUser().getEmail());
                updatePostResponse.setPostID(post.getId());
                updatePostResponse.setCourseId((post.getCourseId()));
                log.debug("Update Post successful");
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Update post success", updatePostResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Update Post failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Update post fail", null);
        }
    }

    @Override
    public ResponseCommon<DeletePostResponse> deletePost(DeletePostRequest deletePostRequest) {
        try {
            User user = userRepository.findByUsername(deletePostRequest.getUsername()).orElse(null);
//            Post post = postRepository.findPostByUserAndCourseId(user, deletePostRequest.getCourseId()).orElse(null);
            Post post = postRepository.findById(deletePostRequest.getPostId()).orElse(null);
            // if post not exist -> tell user
            if (Objects.isNull(post)) {
                log.debug("Delete Post failed: Post does not exist");
                return new ResponseCommon<>(ResponseCode.POST_NOT_EXIST, null);
            } else {
                post.setDeleted(true);
                postRepository.save(post);
                DeletePostResponse deletePostResponse = new DeletePostResponse();
                deletePostResponse.setContent(post.getContent());
                deletePostResponse.setUpdateAt(LocalDateTime.now());
                deletePostResponse.setEmail(post.getUser().getEmail());
                deletePostResponse.setPostID(post.getId());
                deletePostResponse.setCourseId(deletePostResponse.getCourseId());
                log.debug("Delete Post successful");
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Delete post success", deletePostResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Delete Post failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Delete post fail", null);
        }
    }

    @Override
    public ResponseCommon<FindAllPostResponse> findAllPost(GetPostByDeleted getPostByDeleted) {
        try {
            List<Post> listPost = postRepository.findPostByDeleted(getPostByDeleted.isDeleted());
            // if listPost is empty -> tell user
            if (listPost.isEmpty()) {
                log.debug("Get all Post failed: Post list is empty");
                return new ResponseCommon<>(ResponseCode.POST_LIST_IS_EMPTY, null);
            } else {
                FindAllPostResponse findAllPostResponse = new FindAllPostResponse();
                findAllPostResponse.setFindAllPost(listPost);
                log.debug("Get all Post successful");
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Get all post success", findAllPostResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Get all Post failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get all post fail", null);
        }
    }

    @Override
    public ResponseCommon<GetPostByIdResponse> getPostById(GetPostByIdRequest getPostByIdRequest) {
        try {
            Post post = postRepository.findById(getPostByIdRequest.getId()).orElse(null);
            // If post not exist -> tell user
            if (Objects.isNull(post)) {
                log.debug("Get post by id failed: Post not exist");
                return new ResponseCommon<>(ResponseCode.POST_NOT_EXIST, null);
            } else {
                GetPostByIdResponse response = new GetPostByIdResponse();

                response.setId(post.getId());
                response.setContent(post.getContent());
                response.setUser(post.getUser());
                response.setCourseId(post.getCourseId());
                response.setCreatedAt(post.getCreatedAt());
                response.setDeleted(post.isDeleted());

                log.debug("Get post by id successful");
                return new ResponseCommon<>(ResponseCode.SUCCESS.getCode(), "Get post by id success", response);

            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Get post by id failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get post by id failed", null);
        }
    }

    @Override
    public ResponseCommon<GetPostByCourseIdResponse> getPostByCourseId(GetPostByCourseIdRequest getPostByCourseIdRequest) {
        try {
            GetPostByCourseIdResponse response = new GetPostByCourseIdResponse();
            List<Post> postList = postRepository.findPostByCourseIdAndDeleted(getPostByCourseIdRequest.getCourseId(), getPostByCourseIdRequest.isDeleted());
            response.setPostList(postList);
            return new ResponseCommon<>(ResponseCode.SUCCESS, response);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Get post by course id failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL.getCode(), "Get post by course id failed", null);
        }
    }
}
