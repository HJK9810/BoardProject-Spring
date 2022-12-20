package spring.board.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.board.domain.Answer;
import spring.board.service.AnswerService;
import spring.board.service.QuestionService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerService answerService;
    private final QuestionService questionService;

    @GetMapping("/list/{questionId}")
    public ResponseEntity<List<Answer>> showAnswers(@PathVariable Long questionId) {
        List<Answer> list = questionService.viewOne(questionId).getAnswers();

        return new ResponseEntity<List<Answer>>(list, HttpStatus.OK);
    }

    @PostMapping("/add/{questionId}")
    public ResponseEntity<Answer> addAnswer(@PathVariable Long questionId, @RequestBody AnswerForm form) {
        log.info("form={}", form);
        log.info("id={}", questionId);
        Answer addAnswer = answerService.addAnswer(questionId, form);

        return new ResponseEntity<Answer>(addAnswer, HttpStatus.OK);
    }
}
