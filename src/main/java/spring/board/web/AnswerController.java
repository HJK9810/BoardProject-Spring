package spring.board.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.board.domain.Answer;
import spring.board.service.AnswerService;
import spring.board.service.QuestionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerService answerService;
    private final QuestionService questionService;

    @GetMapping("/list/{questionId}")
    public ResponseEntity<List<Answer>> showAnswers(@PathVariable Long questionId) {
//        List<Answer> list = answerService.answerList(questionId);
        List<Answer> list = questionService.viewOne(questionId).getAnswers();

        return new ResponseEntity<List<Answer>>(list, HttpStatus.OK);
    }

    @PostMapping("/add/{questionId}")
    public ResponseEntity<Answer> addAnswer(@PathVariable Long questionId, @RequestBody Answer answer) {
        Answer addAnswer = answerService.addAnswer(questionId, answer);
//        questionService.viewOne(questionId).addAnswer(addAnswer);

        return new ResponseEntity<Answer>(addAnswer, HttpStatus.OK);
    }
}
