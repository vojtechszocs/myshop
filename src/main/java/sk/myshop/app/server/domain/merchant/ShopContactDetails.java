package sk.myshop.app.server.domain.merchant;

import static sk.myshop.app.server.util.StringUtils.capitalizeWords;
import static sk.myshop.app.server.util.StringUtils.join;
import static sk.myshop.app.server.util.StringUtils.lowerCase;
import static sk.myshop.app.server.util.StringUtils.normalizedLowerCase;

import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.persistence.Column;

/**
 * 'Kontaktne udaje obchodu.'
 */
@PersistenceCapable(detachable = "true")
@EmbeddedOnly
public class ShopContactDetails {

    /**
     * 'E-mail'.
     * <p>
     * Normalized, lower case. Unindexed.
     */
    @Persistent
    @Column(nullable = false)
    @Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
    private String email;

    /**
     * 'Telefon'.
     * <p>
     * Unindexed.
     */
    @Persistent
    @Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
    private String phone;

    /**
     * 'Ulica'.
     * <p>
     * Unindexed.
     */
    @Persistent
    @Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
    private String street;

    /**
     * 'Mesto'.
     * <p>
     * Lower case.
     */
    @Persistent
    @Column(nullable = false)
    private String city;

    /**
     * 'PSC'.
     * <p>
     * Unindexed.
     */
    @Persistent
    @Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
    private String zip;

    public ShopContactDetails(String email, String city) {
        setEmail(email);
        setCity(city);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = normalizedLowerCase(email);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return join(capitalizeWords(city));
    }

    public void setCity(String city) {
        this.city = lowerCase(city);
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

}
