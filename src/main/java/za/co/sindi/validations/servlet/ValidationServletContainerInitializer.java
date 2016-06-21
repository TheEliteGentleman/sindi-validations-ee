/**
 * 
 */
package za.co.sindi.validations.servlet;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

import za.co.sindi.validations.core.Validator;

/**
 * @author Bienfait Sindi
 * @since 20 May 2013
 *
 */
@HandlesTypes({Validator.class, za.co.sindi.validations.core.validator.Validator.class})
public class ValidationServletContainerInitializer implements ServletContainerInitializer {

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContainerInitializer#onStartup(java.util.Set, javax.servlet.ServletContext)
	 */
	@Override
	public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
		// TODO Auto-generated method stub

	}
}
