package team2.elearningapplication.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team2.elearningapplication.entity.Course;
import team2.elearningapplication.entity.Lesson;

import java.util.List;
import java.util.Optional;

public interface ILessonRespository extends JpaRepository<Lesson,Integer> {
    Optional<Lesson> findLessonById(int id);
    @Query("SELECT l FROM Lesson l WHERE l.ordNumber = :ordNumber AND l.course.id = :courseId AND l.isDeleted = false")
    Optional<Lesson> findLessonByOrdNumberAndCourse(@Param("ordNumber") int ordNumber, @Param("courseId") int courseId);

    List<Lesson> findAllByIsDeleted(boolean isDeleted);

    Page<Lesson> findAllByIsDeleted(boolean isDeleted, Pageable pageable);

    List<Lesson>  findAllByCourse(Course course);
    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.course.id = :courseId")
    int countLessonsByCourseId(@Param("courseId") int courseId);
}
