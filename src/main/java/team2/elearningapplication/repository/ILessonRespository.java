package team2.elearningapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team2.elearningapplication.entity.Lesson;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface ILessonRespository extends JpaRepository<Lesson,Integer> {
    Optional<Lesson> findLessonById(int id);
    @Query("SELECT l FROM Lesson l WHERE l.ordNumber = :ordNumber AND l.course.id = :courseId AND l.isDeleted = false")
    Optional<Lesson> findLessonByOrdNumberAndCourse(@Param("ordNumber") int ordNumber, @Param("courseId") int courseId);

    List<Lesson> findAllByIsDeleted(boolean isDeleted);
}