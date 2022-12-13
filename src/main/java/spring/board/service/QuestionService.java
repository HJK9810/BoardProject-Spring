package spring.board.service;

import spring.board.domain.Question;
import spring.board.web.QuestionForm;

import java.util.List;

public interface QuestionService {

    List<Question> findList();
    List<Question> findByUserId(String email);

    Question viewOne(Long id);

    Question addQuestion(QuestionForm form);
    Question updateQuestion(Long id, QuestionForm form);
}
