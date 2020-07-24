package com.engine.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name="COMPLETIONS")
@Getter
@Setter
@NoArgsConstructor
public class CompletionEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    long compId;

    @CreationTimestamp
    private LocalDateTime completedAt;

    @NotNull
    private Long id;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public UserEntity user;

}
