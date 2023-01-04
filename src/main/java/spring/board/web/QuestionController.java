package spring.board.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import spring.board.domain.Question;
import spring.board.exception.ApiExceptions;
import spring.board.exception.ErrorCode;
import spring.board.repository.UserRepository;
import spring.board.service.QuestionService;
import spring.board.web.dto.QuestionForm;

@Tag(name = "question API", description = "문의사항 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final UserRepository userRepository;

    @GetMapping("/list")
    @Operation(summary = "문의사항 전체보기")
    public ResponseEntity<Page<Question>> showList(Pageable pageable) {
        return ResponseEntity.ok(questionService.findList(pageable));
    }

    @GetMapping("/list/{userid}")
    @Operation(summary = "자신의 문의사항 조회")
    public ResponseEntity<Page<Question>> showByUser(@PathVariable("userid") String userid, Pageable pageable, Authentication auth) {
        String email = auth.getName();

        if (!email.equals(userid)) return null;

        if (userRepository.existsByEmail(userid)) return ResponseEntity.ok(questionService.findByUserId(email, pageable));
        else return ResponseEntity.ok(null);
    }

    @GetMapping("/viewOne/{id}")
    @Operation(summary = "문의사항 상세보기")
    public ResponseEntity<Question> viewOne(@Parameter(description = "question's id") @PathVariable("id") Long id, Authentication auth) {
        if (!questionService.checkUserAvailable(id, auth.getName())) throw new ApiExceptions(ErrorCode.MEMBER_NOT_ALLOWED);
        return ResponseEntity.ok(questionService.viewOne(id));
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.ALL_VALUE)
    @Operation(summary = "문의사항 추가")
    public ResponseEntity<Question> addQuestion(@ModelAttribute QuestionForm form, Authentication auth) {
        return ResponseEntity.ok(questionService.addQuestion(form, auth.getName()));
    }

    @PostMapping(value = "/edit/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.ALL_VALUE)
    @Operation(summary = "문의사항 수정하기")
    public ResponseEntity<Question> edit(@Parameter(description = "question's id") @PathVariable("id") Long id, @ModelAttribute QuestionForm form, Authentication auth) {
        if (!questionService.checkUserAvailable(id, auth.getName())) throw new ApiExceptions(ErrorCode.MEMBER_NOT_ALLOWED);
        return ResponseEntity.ok(questionService.updateQuestion(id, form));
    }

    @DeleteMapping("/del/{id}")
    @Operation(summary = "문의사항 삭제하기")
    public ResponseEntity<Boolean> deleteQuestion(@Parameter(description = "question's id") @PathVariable("id") Long id, Authentication auth) {
        if (!questionService.checkUserAvailable(id, auth.getName())) throw new ApiExceptions(ErrorCode.MEMBER_NOT_ALLOWED);
        return ResponseEntity.ok(questionService.deleteQuestion(id));
    }
}
