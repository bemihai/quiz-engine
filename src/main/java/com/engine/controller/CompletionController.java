package com.engine.controller;

import com.engine.model.CompletionEntity;
import com.engine.model.UserEntity;
import com.engine.model.UserPrincipal;
import com.engine.service.CompletionService;
import com.engine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/quizzes/completed")
public class CompletionController {

    @Autowired
    CompletionService completionService;

    @Autowired
    UserService userService;

    private UserPrincipal getUser() {
        String username = ((UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();

        return (UserPrincipal) userService.loadUserByUsername(username);
    }

    @GetMapping
    public ResponseEntity<List<CompletionEntity>> getAllCompletions(
            @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "completedAt") String sortBy
    ) {
        String email = getUser().getUsername();
        Optional<UserEntity> user = userService.findByEmail(email);

        if (user.isPresent()) {
            List<CompletionEntity> completions = completionService.findByUser(user.get(), pageNo, pageSize, sortBy);
            return new ResponseEntity<>(completions, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

