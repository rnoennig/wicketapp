package de.rnoennig;

import de.rnoennig.page.CreateCalendarPage;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;

public class CreateCalendarPageTest extends TestBase {

    @Test
    public void createCalendarPageRendersSuccessfully()
    {
        //start and render the test page
        tester.startPage(CreateCalendarPage.class);

        //assert rendered page class
        tester.assertRenderedPage(CreateCalendarPage.class);

        //TODO:ajax form test
    }
}
