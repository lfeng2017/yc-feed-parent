package com.yc.feed.service.impl;

import com.yc.feed.api.http.res.GetDriverTagStatRes;
import com.yc.feed.dao.crm.DriverTagStatMapper;
import com.yc.feed.domain.entity.DriverTagStat;
import com.yc.feed.domain.enums.CodeTypes;
import com.yc.feed.domain.excep.FeedException;
import com.yc.feed.domain.model.CommentTagModel;
import com.yc.feed.domain.model.DriverTagStatModel;
import com.yc.feed.service.StatService;
import com.yc.feed.service.TagService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yusong on 2016/11/15.
 * 统计服务实现
 */
@Service
public class StatServiceImpl implements StatService {

    private final Logger logger = Logger.getLogger(StatServiceImpl.class);

    @Resource
    private DriverTagStatMapper driverTagStatMapper;

    @Autowired
    private TagService tagService;

    @Override
    public GetDriverTagStatRes getDriverTagStat(Long driverId)  {
        List<DriverTagStat> entities =  driverTagStatMapper.selectByDriverId(driverId);
        List<DriverTagStatModel> result = new ArrayList<DriverTagStatModel>();
        if (null == entities || entities.size() == 0){
            logger.warn("getDriverTagStat|未查询到司机标签信息|driverId:"+driverId);
            return new GetDriverTagStatRes(Boolean.TRUE, CodeTypes.Success.getCodeStr(),CodeTypes.Success.getMsg(),0,result);
        }

        for (DriverTagStat entity :entities){
            Integer commentTagId = entity.getCommentTagId();
            CommentTagModel commentTagModel = tagService.getTag(commentTagId);
            DriverTagStatModel model = new DriverTagStatModel(entity.getDriverId(),entity.getCount(),commentTagModel);
            result.add(model);
        }
        return new GetDriverTagStatRes(Boolean.TRUE, CodeTypes.Success.getCodeStr(),CodeTypes.Success.getMsg(),result.size(),result);
    }
}
