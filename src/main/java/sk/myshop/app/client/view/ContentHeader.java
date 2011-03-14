package sk.myshop.app.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class ContentHeader extends Composite {

    interface Binder extends UiBinder<Widget, ContentHeader> {
        static final Binder binder = GWT.create(Binder.class);
    }

    @UiField
    HTMLPanel headerPanel;

    @UiConstructor
    public ContentHeader(String text) {
        initWidget(Binder.binder.createAndBindUi(this));
        setText(text);
    }

    public void setText(String text) {
        headerPanel.getElement().setInnerText(text);
    }

}
