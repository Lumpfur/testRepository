package config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidateConfig {
    double min() default Double.MIN_VALUE;
    double max() default Double.MAX_VALUE;
    int minLength() default 0;
    int maxLength() default Integer.MAX_VALUE;
    String[] allowedValues() default {};
    boolean required() default false;
}