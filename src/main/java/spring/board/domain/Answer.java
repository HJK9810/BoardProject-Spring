package spring.board.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "ANSWER")
public class Answer extends BaseTime {

    @Id @GeneratedValue
    @Column
    private Long id;
    @Column
    private String contents;

    @JsonBackReference
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id")
    private Question question;

    public Answer() {
    }

    public Answer(String contents, Question question) {
        this.contents = contents;
        this.question = question;
    }
}
