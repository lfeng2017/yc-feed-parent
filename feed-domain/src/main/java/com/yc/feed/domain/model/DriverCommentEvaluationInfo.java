package com.yc.feed.domain.model;

import java.io.Serializable;

public class DriverCommentEvaluationInfo implements Serializable {
  //司机ID
  private long driverId;
  //好评单数
  private int goodOrders;
  //差评单数
  private int badOrders;
  //最后一个订单ID
  private long lastOrderId;
  //好评率 百分数
  private int evaluation;



  public DriverCommentEvaluationInfo() {
  }

  public DriverCommentEvaluationInfo(Long driverId, Integer goodOrders, Integer badOrders, Long lastOrderId) {
    this.driverId = driverId;
    this.goodOrders = goodOrders;
    this.badOrders = badOrders;
    this.lastOrderId = lastOrderId;
  }

  @Override
  public String toString() {
    return "DriverCommentEvaluationInfo{" +
            "driverId=" + driverId +
            ", goodOrders=" + getGoodOrders() +
            ", badOrders=" + getBadOrders() +
            ", lastOrderId=" + lastOrderId +
            ", evaluation=" + getEvaluation() +
            '}';
  }

  public long getDriverId() {
    return driverId;
  }

  public void setDriverId(long driverId) {
    this.driverId = driverId;
  }

  public int getGoodOrders() {
    if (goodOrders < 0){
      goodOrders = 0;
    }else if (goodOrders  > 50){
      goodOrders =50;
    }
    return goodOrders;
  }

  public void setGoodOrders(int goodOrders) {
    if (goodOrders < 0){
      goodOrders = 0;
    }else if (goodOrders  > 50){
      goodOrders =50;
    }
    this.goodOrders = goodOrders;
    calEval();
  }

  public int getBadOrders() {
    if (badOrders < 0){
      badOrders = 0;
    }else if (badOrders  > 50){
      badOrders =50;
    }
    return badOrders;
  }

  public void setBadOrders(int badOrders) {
    if (badOrders < 0){
      badOrders = 0;
    }else if (badOrders  > 50){
      badOrders =50;
    }
    this.badOrders = badOrders;
    calEval();
  }

  public long getLastOrderId() {
    return lastOrderId;
  }

  public void setLastOrderId(long lastOrderId) {
    this.lastOrderId = lastOrderId;
  }

  public void setEvaluation(int evaluation) {
    this.evaluation = evaluation;
  }

  public int getEvaluation() {
    return calEval();
  }

  private int calEval(){
    if(getGoodOrders()+getBadOrders() <= 0){
      evaluation = 100;
    }else{
      evaluation = Math.round((getGoodOrders()*100)/(getGoodOrders()+getBadOrders()));
    }
    return evaluation;
  }



}
