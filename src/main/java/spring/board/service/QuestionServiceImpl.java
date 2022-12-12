package spring.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.board.domain.Question;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService{

    private final QuestionRepository questionRepository;

    @Override
    public List<Question> findList() {
        return questionRepository.findAll();
    }

    @Override
    public List<Question> findByUserId(String email) {
        return null;
    }

    @Override
    public Question viewOne(Long id) {
        return questionRepository.findById(id).get();
    }

    @Override
    public Question addQuestion(Question question) {
        questionRepository.save(question);
        return null;
    }

    @Override
    public Question updateQuestion(Long id, Question question) {
        questionRepository.findById(id).ifPresent(q -> {

            if (question.getTitle() != null || question.getTitle().length() != 0) q.setTitle(question.getTitle());

            if (question.getContents() != null || question.getContents().length() != 0) q.setContents(question.getContents());

            if (question.getImages() != null || question.getImages().length() != 0) q.setImages(question.getImages());
        });
        return questionRepository.findById(id).get();
    }
}
