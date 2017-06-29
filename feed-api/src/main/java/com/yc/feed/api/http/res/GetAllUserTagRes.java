package com.yc.feed.api.http.res;

import com.yc.feed.domain.model.CommentTagModel;
import com.yc.feed.domain.model.CommentUserTagModel;

import java.util.List;

/**
 * Created by yusong on 2016/11/2.
 * 查询所有有效标签响应
 */
public class GetAllUserTagRes extends CommonRes{
    //记录总数
    private long count;

    //有效标签列表
    private List<CommentUserTagModel> tags;

    public GetAllUserTagRes() {
    }

    public GetAllUserTagRes(boolean success, String code, String msg, long count, List<CommentUserTagModel> tags) {
        super(success, code, msg);
        this.count = count;
        this.tags = tags;
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

    public List<CommentUserTagModel> getTags() {
        return tags;
    }

    public void setTags(List<CommentUserTagModel> tags) {
        this.tags = tags;
    }
}
