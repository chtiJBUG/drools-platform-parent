package org.chtijbug.example.swimmingpool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 12/09/14
 * Time: 11:57
 * To change this template use File | Settings | File Templates.
 */
public class BaseElement {

    private List<CalculatedAttribute> calculatedAttributeList = new ArrayList<>();

    public BaseElement() {
    }

    public List<CalculatedAttribute> getCalculatedAttributeList() {
        return calculatedAttributeList;


    }

    public void setCalculatedAttributeList(List<CalculatedAttribute> calculatedAttributeList) {
        this.calculatedAttributeList = calculatedAttributeList;
    }

    public void addCalculatedElement(CalculatedAttribute calculatedAttribute) {
        this.calculatedAttributeList.add(calculatedAttribute);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BaseElement{");
        sb.append("calculatedAttributeList=").append(calculatedAttributeList);
        sb.append('}');
        return sb.toString();
    }
}
