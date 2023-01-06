package spring.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.board.domain.Answer;
import spring.board.domain.Question;
import spring.board.file.FileDelete;
import spring.board.file.FileStore;
import spring.board.repository.QuestionRepository;
import spring.board.repository.UserRepository;
import spring.board.web.dto.QuestionForm;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService{

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final AnswerService answerService;
    private final FileStore fileStore;
    private final FileDelete fileDelete;

    @Override
    public Page<Question> findList(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

    @Override
    public Page<Question> findByUserId(String email, Pageable pageable) {
        return questionRepository.findAllByUsers_Email(email, pageable);
    }

    @Override
    public Question viewOne(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    @Override
    public Question addQuestion(QuestionForm form, String email) {
        String images = fileStore.storeFiles(form.getImages());

        Question question = new Question(form.getTitle(), form.getContents(), images);
        userRepository.findByEmail(email).ifPresent(question::setUsers);

        return questionRepository.save(question);
    }

    @Override
    public Question updateQuestion(Long id, QuestionForm form) {
        String images = fileStore.storeFiles(form.getImages());

        questionRepository.findById(id).ifPresent(question -> {
            if (!question.getTitle().equals(form.getTitle()) && form.getTitle() != null) question.setTitle(form.getTitle());
            if (!question.getContents().equals(form.getContents()) && form.getContents() != null) question.setContents(form.getContents());
            fileDelete.deleteFile(question.getImages(), form.getSavedImages()); // delete not use
            question.setImages(form.getSavedImages() + images);

            questionRepository.save(question);
        });

        return questionRepository.findById(id).orElse(null);
    }

    @Override
    public Boolean deleteQuestion(Long id) {
        Question question = questionRepository.findById(id).orElse(null);
        if (question == null) return false;

        List<Answer> answers = question.getAnswers();
        while (!answers.isEmpty()) answerService.delete(question, answers.get(0));

        question.setUsers(null);
        questionRepository.delete(question);
        return true;
    }

    @Override
    public Boolean checkUserAvailable(Long id, String email) {
        return questionRepository.findById(id).map(question -> question.getUsers().getEmail().equals(email)).orElse(false);
    }
}
