package team2.elearningapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team2.elearningapplication.entity.Lesson;
import team2.elearningapplication.entity.LessonCompleted;
import team2.elearningapplication.entity.User;

import java.util.List;
import java.util.Optional;

public interface ILessonCompletedRepository extends JpaRepository<LessonCompleted, Integer> {

    Optional<LessonCompleted> findLessonsByUserAndLesson(User user, Lesson lesson);

    @Query("SELECT lc FROM LessonCompleted lc " +
            "WHERE lc.user.username = :username " +
            "AND lc.lesson.course.id = :courseId")
    List<LessonCompleted> findCompletedLessonsByUserAndCourse(
            @Param("username") String username,
            @Param("courseId") int courseId
    );
}
