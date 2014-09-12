package org.chtijbug.example.swimmingpool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 11/09/14
 * Time: 10:01
 * To change this template use File | Settings | File Templates.
 */
public class Quote extends BaseElement {
    private Date quoteDate;
    private Date validUntil;
    private Period period;
    private Subscription subscription;
    private List<Person> personList = new ArrayList<>();
    private Address address;
    private List<Price> priceList = new ArrayList<>();
    private List<CalculationStep> calculationStepList = new ArrayList<>();

    public Quote() {
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Date getQuoteDate() {
        return quoteDate;
    }

    public void setQuoteDate(Date quoteDate) {
        this.quoteDate = quoteDate;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Price> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<Price> priceList) {
        this.priceList = priceList;
    }

    public List<CalculationStep> getCalculationStepList() {
        return calculationStepList;
    }

    public void setCalculationStepList(List<CalculationStep> calculationStepList) {
        this.calculationStepList = calculationStepList;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Quote{");
        sb.append("quoteDate=").append(quoteDate);
        sb.append(", validUntil=").append(validUntil);
        sb.append(", period=").append(period);
        sb.append(", subscription=").append(subscription);
        sb.append(", personList=").append(personList);
        sb.append(", address=").append(address);
        sb.append(", priceList=").append(priceList);
        sb.append(", calculationStepList=").append(calculationStepList);
        sb.append('}');
        return sb.toString();
    }
}
