package com.yc.feed.api.http.req;

/**
 * Created by ke on 16-12-16.
 */
public class WebAddCommentTagReq {

    //ID
    private String commentTagId;
    //互斥标签ID
    private String conflictTagId;
    //标签内容
    private String tagText;
    //差标签分值
    private String score;
    //类型 1:正面 -1:负面
    private String type;
    //状态
    private String status;
    //排序String
    private String rank;
    //操作者
    private String operatorId;
    //可用车型  0专车和搭车都不能用，1专车，2搭车，3专车和搭车都可以用
    private String flag;

    @Override
    public String toString() {
        return "WebAddCommentTagReq{" +
                "commentTagId='" + commentTagId + '\'' +
                ", conflictTagId='" + conflictTagId + '\'' +
                ", tagText='" + tagText + '\'' +
                ", score='" + score + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", rank='" + rank + '\'' +
                ", operatorId='" + operatorId + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }

    public String getCommentTagId() {
        return commentTagId;
    }

    public void setCommentTagId(String commentTagId) {
        this.commentTagId = commentTagId;
    }

    public String getConflictTagId() {
        return conflictTagId;
    }

    public void setConflictTagId(String conflictTagId) {
        this.conflictTagId = conflictTagId;
    }

    public String getTagText() {
        return tagText;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }


}
