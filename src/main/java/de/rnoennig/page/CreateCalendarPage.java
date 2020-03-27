package de.rnoennig.page;

import de.rnoennig.component.CompoundPropertyExpressionAwareModel;
import de.rnoennig.component.LeafletLocationRadiusPicker;
import de.rnoennig.domain.ApproveRequestEvent;
import de.rnoennig.service.EventService;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Create a custom calendar based on the distance to a point on the map
 */
public class CreateCalendarPage extends LayoutPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private transient EventService eventService;

	public static class FormState implements Serializable, LeafletLocationRadiusPicker.LatLngRadius {
		private static final long serialVersionUID = 1L;
		private String lat;
		private String lng;
		private Integer radius;

		@Override
		public String getLat() {
			return lat;
		}

		@Override
		public void setLat(String lat) {
			this.lat = lat;
		}

		@Override
		public String getLng() {
			return lng;
		}

		@Override
		public void setLng(String lng) {
			this.lng = lng;
		}

		@Override
		public Integer getRadius() {
			return radius;
		}

		@Override
		public void setRadius(Integer radius) {
			this.radius = radius;
		}

		@Override
		public String toString() {
			return "FormState{" +
					"lat='" + lat + '\'' +
					", lng='" + lng + '\'' +
					", radius=" + radius +
					'}';
		}
	}

	LeafletLocationRadiusPicker mapPicker;

	public CreateCalendarPage(final PageParameters parameters) {
		super(parameters);

		Button toogleEventVisibilityButton = new Button("toogleEventVisibilityButton");
		add(toogleEventVisibilityButton);
		toogleEventVisibilityButton.add(new AjaxEventBehavior("click"){

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				System.out.println("Marker visibility was toggled!");
				mapPicker.toggleShowEvents(target);
			}
		});

		CompoundPropertyModel model = new CompoundPropertyExpressionAwareModel(new CreateCalendarPage.FormState());
		Form form = new Form<LeafletLocationRadiusPicker.LatLngRadius>("mapForm", model) {
			@Override
			protected void onSubmit() {
				System.out.println("Form model is now: " + getModelObject());
			}
		};

		mapPicker = new LeafletLocationRadiusPicker("mapPicker", model);
		mapPicker.setMapModel(eventService.getEventsDetails());
		mapPicker.setShowEvents(true);
		form.add(mapPicker);

		List radiuses = Arrays.asList(new Integer[] { 10, 15, 20, 30, 50, 100, 200, 500 });
		RadioGroup radiusRadioGroup = new RadioGroup<>("radiusRadioGroup", Model.of(""));
		RadioChoice radiusRadioChoice = new RadioChoice<Integer>("radiusRadioChoice", model.bind("radius"), radiuses);
		radiusRadioChoice.add(new AjaxFormChoiceComponentUpdatingBehavior() {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				mapPicker.onRadiusUpdate((RadioChoice)getComponent(), target);
			}
		});
		radiusRadioGroup.add(radiusRadioChoice);
		form.add(radiusRadioGroup);
		form.add(new Button("submitCalendar"));
		add(form);
	}
}
