package spring.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring.board.domain.Answer;
import spring.board.domain.Question;
import spring.board.repository.AnswerRepository;
import spring.board.repository.QuestionRepository;
import spring.board.web.dto.AnswerForm;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Override
    public Answer addAnswer(Long questionId, AnswerForm form) {
        Question question = questionRepository.findById(questionId).get();
        answerRepository.save(new Answer(form.getContents(), question));

        List<Answer> list = answerRepository.findAllByQuestion_Id(questionId);
        Answer answer = list.get(list.size() - 1);

        question.addAnswer(answer);
        return answer;
    }

    @Override
    public Answer viewOne(Long id) {
        return answerRepository.findById(id).get();
    }

    @Override
    public Answer updateAnswer(Long id, AnswerForm form) {
        answerRepository.findById(id).ifPresent(answer -> {
            if (!answer.getContents().equals(form.getContents())) answer.setContents(form.getContents());
            answerRepository.save(answer);
        });
        return answerRepository.findById(id).get();
    }

    @Override
    public Boolean deleteAnswer(Long id, Long answerId) {
        Question question = questionRepository.findById(id).get();
        Answer answer = answerRepository.findById(answerId).get();
        answer.setQuestion(null);
        question.getAnswers().remove(answer);
        questionRepository.save(question);

        answerRepository.delete(answer);
        return true;
    }
}
