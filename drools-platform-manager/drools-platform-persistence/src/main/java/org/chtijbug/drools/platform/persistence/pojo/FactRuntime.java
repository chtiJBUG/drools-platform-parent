package org.chtijbug.drools.platform.persistence.pojo;

import org.chtijbug.drools.platform.persistence.utility.StringJsonUserType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * Date: 31/03/14
 * Time: 11:49
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "fact_runtime")
@TypeDefs({@TypeDef(name = "StringJsonObject", typeClass = StringJsonUserType.class)})
public class FactRuntime {
    @Id
    @SequenceGenerator(name = "fact_id_seq", sequenceName = "fact_platform_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fact_id_seq")
    private Long id;

    @Type(type = "org.chtijbug.drools.platform.persistence.utility.StringJsonUserType")
    private String jsonFact;

    private Integer objectVersion;

    private String fullClassName;

    public FactRuntime() {
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("FactRuntime{");
        sb.append("id=").append(id);
        sb.append(", jsonFact='").append(jsonFact).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
