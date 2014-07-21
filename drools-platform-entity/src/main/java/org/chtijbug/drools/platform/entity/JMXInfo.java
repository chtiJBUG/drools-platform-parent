package org.chtijbug.drools.platform.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by IntelliJ IDEA.
 * Date: 24/03/14
 * Time: 10:14
 * To change this template use File | Settings | File Templates.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JMXInfo {

    private long averageTimeExecution;
    private long minTimeExecution = 1000000;
    private long maxTimeExecution = 0;
    private long totalTimeExecution;
    private long totalNumberRulesExecuted;
    private long averageRulesExecuted;
    private long minRulesExecuted = 10000000;
    private long maxRulesExecuted = 0;
    private long numberFireAllRulesExecuted;
    private double averageRuleThroughout;
    private double minRuleThroughout = 1000000;
    private double maxRuleThroughout = 0;

    public JMXInfo() {
    }

    public long getAverageTimeExecution() {
        return averageTimeExecution;
    }

    public void setAverageTimeExecution(long averageTimeExecution) {
        this.averageTimeExecution = averageTimeExecution;
    }

    public long getMinTimeExecution() {
        return minTimeExecution;
    }

    public void setMinTimeExecution(long minTimeExecution) {
        this.minTimeExecution = minTimeExecution;
    }

    public long getMaxTimeExecution() {
        return maxTimeExecution;
    }

    public void setMaxTimeExecution(long maxTimeExecution) {
        this.maxTimeExecution = maxTimeExecution;
    }

    public long getTotalTimeExecution() {
        return totalTimeExecution;
    }

    public void setTotalTimeExecution(long totalTimeExecution) {
        this.totalTimeExecution = totalTimeExecution;
    }

    public long getTotalNumberRulesExecuted() {
        return totalNumberRulesExecuted;
    }

    public void setTotalNumberRulesExecuted(long totalNumberRulesExecuted) {
        this.totalNumberRulesExecuted = totalNumberRulesExecuted;
    }

    public long getAverageRulesExecuted() {
        return averageRulesExecuted;
    }

    public void setAverageRulesExecuted(long averageRulesExecuted) {
        this.averageRulesExecuted = averageRulesExecuted;
    }

    public long getMinRulesExecuted() {
        return minRulesExecuted;
    }

    public void setMinRulesExecuted(long minRulesExecuted) {
        this.minRulesExecuted = minRulesExecuted;
    }

    public long getMaxRulesExecuted() {
        return maxRulesExecuted;
    }

    public void setMaxRulesExecuted(long maxRulesExecuted) {
        this.maxRulesExecuted = maxRulesExecuted;
    }

    public long getNumberFireAllRulesExecuted() {
        return numberFireAllRulesExecuted;
    }

    public void setNumberFireAllRulesExecuted(long numberFireAllRulesExecuted) {
        this.numberFireAllRulesExecuted = numberFireAllRulesExecuted;
    }

    public double getAverageRuleThroughout() {
        return averageRuleThroughout;
    }

    public void setAverageRuleThroughout(double averageRuleThroughout) {
        this.averageRuleThroughout = averageRuleThroughout;
    }

    public double getMinRuleThroughout() {
        return minRuleThroughout;
    }

    public void setMinRuleThroughout(double minRuleThroughout) {
        this.minRuleThroughout = minRuleThroughout;
    }

    public double getMaxRuleThroughout() {
        return maxRuleThroughout;
    }

    public void setMaxRuleThroughout(double maxRuleThroughout) {
        this.maxRuleThroughout = maxRuleThroughout;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("JMXInfo{");
        sb.append("averageTimeExecution=").append(averageTimeExecution);
        sb.append(", minTimeExecution=").append(minTimeExecution);
        sb.append(", maxTimeExecution=").append(maxTimeExecution);
        sb.append(", totalTimeExecution=").append(totalTimeExecution);
        sb.append(", totalNumberRulesExecuted=").append(totalNumberRulesExecuted);
        sb.append(", averageRulesExecuted=").append(averageRulesExecuted);
        sb.append(", minRulesExecuted=").append(minRulesExecuted);
        sb.append(", maxRulesExecuted=").append(maxRulesExecuted);
        sb.append(", numberFireAllRulesExecuted=").append(numberFireAllRulesExecuted);
        sb.append(", averageRuleThroughout=").append(averageRuleThroughout);
        sb.append(", minRuleThroughout=").append(minRuleThroughout);
        sb.append(", maxRuleThroughout=").append(maxRuleThroughout);
        sb.append('}');
        return sb.toString();
    }
}
