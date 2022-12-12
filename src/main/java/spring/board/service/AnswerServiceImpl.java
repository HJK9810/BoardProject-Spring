package spring.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.board.domain.Answer;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;

    @Override
    public List<Answer> answerList(Long id) {
        return answerRepository.findAllById(id);
    }

    @Override
    public Answer addAnswer(Answer answer) {
        answerRepository.save(answer);
        return null;
    }
}
