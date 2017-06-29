package com.yc.feed.api.http.req;

/**
 * Created by ke on 16-12-16.
 */
public class AddCommentTagReq {
    //互斥标签ID
    private Integer conflictTagId;
    //标签内容
    private String tagText;
    //差标签分值
    private Integer score;
    //类型 1:正面 -1:负面
    private Integer type;
    //状态
    private Integer status;
    //排序String
    private Integer rank;
    //操作者
    private Integer operatorId;
    //可用车型  0专车和搭车都不能用，1专车，2搭车，3专车和搭车都可以用
    private Integer flag;

    @Override
    public String toString() {
        return "AddCommentTagReq{" +
                ", conflictTagId=" + conflictTagId +
                ", tagText='" + tagText + '\'' +
                ", score=" + score +
                ", type=" + type +
                ", status=" + status +
                ", rank=" + rank +
                ", operatorId=" + operatorId +
                ", flag=" + flag +
                '}';
    }

    public Integer getConflictTagId() {
        return conflictTagId;
    }

    public void setConflictTagId(Integer conflictTagId) {
        this.conflictTagId = conflictTagId;
    }

    public String getTagText() {
        return tagText;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}
