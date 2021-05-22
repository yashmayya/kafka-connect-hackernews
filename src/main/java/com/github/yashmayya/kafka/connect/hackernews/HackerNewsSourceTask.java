package com.github.yashmayya.kafka.connect.hackernews;

import static com.github.yashmayya.kafka.connect.hackernews.HackerNewsSourceConnectorConfig.API_VERSION;
import static com.github.yashmayya.kafka.connect.hackernews.HackerNewsSourceConnectorConfig.BASE_API_PATH;
import static com.github.yashmayya.kafka.connect.hackernews.HackerNewsSourceConnectorConfig.MAX_ITEM_PATH;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HackerNewsSourceTask extends SourceTask {

  private static final Logger log = LoggerFactory.getLogger(HackerNewsSourceTask.class);
  private ObjectMapper objectMapper;
  private Long currentItemId;
  private Long count;
  private Long maxItemId;
  private HackerNewsSourceConnectorConfig config;
  private HttpRequestFactory requestFactory;

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
    requestFactory = new NetHttpTransport().createRequestFactory();
    try {
      HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(
          BASE_API_PATH + API_VERSION + MAX_ITEM_PATH));
      maxItemId = Long.parseLong(request.execute().parseAsString().trim());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    if (currentItemId > maxItemId) {
      throw new ConfigException(HackerNewsSourceConnectorConfig.INITITIAL_START_ITEM_CONFIG
          + "is greater than the current max item id on Hacker News");
    }
  }

  @Override
  public List<SourceRecord> poll() throws InterruptedException {

    if (config.getMaxItems() > 0 && count >= config.getMaxItems()) {
      throw new ConnectException("Read the configured number of Hacker News items");
    }

    Thread.sleep(config.getPollInterval());
    Map<String, Object> hnItem;
    try {
      hnItem = objectMapper.readValue(new URL(
          BASE_API_PATH + API_VERSION + String.format("/item/%s.json", currentItemId)),
          new TypeReference<Map<String,Object>>(){});
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    List<SourceRecord> records = new ArrayList<>();
    SourceRecord record = new SourceRecord(
        null,
        null,
        config.getKafkaTopic(),
        null,
        hnItem
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
