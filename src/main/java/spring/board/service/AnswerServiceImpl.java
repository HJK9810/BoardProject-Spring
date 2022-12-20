package spring.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring.board.domain.Answer;
import spring.board.domain.Question;
import spring.board.repository.AnswerRepository;
import spring.board.repository.QuestionRepository;
import spring.board.web.AnswerForm;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Override
    public List<Answer> answerList(Long id) {
        return answerRepository.findAllByQuestion_Id(id);
    }

    @Override
    public Answer addAnswer(Long questionId, AnswerForm form) {
        Question question = questionRepository.findById(questionId).get();
        answerRepository.save(new Answer(form.getContents(), question));

        List<Answer> list = answerRepository.findAllByQuestion_Id(questionId);
        Answer answer = list.get(list.size() - 1);

        question.addAnswer(answer);
        return answer;
    }
}
