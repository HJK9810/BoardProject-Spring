package spring.board.service;

import spring.board.domain.Answer;
import spring.board.web.AnswerForm;

import java.util.List;

public interface AnswerService {

    List<Answer> answerList(Long id);

    Answer addAnswer(Long questionId, AnswerForm form);
}
