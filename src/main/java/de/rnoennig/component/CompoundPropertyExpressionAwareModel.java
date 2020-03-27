package de.rnoennig.component;

import org.apache.wicket.Component;
import org.apache.wicket.model.AbstractPropertyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * Allows nested PropertyModel with property expressions different from a component's id
 * @param <T> The model object type
 */
public class CompoundPropertyExpressionAwareModel<T> extends CompoundPropertyModel<T> {
    public CompoundPropertyExpressionAwareModel(T object) {
        super(object);
    }

    @Override
    protected String propertyExpression(Component component) {
        IModel model = component.getDefaultModel();
        if (model != null && model instanceof AbstractPropertyModel) {
            return ((AbstractPropertyModel) model).getPropertyExpression();
        }
        return component.getId();
    }
}
