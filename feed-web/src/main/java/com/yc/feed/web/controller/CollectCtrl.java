package com.yc.feed.web.controller;

import com.yc.feed.api.http.req.GetCollectInfoReq;
import com.yc.feed.api.http.req.WebAddCollectDriverReq;
import com.yc.feed.api.http.res.CommonRes;
import com.yc.feed.api.http.res.GetCollectCountRes;
import com.yc.feed.api.http.res.GetCollectListRes;
import com.yc.feed.api.util.FeedStringUtil;
import com.yc.feed.api.util.ParaUtil;
import com.yc.feed.domain.enums.CodeTypes;
import com.yc.feed.domain.excep.FeedException;
import com.yc.feed.domain.req.AddCollectDriverReq;
import com.yc.feed.domain.req.UpdateNoteReq;
import com.yc.feed.domain.req.UpdateServiceTimesReq;
import com.yc.feed.domain.req.WithdrawCollectReq;
import com.yc.feed.service.CollectInfoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;

/**
 * Created by ke on 2016/11/2.
 * 用户收藏控制器
 */
@Controller
@RequestMapping("/feed/collect")
public class CollectCtrl {

    private final Logger logger = Logger.getLogger(OrderCommentCtrl.class);

    @Autowired
    private CollectInfoService collectInfoService;

    /*
    *获取收藏指定司机的用户数
    */
    @RequestMapping("/getUserCountByDriverId")
    @ResponseBody
    public GetCollectCountRes getUserCountByDriverId(HttpServletRequest request){
        long startTime =  System.currentTimeMillis();
        try{
            String driverId = request.getParameter("driverId");
            logger.info("getCountUserByDriverId|driverId:" + driverId);

            if (!FeedStringUtil.isLong(driverId)) {
                logger.error("getCountUserByDriverId|parameter error");
                return new GetCollectCountRes(Boolean.FALSE, CodeTypes.ParaError.getCode(), "parameter error", 0);
            }

            GetCollectCountRes res = null;
            try {
                res = collectInfoService.getUserCountByDriverId(Long.valueOf(driverId));
            } catch (Exception e) {
                logger.error("getCountUserByDriverId|throw an error.|",e);
                return new GetCollectCountRes(Boolean.FALSE, CodeTypes.SystemError.getCode(), "fail", 0);
            }
            logger.info("getCountUserByDriverId|" + (System.currentTimeMillis()-startTime)+"ms|returnResult:"+res);
            return res;
        }catch (Exception e){
            logger.error("getCountUserByDriverId|程序异常|:"+e.getMessage());
        }
        return new GetCollectCountRes(Boolean.FALSE, CodeTypes.SystemError.getCode(), "fail", 0);
    }


    /*
    *获取收藏指定司机的用户列表
    */
    @RequestMapping("/getCollectList")
    @ResponseBody
    public GetCollectListRes getCollectList(HttpServletRequest request){
        long startTime =  System.currentTimeMillis();
        GetCollectInfoReq domainReq = null ;
        try {
            domainReq = parseListPara(request);
            logger.info("getCollectListByDriverId|Start|req:" + domainReq);
        } catch (InvalidParameterException e) {
            logger.error("getCollectListByDriverId|Para Error|"+e.getMessage());
            return new GetCollectListRes(Boolean.FALSE,CodeTypes.ParaError.getCodeStr(),e.getMessage());
        }
        GetCollectListRes res = collectInfoService.getList(domainReq);
        logger.info("getCollectListByDriverId|End|" + (System.currentTimeMillis()-startTime)+"ms|returnResult:"+res);
        return res;
    }

    /*
    *更改备注
    */
    @RequestMapping("/updateNote")
    @ResponseBody
    public CommonRes updateNote(@RequestBody UpdateNoteReq updateNoteReq){
        long startTime =  System.currentTimeMillis();
        try{
            logger.info("updateNote|req:"+updateNoteReq);
            CommonRes res = null;
            try {
                collectInfoService.updateNote(updateNoteReq);
                res = new CommonRes(Boolean.TRUE, CodeTypes.Success.getCodeStr(),"update success");
            } catch (FeedException e) {
                logger.error("updateNote|Error|"+e.getMessage());
                res = new  CommonRes(Boolean.FALSE, String.valueOf(e.getCode()),"update error");
            }
            logger.info("updateNote|" + (System.currentTimeMillis()-startTime)+"ms|returnResult:"+res);
            return res;
        }catch (Exception e){
            logger.error("updateNote|程序异常|:"+e.getMessage());
        }
        return new CommonRes(Boolean.FALSE,String.valueOf(CodeTypes.SystemError.getCode()),"更新备注程序异常");
    }

