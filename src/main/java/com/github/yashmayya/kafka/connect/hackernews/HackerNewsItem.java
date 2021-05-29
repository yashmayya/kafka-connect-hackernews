package com.github.yashmayya.kafka.connect.hackernews;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;

import java.util.List;

public class HackerNewsItem {

    public static final Schema VALUE_SCHEMA = new SchemaBuilder(Schema.Type.STRUCT)
            .field("id", Schema.INT64_SCHEMA)
            .field("deleted", Schema.OPTIONAL_BOOLEAN_SCHEMA)
            .field("type", Schema.STRING_SCHEMA)
            .field("by", Schema.STRING_SCHEMA)
            .field("time", Schema.INT64_SCHEMA)
            .field("text", Schema.OPTIONAL_STRING_SCHEMA)
            .field("dead", Schema.OPTIONAL_BOOLEAN_SCHEMA)
            .field("parent", Schema.OPTIONAL_INT64_SCHEMA)
            .field("poll", Schema.OPTIONAL_INT64_SCHEMA)
            .field("kids", SchemaBuilder.array(Schema.INT64_SCHEMA).optional().build())
            .field("url", Schema.OPTIONAL_STRING_SCHEMA)
            .field("score", Schema.OPTIONAL_INT64_SCHEMA)
            .field("title", Schema.OPTIONAL_STRING_SCHEMA)
            .field("parts", SchemaBuilder.array(Schema.INT64_SCHEMA).optional().build())
            .field("descendants", Schema.OPTIONAL_INT64_SCHEMA)
            .build();

    private Long id;
    private Boolean deleted;
    private String type;
    private String by;
    private Long time;
    private String text;
    private Boolean dead;
    private Long parent;
    private Long poll;
    private List<Long> kids;
    private String url;
    private Long score;
    private String title;
    private List<Long> parts;
    private Long descendants;

    public Struct toStruct() {
        Struct s = new Struct(VALUE_SCHEMA);
        s.put("id", id);
        s.put("type", type);
        s.put("by", by);
        s.put("time", time);
        if (deleted != null) {
            s.put("deleted", deleted);
        }
        if (text != null) {
            s.put("text", text);
        }
        if (dead != null) {
            s.put("dead", dead);
        }
        if (parent != null) {
            s.put("parent", parent);
        }
        if (poll != null) {
            s.put("poll", poll);
        }
        if (kids != null) {
            s.put("kids", kids);
        }
        if (url != null) {
            s.put("url", url);
        }
        if (score != null) {
            s.put("score", score);
        }
        if (title != null) {
            s.put("title", title);
        }
        if (parts != null) {
            s.put("parts", parts);
        }
        if (descendants != null) {
            s.put("descendants", descendants);
        }
        return s;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDead(Boolean dead) {
        this.dead = dead;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public void setPoll(Long poll) {
        this.poll = poll;
    }

    public void setKids(List<Long> kids) {
        this.kids = kids;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setParts(List<Long> parts) {
        this.parts = parts;
    }

    public void setDescendants(Long descendants) {
        this.descendants = descendants;
    }
}