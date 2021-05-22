package com.github.yashmayya.kafka.connect.hackernews;

import java.util.Map;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

public class HackerNewsSourceConnectorConfig extends AbstractConfig {

  public static final String KAFKA_TOPIC_CONF = "kafka.topic";
  private static final String KAFKA_TOPIC_DOC = "Topic to write to";

  public static final String POLL_INTERVAL_CONFIG = "poll.interval.ms";
  public static final Long POLL_INTERVAL_DEFAULT = 100L;
  public static final String POLL_INTERVAL_DOC = "Interval between polls (ms)";

  public static final String INITITIAL_START_ITEM_CONFIG = "initial.start.item";
  public static final Long INITIAL_START_ITEM_DEFAULT = 1L;
  public static final String INITIAL_START_ITEM_DOC = "Hacker News item id to start reading from";

  public static final String MAX_ITEMS_CONFIG = "max.items";
  public static final Long MAX_ITEMS_DEFAULT = -1L;
  public static final String MAX_ITEMS_DOC = "Maximum number of items to read from Hacker News or "
      + "less than 1 for unlimited";

  public static final String BASE_API_PATH = "https://hacker-news.firebaseio.com/";
  public static final String API_VERSION = "v0";
  public static final String MAX_ITEM_PATH = "/maxitem.json";

  public static final ConfigDef CONFIG_DEF = new ConfigDef()
      .define(
          KAFKA_TOPIC_CONF,
          Type.STRING,
          Importance.HIGH,
          KAFKA_TOPIC_DOC
      )
      .define(
          POLL_INTERVAL_CONFIG,
          Type.LONG,
          POLL_INTERVAL_DEFAULT,
          ConfigDef.Range.atLeast(10),
          Importance.HIGH,
          POLL_INTERVAL_DOC
      )
      .define(
          INITITIAL_START_ITEM_CONFIG,
          Type.LONG,
          INITIAL_START_ITEM_DEFAULT,
          ConfigDef.Range.atLeast(1),
          Importance.MEDIUM,
          INITIAL_START_ITEM_DOC
      )
      .define(
          MAX_ITEMS_CONFIG,
          Type.LONG,
          MAX_ITEMS_DEFAULT,
          Importance.MEDIUM,
          MAX_ITEMS_DOC
      );

  public HackerNewsSourceConnectorConfig(Map<?, ?> originals) {
    super(CONFIG_DEF, originals);
  }

  public String getKafkaTopic() {
    return this.getString(KAFKA_TOPIC_CONF);
  }

  public Long getPollInterval() {
    return this.getLong(POLL_INTERVAL_CONFIG);
  }

  public Long getInitialStartItem() {
    return this.getLong(INITITIAL_START_ITEM_CONFIG);
  }

  public Long getMaxItems() {
    return this.getLong(MAX_ITEMS_CONFIG);
  }
}
