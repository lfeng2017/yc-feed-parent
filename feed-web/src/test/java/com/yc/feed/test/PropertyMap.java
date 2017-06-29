package com.yc.feed.test;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.Map;

@Root(name="properties")
public class PropertyMap {

   @ElementMap(entry="property", key="key", attribute=true, inline=true)
   private Map<String, String> map;

   @Element(required = false)
   private String name;  

   public String getName() {
      return name;
   }

   public Map<String, String> getMap() {
      return map;
   }

   public String get(String key){
      return this.map.get(key);
   }
}