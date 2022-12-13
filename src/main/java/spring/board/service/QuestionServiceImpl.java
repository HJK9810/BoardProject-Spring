package spring.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.board.domain.Question;
import spring.board.file.FileStore;
import spring.board.repository.QuestionRepository;
import spring.board.web.QuestionForm;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService{

    private final QuestionRepository questionRepository;
    private final FileStore fileStore;

    @Override
    public Page<Question> findList(Pageable pageable) {
        return questionRepository.findAll(pageable);
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
    public Question addQuestion(QuestionForm form) {
        String images = fileStore.storeFiles(form.getImages());
        Question question = new Question(form.getTitle(), form.getContents(), images);

        questionRepository.save(question);
        return question;
    }

    @Override
    public Question updateQuestion(Long id, QuestionForm form) {
        String images = fileStore.storeFiles(form.getImages());

        questionRepository.findById(id).ifPresent(question -> {
            if (!question.getTitle().equals(form.getTitle())) question.setTitle(form.getTitle());
            if (!question.getContents().equals(form.getContents())) question.setContents(form.getContents());
            if (!question.equals(images)) question.setImages(images);

            questionRepository.save(question);
        });

        return questionRepository.findById(id).get();
    }
}
