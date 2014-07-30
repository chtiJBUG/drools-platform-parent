package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.SessionExecution;
import org.springframework.data.repository.query.Param;

/**
 * Created by IntelliJ IDEA.
 * Date: 30/07/14
 * Time: 13:54
 * To change this template use File | Settings | File Templates.
 */
public interface SessionExecutionCustomRepository {
    SessionExecution findByRuleBaseIDAndSessionIdAndEndDateIsNull(@Param("ruleBaseID") Integer ruleBaseID, @Param("sessionId") Integer sessionId);

}
