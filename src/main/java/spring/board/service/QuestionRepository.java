package spring.board.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.board.domain.Question;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAll();
//    Page<Question> findByAll(Pageable pageable);

    Optional<Question> findById(Long id);

    Optional<Question> findByUserId(Long id);
}
