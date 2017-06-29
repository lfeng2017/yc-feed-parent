package com.yc.feed.api.http.res;

import java.util.List;

import com.yc.feed.domain.model.OrderCommentInfo;

/** 
 * @author steven 
 * @date 2016年12月8日 下午12:11:11
 * @instruction
 */
public class GetOrderCommentListByIdsRes extends CommonRes {
	//评价列表
    private List<OrderCommentInfo> commentList;
    
    public GetOrderCommentListByIdsRes(boolean success, String code, String msg) {
        super(success, code, msg);
    }

    public GetOrderCommentListByIdsRes(boolean success, String code, String msg,List<OrderCommentInfo> commentList) {
        super(success, code, msg);
        this.commentList = commentList;
    }
    
	 public List<OrderCommentInfo> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<OrderCommentInfo> commentList) {
		this.commentList = commentList;
	}
}
