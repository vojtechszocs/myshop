package sk.myshop.app.server.domain.merchant;

import static sk.myshop.app.server.util.StringUtils.normalizedLowerCase;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.listener.StoreCallback;
import javax.persistence.Column;

import sk.myshop.app.server.domain.KeyBasedPageQuery;
import sk.myshop.app.server.domain.ModelObjectWithKeywords;
import sk.myshop.app.server.domain.PageQuery;
import sk.myshop.app.server.domain.PageResult;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * 'Produkt'.
 */
@PersistenceCapable
public class Product extends ModelObjectWithKeywords<Product> implements StoreCallback {

    /**
     * 'Identifikator produktu'.
     * <p>
     * Unique, immutable. Normalized, lower case.
     */
    @Persistent
    @Column(nullable = false)
    private String id;

    /**
     * 'Nazov produktu'.
     * <p>
     * Unindexed.
     */
    @Persistent
    @Column(nullable = false)
    @Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
    private String name;

    /**
     * 'Popis produktu'.
     * <p>
     * Contains wiki-like markup.
     */
    @Persistent
    @Column(nullable = false)
    private Text description;

    /**
     * 'Obchod'.
     * <p>
     * Can be null (unassociated entity).
     */
    @Persistent
    private String shopId;

    /**
     * 'Aktualna cena produktu'.
     */
    @Persistent
    @Column(nullable = false)
    private Double price;

    /**
     * 'Aktualne mnozstvo na sklade'.
     * <p>
     * Unindexed.
     */
    @Persistent
    @Column(nullable = false)
    @Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
    private Integer stockAmount;

    /**
     * 'Aspon jeden kus na sklade?'.
     * <p>
     * Synthetic, immutable.
     */
    @Persistent
    @Column(nullable = false)
    private Boolean inStock;

    private Product() {
    }

    public Product(String id, String name, String description, Double price, Integer stockAmount) {
        setId(id);
        setName(name);
        setDescription(description);
        setPrice(price);
        setStockAmount(stockAmount);
    }

    @Override
    public void jdoPreStore() {
        if (name != null)
            addWordsToKeywords(name);

        if (stockAmount != null)
            setInStock(stockAmount > 0);
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

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(Integer stockAmount) {
        this.stockAmount = stockAmount;
    }

    public Boolean getInStock() {
        return inStock;
    }

    private void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    private static Product newInstance() {
        return new Product();
    }

    public static Product findByEncodedKey(String encodedKey) {
        return newInstance().findByEncodedKey(encodedKey, Product.class);
    }

    public static Product findById(String id) {
        KeyBasedPageQuery<Product> pq = newInstance().pageQuery(1, Product.class);
        pq.onNormalizedLowerCase("id", id);
        return uniqueResult(pq.execute().getItems());
    }

    public static boolean isAvailable(String id) {
        return findById(id) == null;
    }

    public static void deleteAllByShop(String shopId) {
        Query q = newInstance().getPersistenceManager().newQuery(Product.class, "shopId == :p");
        q.deletePersistentAll(shopId);
    }

    public static PageResult<Product, Key> pageAll(int pageSize, Key bookmark) {
        return newInstance().pageQuery(pageSize, bookmark, Product.class).execute();
    }

    public static PageResult<Product, Key> pageByKeywords(List<String> keywords, int pageSize, Key bookmark) {
        KeyBasedPageQuery<Product> pq = newInstance().keywordPageQuery(keywords, pageSize, bookmark, Product.class);
        return pq.execute();
    }

    public static PageResult<Product, Key> pageAllInStock(int pageSize, Key bookmark) {
        KeyBasedPageQuery<Product> pq = newInstance().pageQuery(pageSize, bookmark, Product.class);
        pq.on("inStock", Boolean.TRUE);
        return pq.execute();
    }

    public static PageResult<Product, Double> pageAllByPrice(boolean pageAscending, int pageSize, Double priceBookmark) {
        PricePageQuery pq = new PricePageQuery(pageSize, priceBookmark, pageAscending, Product.class, newInstance().getPersistenceManager());
        return pq.execute();
    }

    public static PageResult<Product, Key> pageByShop(String shopId, int pageSize, Key bookmark) {
        KeyBasedPageQuery<Product> pq = newInstance().pageQuery(pageSize, bookmark, Product.class);
        pq.on("shopId", shopId);
        return pq.execute();
    }

    public static PageResult<Product, Key> pageByShopAndKeywords(String shopId, List<String> keywords, int pageSize, Key bookmark) {
        KeyBasedPageQuery<Product> pq = newInstance().keywordPageQuery(keywords, pageSize, bookmark, Product.class);
        pq.on("shopId", shopId);
        return pq.execute();
    }

    @SuppressWarnings("unchecked")
    public static int countByShop(String shopId) {
        Query q = newInstance().keysOnlyQuery(Product.class, "shopId == :p");
        List<Key> keys = (List<Key>) q.execute(shopId);
        return keys.size();
    }

}

class PricePageQuery extends PageQuery<Product, Double> {

    PricePageQuery(int pageSize, Double bookmark, boolean pageAscending, Class<Product> entityClass, PersistenceManager persistenceManager) {
        super(pageSize, bookmark, pageAscending, entityClass, persistenceManager);
    }

    @Override
    protected Double extractBookmark(Product modelObject) {
        return modelObject.getPrice();
    }

    @Override
    protected String getBookmarkFieldName() {
        return "price";
    }

}
