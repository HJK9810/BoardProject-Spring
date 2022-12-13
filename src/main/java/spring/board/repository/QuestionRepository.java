package spring.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.board.domain.Question;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

//    List<Question> findAll();
    Page<Question> findAll(Pageable pageable);

    Optional<Question> findById(Long id);

    Optional<Question> findByUserId(Long id);
}
