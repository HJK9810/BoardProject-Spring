package spring.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.board.domain.Answer;
import spring.board.domain.Question;
import spring.board.repository.AnswerRepository;
import spring.board.repository.QuestionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Override
    public List<Answer> answerList(Long id) {
        return answerRepository.findAllById(id);
    }

    @Override
    public Answer addAnswer(Long questionId, Answer answer) {
        Question question = questionRepository.findById(questionId).get();
        question.addAnswer(answer);

        answerRepository.save(answer);
        return answer;
    }
}
