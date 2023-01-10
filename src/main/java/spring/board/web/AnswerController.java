package spring.board.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.board.domain.Answer;
import spring.board.service.AnswerService;
import spring.board.service.QuestionService;
import spring.board.web.dto.AnswerForm;

import java.util.List;

@Tag(name = "Answer API", description = "답변 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerService answerService;
    private final QuestionService questionService;

    @GetMapping("/list/{questionId}")
    @Operation(summary = "해당 문항 답변보기")
    public ResponseEntity<List<Answer>> showAnswers(@Parameter(description = "question's id") @PathVariable("questionId") Long questionId) {
        return ResponseEntity.ok(questionService.viewOne(questionId).getAnswers());
    }

    @PostMapping("/add/{questionId}")
    @Operation(summary = "답변 추가")
    public ResponseEntity<Answer> addAnswer(@Parameter(description = "question's id") @PathVariable("questionId") Long questionId, @RequestBody AnswerForm form) {
        return ResponseEntity.ok(answerService.addAnswer(questionId, form));
    }

    @GetMapping("/edit/{id}")
    @Operation(summary = "답변 수정 - Get")
    public ResponseEntity<Answer> editAnswerView(@Parameter(description = "answer's id") @PathVariable("id") Long id) {
        return ResponseEntity.ok(answerService.viewOne(id));
    }

    @PostMapping("/edit/{id}")
    @Operation(summary = "답변 수정 - Post")
    public ResponseEntity<Answer> editAnswer(@Parameter(description = "answer's id") @PathVariable("id") Long id, @RequestBody AnswerForm form) {
        return ResponseEntity.ok(answerService.updateAnswer(id, form));
    }

    @DeleteMapping("/del/{id}")
    @Operation(summary = "댓글 삭제", parameters = {
            @Parameter(name = "id", description = "question's id", in = ParameterIn.PATH),
            @Parameter(name = "aId", description = "answer's id", in = ParameterIn.QUERY)
    })
    public ResponseEntity<Answer> delAnswer(@PathVariable("id") Long id, @RequestParam("aId") Long answerId) {
        return ResponseEntity.ok(new Answer(String.valueOf(answerService.deleteAnswer(id, answerId)), null));
    }
}
