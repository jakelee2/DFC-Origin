package org.dbadmin.handler;

import org.springframework.security.access.ConfigAttribute;

/**
 * Created by henrynguyen on 5/11/16.
 * This class implements the Meta Data Source with priority.
 */
public class ConfigAttributeImpl implements ConfigAttribute {

  String attribute = "";
  int priority = 1;

  public ConfigAttributeImpl(String attr, int p) {
    this.attribute = attr;
    this.priority = p;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  public void setAttribute(String attr) {
    attribute = attr;
  }

  public String getAttribute() {
    return attribute;
  }

}
