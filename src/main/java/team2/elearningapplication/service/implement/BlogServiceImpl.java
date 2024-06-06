package team2.elearningapplication.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
import team2.elearningapplication.entity.Blog;
import team2.elearningapplication.entity.User;
import team2.elearningapplication.repository.IBlogRepository;
import team2.elearningapplication.repository.IUserRepository;
import team2.elearningapplication.service.IBlogService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlogServiceImpl implements IBlogService {

    private final IBlogRepository blogRepository;
    private final IUserRepository userRepository;

    @Override
    public ResponseCommon<AddBlogResponse> addBlog(AddBlogRequest addBlogRequest) {
        try {
            Blog blog = new Blog();
            User user = userRepository.findByUsername(addBlogRequest.getUsername()).orElse(null);

            if (Objects.isNull(user)) {
                log.debug("Add Blog failed: User does not exist");
                return new ResponseCommon<>(ResponseCode.USER_NOT_FOUND, null);
            }

            blog.setTitle(addBlogRequest.getTitle());
            blog.setContent(addBlogRequest.getContent());
            blog.setLinkThumnail(addBlogRequest.getLinkThumnail());
            blog.setUserCreated(user);
            blog.setCreatedAt(LocalDateTime.now());
            Blog savedBlog = blogRepository.save(blog);

            if (savedBlog == null) {
                log.debug("Add Blog failed: Unable to save the blog");
                return new ResponseCommon<>(ResponseCode.FAIL, null);
            }

            log.debug("Add Blog successful");
            AddBlogResponse addBlogResponse = new AddBlogResponse();
            addBlogResponse.setMessage("Add Blog successful");
            return new ResponseCommon<>(ResponseCode.SUCCESS, addBlogResponse);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Add Blog failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<UpdateBlogResponse> updateBlog(UpdateBlogRequest updateBlogRequest) {
        try {
            Blog blog = blogRepository.findById(updateBlogRequest.getBlogId()).orElse(null);
            User user = userRepository.findByUsername(updateBlogRequest.getUsername()).orElse(null);

            if (Objects.isNull(blog)) {
                log.debug("Update Blog failed: Blog does not exist");
                return new ResponseCommon<>(ResponseCode.BLOG_NOT_EXIST, null);
            }

            blog.setTitle(updateBlogRequest.getTitle());
            blog.setContent(updateBlogRequest.getContent());
            blog.setLinkThumnail(updateBlogRequest.getLinkThumnail());
            blog.setUpdatedAt(LocalDateTime.now());
            blog.setUserUpdated(user);
            Blog updatedBlog = blogRepository.save(blog);

            if (updatedBlog == null) {
                log.debug("Update Blog failed: Unable to update the blog");
                return new ResponseCommon<>(ResponseCode.FAIL, null);
            }

            log.debug("Update Blog successful");
            UpdateBlogResponse updateBlogResponse = new UpdateBlogResponse();
            updateBlogResponse.setMessage("Update Blog successful");
            return new ResponseCommon<>(ResponseCode.SUCCESS, updateBlogResponse);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Update Blog failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<DeleteBlogResponse> deleteBlog(DeleteBlogRequest deleteBlogRequest) {
        try {
            Blog blog = blogRepository.findById(deleteBlogRequest.getBlogId()).orElse(null);
            User user = userRepository.findByUsername(deleteBlogRequest.getUsername()).orElse(null);

            if (Objects.isNull(blog)) {
                log.debug("Delete Blog failed: Blog does not exist");
                return new ResponseCommon<>(ResponseCode.BLOG_NOT_EXIST, null);
            }

            blogRepository.delete(blog);

            log.debug("Delete Blog successful");
            DeleteBlogResponse deleteBlogResponse = new DeleteBlogResponse();
            deleteBlogResponse.setMessage("Delete Blog successful");
            return new ResponseCommon<>(ResponseCode.SUCCESS, deleteBlogResponse);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Delete Blog failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }


    @Override
    public ResponseCommon<GetAllBlogResponse> findAllBlog(GetAllBlogRequest getAllBlogRequest) {
        try {
            List<Blog> blogList = blogRepository.findBlogByDeleted(getAllBlogRequest.isDeleted());

            if (blogList.isEmpty()) {
                log.debug("Get All Blogs failed: Blog list is empty");
                return new ResponseCommon<>(ResponseCode.BLOG_LIST_IS_EMPTY, null);
            }

            log.debug("Get All Blogs successful");
            GetAllBlogResponse getAllBlogResponse = new GetAllBlogResponse();
            getAllBlogResponse.setBlogList(blogList);
            return new ResponseCommon<>(ResponseCode.SUCCESS, getAllBlogResponse);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Get All Blogs failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }

    @Override
    public ResponseCommon<GetBlogByIdResponse> findById(GetBlogByIdRequest getBlogByIdRequest) {
        try {
            Blog blog = blogRepository.findById(getBlogByIdRequest.getBlogId()).orElse(null);

            if (Objects.isNull(blog)) {
                log.debug("Get Blog by ID failed: Blog does not exist");
                return new ResponseCommon<>(ResponseCode.BLOG_NOT_EXIST, null);
            }

            log.debug("Get Blog by ID successful");
            GetBlogByIdResponse getBlogByIdResponse = new GetBlogByIdResponse();
            getBlogByIdResponse.setBlog(blog);
            return new ResponseCommon<>(ResponseCode.SUCCESS, getBlogByIdResponse);
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("Get Blog by ID failed: " + e.getMessage());
            return new ResponseCommon<>(ResponseCode.FAIL, null);
        }
    }
}
