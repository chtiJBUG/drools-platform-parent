package org.chtijbug.drools.platform.persistence.pojo;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * Date: 30/06/14
 * Time: 16:25
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "rule_asset_category")
public class RuleAssetCategory {
    @Id
    @SequenceGenerator(name = "rule_asset_category_id_seq", sequenceName = "rule_asset_category_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rule_asset_category_id_seq")
    private Long id;
    private String categoryName;

    public RuleAssetCategory() {
    }

    public RuleAssetCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
