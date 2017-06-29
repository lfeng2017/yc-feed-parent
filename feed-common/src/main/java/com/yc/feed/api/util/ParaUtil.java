package com.yc.feed.api.util;


import com.yc.feed.domain.req.PageReq;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;

/**
 * Created by yusong on 2016/11/11.
 * 参数工具类
 */
public class ParaUtil {

    /*
    *设置页面参数
    */
    public static void setPagePara(HttpServletRequest request,PageReq pageReq) throws InvalidParameterException{
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        String beCount = request.getParameter("beCount");
        String startTimeStamp = request.getParameter("startTimeStamp");
        String endTimeStamp = request.getParameter("endTimeStamp");

        //分页参数
        if(!StringUtils.isEmpty(pageNum)){
            if(!FeedStringUtil.isLong(pageNum)){
                throw new InvalidParameterException("pageNum格式必须为数字。");
            }else{
                pageReq.setPageNum(Long.parseLong(pageNum));
            }
        }

        if(!StringUtils.isEmpty(pageSize)){
            if(!FeedStringUtil.isLong(pageSize)){
                throw new InvalidParameterException("pageSize格式必须为数字。");
            }else{
                pageReq.setPageSize(Long.parseLong(pageSize));
            }
        }

        if(!StringUtils.isEmpty(beCount)){
            if(!FeedStringUtil.isBoolean(beCount)){
                throw new InvalidParameterException("beCount格式必须为布尔型。");
            }else{
                pageReq.setBeCount(Boolean.parseBoolean(beCount));
            }
        }

        if(!StringUtils.isEmpty(startTimeStamp)){
            if(!FeedStringUtil.isLong(startTimeStamp)){
                throw new InvalidParameterException("startTimeStamp格式必须为数字。");
            }else{
                pageReq.setStartTimeStamp(Long.parseLong(startTimeStamp));
            }
        }

        if(!StringUtils.isEmpty(endTimeStamp)){
            if(!FeedStringUtil.isLong(endTimeStamp)){
                throw new InvalidParameterException("endTimeStamp格式必须为数字。");
            }else{
                pageReq.setEndTimeStamp(Long.parseLong(endTimeStamp));
            }
        }
    }





}
