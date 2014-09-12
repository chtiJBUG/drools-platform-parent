package org.chtijbug.example.swimmingpool;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * Date: 12/09/14
 * Time: 11:52
 * To change this template use File | Settings | File Templates.
 */
public class CalculatedAttribute {
    private String key;
    private String className;
    private boolean booleanValue;
    private String stringValue;
    private BigDecimal bigDecimalValue;
    private Integer integerValue;

    public CalculatedAttribute() {
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
        new Boolean(booleanValue).toString();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public BigDecimal getBigDecimalValue() {
        return bigDecimalValue;
    }

    public void setBigDecimalValue(BigDecimal bigDecimalValue) {
        this.bigDecimalValue = bigDecimalValue;
    }

    public Integer getIntegerValue() {
        return integerValue;
    }

    public void setIntegerValue(Integer integerValue) {
        this.integerValue = integerValue;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CalculatedAttribute{");
        sb.append("key='").append(key).append('\'');
        sb.append(", className='").append(className).append('\'');
        sb.append(", booleanValue=").append(booleanValue);
        sb.append(", stringValue='").append(stringValue).append('\'');
        sb.append(", bigDecimalValue=").append(bigDecimalValue);
        sb.append(", integerValue=").append(integerValue);
        sb.append('}');
        return sb.toString();
    }
}
