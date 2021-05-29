package com.github.yashmayya.kafka.connect.hackernews;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.github.yashmayya.kafka.connect.hackernews.HackerNewsSourceConnectorConfig.API_VERSION;
import static com.github.yashmayya.kafka.connect.hackernews.HackerNewsSourceConnectorConfig.BASE_API_PATH;

public class HackerNewsSourceTask extends SourceTask {

  public static final String ITEM_ID = "item.id";
  public static final String TASK_ID = "task.id";
  private static final Logger log = LoggerFactory.getLogger(HackerNewsSourceTask.class);
  private ObjectMapper objectMapper;
  private Long currentItemId;
  private Long count;
  private Long maxItemId;
  private HackerNewsSourceConnectorConfig config;
  private Map<String, Object> sourcePartition;

  @Override
  public String version() {
    return VersionUtil.get();
  }

  @Override
  public void start(Map<String, String> props) {
    config = new HackerNewsSourceConnectorConfig(props);
    objectMapper = new ObjectMapper();
    currentItemId = config.getInitialStartItem();
    count = 0L;
    int taskId = Integer.parseInt(props.get(TASK_ID));
    sourcePartition = Collections.singletonMap(TASK_ID, taskId);
    maxItemId = HackerNewsUtil.getMaxItemId();

    if (currentItemId > maxItemId) {
      throw new ConfigException(HackerNewsSourceConnectorConfig.INITITIAL_START_ITEM_CONFIG
          + " is greater than the current max item id on Hacker News (" + maxItemId + ")");
    }

    Map<String, Object> offset = context.offsetStorageReader().offset(sourcePartition);
    if (offset != null) {
      currentItemId = ((Long) offset.get(ITEM_ID)) + 1;
    }
  }

  @Override
  public List<SourceRecord> poll() throws InterruptedException {
    if (config.getMaxItems() > 0 && count >= config.getMaxItems()) {
      throw new ConnectException("Completed reading the configured number of Hacker News items");
    }

    List<SourceRecord> records = new ArrayList<>();
    Thread.sleep(config.getPollInterval());

    if (currentItemId > maxItemId) {
      maxItemId = HackerNewsUtil.getMaxItemId();
      log.info("Got max item id = {}", maxItemId);
      return records;
    }

    HackerNewsItem hnItem;
    try {
      hnItem = objectMapper.readValue(new URL(
          BASE_API_PATH + API_VERSION + String.format("/item/%s.json", currentItemId)),
          HackerNewsItem.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    if (hnItem == null) {
      return records;
    }

    Map<String, Object> sourceOffset = new HashMap<>();
    sourceOffset.put(ITEM_ID, currentItemId);

    SourceRecord record = new SourceRecord(
        sourcePartition,
        sourceOffset,
        config.getKafkaTopic(),
        HackerNewsItem.VALUE_SCHEMA,
        hnItem.toStruct()
    );
    records.add(record);
    count++;
    currentItemId++;
    return records;
  }

  @Override
  public void stop() {

  }
}
