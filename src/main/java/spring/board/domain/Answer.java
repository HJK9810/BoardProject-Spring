package spring.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "ANSWER")
public class Answer {

    @Id @GeneratedValue
    @Column
    private Long id;
    @Column
    private String contents;

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id")
    private Question question;

    public Answer() {
    }

    public Answer(String contents, Question question) {
        this.contents = contents;
        this.question = question;
    }
}