    /*
    *更新服务次数
    */
    @RequestMapping("/updateServiceTimes")
    @ResponseBody
    public CommonRes updateServiceTimes(@RequestBody UpdateServiceTimesReq updateServiceTimesReq){
        long startTime =  System.currentTimeMillis();
        logger.info("updateServiceTimes|req:"+updateServiceTimesReq);
        CommonRes res = null;
        try {
            collectInfoService.updateServiceTimes(updateServiceTimesReq);
            res = new CommonRes(Boolean.TRUE, CodeTypes.Success.getCodeStr(),"update success");
        } catch (FeedException e) {
            logger.error("updateServiceTimes|Error|"+e.getMessage());
            res = new  CommonRes(Boolean.FALSE, String.valueOf(e.getCode()),e.getMessage());
        }
        logger.info("updateServiceTimes|" + (System.currentTimeMillis()-startTime)+"ms|returnResult:"+res);
        return res;
    }

    /*
    *撤销收藏
    */
    @RequestMapping("/withdraw")
    @ResponseBody
    public CommonRes withdraw(@RequestBody WithdrawCollectReq withdrawCollectReq){
        logger.info("withdraw|req:"+withdrawCollectReq);
        long startTime =  System.currentTimeMillis();
        checkPara(withdrawCollectReq);
        try{
            CommonRes res = null;
            try {
                collectInfoService.withdraw(withdrawCollectReq);
                res = new CommonRes(Boolean.TRUE, CodeTypes.Success.getCodeStr(),"success");
            } catch (FeedException e) {
                logger.error("withdraw|Error|"+e.getMessage());
                res = new  CommonRes(Boolean.FALSE, String.valueOf(e.getCode()),e.getMessage());
            }
            logger.info("withdraw|" + (System.currentTimeMillis()-startTime)+"ms|returnResult:"+res);
            return res;
        }catch (Exception e){
            logger.error("withdraw|程序异常|:"+e.getMessage());
        }
        return new CommonRes(Boolean.FALSE,String.valueOf(CodeTypes.SystemError.getCode()),"撤销收藏程序异常");
    }


    /*
    *新增收藏
    */
    @RequestMapping("/add")
    @ResponseBody
    public CommonRes add(@RequestBody WebAddCollectDriverReq webAddCollectDriverReq){
        long startTime =  System.currentTimeMillis();
        AddCollectDriverReq domainReq=null;
        try{
            domainReq = parseAddPara(webAddCollectDriverReq);
            logger.info("add|新增收藏参数:"+domainReq);
        }catch (InvalidParameterException e) {
            logger.error("add|新增收藏|传递参数错误");
            return new CommonRes(Boolean.FALSE, CodeTypes.ParaError.getCode()+"", e.getMessage());
        }

        CommonRes res = null;
        try {
            collectInfoService.add(domainReq);
            res = new CommonRes(Boolean.TRUE, CodeTypes.Success.getCodeStr(),"add success");
        } catch (FeedException e) {
            res = new  CommonRes(Boolean.FALSE, String.valueOf(e.getCode()),e.getMsg());
            logger.error("add|Error|"+e.getMessage());
        }
        logger.info("add|" + (System.currentTimeMillis()-startTime)+"ms|returnResult:"+res);
        return res;
    }

    /*
    *解析新增参数
    */
    private AddCollectDriverReq parseAddPara(WebAddCollectDriverReq webAddCollectDriverReq){
        logger.info("parseAddPara|receive add para:"+webAddCollectDriverReq);
        AddCollectDriverReq domainReq = new AddCollectDriverReq();
        String userId = webAddCollectDriverReq.getUserId();
        String driverId = webAddCollectDriverReq.getDriverId();

        if(!StringUtils.isEmpty(userId)){
            if(!FeedStringUtil.isLong(userId)){
                throw new InvalidParameterException("用户ID格式必须为数字。");
            }else{
                domainReq.setUserId(Long.parseLong(userId));
            }
        }else{
            throw new InvalidParameterException("用户ID不能為空");
        }

        if(!StringUtils.isEmpty(driverId)){
            if(!FeedStringUtil.isLong(driverId)){
                throw new InvalidParameterException("司机ID格式必须为数字。");
            }else{
                domainReq.setDriverId(Long.parseLong(driverId));
            }
        }else{
            throw new InvalidParameterException("司机ID不能為空");
        }
        domainReq.setDriverCity(webAddCollectDriverReq.getDriverCity());
        domainReq.setDriverNote(webAddCollectDriverReq.getDriverNote());
        logger.info("parseAddPara|domain req:"+domainReq);
        return domainReq;
    }

