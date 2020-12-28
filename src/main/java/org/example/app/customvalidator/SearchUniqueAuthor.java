package org.example.app.customvalidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueAuthorSearchConstraintValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchUniqueAuthor {

    String message() default "My custom validator ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
