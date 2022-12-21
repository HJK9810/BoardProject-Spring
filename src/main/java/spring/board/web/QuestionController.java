package spring.board.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import spring.board.domain.Question;
import spring.board.repository.UserRepository;
import spring.board.service.QuestionService;
import spring.board.web.dto.QuestionForm;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final UserRepository userRepository;

    @GetMapping("/list")
    public ResponseEntity<Page<Question>> showList(Pageable pageable) {
        return new ResponseEntity<>(questionService.findList(pageable), HttpStatus.OK);
    }

    @GetMapping("/list/{userid}")
    public ResponseEntity<List<Question>> showByUser(@PathVariable String userid, Authentication auth) {
        String email = auth.getName();

        if (!email.equals(userid)) return null;

        if (userRepository.existsByEmail(userid)) return new ResponseEntity<>(questionService.findByUserId(email), HttpStatus.OK);
        else return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    @GetMapping("/viewOne/{id}")
    public ResponseEntity<Question> viewOne(@PathVariable Long id) {
        return new ResponseEntity<>(questionService.viewOne(id), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Question> addQuestion(@ModelAttribute QuestionForm form, Authentication auth) {
        return new ResponseEntity<>(questionService.addQuestion(form, auth.getName()), HttpStatus.OK);
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<Question> editOne(@PathVariable Long id) {
        return new ResponseEntity<>(questionService.viewOne(id), HttpStatus.OK);
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<Question> edit(@PathVariable Long id, @ModelAttribute QuestionForm form) {
        return new ResponseEntity<>(questionService.updateQuestion(id, form), HttpStatus.OK);
    }
}
