package com.engine.service;

import com.engine.model.CompletionEntity;
import com.engine.model.QuizEntity;
import com.engine.model.UserEntity;
import com.engine.repository.CompletionRepository;
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
public class CompletionService {

    @Autowired
    UserService userService;

    @Autowired
    CompletionRepository completionRepository;

    public void addCompletion(QuizEntity quizEntity, UserEntity user) {
        String userEmail = user.getUsername();
        Optional<UserEntity> currentUser = userService.findByEmail(userEmail);
        if (currentUser.isPresent()) {
            CompletionEntity c = new CompletionEntity();
            c.setId(quizEntity.getId());
            currentUser.get().addCompletion(c);
            completionRepository.save(c);
        }
    }

    public List<CompletionEntity> findByUser(UserEntity user, Integer pageNo, Integer pageSize, String sortBy) {

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<CompletionEntity> pagedCompletions = completionRepository.findByUser(user, pageable);

        if(pagedCompletions.hasContent()) {
            return pagedCompletions.getContent();
        } else {
            return new ArrayList<>();
        }

    }
}