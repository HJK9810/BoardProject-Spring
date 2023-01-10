package spring.board.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import spring.board.domain.Question;
import spring.board.web.dto.QuestionForm;

public interface QuestionService {
    Page<Question> findList(Pageable pageable);
    Page<Question> findByUserId(String userid, String email, Pageable pageable);

    Question viewOne(Long id);

    Question addQuestion(QuestionForm form, String email);
    Question updateQuestion(Long id, QuestionForm form);
    Boolean deleteQuestion(Long id);
}
