package com.github.yashmayya.kafka.connect.hackernews;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;

import static com.github.yashmayya.kafka.connect.hackernews.HackerNewsSourceConnectorConfig.*;

public class HackerNewsUtil {
    private static final HttpRequestFactory requestFactory;

    static {
        requestFactory = new NetHttpTransport().createRequestFactory();
    }

    public static Long getMaxItemId() {
        try {
            HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(
                    BASE_API_PATH + API_VERSION + MAX_ITEM_PATH));
            return Long.parseLong(request.execute().parseAsString().trim());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
