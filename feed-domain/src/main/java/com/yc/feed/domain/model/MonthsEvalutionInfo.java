package com.yc.feed.domain.model;

import java.util.List;

/** 
 * @author steven 
 * @date 2016年12月6日 下午6:08:08
 * @instruction
 */
public class MonthsEvalutionInfo {

	//月好评率
	private int evaluation;
	//月差评数
	private int badOrdersCount;
	//差评订单号
	private String badOrderIds;
	//司机id
	private String driverId;

	public String getBadOrderIds() {
		return badOrderIds;
	}
	public void setBadOrderIds(String badOrderIds) {
		this.badOrderIds = badOrderIds;
	}
	public int getEvaluation() {
		return evaluation;
	}
	public void setEvaluation(int evaluation) {
		this.evaluation = evaluation;
	}
	public int getBadOrdersCount() {
		return badOrdersCount;
	}
	public void setBadOrdersCount(int badOrdersCount) {
		this.badOrdersCount = badOrdersCount;
	}
	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

}
