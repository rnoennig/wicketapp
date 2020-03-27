package de.rnoennig.component;

import de.rnoennig.config.AppConfig;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.*;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.util.List;
/**
 *
 * @param <T> The model object type
 * @param <E> class of a single element in the list
 */
public class LeafletMap<T extends List<? extends E>, E extends LeafletMap.LatLng> extends FormComponentPanel<T> implements IHeaderContributor {
    private static final long serialVersionUID = 1L;

    public static interface TooltipRenderer<T> {
        String render(T e);
    }

    private TooltipRenderer tooltipRenderer = new DefaultTooltipRenderer();
    private boolean showEvents;

    public static class DefaultTooltipRenderer<T> implements TooltipRenderer<T>, Serializable {

        @Override
        public String render(T e) {
            return e.toString();
        }
    }

    public interface LatLng extends Serializable {
        String getLat();
        void setLat(String lat);
        String getLng();
        void setLng(String lng);
    }

    @SpringBean
    private transient AppConfig appConfig;

    public LeafletMap(String id, ListModel<T> model) {
        super(id, (IModel)model);
    }

    public void setTooltipRenderer(TooltipRenderer tooltipRenderer) {
        this.tooltipRenderer = tooltipRenderer;
    }

    public void setShowEvents(boolean showEvents) {
        this.showEvents = showEvents;
    }

    public boolean getShowEvents() {
        return this.showEvents;
    }

    public void showEvents(AjaxRequestTarget target) {
        this.showEvents = true;
        target.appendJavaScript("showEvents();");
    }

    public void hideEvents(AjaxRequestTarget target) {
        this.showEvents = false;
        target.appendJavaScript("hideEvents();");
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssContentHeaderItem.forCSS("#"+getMarkupId()+" { height: 480px; }", "leafletmapid"));
        response.render(CssHeaderItem.forUrl("https://unpkg.com/leaflet@1.6.0/dist/leaflet.css"));
        response.render(JavaScriptHeaderItem.forUrl("https://unpkg.com/leaflet@1.6.0/dist/leaflet.js"));

        response.render(JavaScriptHeaderItem.forScript("var my_map;", "var_mymap"));
        response.render(JavaScriptHeaderItem.forScript("var my_markerLayer;", "var_mymarkerlayer"));

        String showEvents = "";
        if (this.showEvents) {
            showEvents = "showEvents()";
        }

        response.render(JavaScriptHeaderItem.forScript("function hideEvents() {\n" +
                "    my_markerLayer.removeFrom(my_map);\n" +
                "}", "fun_hideMarkers"));

        response.render(JavaScriptHeaderItem.forScript("function showEvents() {\n" +
                "    my_markerLayer.addTo(my_map);\n" +
                "}", "fun_showMarkers"));

        response.render(JavaScriptHeaderItem.forScript("function putMarkerOnMap(latlng, tooltip) {\n" +
                "    console.log('Putting marker on map in js...');\n" +
                "    console.log(latlng);\n" +
                "    var my_marker = L.marker(latlng);\n" +
                "    my_marker.bindTooltip(tooltip);\n" +
                "    my_marker.addTo(my_markerLayer);\n" +
                "}", "fun_putMarkerOnMap"));

        response.render(OnDomReadyHeaderItem.forScript("my_map = L.map('"+getMarkupId()+"').setView([50.731111111111, 10.194722222222], 5);\n" +
                "L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}', {\n" +
                "    attribution: 'Map data &copy; <a href=\"https://www.openstreetmap.org/\">OpenStreetMap</a> contributors, <a href=\"https://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>, Imagery © <a href=\"https://www.mapbox.com/\">Mapbox</a>',\n" +
                "    maxZoom: 18,\n" +
                "    id: 'mapbox/streets-v11',\n" +
                "    accessToken: '"+appConfig.getMapboxAccessToken()+"'\n" +
                "}).addTo(my_map);"));

        response.render(OnDomReadyHeaderItem.forScript("my_markerLayer = L.layerGroup();\n" +
                "        my_markerLayer.addTo(my_map);\n" +
                ""));

        T mo = getModelObject();
        if (mo == null) {
            System.out.println("model object is null in LeafletMap.readerHead()");
        } else {
            for (E e : getModelObject()) {
                String tooltip = LeafletMap.this.renderTooltip(e);
                response.render(OnDomReadyHeaderItem.forScript("putMarkerOnMap({lat:'" + e.getLat() + "',lng:'" + e.getLng() + "'},'"+tooltip+"');"));
            }
        }
    }

    protected String renderTooltip(E e) {
        String rendered = this.tooltipRenderer.render(e);
        String sanitized = rendered.replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\"");
        return sanitized;
    }
}
