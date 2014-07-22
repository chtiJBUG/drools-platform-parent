package org.chtijbug.example.swimmingpool;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 16/07/14
 * Time: 11:23
 * To change this template use File | Settings | File Templates.
 */
public class abo {

    private SeasonType season;
    private Date birthDate;
    private String cityName;
    private BigDecimal price;
    private BigDecimal nettoPrice;

    public abo() {
    }

    public SeasonType getSeason() {
        return season;
    }

    public void setSeason(SeasonType season) {
        this.season = season;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getNettoPrice() {
        return nettoPrice;
    }

    public void setNettoPrice(BigDecimal nettoPrice) {
        this.nettoPrice = nettoPrice;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("abo{");
        sb.append("season=").append(season);
        sb.append(", birthDate=").append(birthDate);
        sb.append(", cityName='").append(cityName).append('\'');
        sb.append(", price=").append(price);
        sb.append(", nettoPrice=").append(nettoPrice);
        sb.append('}');
        return sb.toString();
    }
}
