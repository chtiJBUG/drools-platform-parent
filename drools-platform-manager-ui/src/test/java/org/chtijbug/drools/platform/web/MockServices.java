package org.chtijbug.drools.platform.web;

import org.chtijbug.drools.guvnor.rest.model.Snapshot;
import org.chtijbug.drools.platform.rules.config.Environment;
import org.chtijbug.drools.platform.rules.config.RuntimeSiteTopology;
import org.chtijbug.drools.platform.rules.management.RuleManager;
import org.chtijbug.drools.platform.rules.management.RuntimeManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class MockServices {

    @Bean
    public RuleManager ruleManager() throws Exception {
        RuleManager mock = mock(RuleManager.class);
        when(mock.getAvailableSnapshots()).thenReturn(newArrayList(
                new Snapshot("1", "1-SNAPSHOT"),
                new Snapshot("2", "2-SNAPSHOT")));
        return mock;
    }

    @Bean
    public RuntimeManager runtimeManager() {
        RuntimeManager mock = mock(RuntimeManager.class);
        return mock;
    }

    @Bean
    public RuntimeSiteTopology aladsWebClient() throws MalformedURLException {
        RuntimeSiteTopology mock = mock(RuntimeSiteTopology.class);
        Collection<Environment> envs = asList(
                new Environment("env1",new URL("http://url1")),
                new Environment("env2",new URL("http://url2")),
                new Environment("env3",new URL("http://url3")));
        when(mock.getEnvironments()).thenReturn(envs);
        return mock;
    }

}
