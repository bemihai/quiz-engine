package com.engine.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name="QUIZZES")
@Getter
@Setter
@NoArgsConstructor
public class QuizEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @NotBlank
    private String text;

    @ElementCollection
    @CollectionTable(
            name="OPTIONS",
            joinColumns=@JoinColumn(name="QUIZ_ID")
    )
    @Column(name="OPTION")
    @NotNull @Size(min=2)
    private List<String> options;

    @ElementCollection
    @CollectionTable(
            name="ANSWERS",
            joinColumns=@JoinColumn(name="QUIZ_ID")
    )
    @Column(name="ANSWER")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> answer;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public UserEntity user;

}



