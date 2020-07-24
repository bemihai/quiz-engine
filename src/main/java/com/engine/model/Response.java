package com.engine.model;

import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class Response {

    @NotNull
    private boolean success;

    private String feedback;

    public Response(boolean success) {
        this.success = success;
        this.feedback = this.success ? "Congratulations, you're right!" : "Wrong answer! Please, try again.";
    }

}
