/**
 * 
 */
package za.co.sindi.validations.resolver;

import java.util.Locale;

import javax.faces.context.FacesContext;

import za.co.sindi.validations.core.ResourceBundleAwareMessageResolver;

/**
 * @author Bienfait Sindi
 * @since 03 February 2013
 *
 */
public class FacesContextMessageResolver extends ResourceBundleAwareMessageResolver {

	private FacesContext context;
	
	/**
	 * 
	 */
	public FacesContextMessageResolver() {
		this(FacesContext.getCurrentInstance());
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 */
	public FacesContextMessageResolver(FacesContext context) {
		super();
		if (context == null) {
			throw new IllegalArgumentException("A FacesContext may not be null.");
		}
		
		setBundleName(context.getApplication().getMessageBundle());
		this.context = context;		
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.validations.core.MessageResolver#resolveMessage(java.lang.String)
	 */
	@Override
	public String resolveMessage(String key) {
		// TODO Auto-generated method stub
		Locale locale= context.getViewRoot().getLocale();
		
		if (locale == null) {
			locale = context.getApplication().getViewHandler().calculateLocale(context);
		}
		
		return getBundle(locale).getString(key);
	}
}
