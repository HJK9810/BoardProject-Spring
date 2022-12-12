package spring.board.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import spring.board.domain.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAll();
    Page<Question> findByAll(Pageable pageable);

    Optional<Question> findById(Long id);

    Optional<Question> findByUserId(Long id);
}
