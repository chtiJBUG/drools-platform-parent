package org.chtijbug.drools.platform.persistence.pojo;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * Date: 28/03/14
 * Time: 11:25
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "rule_runtime")
public class RuleRuntime {

    @Id
    @SequenceGenerator(name = "rule_id_seq", sequenceName = "rule_platform_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rule_id_seq")
    private Long id;


}
