package team2.elearningapplication.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  // Thêm import
import team2.elearningapplication.Enum.ResponseCode;
import team2.elearningapplication.dto.common.ResponseCommon;
import team2.elearningapplication.dto.request.user.post.AddPostRequest;
import team2.elearningapplication.dto.request.user.post.DeletePostRequest;
import team2.elearningapplication.dto.request.user.post.UpdatePostRequest;
import team2.elearningapplication.dto.response.user.post.AddPostResponse;
import team2.elearningapplication.dto.response.user.post.DeletePostResponse;
import team2.elearningapplication.dto.response.user.post.FindAllPostResponse;
import team2.elearningapplication.dto.response.user.post.UpdatePostResponse;
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
    private final ILessonRespository lessonRespository;
    private final IUserRepository userRepository;
    private final IPostRepository postRepository;
    private final IUserService userService;
    private static final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);

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
                postAdd.setUser(userRepository.findByUsername(userService.genUserFromEmail(addPostRequest.getEmail())).orElse(null));
                postAdd.setLessonId(addPostRequest.getLessonID());
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
            User user = userRepository.findByUsername(userService.genUserFromEmail(updatePostRequest.getEmail())).orElse(null);
            Post post = postRepository.findPostByUserAndAndLessonId(user, updatePostRequest.getLessonID()).orElse(null);
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
                updatePostResponse.setLessonID(post.getLessonId());
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
            User user = userRepository.findByUsername(userService.genUserFromEmail(deletePostRequest.getEmail())).orElse(null);
            Post post = postRepository.findPostByUserAndAndLessonId(user, deletePostRequest.getLessonID()).orElse(null);
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
                deletePostResponse.setLessonID(post.getLessonId());
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
    public ResponseCommon<FindAllPostResponse> findAllPost() {
        try {
            List<Post> listPost = postRepository.findPostByDeleted(false);
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
}