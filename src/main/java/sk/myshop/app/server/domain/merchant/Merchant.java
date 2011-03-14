package sk.myshop.app.server.domain.merchant;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.persistence.Column;

import sk.myshop.app.server.domain.KeyBasedPageQuery;
import sk.myshop.app.server.domain.ModelObject;

import com.google.appengine.api.users.User;

/**
 * 'Obchodnik'.
 */
@PersistenceCapable
public class Merchant extends ModelObject<Merchant> {

    /**
     * 'Google konto spojene s obchodnikom'.
     * <p>
     * Unique, immutable.
     */
    @Persistent
    @Column(nullable = false)
    private User user;

    /**
     * 'Meno a priezvisko (fyzicka osoba podnikatel)'.
     * <p>
     * Unindexed.
     */
    @Persistent
    @Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
    private String personName;

    /**
     * 'Obchodne meno spolocnosti (pravnicka osoba)'.
     * <p>
     * Unindexed.
     */
    @Persistent
    @Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
    private String companyName;

    private Merchant() {
    }

    public Merchant(User user, String name, boolean company) {
        setUser(user);

        if (company)
            setCompanyName(name);
        else
            setPersonName(name);
    }

    public User getUser() {
        return user;
    }

    private void setUser(User user) {
        this.user = user;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    private static Merchant newInstance() {
        return new Merchant();
    }

    public static Merchant findByEncodedKey(String encodedKey) {
        return newInstance().findByEncodedKey(encodedKey, Merchant.class);
    }

    public static Merchant findByUser(User user) {
        KeyBasedPageQuery<Merchant> pq = newInstance().pageQuery(1, Merchant.class);
        pq.on("user", user);
        return uniqueResult(pq.execute().getItems());
    }

    public static boolean hasAccount(User user) {
        return findByUser(user) != null;
    }

}
