package sk.myshop.app.client.resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface MyShopResources extends ClientBundle {

    public static final MyShopResources instance = GWT.create(MyShopResources.class);

    @Source("shop.jpg")
    ImageResource defaultShopImage();

}
