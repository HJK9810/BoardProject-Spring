package spring.board.service;

import lombok.RequiredArgsConstructor;
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
    public Question addQuestion(QuestionForm form) {
        String images = fileStore.storeFiles(form.getImages());
        Question question = new Question(form.getTitle(), form.getContents(), images);

        questionRepository.save(question);
        return null;
    }

    @Override
    public Question updateQuestion(Long id, QuestionForm form) {
        String images = fileStore.storeFiles(form.getImages());

        questionRepository.findById(id).ifPresent(question -> {

            if (!form.getTitle().isEmpty()) question.setTitle(form.getTitle());
            if (!form.getContents().isEmpty()) question.setContents(form.getContents());
            if (!images.equals("") && images.length() != 0) question.setImages(images);

            questionRepository.save(question);
        });

        return questionRepository.findById(id).get();
    }
}
