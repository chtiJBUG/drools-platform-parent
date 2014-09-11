package org.chtijbug.example.swimmingpool;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 16/07/14
 * Time: 11:23
 * To change this template use File | Settings | File Templates.
 */
public class Subscription {

    private BigDecimal price;
    private Date valideFrom;
    private Date validUntil;
    private String type;

    public Subscription() {
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getValideFrom() {
        return valideFrom;
    }

    public void setValideFrom(Date valideFrom) {
        this.valideFrom = valideFrom;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Subscription{");
        sb.append("price=").append(price);
        sb.append(", valideFrom=").append(valideFrom);
        sb.append(", validUntil=").append(validUntil);
        sb.append(", type='").append(type).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
