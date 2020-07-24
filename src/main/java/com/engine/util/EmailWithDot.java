package com.engine.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Email
@Pattern(regexp = ".+@.+\\..+", message = "Invalid email format")
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface EmailWithDot {

    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
