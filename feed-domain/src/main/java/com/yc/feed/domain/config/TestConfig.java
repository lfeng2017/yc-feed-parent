package com.yc.feed.domain.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.simpleframework.xml.*;
import org.simpleframework.xml.core.Commit;
@Root(name = "test")
public class TestConfig {
    @Attribute(name = "name", required = false)
    private String name;
    @Element(name = "desc", required = false)
    private String desc;
    @Element(name = "user", required = false)
    private User user;
    @ElementList(name = "items", entry = "item", inline = false, required = false)
    private List<Item> inlinedItems;
    @ElementMap(name = "dicts", entry = "dict", key = "key", attribute
            = true, required = false)
    private Map<String, String> inlinedDict;
    private Map<Integer, Item> id2Item;
    @Commit
    private void init() {
        if (null != inlinedItems && !inlinedItems.isEmpty()) {
            id2Item = new HashMap<Integer, Item>();
            for (Item item : inlinedItems) {
                id2Item.put(item.getId(), item);
            }
        }
    }
    public Item getItemById(int itemId) {
        return null == id2Item ? null : id2Item.get(itemId);
    }
    public Map<String, String> getInlinedDict() {
        return inlinedDict;
    }
    public String getName() {
        return name;
    }
    public String getDesc() {
        return desc;
    }
    public User getUser() {
        return user;
    }
    public List<Item> getInlinedItems() {
        return inlinedItems;
    }
    public static class User {
        @Attribute
        private int id;
        @Attribute
        private String name;
        public int getId() {
            return id;
        }
        public String getName() {
            return name;
        }
    }
    public static class Item {
        @Attribute
        private int id;
        @Attribute
        private String name;
        @Attribute
        private float price;
        public int getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public float getPrice() {
            return price;
        }
    }
}