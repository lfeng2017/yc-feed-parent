package com.yc.feed.domain.entity;

public class FeedActivityEvaluationInfo {

	private long feedActivityEvaluationId;
	private long activityId;
	private long driverId;
	private long lastOrderId;
	private int orders;
	private int goodOrders;
	private int badOrders;
	private long createTime;
	private int evaluation;
	private int deleted;

	public long getFeedActivityEvaluationId() {
		return feedActivityEvaluationId;
	}

	public void setFeedActivityEvaluationId(long feedActivityEvaluationId) {
		this.feedActivityEvaluationId = feedActivityEvaluationId;
	}

	public long getActivityId() {
		return activityId;
	}

	public void setActivityId(long activityId) {
		this.activityId = activityId;
	}

	public long getDriverId() {
		return driverId;
	}

	public void setDriverId(long driverId) {
		this.driverId = driverId;
	}

	public long getLastOrderId() {
		return lastOrderId;
	}

	public void setLastOrderId(long lastOrderId) {
		this.lastOrderId = lastOrderId;
	}

	public int getOrders() {
		return orders;
	}

	public void setOrders(int orders) {
		this.orders = orders;
	}

	public int getGoodOrders() {
		return goodOrders;
	}

	public void setGoodOrders(int goodOrders) {
		this.goodOrders = goodOrders;
	}

	public int getBadOrders() {
		return badOrders;
	}

	public void setBadOrders(int badOrders) {
		this.badOrders = badOrders;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(int evaluation) {
		this.evaluation = evaluation;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
}
