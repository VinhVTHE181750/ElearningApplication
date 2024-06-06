package team2.elearningapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team2.elearningapplication.entity.Course;
import team2.elearningapplication.entity.HistoryRegisterCourse;
import team2.elearningapplication.entity.User;

import java.util.Optional;

public interface IHistoryResgisterCourseRepository extends JpaRepository<HistoryRegisterCourse, Integer> {
    @Query("SELECT hrc FROM HistoryRegisterCourse hrc WHERE hrc.course = :course AND hrc.user = :user")
    Optional<HistoryRegisterCourse> findHistoryRegisterCourseByCourseIdAndUser(@Param("course") Course course, @Param("user") User user);

}
