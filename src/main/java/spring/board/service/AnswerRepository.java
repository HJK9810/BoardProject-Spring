package spring.board.service;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.board.domain.Answer;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByALl();

    List<Answer> findByQuestionId(Long id);
}
