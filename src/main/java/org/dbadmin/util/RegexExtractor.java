package org.dbadmin.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extract ip-address and db name from url string.
 * 
 * @author denys
 *
 */
public class RegexExtractor {

  /**
   * if ip-address validation required use this pattern:
   * "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)"
   */
  private final static String IP_PATTERN = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}):(\\d{1,5})";

  private final static String DB_PATTERN_SQL_SERVER = "(databaseName=)(\\w+)";

  private final static String DB_PATTERN_ORACLE =
      "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}):(\\d{1,5}):(\\w+)";

  private final static String DB_PATTERN_POSTGRESQL =
      "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}):(\\d{1,5})/(\\w+)";

  public static String extractIp(String url) {
    if (url != null) {
      Pattern pattern = Pattern.compile(IP_PATTERN);
      Matcher matcher = pattern.matcher(url);
      if (matcher.find()) {
        return matcher.group();
      }
    }
    return "incorrect ip";
  }

  public static String extractDbName(String url, String databaseJpaVendorName) {
    if (url != null) {
      Pattern pattern = null;
      Matcher matcher = null;
      switch (databaseJpaVendorName) {

        case "SQL_SERVER":
          pattern = Pattern.compile(DB_PATTERN_SQL_SERVER);
          matcher = pattern.matcher(url);
          if (matcher.find()) {
            return matcher.group(2);
          }
          break;
        case "ORACLE":
          pattern = Pattern.compile(DB_PATTERN_ORACLE);
          matcher = pattern.matcher(url);
          if (matcher.find()) {
            return matcher.group(3);
          }
          break;
        case "POSTGRESQL":
          pattern = Pattern.compile(DB_PATTERN_POSTGRESQL);
          matcher = pattern.matcher(url);
          if (matcher.find()) {
            return matcher.group(3);
          }
          break;
      }
    }
    return "";
  }
}
