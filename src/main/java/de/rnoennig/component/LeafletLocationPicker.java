package de.rnoennig.component;

import de.rnoennig.config.AppConfig;
import de.rnoennig.domain.EventDetail;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.*;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.util.List;

public class LeafletLocationPicker<T extends LeafletMap.LatLng> extends FormComponentPanel<T> implements IHeaderContributor {
    private static final long serialVersionUID = 1L;

    @SpringBean
    protected transient AppConfig appConfig;

    protected LeafletMap map;
    protected final AbstractDefaultAjaxBehavior markerUpdateCallback;
    private boolean showEvents;

    public LeafletLocationPicker(String id, IModel<T> model) {
        super(id, model);

        info("Using model: " + model);
        System.out.println("Using model: " + model);

        map = new LeafletMap("mapid", new ListModel<T>());
        add(map);

        markerUpdateCallback = new AbstractDefaultAjaxBehavior()
        {
            @Override
            protected void respond(AjaxRequestTarget target)
            {
                LeafletLocationPicker.this.onMarkerUpdate(target);
            }
        };
        add(markerUpdateCallback);
    }

    public void setMapTooltipRenderer(LeafletMap.TooltipRenderer tooltipRenderer) {
        this.map.setTooltipRenderer(tooltipRenderer);
    }

    protected void onMarkerUpdate(AjaxRequestTarget target) {
        IRequestParameters requestParameters = RequestCycle.get().getRequest().getRequestParameters();
        String lat = requestParameters.getParameterValue("lat").toString();
        String lng = requestParameters.getParameterValue("lng").toString();
        System.out.println("Got a response! " + lat + ";" + lng);

        getModelObject().setLat(lat);
        getModelObject().setLng(lng);
        // change ui according to new latlng
        target.appendJavaScript("putSingleMarkerOnMap({lat:'"+lat+"',lng:'"+lng+"'});");
    }

    /**
     * without this the model object will be set to whatever getConvertedInput returns
     */
    @Override
    public void convertInput() {
        setConvertedInput(getModelObject());
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(JavaScriptHeaderItem.forScript("var my_map;", "var_mymap"));
        response.render(JavaScriptHeaderItem.forScript("var my_marker;", "var_mymarker"));

        response.render(JavaScriptHeaderItem.forScript("function putSingleMarkerOnMap(latlng) {" +
                "    if (my_marker == null) {\n" +
                "        my_marker = L.marker(latlng);\n" +
                "        my_marker.addTo(my_map);\n" +
                "    } else {" +
                "        my_marker.setLatLng(latlng);" +
                "    }\n" +
                "}", "fun_putSingleMarkerOnMap"));

        response.render(OnDomReadyHeaderItem.forScript("my_map.on('click', function(e) {\n" +
                "    Wicket.Ajax.get({'u':'"+ markerUpdateCallback.getCallbackUrl() +"&lat=' + e.latlng.lat + '&lng=' + e.latlng.lng})\n" +
                "} );"));
    }

    public void setMapModel(List<EventDetail> eventDetails) {
        map.getModel().setObject(eventDetails);
    }

    public void setShowEvents(boolean showEvents) {
        map.setShowEvents(showEvents);
    }

    public void toggleShowEvents(AjaxRequestTarget target) {
        if (map.getShowEvents()) {
            map.hideEvents(target);
        } else {
            map.showEvents(target);
        }
    }
}
