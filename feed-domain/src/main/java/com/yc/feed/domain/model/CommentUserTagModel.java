package com.yc.feed.domain.model;

/**
 * Created by lfeng on 2016/11/30.
 * 标签模型
 */
public class CommentUserTagModel {
    //评价标签ID
    private Integer commentUserTagId;
    //互斥标签ID
    private Integer conflictTagId;
    //标签内容
    private String tagText;
    //类型 1:男 2:女 3:all
    private Byte type;
    //1.男,2.女,3.所有
    private Byte gender;

    @Override
    public String toString() {
        return "CommentUserTagModel{" +
                "commentUserTagId=" + commentUserTagId +
                ", conflictTagId=" + conflictTagId +
                ", tagText='" + tagText + '\'' +
                ", type=" + type +
                ", gender=" + gender +
                '}';
    }

    public Integer getCommentUserTagId() {
        return commentUserTagId;
    }

    public Integer getConflictTagId() {
        return conflictTagId;
    }

    public String getTagText() {
        return tagText;
    }

    public Byte getType() {
        return type;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public void setCommentUserTagId(Integer commentUserTagId) {
        this.commentUserTagId = commentUserTagId;
    }

    public void setConflictTagId(Integer conflictTagId) {
        this.conflictTagId = conflictTagId;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText;
    }

    public void setType(Byte type) {
        this.type = type;
    }

}
