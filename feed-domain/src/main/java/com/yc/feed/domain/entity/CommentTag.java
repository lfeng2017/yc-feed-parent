package com.yc.feed.domain.entity;

public class CommentTag {
    //ID
    private Integer commentTagId;
    //互斥标签ID
    private Integer conflictTagId;
    //标签内容
    private String tagText;
    //差标签分值
    private Byte score;
    //类型 1:正面 -1:负面
    private Byte type;
    //状态
    private Byte status;
    //排序
    private Integer rank;

    private Integer operatorId;

    private Integer createTime;

    private Integer updateTime;

    private Integer flag;

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
        this.tagText = tagText == null ? null : tagText.trim();
    }

    public Byte getScore() {
        return score;
    }

    public void setScore(Byte score) {
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

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}