package org.chtijbug.drools.platform.persistence.pojo;

import org.chtijbug.drools.platform.persistence.utility.StringJsonUserType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 31/03/14
 * Time: 11:49
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "fact", indexes = {@Index(columnList = "fullClassName")})
@TypeDefs({@TypeDef(name = "json", typeClass = StringJsonUserType.class)})
public class Fact {
    @Id
    @SequenceGenerator(name = "fact_id_seq", sequenceName = "fact_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fact_id_seq")
    private Long id;

    @Type(type = "json")
    private String jsonFact;

    private Integer objectVersion;

    private String fullClassName;

    private Date modificationDate;

    @Enumerated(EnumType.STRING)
    private FactType factType;

    private Integer eventid;

    public Fact() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJsonFact() {
        return jsonFact;
    }

    public void setJsonFact(String jsonFact) {
        this.jsonFact = jsonFact;
    }

    public Integer getObjectVersion() {
        return objectVersion;
    }

    public void setObjectVersion(Integer objectVersion) {
        this.objectVersion = objectVersion;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public void setFullClassName(String fullClassName) {
        this.fullClassName = fullClassName;
    }

    public FactType getFactType() {
        return factType;
    }

    public void setFactType(FactType factType) {
        this.factType = factType;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Integer getEventid() {
        return eventid;
    }

    public void setEventid(Integer eventid) {
        this.eventid = eventid;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("FactRuntime{");
        sb.append("id=").append(id);
        sb.append(", jsonFact='").append(jsonFact).append('\'');
        sb.append(", objectVersion=").append(objectVersion);
        sb.append(", fullClassName='").append(fullClassName).append('\'');
        sb.append(", modificationDate=").append(modificationDate);
        sb.append(", factRuntimeType=").append(factType);
        sb.append('}');
        return sb.toString();
    }
}
