package spring.board.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.board.domain.Question;
import spring.board.service.QuestionService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/list")
    public ResponseEntity<Page<Question>> showList(Pageable pageable) {
        Page<Question> list = questionService.findList(pageable);

        return new ResponseEntity<Page<Question>>(list, HttpStatus.OK);
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
    public ResponseEntity<Question> addQuestion(@ModelAttribute QuestionForm form) {
        log.info("form={}", form);
        Question addQuestion = questionService.addQuestion(form);

        return new ResponseEntity<Question>(addQuestion, HttpStatus.OK);
    }

    @GetMapping ("/edit/{id}")
    public ResponseEntity<Question> editOne(@PathVariable Long id) {
        return new ResponseEntity<>(questionService.viewOne(id), HttpStatus.OK);
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<Question> edit(@PathVariable Long id, @ModelAttribute QuestionForm form) {
        log.info("form = {}", form);
        Question updateQuestion = questionService.updateQuestion(id, form);

        return new ResponseEntity<>(updateQuestion, HttpStatus.OK);
    }
}
