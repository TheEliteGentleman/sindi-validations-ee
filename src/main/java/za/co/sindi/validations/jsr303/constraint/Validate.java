/**
 * 
 */
package za.co.sindi.validations.jsr303.constraint;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Payload;

import za.co.sindi.validations.core.ValidationFactory;

/**
 * @author Bienfait Sindi
 * @since 08 February 2013
 *
 */
@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface Validate {

	String message() default "";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default {};
	
	//Other parameters
	Class<? extends ValidationFactory> factory();
	
	String validatorId() default "";
}
