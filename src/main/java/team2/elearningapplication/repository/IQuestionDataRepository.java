package team2.elearningapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team2.elearningapplication.dto.request.admin.question.QuestionData;
import team2.elearningapplication.entity.Question;

import java.util.Optional;

public interface IQuestionDataRepository extends JpaRepository<Question, Integer> {
    Optional<QuestionData> findQuestionByQuestionName(String questionName);

}
