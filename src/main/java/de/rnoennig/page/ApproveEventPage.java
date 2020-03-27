package de.rnoennig.page;

import de.rnoennig.dao.EventDAO;
import de.rnoennig.domain.ApproveRequestEvent;
import de.rnoennig.domain.RejectRequestEvent;
import de.rnoennig.domain.RequestEvent;
import de.rnoennig.service.EventService;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Fetch events from database
 * Approve or deny events
 */
public class ApproveEventPage extends LayoutPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private transient EventDAO eventDAO;

	@SpringBean
	private EventService eventService;

	private final ListView<RequestEvent> listView;

	public ApproveEventPage(final PageParameters parameters) {
		super(parameters);

		listView = new ListView<RequestEvent>("events") {
			@Override
			protected void populateItem(ListItem<RequestEvent> item) {
				item.add(new Label("name", new PropertyModel(item.getModel(), "name")));
				item.add(new Link<String>("approveEventButton") {
					@Override
					public void onClick() {
						eventService.process(new ApproveRequestEvent(item.getModelObject().getId()));
					}
				});
				item.add(new Link<String>("rejectEventButton") {
					@Override
					public void onClick() {
						eventService.process(new RejectRequestEvent(item.getModelObject().getId()));
					}
				});
			}
		};

		add(listView);
	}

	@Override
	protected void onBeforeRender() {
		listView.setList(eventService.getEventRequests());
		super.onBeforeRender();
	}
}
