package spring.board.web;

import io.swagger.v3.oas.annotations.Parameter;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final UserRepository userRepository;

    @GetMapping("/list")
    public ResponseEntity<Page<Question>> showList(Pageable pageable) {
        return ResponseEntity.ok(questionService.findList(pageable));
    }

    @GetMapping("/list/{userid}")
    public ResponseEntity<Page<Question>> showByUser(@Parameter(name = "userid", description = "user's email") @PathVariable String userid,
                                                     Pageable pageable, Authentication auth) {
        String email = auth.getName();

        if (!email.equals(userid)) return null;

        if (userRepository.existsByEmail(userid)) return ResponseEntity.ok(questionService.findByUserId(email, pageable));
        else return ResponseEntity.ok(null);
    }

    @GetMapping("/viewOne/{id}")
    public ResponseEntity<Question> viewOne(@Parameter(name = "id", description = "question's id") @PathVariable Long id,
                                            Authentication auth) {
        if (!questionService.checkUserAvailable(id, auth.getName())) throw new ApiExceptions(ErrorCode.MEMBER_NOT_ALLOWED);
        return ResponseEntity.ok(questionService.viewOne(id));
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Question> addQuestion(@ModelAttribute QuestionForm form, Authentication auth) {
        return ResponseEntity.ok(questionService.addQuestion(form, auth.getName()));
    }

    @PostMapping(value = "/edit/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Question> edit(@Parameter(name = "id", description = "question's id") @PathVariable Long id, @ModelAttribute QuestionForm form,
                                         Authentication auth) {
        if (!questionService.checkUserAvailable(id, auth.getName())) throw new ApiExceptions(ErrorCode.MEMBER_NOT_ALLOWED);
        return ResponseEntity.ok(questionService.updateQuestion(id, form));
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<Boolean> deleteQuestion(@Parameter(name = "id", description = "question's id") @PathVariable Long id, Authentication auth) {
        if (!questionService.checkUserAvailable(id, auth.getName())) throw new ApiExceptions(ErrorCode.MEMBER_NOT_ALLOWED);
        return ResponseEntity.ok(questionService.deleteQuestion(id));
    }
}
