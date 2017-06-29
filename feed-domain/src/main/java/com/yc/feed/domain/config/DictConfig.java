package com.yc.feed.domain.config;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.Map;

/**
 * Created by Administrator on 2016/12/22.
 */
@Root(name = "dictinfo")
public class DictConfig {

    @ElementMap(entry = "dict",key = "key",inline = true,attribute = true ,required = true)
    private Map<String,String> dictMap;

    public DictConfig() {
    }

    public Map<String, String> getDictMap() {
        return dictMap;
    }

    public void setDictMap(Map<String, String> dictMap) {
        this.dictMap = dictMap;
    }
}
