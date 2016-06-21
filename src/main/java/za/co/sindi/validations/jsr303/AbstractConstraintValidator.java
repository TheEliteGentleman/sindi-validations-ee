/**
 * 
 */
package za.co.sindi.validations.jsr303;

import java.lang.annotation.Annotation;
import java.util.logging.Logger;

import javax.validation.ConstraintValidator;

/**
 * @author Buhake Sindi
 * @since 25 July 2012
 *
 */
public abstract class AbstractConstraintValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {

	protected final Logger logger = Logger.getLogger(this.getClass().getName());
	private A constraint;
	
	/* (non-Javadoc)
	 * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
	 */
	@Override
	public void initialize(A constraint) {
		// TODO Auto-generated method stub
		this.constraint = constraint;
	}

	/**
	 * @return the constraint
	 */
	protected A getConstraint() {
		return constraint;
	}
}
