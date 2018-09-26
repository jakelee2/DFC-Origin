/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.dbadmin.web;

import org.dbadmin.model.Job;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * <code>Validator</code> for <code>Job</code> forms.
 * <p>
 * We're not using Bean Validation annotations here because it is easier to define such validation
 * rule in Java.
 * </p>
 */
public class JobValidator implements Validator {

  @Override
  public void validate(Object obj, Errors errors) {
    Job job = (Job) obj;
    String name = job.getName();
    // name validation
    if (!StringUtils.hasLength(name)) {
      errors.rejectValue("name", "required", "required");
    }

  }

  /**
   * This Validator validates *just* Job instances
   */
  @Override
  public boolean supports(Class<?> clazz) {
    return Job.class.isAssignableFrom(clazz);
  }


}
