package sk.myshop.app.server.domain.merchant;

import static sk.myshop.app.server.util.StringUtils.CB_NORMALIZED_LOWER_CASE;
import static sk.myshop.app.server.util.StringUtils.transformWords;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.Query;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.listener.StoreCallback;
import javax.persistence.Column;

import sk.myshop.app.server.cache.Cached;
import sk.myshop.app.server.cache.FlushCacheEntry;
import sk.myshop.app.server.domain.ModelObjectWithKeywords;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;

/**
 * 'Obrazok z fotogalerie'.
 */
@PersistenceCapable
public class Picture extends ModelObjectWithKeywords<Picture> implements StoreCallback {

    /**
     * 'Nazov obrazku'.
     * <p>
     * Unindexed.
     */
    @Persistent
    @Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
    private String name;

    /**
     * Image data.
     * <p>
     * Immutable.
     */
    @Persistent
    @Column(nullable = false)
    private Blob data;

    /**
     * 'Obchodnik'.
     * <p>
     * Immutable.
     */
    @Persistent
    @Column(nullable = false)
    private String merchantId;

    /**
     * 'Obchody spojene s obrazkom'.
     */
    @Persistent
    private List<String> relatedShopIds = new ArrayList<String>();

    /**
     * 'Produkty spojene s obrazkom'.
     */
    @Persistent
    private List<String> relatedProductIds = new ArrayList<String>();

    private Picture() {
    }

    public Picture(byte[] data, String merchantId) {
        setData(data);
        setMerchantId(merchantId);
    }

    @Override
    public void jdoPreStore() {
        if (name != null)
            addWordsToKeywords(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data.getBytes();
    }

    private void setData(byte[] data) {
        this.data = new Blob(data);
    }

    public String getMerchantId() {
        return merchantId;
    }

    private void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public void addRelatedShop(String shopId) {
        relatedShopIds.add(shopId);
    }

    public void removeRelatedShop(String shopId) {
        relatedShopIds.remove(shopId);
    }

    public void addRelatedProduct(String productId) {
        relatedProductIds.add(productId);
    }

    public void removeRelatedProduct(String productId) {
        relatedProductIds.remove(productId);
    }

    private static Picture newInstance() {
        return new Picture();
    }

    @Cached("Picture")
    public static PictureCached findByKey(Key key) {
        Picture p = newInstance().findByKey(key, Picture.class);
        return (p != null) ? new PictureCached(key, p.getKeywords(), p.getName(), p.getData()) : null;
    }

    @Override
    public void delete() {
        throw new RuntimeException("Delete pictures using deleteByKey() method");
    }

    @FlushCacheEntry("Picture")
    public static void deleteByKey(Key key) {
        Query q = newInstance().getPersistenceManager().newQuery(Picture.class, "key == :p");
        q.deletePersistentAll(key);
    }

    @SuppressWarnings("unchecked")
    public static void deleteAllByMerchant(String merchantId) {
        Query q = newInstance().keysOnlyQuery(Picture.class, "merchantId == :p");
        List<Key> keys = (List<Key>) q.execute(merchantId);

        for (Key key : keys)
            deleteByKey(key);
    }

    @SuppressWarnings("unchecked")
    public static List<Key> findKeysByMerchantAndKeywords(String merchantId, List<String> keywords) {
        Query q = newInstance().keysOnlyQuery(Picture.class, "merchantId == :p1 && :p2.contains(keywords)");
        return (List<Key>) q.execute(merchantId, transformWords(keywords, CB_NORMALIZED_LOWER_CASE));
    }

    @SuppressWarnings("unchecked")
    public static List<Key> findKeysByMerchantAndShop(String merchantId, String shopId) {
        Query q = newInstance().keysOnlyQuery(Picture.class, "merchantId == :p1 && relatedShopIds == :p2");
        return (List<Key>) q.execute(merchantId, shopId);
    }

    public static boolean existsForMerchantAndShop(String merchantId, String shopId) {
        return !findKeysByMerchantAndShop(merchantId, shopId).isEmpty();
    }

    @SuppressWarnings("unchecked")
    public static List<Key> findKeysByMerchantAndProduct(String merchantId, String productId) {
        Query q = newInstance().keysOnlyQuery(Picture.class, "merchantId == :p1 && relatedProductIds == :p2");
        return (List<Key>) q.execute(merchantId, productId);
    }

    public static boolean existsForMerchantAndProduct(String merchantId, String productId) {
        return !findKeysByMerchantAndProduct(merchantId, productId).isEmpty();
    }

}
