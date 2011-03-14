package sk.myshop.app.client.command;

import java.util.List;

import sk.myshop.command.client.CommandResultHandler;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ListShopsResult extends ListCommandResult<CommandResultHandler<ListShopsResult>, ListShopsResult.ShopListItem> {

    private static Type<CommandResultHandler<ListShopsResult>> TYPE;

    public static Type<CommandResultHandler<ListShopsResult>> getType() {
        return TYPE != null ? TYPE : (TYPE = new Type<CommandResultHandler<ListShopsResult>>());
    }

    @Override
    public Type<CommandResultHandler<ListShopsResult>> getAssociatedType() {
        return getType();
    }

    protected ListShopsResult() {
    }

    public ListShopsResult(List<ShopListItem> items, String keyBookmark) {
        super(items, keyBookmark);
    }

    public static class ShopListItem implements IsSerializable {

        private String id;
        private String name;
        private String description;
        private String street;
        private String city;
        private String zip;
        private String email;
        private String phone;
        private String imageUrl;
        private int relatedProductCount;

        protected ShopListItem() {
        }

        public ShopListItem(String id, String name, String description, String street, String city, String zip, String email, String phone,
                String imageUrl, int relatedProductCount) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.street = street;
            this.city = city;
            this.zip = zip;
            this.email = email;
            this.phone = phone;
            this.imageUrl = imageUrl;
            this.relatedProductCount = relatedProductCount;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getStreet() {
            return street;
        }

        public String getCity() {
            return city;
        }

        public String getZip() {
            return zip;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public int getRelatedProductCount() {
            return relatedProductCount;
        }

    }

}