    /*
    *解析撤销收藏司机接口的参数
    */
    private void checkPara(WithdrawCollectReq withdrawCollectReq){
        logger.info("checkPara|receive parameter:"+withdrawCollectReq);
        String userId = withdrawCollectReq.getUserId();
        String driverId = withdrawCollectReq.getDriverId();

        if(!StringUtils.isEmpty(userId)){
            if(!FeedStringUtil.isLong(userId)){
                throw new InvalidParameterException("用户ID格式必须为数字。");
            }
            if(!StringUtils.isEmpty(driverId)){
                if(!FeedStringUtil.isLong(driverId)){
                    throw new InvalidParameterException("司机ID格式必须为数字。");
                }
            }else{
                throw new InvalidParameterException("司机ID不能为空。");
            }
        }
    }


    /*
    *获取在某个时间内收藏指定司机的乘客
    */
  /*  @RequestMapping("/getCollectListByTime")
    @ResponseBody
    private GetCollectListRes getCollectListByTime(@RequestBody GetCollectInfoReq getCollectUserReq){
        long startTime =  System.currentTimeMillis();
        logger.info("getCollectListByTime|getInfo:" + getCollectUserReq);

        if (!FeedStringUtil.isLong(getCollectUserReq.getDriverId()) || !FeedStringUtil.isLong(getCollectUserReq.getStartTime()) ||
                !FeedStringUtil.isLong(getCollectUserReq.getEndTime())) {
            logger.error("getCollectListByTime|parameter error");
            return new GetCollectListRes(Boolean.FALSE, CodeTypes.ParaError.getCode(), "parameter error", null);
        }

        GetCollectListRes res = null;
        try {
            res = collectInfoService.getCollectListByTime(Long.valueOf(getCollectUserReq.getDriverId()),
                    Long.valueOf(getCollectUserReq.getStartTime()), Long.valueOf(getCollectUserReq.getEndTime()));
        } catch (Exception e) {
            logger.error("getCollectListByTime throw an error.",e);
            return new GetCollectListRes(Boolean.FALSE, CodeTypes.SystemError.getCode(), "fail", null);
        }
        logger.info("getCollectListByTime|" + (System.currentTimeMillis()-startTime)+"ms|returnResult:"+res);
        return res;
    }*/

    /*
    *获取收藏指定司机服务该乘客的次数
    */
   /* @RequestMapping("/getServiceTimesById")
    @ResponseBody
    private GetCollectCountRes getServiceTimesById(@RequestBody GetCollectInfoReq getCollectUserReq){
        long startTime =  System.currentTimeMillis();
        logger.info("getServiceTimesById|getInfo:" + getCollectUserReq);

        if (!FeedStringUtil.isLong(getCollectUserReq.getDriverId())) {
            logger.error("getServiceTimesById|parameter error");
            return new GetCollectCountRes(Boolean.FALSE, CodeTypes.ParaError.getCode(), "parameter error", 0);
        }

        GetCollectCountRes res = null;
        try {
            res = collectInfoService.getServiceTimesById(Long.valueOf(getCollectUserReq.getDriverId()),
                    Long.valueOf(getCollectUserReq.getUserId()));
        } catch (Exception e) {
            logger.error("getServiceTimesById|throw an error.|",e);
            return new GetCollectCountRes(Boolean.FALSE, CodeTypes.SystemError.getCode(), "fail", 0);
        }
        logger.info("getServiceTimesById|" + (System.currentTimeMillis()-startTime)+"ms|returnResult:"+res);
        return res;
    }*/

