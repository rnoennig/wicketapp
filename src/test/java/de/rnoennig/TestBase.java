package de.rnoennig;

import de.rnoennig.config.AppConfig;
import de.rnoennig.config.MyContextLoaderListener;
import de.rnoennig.config.WicketApplication;
import de.rnoennig.page.CreateCalendarPage;
import de.rnoennig.page.HomePage;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ComponentScan(basePackages = "de.rnoennig")
@ContextConfiguration(classes = AppConfig.class)
public class TestBase
{
	protected WicketTester tester;

	@Autowired
	protected WebApplicationContext wac;

	@Before
	public void setUpBeforeEachTest()
	{
		tester = new WicketTester(new WicketApplication(wac));
	}

	@Test
	public void homepageRendersSuccessfully()
	{
		//start and render the test page
		tester.startPage(HomePage.class);

		//assert rendered page class
		tester.assertRenderedPage(HomePage.class);
	}
}
