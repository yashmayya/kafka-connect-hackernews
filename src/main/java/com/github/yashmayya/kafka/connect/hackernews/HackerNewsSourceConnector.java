package com.github.yashmayya.kafka.connect.hackernews;

import com.google.common.collect.ImmutableList;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class HackerNewsSourceConnector extends SourceConnector {

  private static final Logger log = LoggerFactory.getLogger(HackerNewsSourceConnector.class);
  private HackerNewsSourceConnectorConfig config;
  private Map<String, String> props;

  @Override
  public void start(Map<String, String> props) {
    this.props = props;
    this.config = new HackerNewsSourceConnectorConfig(props);
  }

  @Override
  public Class<? extends Task> taskClass() {
    return HackerNewsSourceTask.class;
  }

  @Override
  public List<Map<String, String>> taskConfigs(int i) {
    if (i > 1) {
      log.warn("This connector currently only supports a single task.");
    }

    props.put(HackerNewsSourceTask.TASK_ID, Integer.toString(0));
    return ImmutableList.of(props);
  }

  @Override
  public void stop() {

  }

  @Override
  public ConfigDef config() {
    return HackerNewsSourceConnectorConfig.CONFIG_DEF;
  }

  @Override
  public String version() {
    return VersionUtil.get();
  }
}
