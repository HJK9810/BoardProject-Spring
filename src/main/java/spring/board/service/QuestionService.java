package spring.board.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import spring.board.domain.Question;
import spring.board.domain.Users;
import spring.board.web.QuestionForm;

import java.util.List;

public interface QuestionService {

    Users changeUser(Object auth);

    Page<Question> findList(Pageable pageable);
    List<Question> findByUserId(String email);

    Question viewOne(Long id, Users user);

    Question addQuestion(QuestionForm form, Users user);
    Question updateQuestion(Long id, QuestionForm form);
}
