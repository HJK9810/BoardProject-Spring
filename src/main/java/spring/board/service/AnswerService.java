package spring.board.service;

import spring.board.domain.Answer;
import spring.board.web.dto.AnswerForm;

public interface AnswerService {
    Answer addAnswer(Long questionId, AnswerForm form);
    Answer viewOne(Long id);
    Answer updateAnswer(Long id, AnswerForm form);

    Boolean deleteAnswer(Long id, Long answerId);
}
