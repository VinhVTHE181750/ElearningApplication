package team2.elearningapplication.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "lesson_complete")
@Accessors(chain = true)
@Data
public class LessonCompleted {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn
    private User user;

    @OneToOne
    @JoinColumn
    private Lesson lesson;

    @ManyToOne
    @JoinColumn
    private Course course;

    @Column(name = "isDone")
    private boolean isDone;
}