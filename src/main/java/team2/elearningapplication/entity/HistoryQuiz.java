package team2.elearningapplication.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "history_quiz")
@Accessors(chain = true)
@Data
public class HistoryQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @Column(name = "sessionId")
    private int sessionId;
    @Column(name = "answerId_user")
    private int answerId;
    @Column(name = "answer_is_correct")
    private boolean isCorrect;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
