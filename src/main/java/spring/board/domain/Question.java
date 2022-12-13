package spring.board.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonManagedReference
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
        answer.setQuestion(this);
    }

    public Question() {
    }

    public Question(String title, String contents, String images) {
        this.title = title;
        this.contents = contents;
        this.images = images;
    }
}
