package org.chtijbug.drools.platform.persistence.pojo;

/**
 * Created by IntelliJ IDEA.
 * Date: 24/03/14
 * Time: 16:29
 * To change this template use File | Settings | File Templates.
 */

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "RealtimeParameters_runtime")
public class RealTimeParameters {

    @Id
    @SequenceGenerator(name = "realtimeparams_id_seq", sequenceName = "realtimeparams_platform_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "realtimeparams_id_seq")
    private Long id;

   @ManyToOne
    private PlatformRuntime platformRuntime;

    private Date eventDate;

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

    public RealTimeParameters() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
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

    public PlatformRuntime getPlatformRuntime() {
        return platformRuntime;
    }

    public void setPlatformRuntime(PlatformRuntime platformRuntime) {
        this.platformRuntime = platformRuntime;
    }
}
