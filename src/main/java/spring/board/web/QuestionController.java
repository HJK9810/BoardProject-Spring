package spring.board.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.board.domain.Question;
import spring.board.file.FileStore;
import spring.board.service.QuestionService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final FileStore fileStore;

    @GetMapping("/list")
    public ResponseEntity<List<Question>> showList() {
        List<Question> list = questionService.findList();

        return new ResponseEntity<List<Question>>(list, HttpStatus.OK);
    }

    @GetMapping("/list/{userid}")
    public ResponseEntity<List<Question>> showByUser(@PathVariable String userid) {
        List<Question> list = questionService.findByUserId(userid);

        return new ResponseEntity<List<Question>>(list, HttpStatus.OK);
    }

    @GetMapping("/viewOne/{id}")
    public ResponseEntity<Question> viewOne(@PathVariable Long id) {
        Question question = questionService.viewOne(id);

        return new ResponseEntity<Question>(question, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Question> addQuestion(@RequestBody QuestionForm form) {
        String images = fileStore.storeFiles(form.getImages());

        log.info("form={}", form);
        Question question = new Question(form.getTitle(), form.getContents(), images);
        Question addQuestion = questionService.addQuestion(question);

        return new ResponseEntity<Question>(addQuestion, HttpStatus.OK);
    }

    @GetMapping ("/edit/{id}")
    public ResponseEntity<Question> editOne(@PathVariable Long id) {
        return new ResponseEntity<>(questionService.viewOne(id), HttpStatus.OK);
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<Question> edit(@PathVariable Long id, @RequestBody QuestionForm form) {
        String images = fileStore.storeFiles(form.getImages());

        Question question = new Question(form.getTitle(), form.getContents(), images);
        Question updateQuestion = questionService.updateQuestion(id, question);

        return new ResponseEntity<>(updateQuestion, HttpStatus.OK);
    }
}
