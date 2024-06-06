package team2.elearningapplication.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team2.elearningapplication.entity.Question;

import java.util.List;
import java.util.Optional;

public interface IQuestionRepository extends JpaRepository<Question, Integer> {
    Optional<Question> findQuestionById(int questionID);

    Page<Question> findAllByDeleted(boolean deleted, Pageable pageable);

    Question findQuestionByQuizIDAndAndOrdQuestion(int quizId, int ordQuestion);

    List<Question> findQuestionByDeleted(boolean deleted);

    @Query("SELECT COUNT(q) FROM Question q WHERE q.quizID = :quizId")
    int countQuestionsByQuizId(@Param("quizId") int quizId);

    List<Question> getQuestionByQuizID(int quizId);

}
