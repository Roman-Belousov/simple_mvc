package org.example.app.customvalidator;


import java.lang.annotation.*;
import javax.validation.*;

@Documented
@Constraint(validatedBy = UniqueIdConstraintValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueId {

    String message() default "My custom validator ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
