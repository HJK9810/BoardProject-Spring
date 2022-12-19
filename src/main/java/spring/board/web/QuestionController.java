package spring.board.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import spring.board.domain.Question;
import spring.board.domain.Users;
import spring.board.repository.UserRepository;
import spring.board.security.UserDetailsVO;
import spring.board.service.QuestionService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final UserRepository userRepository;

    @GetMapping("/user")
    public ResponseEntity<Users> user(Authentication auth) {
        Users user = ((UserDetailsVO) auth.getPrincipal()).getUser();
//        Users printUser = userRepository.findByEmail(user.getEmail()).get();

        return new ResponseEntity<Users>(user, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<Question>> showList(Pageable pageable) {
        Page<Question> list = questionService.findList(pageable);

        return new ResponseEntity<Page<Question>>(list, HttpStatus.OK);
    }

    @GetMapping("/list/{userid}")
    public ResponseEntity<List<Question>> showByUser(@PathVariable String userid, Authentication auth) {
        Users user = questionService.changeUser(auth.getPrincipal());

        if (!user.getEmail().equals(userid)) return null;

        List<Question> list;
        if (userRepository.existsByEmail(userid)) list = questionService.findByUserId(user.getEmail());
        else list = null;

        return new ResponseEntity<List<Question>>(list, HttpStatus.OK);
    }

    @GetMapping("/viewOne/{id}")
    public ResponseEntity<Question> viewOne(@PathVariable Long id, Authentication auth) {
        Users user = questionService.changeUser(auth.getPrincipal());
        Question question = questionService.viewOne(id, user);

        return new ResponseEntity<Question>(question, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Question> addQuestion(@ModelAttribute QuestionForm form, Authentication auth) {
        log.info("form={}", form);
        Users user = questionService.changeUser(auth.getPrincipal());
        Question addQuestion = questionService.addQuestion(form, user);

        return new ResponseEntity<Question>(addQuestion, HttpStatus.OK);
    }

    @GetMapping ("/edit/{id}")
    public ResponseEntity<Question> editOne(@PathVariable Long id, Authentication auth) {
        Users user = questionService.changeUser(auth.getPrincipal());
        return new ResponseEntity<>(questionService.viewOne(id, user), HttpStatus.OK);
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<Question> edit(@PathVariable Long id, @ModelAttribute QuestionForm form) {
        log.info("form = {}", form);
        Question updateQuestion = questionService.updateQuestion(id, form);

        return new ResponseEntity<>(updateQuestion, HttpStatus.OK);
    }
}
