package sk.myshop.app.server.command;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sk.myshop.app.client.command.ListShops;
import sk.myshop.app.client.command.ListShopsResult;
import sk.myshop.app.client.command.ListShopsResult.ShopListItem;
import sk.myshop.app.server.Executable;
import sk.myshop.app.server.domain.PageResult;
import sk.myshop.app.server.domain.merchant.Shop;
import sk.myshop.app.server.domain.merchant.ShopContactDetails;
import sk.myshop.app.server.domain.merchant.ShopLegalInformation;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@Configurable
public class ListShopsCommand extends ListShops implements Executable<ListShopsResult> {

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ListShopsResult execute() {
        // TODO init test data
        if (Shop.pageAll(1, null).getItems().size() == 0)
            initData();

        PageResult<Shop, Key> result = Shop.pageAll(getPageSize(), decodeBookmark(getKeyBookmark()));

        List<ShopListItem> items = new ArrayList<ShopListItem>(result.getItems().size());
        for (Shop shop : result.getItems())
            items.add(shop.toShopListItem());

        return new ListShopsResult(items, encodeBookmark(result.getBookmark()));
    }

    Key decodeBookmark(String value) {
        return value != null ? KeyFactory.stringToKey(value) : null;
    }

    String encodeBookmark(Key value) {
        return value != null ? KeyFactory.keyToString(value) : null;
    }

    void initData() {
        String bufu = "bufu lorem ipsum bufu lorem ipsum bufu lorem ipsum bufu lorem ipsum bufu lorem ipsum";

        for (int i = 0; i < 15; i++) {
            ShopLegalInformation legalInformation = new ShopLegalInformation("saleConditions", "deliveryConditions", "returnConditions");
            ShopContactDetails contactDetails = new ShopContactDetails("shop" + i + "@zoznam.sk", "Bratislava");

            if (random())
                contactDetails.setPhone("+421 905 11 22 33");
            if (random())
                contactDetails.setStreet("Ulica súdruha Fica 14");
            if (random())
                contactDetails.setZip("841 07");

            Shop shop = new Shop("shop-" + i, "Shop #" + i, bufu, null, contactDetails, legalInformation);
            persistShop(shop);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void persistShop(Shop shop) {
        shop.persist();
    }

    boolean random() {
        return Math.random() < 0.5;
    }

}
