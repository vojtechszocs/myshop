package sk.myshop.app.server.domain.merchant;

import static sk.myshop.app.server.util.StringUtils.normalizedLowerCase;

import java.util.List;

import javax.jdo.Query;
import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.listener.StoreCallback;
import javax.persistence.Column;

import sk.myshop.app.client.command.ListShopsResult.ShopListItem;
import sk.myshop.app.server.domain.KeyBasedPageQuery;
import sk.myshop.app.server.domain.ModelObjectWithKeywords;
import sk.myshop.app.server.domain.PageResult;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * 'Obchod'.
 */
@PersistenceCapable
public class Shop extends ModelObjectWithKeywords<Shop> implements StoreCallback {

    /**
     * 'Identifikator obchodu'.
     * <p>
     * Unique, immutable. Normalized, lower case.
     */
    @Persistent
    @Column(nullable = false)
    private String id;

    /**
     * 'Nazov obchodu'.
     * <p>
     * Unindexed.
     */
    @Persistent
    @Column(nullable = false)
    @Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
    private String name;

    /**
     * 'Popis obchodu'.
     * <p>
     * Contains wiki-like markup.
     */
    @Persistent
    @Column(nullable = false)
    private Text description;

    /**
     * 'Obchodnik'.
     * <p>
     * Immutable.
     */
    @Persistent
    @Column(nullable = false)
    private Merchant merchant;

    /**
     * 'Kontaktne udaje obchodu.'
     */
    @Persistent
    @Column(nullable = false)
    @Embedded
    private ShopContactDetails contactDetails;

    /**
     * 'Pravne informacie pre zakaznikov'.
     */
    @Persistent
    @Column(nullable = false)
    @Embedded
    private ShopLegalInformation legalInformation;

    private Shop() {
    }

    public Shop(String id, String name, String description, Merchant merchant, ShopContactDetails contactDetails,
            ShopLegalInformation legalInformation) {
        setId(id);
        setName(name);
        setDescription(description);
        setMerchant(merchant);
        setContactDetails(contactDetails);
        setLegalInformation(legalInformation);
    }

    @Override
    public void jdoPreStore() {
        if (name != null)
            addWordsToKeywords(name);
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = normalizedLowerCase(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description.getValue();
    }

    public void setDescription(String description) {
        this.description = new Text(description);
    }

    public Merchant getMerchant() {
        return merchant;
    }

    private void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public ShopContactDetails getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(ShopContactDetails contactDetails) {
        this.contactDetails = contactDetails;
    }

    public ShopLegalInformation getLegalInformation() {
        return legalInformation;
    }

    public void setLegalInformation(ShopLegalInformation legalInformation) {
        this.legalInformation = legalInformation;
    }

    public ShopListItem toShopListItem() {
        return new ShopListItem(getId(), getName(), getDescription(), getContactDetails().getStreet(), getContactDetails().getCity(), getContactDetails().getZip(), getContactDetails().getEmail(), getContactDetails().getPhone(), null, 15);
    }

    private static Shop newInstance() {
        return new Shop();
    }

    public static Shop findByEncodedKey(String encodedKey) {
        return newInstance().findByEncodedKey(encodedKey, Shop.class);
    }

    public static Shop findById(String id) {
        KeyBasedPageQuery<Shop> pq = newInstance().pageQuery(1, Shop.class);
        pq.onNormalizedLowerCase("id", id);
        return uniqueResult(pq.execute().getItems());
    }

    public static boolean isAvailable(String id) {
        return findById(id) == null;
    }

    public static void deleteAllByMerchant(Merchant merchant) {
        Query q = newInstance().getPersistenceManager().newQuery(Shop.class, "merchant == :p");
        q.deletePersistentAll(merchant);
    }

    public static PageResult<Shop, Key> pageAll(int pageSize, Key bookmark) {
        return newInstance().pageQuery(pageSize, bookmark, Shop.class).execute();
    }

    public static PageResult<Shop, Key> pageByKeywords(List<String> keywords, int pageSize, Key bookmark) {
        KeyBasedPageQuery<Shop> pq = newInstance().keywordPageQuery(keywords, pageSize, bookmark, Shop.class);
        return pq.execute();
    }

    public static PageResult<Shop, Key> pageByCity(String city, int pageSize, Key bookmark) {
        KeyBasedPageQuery<Shop> pq = newInstance().pageQuery(pageSize, bookmark, Shop.class);
        pq.onLowerCase("contactDetails.city", city);
        return pq.execute();
    }

    public static PageResult<Shop, Key> pageByMerchant(Merchant merchant, int pageSize, Key bookmark) {
        KeyBasedPageQuery<Shop> pq = newInstance().pageQuery(pageSize, bookmark, Shop.class);
        pq.on("merchant", merchant);
        return pq.execute();
    }

    public static PageResult<Shop, Key> pageByMerchantAndKeywords(Merchant merchant, List<String> keywords, int pageSize, Key bookmark) {
        KeyBasedPageQuery<Shop> pq = newInstance().keywordPageQuery(keywords, pageSize, bookmark, Shop.class);
        pq.on("merchant", merchant);
        return pq.execute();
    }

    public static PageResult<Shop, Key> pageByMerchantAndCity(Merchant merchant, String city, int pageSize, Key bookmark) {
        KeyBasedPageQuery<Shop> pq = newInstance().pageQuery(pageSize, bookmark, Shop.class);
        pq.on("merchant", merchant);
        pq.onLowerCase("contactDetails.city", city);
        return pq.execute();
    }

}
