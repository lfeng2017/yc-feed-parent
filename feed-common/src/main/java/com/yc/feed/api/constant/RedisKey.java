package com.yc.feed.api.constant;


public class RedisKey
{
	//连接符
	public final static String JOIN_KEY= "_";
	/**
	 * 活动注册自增ID
	 * String结构：incr activity_increment_id
	 */
	public final static String ACTIVITY_INCREMENT_ID_KEY = "activity_increment_id";
	
	/**
	 * 活动司机的好评信息key前缀
	 * String结构： activity_driver_info_${activity_id}${driver_id}
	 *   {
	 *   goodOrders:XX,//好评订单数
	 *   badOrders :XX,//差评订单数
	 *   orders : XX,//订单数
	 *   evalute:XX //好评率
	 *   }
	 * 
	 */
	public final static String ACTIVITY_DRIVER_INFO_KEY_PREFIX = "activity_driver_info_";
	
	/**
	 * 活动列表
	 * 结构: SortedSet
	 * 示例: zadd activity_list {activity_id}{start_time}{end_time}
	 */
	public final static String ACTIVITY_LIST = "activity_list";
	
	/**
	 * 活动开始时间列表
	 * 结构: SortedSet
	 * 示例: zadd activity_list_startTime {start_time}{activityId}
	 */
	public final static String ACTIVITY_LIST_STARTTIME = "activity_list_startTime";
	
	/**
	 * 活动结束时间列表
	 * 结构: SortedSet
	 * 示例: zadd activity_list_endTime {end_time}{activityId}
	 */
	public final static String ACTIVITY_LIST_ENDTIME = "activity_list_endTime";
	
	/**
	 * 活动司机列表
	 * 结构: SortedSet
	 * 说明:
	 *          value: 司机ID
	 * 示例: zadd activity_driver_list_${activity_id}   {driver_id}
	 */
	public final static String ACTIVITY_DRIVER_LIST_KEY_PREFIX = "activity_driver_list_";

	/*
	*Hash中评价key
	*/
	public final static String EVALUATION_MAP_KEY = "evaluation";

	/*
	*Hash中最后一次订单号Key
	*/
	public final static String ORDER_ID_MAP_KEY = "lastOrderId";

	/*
	*司机历史好评率前缀
	*/
	public final static String DRIVER_HIS_EVAL_PREFIX = "driver_comment_evaluation_";
	
	

	public static String buildRedisKey(String prefix, Object parameter)
	{
		return prefix.concat(parameter.toString());
	}
}