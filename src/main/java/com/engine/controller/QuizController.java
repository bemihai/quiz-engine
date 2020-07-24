package com.engine.controller;

import com.engine.exception.RecordNotFoundException;
import com.engine.exception.UserNotEligibleException;
import com.engine.model.*;
import com.engine.service.CompletionService;
import com.engine.service.QuizService;
import com.engine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/api/quizzes")
class QuizController {

    @Autowired
    QuizService quizService;

    @Autowired
    UserService userService;

    @Autowired
    CompletionService completionService;

    // get registered user
    private UserPrincipal getUser() {
        String username = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return (UserPrincipal) userService.loadUserByUsername(username);
        }

    // get all quizzes
    @GetMapping()
    public ResponseEntity<List<QuizEntity>> getAllQuizzes(
            @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy
            ) {
        List<QuizEntity> quizEntities = quizService.getAllQuizzes(pageNo, pageSize, sortBy);

        return new ResponseEntity<>(quizEntities, new HttpHeaders(), HttpStatus.OK);
    }

    // get a quiz by id
    @GetMapping("/{id}")
    public ResponseEntity<QuizEntity> getQuizById(@PathVariable("id") Long id) throws RecordNotFoundException {
        QuizEntity quizEntity = quizService.getQuizById(id);
        return new ResponseEntity<>(quizEntity, new HttpHeaders(), HttpStatus.OK);
    }

    // add a new quiz
    @PostMapping()
    public ResponseEntity<QuizEntity> addQuiz(@Valid @RequestBody final QuizEntity quizEntity) throws RecordNotFoundException {
        UserPrincipal principal = getUser();
        QuizEntity newQuizEntity = quizService.createQuiz(quizEntity, principal);
        return new ResponseEntity<>(newQuizEntity, new HttpHeaders(), HttpStatus.OK);
    }

    // update an existing quiz
    @PutMapping("/{id}")
    public ResponseEntity<QuizEntity> updateQuizById(@PathVariable("id") final Long id,
                                                     @Valid @RequestBody final QuizEntity quizEntityToUpdate)
            throws RecordNotFoundException, UserNotEligibleException {
        UserPrincipal principal = getUser();
        QuizEntity newQuizEntity = quizService.updateQuizById(id, quizEntityToUpdate, principal);
        return new ResponseEntity<>(newQuizEntity, new HttpHeaders(), HttpStatus.OK);
    }

    // remove a quiz by id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuizById(@PathVariable("id") final Long id) throws RecordNotFoundException {
        UserPrincipal principal = getUser();
        if (quizService.isEligibleUser(quizService.getQuizById(id), principal)) {
            quizService.deleteQuizById(id);
            return new ResponseEntity<>("Success", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN);
        }
    }

    // solve a quiz by id
    @PostMapping("/{id}/solve")
    public ResponseEntity<Response> solveQuizById(@PathVariable("id") Long id, @Valid @RequestBody Answer answer)
            throws RecordNotFoundException {

        QuizEntity quizEntity = quizService.getQuizById(id);
        Response response = quizService.getResponse(quizEntity, answer);

        if (response.isSuccess()) {
            String email = getUser().getUsername();
            Optional<UserEntity> user = userService.findByEmail(email);
            user.ifPresent(value -> completionService.addCompletion(quizEntity, value));
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
