package org.dbadmin.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.dbadmin.model.Businessrule;
import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class ValidatorTests {

  private Validator createValidator() {
    LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
    localValidatorFactoryBean.afterPropertiesSet();
    return localValidatorFactoryBean;
  }

  @Test
  public void shouldNotValidateWhenJobsIsEmpty() {

    LocaleContextHolder.setLocale(Locale.ENGLISH);
    Businessrule businessRule = new Businessrule();
    businessRule.setName("rule1");
    businessRule.setDescription("this is the first rule");
    businessRule.setId(123);

    Validator validator = createValidator();
    Set<ConstraintViolation<Businessrule>> constraintViolations = validator.validate(businessRule);

    assertThat(constraintViolations.size()).isEqualTo(1);
    ConstraintViolation<Businessrule> violation = constraintViolations.iterator().next();
    assertThat(violation.getPropertyPath().toString()).isEqualTo("restconnection");
    assertThat(violation.getMessage()).isEqualTo("may not be empty");
  }

}
