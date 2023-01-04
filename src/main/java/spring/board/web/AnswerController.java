package spring.board.web;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<List<Answer>> showAnswers(@Parameter(name = "questionId", description = "question's id") @PathVariable Long questionId) {
        return ResponseEntity.ok(questionService.viewOne(questionId).getAnswers());
    }

    @PostMapping("/add/{questionId}")
    public ResponseEntity<Answer> addAnswer(@Parameter(name = "questionId", description = "question's id") @PathVariable Long questionId, @RequestBody AnswerForm form) {
        return ResponseEntity.ok(answerService.addAnswer(questionId, form));
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<Answer> editAnswerView(@Parameter(name = "id", description = "answer's id") @PathVariable Long id) {
        return ResponseEntity.ok(answerService.viewOne(id));
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<Answer> editAnswer(@Parameter(name = "id", description = "answer's id") @PathVariable Long id, @RequestBody AnswerForm form) {
        return ResponseEntity.ok(answerService.updateAnswer(id, form));
    }

    @DeleteMapping("/del/{id}")
    @Parameter(name = "aId", description = "answer's id")
    public ResponseEntity<Answer> delAnswer(@Parameter(name = "id", description = "question's id") @PathVariable Long id, @RequestParam("aId") Long answerId) {
        return ResponseEntity.ok(new Answer(String.valueOf(answerService.deleteAnswer(id, answerId)), null));
    }
}
