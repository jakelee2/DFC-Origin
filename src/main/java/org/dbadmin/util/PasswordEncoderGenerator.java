package org.dbadmin.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderGenerator {

  public static String encode(String str) {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    return passwordEncoder.encode(str);
  }

  public static void main(String[] args) {

    int i = 0;
    while (i < 10) {
      String password = "password";
      System.out.println(encode(password));
      i++;
    }

  }
}
