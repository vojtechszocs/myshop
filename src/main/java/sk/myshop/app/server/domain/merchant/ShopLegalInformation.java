package sk.myshop.app.server.domain.merchant;

import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.persistence.Column;

import com.google.appengine.api.datastore.Text;

/**
 * 'Pravne informacie pre zakaznikov'.
 */
@PersistenceCapable(detachable = "true")
@EmbeddedOnly
public class ShopLegalInformation {

    /**
     * 'Obchodne podmienky'.
     * <p>
     * Contains wiki-like markup.
     */
    @Persistent
    @Column(nullable = false)
    private Text saleConditions;

    /**
     * 'Dodacie podmienky'.
     * <p>
     * Contains wiki-like markup.
     */
    @Persistent
    @Column(nullable = false)
    private Text deliveryConditions;

    /**
     * 'Reklamacne podmienky'.
     * <p>
     * Contains wiki-like markup.
     */
    @Persistent
    @Column(nullable = false)
    private Text returnConditions;

    /**
     * 'Casto kladene otazky'.
     * <p>
     * Contains wiki-like markup.
     */
    @Persistent
    private Text faq;

    public ShopLegalInformation(String saleConditions, String deliveryConditions, String returnConditions) {
        setSaleConditions(saleConditions);
        setDeliveryConditions(deliveryConditions);
        setReturnConditions(returnConditions);
    }

    public String getSaleConditions() {
        return saleConditions.getValue();
    }

    public void setSaleConditions(String saleConditions) {
        this.saleConditions = new Text(saleConditions);
    }

    public String getDeliveryConditions() {
        return deliveryConditions.getValue();
    }

    public void setDeliveryConditions(String deliveryConditions) {
        this.deliveryConditions = new Text(deliveryConditions);
    }

    public String getReturnConditions() {
        return returnConditions.getValue();
    }

    public void setReturnConditions(String returnConditions) {
        this.returnConditions = new Text(returnConditions);
    }

    public String getFaq() {
        return faq.getValue();
    }

    public void setFaq(String faq) {
        this.faq = new Text(faq);
    }

}
