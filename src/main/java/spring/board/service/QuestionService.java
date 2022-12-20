package spring.board.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import spring.board.domain.Question;
import spring.board.domain.Users;
import spring.board.security.UserDetailsVO;
import spring.board.web.QuestionForm;

import java.util.List;

public interface QuestionService {

    Users changeUser(UserDetailsVO detailsVO);

    Page<Question> findList(Pageable pageable);
    List<Question> findByUserId(String email);

    Question viewOne(Long id);

    Question addQuestion(QuestionForm form, String email);
    Question updateQuestion(Long id, QuestionForm form);
}
