package spring.board.service;

import spring.board.domain.Answer;

import java.util.List;

public interface AnswerService {

    List<Answer> answerList(Long id);

    Answer addAnswer(Long questionId, Answer answer);
}
