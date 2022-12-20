package spring.board.service;

import spring.board.domain.Answer;
import spring.board.web.dto.AnswerForm;

public interface AnswerService {
    Answer addAnswer(Long questionId, AnswerForm form);
}
