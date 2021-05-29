# kafka-connect-hackernews
A Kafka Connector to read items from Hacker News and stream it into Kafka. Because why not `¯\_(ツ)_/¯`

## Overview

This source connector reads items (stories, comments, jobs, Ask HNs, polls) from Hacker News via https://github.com/HackerNews/API. Currently, only a single connector task is supported.


## Installation

Run `mvn clean package` from the repo's root and then unzip and copy the zip archive created in `target/components/packages/` to any directory on your [Connect worker's plugin path.](https://docs.confluent.io/home/connect/userguide.html#installing-kconnect-plugins)


## Configuration

These are the supported configs :- 

|Name|Description|Type|Importance|
|---|---|---|---|
|`kafka.topic`| Topic to write to | String| High |
|`poll.interval.ms`| Interval between polls (ms) | Long | High |
|`initial.start.item`| Hacker News item id to start reading from | Long | Medium |
|`max.items`| Maximum number of items to read from Hacker News or less than 1 for unlimited | Long | Medium|

Currently, this connector only supports StringConverter (`org.apache.kafka.connect.storage.StringConverter`) and JsonConverter (`org.apache.kafka.connect.json.JsonConverter`) without embedded schemas.

An example config for this connector :-

```
{
  "name": "HN",
  "connector.class": "com.github.yashmayya.kafka.connect.hackernews.HackerNewsSourceConnector",
  "value.converter": "org.apache.kafka.connect.json.JsonConverter",
  "value.converter.schemas.enable": "false",
  "kafka.topic": "hn-items",
  "poll.interval.ms": "100",
  "initial.start.item": "1"
}
```

## TODO

- [ ] Add unit tests
- [X] ~~Implement offset tracking and recovery~~
- [X] ~~Support dynamic reloading of [max item id](https://github.com/HackerNews/API#max-item-id) so that the connector can run forever~~
- [X] ~~Add support for schemas~~
