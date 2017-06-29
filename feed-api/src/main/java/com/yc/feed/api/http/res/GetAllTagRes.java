package com.yc.feed.api.http.res;

import com.yc.feed.domain.entity.CommentTag;
import com.yc.feed.domain.model.CommentTagModel;

import java.util.List;

/**
 * Created by yusong on 2016/11/2.
 * 查询所有有效标签响应
 */
public class GetAllTagRes extends CommonRes{
    //记录总数
    private long count;

    //有效标签列表
    private List<CommentTagModel> tags;

    public GetAllTagRes() {
    }

    public GetAllTagRes(boolean success, String code, String msg, long count, List<CommentTagModel> tags) {
        super(success, code, msg);
        this.count = count;
        this.tags = tags;
    }

    public GetAllTagRes(boolean success, String code, String msg, List<CommentTagModel> tags) {
        super(success, code, msg);
        this.tags = tags;
    }



    public GetAllTagRes(boolean success, String code, String msg) {
        super(success, code, msg);
    }

    @Override
    public String toString() {
        return "GetAllTagRes{" +
                "count=" + count +
                ", tags=" + tags +
                '}';
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<CommentTagModel> getTags() {
        return tags;
    }

    public void setTags(List<CommentTagModel> tags) {
        this.tags = tags;
    }
}
