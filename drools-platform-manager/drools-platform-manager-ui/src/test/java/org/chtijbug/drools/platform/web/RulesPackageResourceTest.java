package org.chtijbug.drools.platform.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration()
@ContextConfiguration(locations = {"classpath:test-beans.xml", "classpath:/spring/spring-test-persistence-config.xml"})
public class RulesPackageResourceTest {
    protected MockMvc mockMvc;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void should_get_snapshots() throws Exception {
        this.mockMvc.perform(get("/rules_package/snapshots")
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("stringList[0]").value("1-SNAPSHOT"))
                .andExpect(jsonPath("stringList[1]").value("2-SNAPSHOT"));
    }

    @Test
    public void should_get_envs() throws Exception {
        this.mockMvc.perform(get("/rules_package/envs")
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("environmentList[0].name").value("env1"))
                .andExpect(jsonPath("environmentList[0].url").value("http://url1"))
                .andExpect(jsonPath("environmentList[1].name").value("env2"))
                .andExpect(jsonPath("environmentList[1].url").value("http://url2"))
                .andExpect(jsonPath("environmentList[2].name").value("env3"))
                .andExpect(jsonPath("environmentList[2].url").value("http://url3"));
    }

}

