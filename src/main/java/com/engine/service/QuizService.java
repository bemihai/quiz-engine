package com.engine.service;

import com.engine.exception.RecordNotFoundException;
import com.engine.exception.UserNotEligibleException;
import com.engine.model.*;
import com.engine.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizService {

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    UserService userService;

    public List<QuizEntity> getAllQuizzes(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<QuizEntity> pagedResult = quizRepository.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public QuizEntity getQuizById(Long id) throws RecordNotFoundException {

        Optional<QuizEntity> quiz = quizRepository.findById(id);

        if(quiz.isPresent()) {
            return quiz.get();
        } else {
            throw new RecordNotFoundException(String.format("There is no quiz with id %s", id));
        }
    }

    public QuizEntity createQuiz(QuizEntity quizEntity, UserPrincipal principal) throws RecordNotFoundException {
        String userEmail = principal.getUsername();
        Optional<UserEntity> user = userService.findByEmail(userEmail);

        if (user.isPresent()) {
            quizEntity.setId(null);
            user.get().addQuiz(quizEntity);
            return quizRepository.save(quizEntity);
        } else {
            throw new RecordNotFoundException(String.format("There is no user registered with email %s", userEmail));
        }
    }

    public QuizEntity updateQuizById(Long id, QuizEntity newQuizEntity, UserPrincipal principal) throws RecordNotFoundException, UserNotEligibleException {
        Optional<QuizEntity> quiz = quizRepository.findById(id);

        if (quiz.isPresent()) {

            QuizEntity oldQuizEntity = quiz.get();

            if (isEligibleUser(oldQuizEntity, principal)) {
                oldQuizEntity.setTitle(newQuizEntity.getTitle());
                oldQuizEntity.setText(newQuizEntity.getText());
                oldQuizEntity.setOptions(newQuizEntity.getOptions());
                oldQuizEntity.setAnswer(newQuizEntity.getAnswer());
                return quizRepository.save(oldQuizEntity);
            } else {
                throw new UserNotEligibleException("Action not allowed.");
            }
        } else {
            throw new RecordNotFoundException(String.format("There is no quiz with id %s", id));
        }
    }

    public void deleteQuizById(Long id) throws RecordNotFoundException {
        Optional<QuizEntity> quiz = quizRepository.findById(id);

        if(quiz.isPresent()) {
            quizRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException(String.format("There is no quiz with id %s", id));
        }
    }

    public boolean isEligibleUser(QuizEntity quizEntity, UserPrincipal principal) {
        return quizEntity.getUser().getEmail().equals(principal.getUsername());
    }

    private Answer getCorrectAnswer(QuizEntity quizEntity) {
        List<Integer> answer = quizEntity.getAnswer();
        return answer == null ? new Answer(new ArrayList<>(0)) : new Answer(answer);
    }

    public Response getResponse(QuizEntity quizEntity, Answer answer) {
        List<Integer> correct = getCorrectAnswer(quizEntity).getAnswer();
        List<Integer> given = answer.getAnswer();
        return new Response(given.equals(correct));
    }

}