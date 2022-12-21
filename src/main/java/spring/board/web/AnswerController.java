package spring.board.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.board.domain.Answer;
import spring.board.service.AnswerService;
import spring.board.service.QuestionService;
import spring.board.web.dto.AnswerForm;

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

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/add/{questionId}")
    public ResponseEntity<Answer> addAnswer(@PathVariable Long questionId, @RequestBody AnswerForm form) {
        Answer addAnswer = answerService.addAnswer(questionId, form);

        return new ResponseEntity<>(addAnswer, HttpStatus.OK);
    }
}
