package com.yc.feed.domain.model;

/**
 * Created by Administrator on 2017/2/13.
 */
public class CommentTagResults {
    private Integer comment_tag_id;
    private Integer count;
    private String tag_text;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getComment_tag_id() {
        return comment_tag_id;
    }

    public void setComment_tag_id(Integer comment_tag_id) {
        this.comment_tag_id = comment_tag_id;
    }

    public String getTag_text() {
        return tag_text;
    }

    public void setTag_text(String tag_text) {
        this.tag_text = tag_text;
    }

    @Override
    public String toString() {
        return "CommentTagResults{" +
                "comment_tag_id=" + comment_tag_id +
                ", count=" + count +
                ", tag_text='" + tag_text + '\'' +
                '}';
    }
}