    /*
    *分页获取收藏指定司机的用户
    */
    /*@RequestMapping("/getCollectListByPage")
    @ResponseBody
    private GetCollectListRes getCollectListByPage(@RequestBody GetCollectInfoReq getCollectUserReq){
        long startTime =  System.currentTimeMillis();
        logger.info("getCollectListByPage|getInfo:" + getCollectUserReq);

        if (!FeedStringUtil.isLong(getCollectUserReq.getDriverId()) || !FeedStringUtil.isInteger(getCollectUserReq.getPageNum()) ||
                !FeedStringUtil.isInteger(getCollectUserReq.getPageSize())) {
            logger.error("getCollectListByPage|parameter error");
            return new GetCollectListRes(Boolean.FALSE, CodeTypes.ParaError.getCode(), "parameter error", null);
        }

        GetCollectListRes res = null;
        try {
            int page = Integer.valueOf(getCollectUserReq.getPage());
            int pageSize = Integer.valueOf(getCollectUserReq.getPageSize());
            res = collectInfoService.getCollectListByPage(Long.valueOf(getCollectUserReq.getDriverId()),
                    pageSize, pageSize * (page - 1));
        } catch (Exception e) {
            logger.error("getCollectListByPage throw an error.",e);
            return new GetCollectListRes(Boolean.FALSE, CodeTypes.SystemError.getCode(), "fail", null);
        }
        logger.info("getCollectListByPage|" + (System.currentTimeMillis()-startTime)+"ms|returnResult:"+res);
        return res;
    }*/

    /*
    *编辑司机对乘客备注
    */
    /*@RequestMapping("/editDriverNote")
    @ResponseBody
    private CollectDriverCommonRes editDriverNote(@RequestBody CollectDriverNoteReq noteReq){
        long startTime =  System.currentTimeMillis();
        logger.info("editDriverNote|getInfo:" + noteReq);

        if (!FeedStringUtil.isLong(noteReq.getDriverId()) || !FeedStringUtil.isLong(noteReq.getUserId()) ||
                !FeedStringUtil.isNullOrEmpty(noteReq.getNote())) {
            logger.error("editDriverNote|parameter error");
            return new CollectDriverCommonRes(Boolean.FALSE, CodeTypes.ParaError.getCode(), "parameter error");
        }

        CollectDriverCommonRes res = null;
        try {
            res = collectInfoService.editDriverNote(Long.valueOf(noteReq.getDriverId()),
                    Long.valueOf(noteReq.getUserId()), noteReq.getNote());
        } catch (Exception e) {
            logger.error("editDriverNote throw an error.",e);
            return new CollectDriverCommonRes(Boolean.FALSE, CodeTypes.SystemError.getCode(), "fail");
        }
        logger.info("editDriverNote|" + (System.currentTimeMillis()-startTime)+"ms|returnResult:"+res);
        return res;
    }*/

    /*
    *获取司机对乘客备注
    */
    /*@RequestMapping("/getDriverNote")
    @ResponseBody
    private GetCollectDriverNoteRes getDriverNote(@RequestBody CollectDriverNoteReq noteReq){
        long startTime =  System.currentTimeMillis();
        logger.info("getDriverNote|getInfo:" + noteReq);

        if (!FeedStringUtil.isLong(noteReq.getDriverId()) || !FeedStringUtil.isInteger(noteReq.getUserId())) {
            logger.error("getDriverNote|parameter error");
            return new GetCollectDriverNoteRes(Boolean.FALSE, CodeTypes.ParaError.getCode(), "parameter error", null);
        }

        GetCollectDriverNoteRes res = null;
        try {
            res = collectInfoService.getDriverNote(Long.valueOf(noteReq.getDriverId()),
                    Long.valueOf(noteReq.getUserId()));
        } catch (Exception e) {
            logger.error("getDriverNote throw an error.",e);
            return new GetCollectDriverNoteRes(Boolean.FALSE, CodeTypes.SystemError.getCode(), "fail", null);
        }
        logger.info("getDriverNote|" + (System.currentTimeMillis()-startTime)+"ms|returnResult:"+res);
        return res;
    }*/

    /*
   *获取用户的收藏司机列表
   */
   /* @RequestMapping("/getCollectListByUserId")
    @ResponseBody
    private GetCollectListRes getCollectListByUserId(@RequestBody GetCollectInfoReq getCollectUserReq){
        long startTime =  System.currentTimeMillis();
        logger.info("getCollectListByUserId|getInfo:" + getCollectUserReq);

        if (!FeedStringUtil.isLong(getCollectUserReq.getUserId())) {
            logger.error("getCollectListByUserId|parameter error");
            return new GetCollectListRes(Boolean.FALSE, CodeTypes.ParaError.getCode(), "parameter error", null);
        }

        GetCollectListRes res = null;
        try {
            res = collectInfoService.getCollectListByUserId(Long.valueOf(getCollectUserReq.getUserId()));
        } catch (Exception e) {
            logger.error("getCollectListByUserId|throw an error.|",e);
            return new GetCollectListRes(Boolean.FALSE, CodeTypes.SystemError.getCode(), "fail", null);
        }
        logger.info("getCollectListByUserId|" + (System.currentTimeMillis()-startTime)+"ms|returnResult:"+res);
        return res;
    }
*/

