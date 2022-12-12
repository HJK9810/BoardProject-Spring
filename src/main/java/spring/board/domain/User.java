package spring.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "USERS")
public class User {

    @Id @GeneratedValue
    @Column
    private Long id;
    @Column
    private String name;
    @Column
    private String email;
}
