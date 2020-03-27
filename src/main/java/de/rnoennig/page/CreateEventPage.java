package de.rnoennig.page;

import de.rnoennig.component.*;
import de.rnoennig.domain.RequestEvent;
import de.rnoennig.service.EventService;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;

/**
 * Fetch events from database
 * Add events to database
 */
public class CreateEventPage extends LayoutPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private transient EventService eventService;

	public CreateEventPage(final PageParameters parameters) {
		super(parameters);

		CompoundPropertyModel<RequestEvent> model = new CompoundPropertyExpressionAwareModel(new RequestEvent());
		Form form = new Form<RequestEvent>("form", model) {
			protected void onSubmit() {
				model.getObject().setOccured(new Date());
				System.out.println("Trying to processRequestEvent model object: " + model.getObject());
				eventService.process(model.getObject());
			}
		};

		form.add(new TextField<String>("eventName", model.bind("name")));
		form.add(new LeafletLocationPicker("eventLocation", model));

		Button button = new Button("addEventButton");

		form.add(button);
		add(form);
	}
}
