package de.rnoennig.page;

import de.rnoennig.component.LeafletMap;
import de.rnoennig.domain.EventDetail;
import de.rnoennig.service.EventService;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Fetch events from database
 * Add events to database
 */
public class EventMapPage extends LayoutPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private transient EventService eventService;
	private final LeafletMap map;

	public EventMapPage(final PageParameters parameters) {
		super(parameters);

		map = new LeafletMap("eventMap", new ListModel<EventDetail>());
		map.setTooltipRenderer(new LeafletMap.TooltipRenderer<EventDetail>() {
			@Override
			public String render(EventDetail e) {
				return e.getCreatedAt().toString() + ": " + e.getName();
			}
		});
		add(map);
	}

	@Override
	protected void onBeforeRender() {
		map.getModel().setObject(eventService.getEventsDetails());
		super.onBeforeRender();
	}
}
