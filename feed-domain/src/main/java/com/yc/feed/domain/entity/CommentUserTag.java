package com.yc.feed.domain.entity;

public class CommentUserTag {
    private Integer commentUserTagId;

    private Integer conflictTagId;

    private String tagText;

    private Byte type;

    private Byte gender;

    private Byte status;

    private Integer rank;

    private Integer operatorId;

    private Integer createTime;

    private Integer updateTime;

    public Integer getCommentUserTagId() {
        return commentUserTagId;
    }

    public void setCommentUserTagId(Integer commentUserTagId) {
        this.commentUserTagId = commentUserTagId;
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

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
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
}