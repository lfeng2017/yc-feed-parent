package com.yc.feed.api.http.req;

/**
 * Created by steven on 16-12-16.
 */
public class GetTagsReq {
    //类型
    private Integer type;
    //状态
    private String[] status;
    //互斥标签id
    private String[] conflictTagIds;
    //排序
    private Integer rank;
    //标签内容
    private String tagText;


    @Override
    public String toString() {
        return "GetTagsReq{" +
                "type=" + type +
                ", rank=" + rank +
                ", tagText='" + tagText + '\'' +
                '}';
    }



    public  Integer getType() {
        return type;
    }
    public void    setType(Integer type) {
        this.type = type;
    }
    public Integer getRank() {
        return rank;
    }
    public void    setRank(Integer rank) {
        this.rank = rank;
    }
    public String  getTagText() {
        return tagText;
    }
    public void    setTagText(String tagText) {
        this.tagText = tagText;
    }

    public String[] getConflictTagIds() {
        return conflictTagIds;
    }

    public void setConflictTagIds(String[] conflictTagIds) {
        this.conflictTagIds = conflictTagIds;
    }

    public String[] getStatus() {
        return status;
    }

    public void setStatus(String[] status) {
        this.status = status;
    }
}
