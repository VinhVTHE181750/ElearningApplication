package team2.elearningapplication.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import team2.elearningapplication.Enum.EnumQuestionType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "question")
@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "question")
    private String questionName;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type")
    private EnumQuestionType questionType;

    @OneToMany
    @JoinColumn
    private List<Answer> answerList;
    @Column(name = "ord_question")
    private int ordQuestion;
    @Column(name = "quiz_id")
    private int quizID;
    @Column(name = "deleted")
    private boolean deleted;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private User userCreated;
    @ManyToOne
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    private User userUpdated;
}
