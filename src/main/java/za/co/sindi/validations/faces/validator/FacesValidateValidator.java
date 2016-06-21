/**
 * 
 */
package za.co.sindi.validations.faces.validator;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import za.co.sindi.validations.core.ValidationFactory;
import za.co.sindi.validations.core.ValidationMessage;
import za.co.sindi.validations.core.ValidationMessage.Type;
import za.co.sindi.validations.core.ValidationResult;
import za.co.sindi.validations.resolver.FacesContextMessageResolver;

/**
 * @author Bienfait Sindi
 * @since 11 February 2013
 *
 */
@FacesValidator(FacesValidateValidator.VALIDATOR_ID)
public class FacesValidateValidator implements Validator {

	public static final String VALIDATOR_ID = "za.co.sindi.validations.faces.validator.FacesValidate";
	public static final String VALIDATION_FACTORY_KEY = ValidationFactory.class.getName();
	private static final Logger LOGGER = Logger.getLogger(FacesValidateValidator.class.getName());
	
	/* (non-Javadoc)
	 * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		// TODO Auto-generated method stub
		if (context == null) {
			throw new IllegalArgumentException("A FacesContext is required.");
		}
		 
		if (component == null) {
			throw new IllegalArgumentException("A UIComponent is required.");
		}
		 
		ValidationFactory validationFactory = null;
		Object cachedValue = context.getExternalContext().getApplicationMap().get(VALIDATION_FACTORY_KEY);
		if (cachedValue != null && cachedValue instanceof ValidationFactory) {
			validationFactory = (ValidationFactory) cachedValue;
		} else {
			context.getExternalContext().getApplicationMap().remove(VALIDATION_FACTORY_KEY);
			validationFactory = ValidationFactory.getInstance();
			 
			if (validationFactory != null) {
				context.getExternalContext().getApplicationMap().put(VALIDATION_FACTORY_KEY, validationFactory);
			}
		}
		 
		if (validationFactory == null) {
			throw new IllegalStateException("Unable to instantiate/retrieve ValidationFactory.");
		}

		UIInput validatorIdInput = (UIInput) component.getAttributes().get("validatorId");
		if (validatorIdInput == null) {
			throw new IllegalStateException("Couldn't find Faces component attribute 'validatorId'.");
		}
		
		String validatorId = (String) validatorIdInput.getSubmittedValue();
		za.co.sindi.validations.core.Validator<Object> validator = validationFactory.getValidator(validatorId);
		if (validator == null) {
			throw new IllegalStateException("Unable to find validator of validator ID '" + validatorId + "'.");
		}
		validator.setMessageResolver(new FacesContextMessageResolver(context));
		Set<ValidationResult> results = null;
		try {
			results = validator.validate(value);
		} catch (za.co.sindi.validations.exception.ValidatorException e) {
			// TODO Auto-generated catch block
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.log(Level.FINE, "Unable to validate value '" + String.valueOf(value) + "'.", e);
			}
		}
		
		if (results != null) {
			//Let's see if we have validations.
			Set<FacesMessage> facesMessages = new LinkedHashSet<FacesMessage>();
			for (ValidationResult result : results) {
				Set<ValidationMessage> messages = result.getValidationMessages();
				if (messages != null) {
					for (ValidationMessage message : messages) {
						Severity severity = mapToSeverity(message.getType());
						if (severity != null) {
							FacesMessage facesMessage = new FacesMessage(message.getMessage());
							facesMessage.setSeverity(severity);
							facesMessages.add(facesMessage);
						}
					}
				}
			}
			
			if (!facesMessages.isEmpty()) {
				throw new ValidatorException(facesMessages);
			}
		}
	}
	
	private Severity mapToSeverity(Type messageType) {
		if (messageType != null) {
			if (Type.ERROR == messageType) {
				return FacesMessage.SEVERITY_ERROR;
			}
			
			if (Type.WARNING == messageType) {
				return FacesMessage.SEVERITY_WARN;
			}
		}
		
		return null;
	}
}
