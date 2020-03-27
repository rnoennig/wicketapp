package de.rnoennig.config;

import de.rnoennig.page.ApproveEventPage;
import de.rnoennig.page.CreateCalendarPage;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Component
public class WicketApplication extends WebApplication implements ApplicationContextAware
{
	private ApplicationContext ctx;

	public WicketApplication() {
	}

	public WicketApplication(WebApplicationContext ctx) {
		this.ctx = ctx;
	}

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return CreateCalendarPage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();
		if (this.ctx == null) {
			System.out.println("WicketApplication.ctx was null, setting to context stored in servlet...");
			this.ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(
					getServletContext());
		}
        getComponentInstantiationListeners().add(new SpringComponentInjector(this,
				this.ctx, true));

		if (getConfigurationType() == RuntimeConfigurationType.DEVELOPMENT) {
			getDebugSettings().setOutputMarkupContainerClassName(true);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
	}
}
