package team2.elearningapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team2.elearningapplication.entity.Post;
import team2.elearningapplication.entity.User;

import java.util.List;
import java.util.Optional;

public interface IPostRepository extends JpaRepository<Post, Integer> {

    Optional<Post> findPostByUserAndCourseId(User user, int courseId);

    List<Post> findPostByCourseIdAndDeleted(int courseId, boolean deleted);

    List<Post> findPostByDeleted(boolean isDeleted);

}
