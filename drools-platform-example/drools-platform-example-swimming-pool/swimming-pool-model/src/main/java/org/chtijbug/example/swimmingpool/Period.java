package org.chtijbug.example.swimmingpool;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 11/09/14
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */
public class Period extends BaseElement {
    private Date desidedStartDate;
    private SeasonType seasonType;

    public Period() {
    }

    public Date getDesidedStartDate() {
        return desidedStartDate;
    }

    public void setDesidedStartDate(Date desidedStartDate) {
        this.desidedStartDate = desidedStartDate;
    }

    public SeasonType getSeasonType() {
        return seasonType;
    }

    public void setSeasonType(SeasonType seasonType) {
        this.seasonType = seasonType;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Period{");
        sb.append("desidedStartDate=").append(desidedStartDate);
        sb.append(", seasonType=").append(seasonType);
        sb.append('}');
        return sb.toString();
    }
}
