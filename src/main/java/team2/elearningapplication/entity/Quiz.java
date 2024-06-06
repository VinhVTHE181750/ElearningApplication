

package team2.elearningapplication.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quiz")
@Accessors(chain = true)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn
    private Lesson lesson;
    @Column(name = "name")
    private String name;
    @Column(name="deleted")
    private boolean isDeleted;
    @Column(name="updated_at")
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn (name="created_by",referencedColumnName = "id")
    private User userCreated;
    @ManyToOne
    @JoinColumn(name="updated_by",referencedColumnName = "id")
    private User userUpdated;
}