package org.chtijbug.drools.platform.persistence.pojo;

/**
 * Created by alexandre on 17/07/2014.
 */
public class Page {
    private Integer currentIndex;
    private Long totalCount;
    /** Setting a default value tha we can override */
    private Integer maxItemPerPage = 5;

    public Page() {/** nop */}

    public Page(Integer currentIndex, Long totalCount, Integer maxItemPerPage) {
        this.currentIndex = currentIndex;
        this.totalCount = totalCount;
        this.maxItemPerPage = maxItemPerPage;
    }

    public Integer getCurrentIndex() {
        return currentIndex;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public Integer getMaxItemPerPage() {
        return maxItemPerPage;
    }

    public void setMaxItemPerPage(Integer maxItemPerPage) {
        this.maxItemPerPage = maxItemPerPage;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public void setCurrentIndex(Integer currentIndex) {
        this.currentIndex = currentIndex;
    }
}
