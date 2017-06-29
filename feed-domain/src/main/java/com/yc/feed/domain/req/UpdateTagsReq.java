package com.yc.feed.domain.req;

/**
 * Created by steven on 16-12-16.
 */
public class UpdateTagsReq {

    private String commentTagId;

    private String status;

    private String conflictTagId;

    private String score;

    private String rank;

    private String flag;


    @Override
    public String toString() {
        return "UpdateTagsReq{" +
                "commentTagId='" + commentTagId + '\'' +
                ", status='" + status + '\'' +
                ", score='" + score + '\'' +
                ", rank='" + rank + '\'' +
                ", flag='" + flag + '\'' +
                ", conflictTagId='" + conflictTagId + '\'' +
                '}';
    }

    public String getCommentTagId() {
        return commentTagId;
    }

    public void setCommentTagId(String commentTagId) {
        this.commentTagId = commentTagId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConflictTagId() {
        return conflictTagId;
    }

    public void setConflictTagId(String conflictTagId) {
        this.conflictTagId = conflictTagId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
