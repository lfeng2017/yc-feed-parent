package com.yc.feed.domain.model;

/**
 * Created by yusong on 2016/11/8.
 * 标签模型
 */
public class CommentTagModel {
    //ID
    private Integer commentTagId;
    //互斥标签ID
    private Integer conflictTagId;
    //标签内容
    private String tagText;
    //差标签分值
    private int score;
    //类型 1:正面 -1:负面
    private Byte type;
    //状态
    private Byte status;
    //排序
    private Integer rank;
    //可用车型  0专车和搭车都不能用，1专车，2搭车，3专车和搭车都可以用
    private Integer flag;

    private Integer createTime;

    private Integer operatorId;

    @Override
    public String toString() {
        return "CommentTagModel{" +
                "commentTagId=" + commentTagId +
                ", conflictTagId=" + conflictTagId +
                ", tagText='" + tagText + '\'' +
                ", score=" + score +
                ", type=" + type +
                ", status=" + status +
                ", rank=" + rank +
                ", createTime=" + createTime +
                ", operatorId=" + operatorId +
                ", flag=" + flag +
                '}';
    }

    public Integer getCommentTagId() {
        return commentTagId;
    }

    public void setCommentTagId(Integer commentTagId) {
        this.commentTagId = commentTagId;
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


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }
}
