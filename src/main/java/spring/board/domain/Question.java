package spring.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
@Table(name = "QUESTION")
public class Question extends BaseTime {

    @Id @GeneratedValue
    @Column
    private Long id;

    @Column
    private String title;
    @Column
    private String contents;
    @Column
    private String images;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers;

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public Question() {
    }

    public Question(String title, String contents, String images) {
        this.title = title;
        this.contents = contents;
        this.images = images;
    }
}
