package org.chtijbug.example.swimmingpool;

/**
 * Created by IntelliJ IDEA.
 * Date: 11/09/14
 * Time: 13:28
 * To change this template use File | Settings | File Templates.
 */
public class CalculationStep {
    private String stepName;
    private String stepDetail;

    public CalculationStep() {
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getStepDetail() {
        return stepDetail;
    }

    public void setStepDetail(String stepDetail) {
        this.stepDetail = stepDetail;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CalculationStep{");
        sb.append("stepName='").append(stepName).append('\'');
        sb.append(", stepDetail='").append(stepDetail).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
