package org.dbadmin.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Created by henrynguyen on 2/20/16.
 */
public class Configuration {
  private static Properties conf = null;

  /**
   * Get a config property
   * 
   * @param property is the property needs to query against.
   * @return
   */
  public static String getProperty(String property) {
    if (conf == null)
      init();
    return conf.getProperty(property);
  }

  /**
   * Initialize the config file
   */
  public static void init() {

    conf = new Properties();
    InputStream input = null;
    try {
      conf = new Properties();
      String configPath =
          Configuration.class.getProtectionDomain().getCodeSource().getLocation().getPath()
              + "spring/data-access.properties";
      input = new FileInputStream(configPath);
      // load a properties file
      conf.load(input);

    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
