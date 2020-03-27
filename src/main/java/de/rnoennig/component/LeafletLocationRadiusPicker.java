package de.rnoennig.component;

import de.rnoennig.config.AppConfig;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.*;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;

public class LeafletLocationRadiusPicker<T extends LeafletLocationRadiusPicker.LatLngRadius> extends LeafletLocationPicker<T> implements IHeaderContributor {
    private static final long serialVersionUID = 1L;

    public interface LatLngRadius extends LeafletMap.LatLng {
        Integer getRadius();
        void setRadius(Integer radius);
    }

    public LeafletLocationRadiusPicker(String id, IModel<T> model) {
        super(id, model);
    }

    @Override
    protected void onMarkerUpdate(AjaxRequestTarget target) {
        super.onMarkerUpdate(target);
        // when the marker is updated, the position of the circle also needs to be updated
        target.appendJavaScript("putCircleMarkerOnMap({lat:'"+getModelObject().getLat()+"',lng:'"+getModelObject().getLng()+"'}, "+getModelObject().getRadius()+");");
    }

    public void onRadiusUpdate(RadioChoice<Integer> choice, AjaxRequestTarget target) {
        getModelObject().setRadius(choice.getModelObject());
        target.appendJavaScript("putCircleMarkerOnMap({lat:'"+getModelObject().getLat()+"',lng:'"+getModelObject().getLng()+"'}, "+getModelObject().getRadius()+");");
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(JavaScriptHeaderItem.forScript("var my_circleMarker;", "var_mycirclemarker"));

        response.render(JavaScriptHeaderItem.forScript("function putCircleMarkerOnMap(latlng, radius) {" +
                "        var new_radius = radius*1000;" +
                "    if (my_circleMarker == null) {\n" +
                "        my_circleMarker = L.circle(latlng, {'radius':new_radius});\n" +
                "        my_circleMarker.addTo(my_map);\n" +
                "    } else {" +
                "        my_circleMarker.setLatLng(latlng);" +
                "        my_circleMarker.setRadius(new_radius);" +
                "    }\n" +
                "}", "fun_putCircleMarkerOnMap"));
    }
}
