package org.chtijbug.example.swimmingpool;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * Date: 11/09/14
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */
public class Price {
    private BigDecimal amount;
    private String description;
    private PriceType priceType;

    public Price() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PriceType getPriceType() {
        return priceType;
    }

    public void setPriceType(PriceType priceType) {
        this.priceType = priceType;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Price{");
        sb.append("amount=").append(amount);
        sb.append(", description='").append(description).append('\'');
        sb.append(", priceType=").append(priceType);
        sb.append('}');
        return sb.toString();
    }
}
