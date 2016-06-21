/**
 * 
 */
package za.co.sindi.validations.jsr303.validator;

import java.util.Set;
import java.util.logging.Level;

import javax.validation.ConstraintValidatorContext;

import za.co.sindi.validations.core.MessageResolver;
import za.co.sindi.validations.core.ValidationFactory;
import za.co.sindi.validations.core.ValidationMessage;
import za.co.sindi.validations.core.ValidationMessage.Type;
import za.co.sindi.validations.core.ValidationResult;
import za.co.sindi.validations.core.Validator;
import za.co.sindi.validations.core.resolver.GenericResourceBundleMessageResolver;
import za.co.sindi.validations.exception.ValidatorException;
import za.co.sindi.validations.jsr303.AbstractConstraintValidator;
import za.co.sindi.validations.jsr303.constraint.Validate;

/**
 * @author Bienfait Sindi
 * @since 08 February 2013
 *
 */
public class ValidateConstraintValidator extends AbstractConstraintValidator<Validate, Object> {

	private static final MessageResolver JSR_303_MESSAGE_RESOLVER = new GenericResourceBundleMessageResolver("ValidationMessages");
	
	/* (non-Javadoc)
	 * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
	 */
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
		// TODO Auto-generated method stub
		boolean valid = false; //Innocent until proven guilty
		
		try {
			final String validatorId = getConstraint().validatorId();
			if (validatorId == null || validatorId.isEmpty()) {
				throw new IllegalStateException("@Validate annotation doesn't have any value set on attribute validatorId. Cannot create validator.");
			}
			
			ValidationFactory validationFactory = null;
			if (getConstraint().factory() != null) {
				validationFactory = getConstraint().factory().newInstance();
			}
			
			if (validationFactory == null) {
				validationFactory = ValidationFactory.getInstance();
			}
			
			if (validationFactory == null) {
				throw new IllegalStateException("Unable to instantiate/retrieve ValidationFactory.");
			}
			
			Validator<Object> validator = validationFactory.getValidator(validatorId);
			if (validator == null) {
				throw new IllegalStateException("Unable to find validator of validator ID '" + validatorId + "'.");
			}
			validator.setMessageResolver(JSR_303_MESSAGE_RESOLVER);
			Set<ValidationResult> results = validator.validate(value);
			if (results != null) {
				//Let's see if we have errors.
				int errorCount = 0;
				for (ValidationResult result : results) {
					Set<ValidationMessage> messages = result.getValidationMessages();
					if (messages != null) {
						for (ValidationMessage message : messages) {
							if (message.getType() == Type.ERROR) {
								errorCount++;
								
								if (errorCount == 1 && (getConstraint().message() == null || getConstraint().message().isEmpty())) {
									constraintValidatorContext.disableDefaultConstraintViolation();
									constraintValidatorContext.buildConstraintViolationWithTemplate(message.getMessage()).addConstraintViolation();
								}
							}
						}
					}
				}
				
				valid = (errorCount == 0);
			}
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (ValidatorException e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return valid;
	}
}