    /*
    *撤销收藏的司机
    */
    /*@RequestMapping("/cancel")
    @ResponseBody
    private CollectDriverCommonRes cancel(@RequestBody CollectDriverNoteReq noteReq){
        long startTime =  System.currentTimeMillis();
        logger.info("cancel|getInfo:" + noteReq);

        if (!FeedStringUtil.isLong(noteReq.getDriverId()) || !FeedStringUtil.isLong(noteReq.getUserId())) {
            logger.error("cancel|parameter error");
            return new CollectDriverCommonRes(Boolean.FALSE, CodeTypes.ParaError.getCode(), "parameter error");
        }

        CollectDriverCommonRes res = null;
        try {
            res = collectInfoService.cancelCollect(Long.valueOf(noteReq.getDriverId()),
                    Long.valueOf(noteReq.getUserId()));
        } catch (Exception e) {
            logger.error("cancel throw an error.",e);
            return new CollectDriverCommonRes(Boolean.FALSE, CodeTypes.SystemError.getCode(), "fail");
        }
        logger.info("cancel|" + (System.currentTimeMillis()-startTime)+"ms|returnResult:"+res);
        return res;
    }*/

    /*
     *添加收藏司机
     */
    /*@RequestMapping("/add")
    @ResponseBody
    private CollectDriverCommonRes add(@RequestBody AddCollectInfoReq addReq){
        long startTime =  System.currentTimeMillis();
        logger.info("add|getInfo:" + addReq);

        if (!FeedStringUtil.isLong(addReq.getDriverId()) || !FeedStringUtil.isLong(addReq.getUserId())) {
            logger.error("add|parameter error");
            return new CollectDriverCommonRes(Boolean.FALSE, CodeTypes.ParaError.getCode(), "parameter error");
        }

        CollectDriverCommonRes res = null;
        //构建创建的信息
        CollectDriverInfo addInfo = new CollectDriverInfo();
        addInfo.setUserId(Integer.valueOf(addReq.getUserId()));
        addInfo.setDriverId(Integer.valueOf(addReq.getDriverId()));
        long time = System.currentTimeMillis()/1000;
        addInfo.setCreateTime((int)time);
        addInfo.setUpdateTime((int)time);
        addInfo.setServiceTimes(1);
        addInfo.setStatus(1);
        addInfo.setDriverNote(addReq.getDriverNote() == null ? "" : addReq.getDriverNote());
        addInfo.setCity(addReq.getCity() == null ? "" : addReq.getCity());
        addInfo.setDriverCity(addReq.getDriverCity() == null ? "" : addReq.getDriverCity());
        try {

            res = collectInfoService.add(addInfo);
        } catch (Exception e) {
            logger.error("add throw an error.",e);
            return new CollectDriverCommonRes(Boolean.FALSE, CodeTypes.SystemError.getCode(), "fail");
        }
        logger.info("add|" + (System.currentTimeMillis()-startTime)+"ms|returnResult:"+res);
        return res;
    }*/


    /*
   *解析参数
   */
    private GetCollectInfoReq parseListPara(HttpServletRequest request)throws InvalidParameterException {
        if(null == request){
            logger.error("parseListPara|参数为空");
            throw new InvalidParameterException("参数不能为空");
        }
        GetCollectInfoReq domainReq = new GetCollectInfoReq();
        ParaUtil.setPagePara(request,domainReq);
        String userId = request.getParameter("userId");
        String driverId = request.getParameter("driverId");

        if(!StringUtils.isEmpty(userId)){
            if(!FeedStringUtil.isLong(userId)){
                throw new InvalidParameterException("用户ID格式必须为数字。");
            }else{
                domainReq.setUserId(Long.parseLong(userId));
            }
        }

        if(!StringUtils.isEmpty(driverId)){
            if(!FeedStringUtil.isLong(driverId)){
                throw new InvalidParameterException("司机ID格式必须为数字。");
            }else{
                domainReq.setDriverId(Long.parseLong(driverId));
            }
        }

        return domainReq;
    }

}
