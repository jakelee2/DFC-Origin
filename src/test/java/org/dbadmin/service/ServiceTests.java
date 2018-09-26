package org.dbadmin.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.dbadmin.model.Businessrule;
import org.dbadmin.service.TemplateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(locations = {"classpath:spring/business-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("jpa")
public class ServiceTests {
  @Autowired
  protected TemplateService templateService;

  @Test
  public void shouldFindBusinessruleByName() {
    Collection<Businessrule> rules =
        this.templateService.findBusinessruleByName("First businessrule");
    assertThat(rules.size()).isEqualTo(1);

    rules = this.templateService.findBusinessruleByName("No businessrule");
    assertThat(rules.isEmpty());
  }

  @Test
  @Transactional
  public void shouldInsertBusinessrule() {
    Collection<Businessrule> rules =
        this.templateService.findBusinessruleByName("Test businessrule");

    Businessrule rule = new Businessrule();
    rule.setName("rule1");
    rule.setDescription("this is the first rule");
    rule.setRestconnection("this is the rest connection");

    this.templateService.saveBusinessrule(rule);
    assertThat(rule.getId().longValue()).isNotEqualTo(0);

  }
}
