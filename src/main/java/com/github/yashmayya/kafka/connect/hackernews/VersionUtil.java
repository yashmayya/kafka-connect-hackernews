package com.github.yashmayya.kafka.connect.hackernews;

import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VersionUtil {
  private static final Logger log = LoggerFactory.getLogger(VersionUtil.class);

  private static final String VERSION_FILE = "kafka-connect-hackernews-version.properties";
  private static final String VERSION = findVersion();

  public static final String UNKNOWN_VERSION = "unknown";

  private static String findVersion() {
    try (InputStream stream = VersionUtil.class.getClassLoader().getResourceAsStream(VERSION_FILE)) {
      Properties props = new Properties();
      props.load(stream);
      return props.getProperty("version", UNKNOWN_VERSION).trim();
    } catch (Exception e) {
      log.warn("Error while loading version:", e);
      return UNKNOWN_VERSION;
    }
  }

  public static String get() {
    return VERSION;
  }
}
