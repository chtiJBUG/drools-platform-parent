package org.chtijbug.drools.platform.persistence.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 14/02/14
 * Time: 09:15
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "session_runtime")
public class SessionRuntime {
    @Id
    @SequenceGenerator(name = "session_id_seq", sequenceName = "session_platform_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "session_id_seq")
    private Long id;
    @ManyToOne
    private PlatformRuntime platformRuntime;

    private Integer sessionId;

    @Column(nullable = false)
    private Date startDate;

    private Date endDate;

    private Integer eventID;

    @Enumerated(EnumType.STRING)
    private SessionRuntimeStatus sessionRuntimeStatus;

    public SessionRuntime() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlatformRuntime getPlatformRuntime() {
        return platformRuntime;
    }

    public void setPlatformRuntime(PlatformRuntime platformRuntime) {
        this.platformRuntime = platformRuntime;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getEventID() {
        return eventID;
    }

    public void setEventID(Integer eventID) {
        this.eventID = eventID;
    }

    public SessionRuntimeStatus getSessionRuntimeStatus() {
        return sessionRuntimeStatus;
    }

    public void setSessionRuntimeStatus(SessionRuntimeStatus sessionRuntimeStatus) {
        this.sessionRuntimeStatus = sessionRuntimeStatus;
    }
}
