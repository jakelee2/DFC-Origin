package org.dbadmin.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.dbadmin.web.BusinessruleController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Created by henrynguyen on 2/16/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/business-config.xml", "classpath:spring/tools-config.xml",
    "classpath:spring/mvc-core-config.xml"})
@WebAppConfiguration
@ActiveProfiles("jpa")
public class BusinessRuleControllerTests {

  private static final int TEST_RULE_ID = 1;

  @Autowired
  private BusinessruleController controller;

  @Autowired
  private FormattingConversionServiceFactoryBean formattingConversionServiceFactoryBean;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
        .setConversionService(formattingConversionServiceFactoryBean.getObject()).build();
  }

  @Test
  public void testInitCreationForm() throws Exception {
    mockMvc.perform(get("/businessrules/new", TEST_RULE_ID)).andExpect(status().isOk())
        .andExpect(view().name("businessrules/createOrUpdateBusinessruleForm"))
        .andExpect(model().attributeExists("businessrule"));
  }
}
