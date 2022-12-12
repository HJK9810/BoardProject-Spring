package spring.board.service;

import spring.board.domain.Question;

import java.util.List;

public interface QuestionService {

    List<Question> findList();
    List<Question> findByUserId(String email);

    Question viewOne(Long id);

    Question addQuestion(Question question);
    Question updateQuestion(Long id, Question question);
}
