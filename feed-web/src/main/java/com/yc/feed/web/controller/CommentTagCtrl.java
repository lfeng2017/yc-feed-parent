package com.yc.feed.web.controller;

import com.yc.feed.api.http.req.AddCommentTagReq;
import com.yc.feed.api.http.req.GetTagsReq;
import com.yc.feed.api.http.req.WebAddCommentTagReq;
import com.yc.feed.api.http.res.*;
import com.yc.feed.dao.mapper.OrderCommentTagMapper;
import com.yc.feed.domain.entity.CommentTag;
import com.yc.feed.domain.excep.FeedException;
import com.yc.feed.domain.model.CommentTagModel;
import com.yc.feed.api.util.FeedStringUtil;
import com.yc.feed.domain.enums.CodeTypes;
import com.yc.feed.domain.model.CommentTagResults;
import com.yc.feed.domain.model.CommentUserTagModel;
import com.yc.feed.domain.model.OrderCommentInfo;
import com.yc.feed.domain.req.GetCommentListReq;
import com.yc.feed.domain.req.UpdateNoteReq;
import com.yc.feed.domain.req.UpdateTagsReq;
import com.yc.feed.service.HistoryEvalRateService;
import com.yc.feed.service.TagService;
import com.yc.feed.service.UserTagService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yusong on 2016/10/27.
 * 用户评价标签控制器
 */
@Controller
@RequestMapping("/feed/tag")
public class CommentTagCtrl {

    private final Logger logger = Logger.getLogger(CommentTagCtrl.class);

    @Autowired
    private TagService tagService;
    @Autowired
    private UserTagService userTagService;

    /*
    *  排序最近50单的评价标签，并返回使用最频繁的三个标签
    */
    @RequestMapping("/getTop3Tags")
    @ResponseBody
    public GetTop3TagRes getTop3TagsByLastOrders(HttpServletRequest request) {
        long startTime = System.currentTimeMillis();

        if (StringUtils.isEmpty(request.getParameter("driverIds"))) {
            return new GetTop3TagRes(Boolean.FALSE, String.valueOf(CodeTypes.ParaError.getCode()), "司机ID不能为空", null);
        }else{
            logger.info("getTop3TagsByLastOrders|查询driverIds:" + request.getParameter("driverIds"));
            try {
                //tagService.runDriverTop3Tags(3,1);
                HashMap<String,String> top3TagsResults = tagService.getTop3Tags(request.getParameter("driverIds"));
                logger.info("getTop3TagsByLastOrders|成功响应:" + (System.currentTimeMillis() - startTime) + "ms");
                return new GetTop3TagRes(Boolean.TRUE, String.valueOf(CodeTypes.Success.getCode()), "查询成功", top3TagsResults);
            } catch (Exception e) {
                logger.error("getTop3TagsByLastOrders|程序异常|:" + e.getMessage());
            }
        }
        return new GetTop3TagRes(Boolean.FALSE, String.valueOf(CodeTypes.SystemError.getCode()), "查询异常", null);
    }



    /*
    *获取所有可用标签
    */
    @RequestMapping("/getAll")
    @ResponseBody
    public GetAllTagRes getAll() {
        long startTime = System.currentTimeMillis();
        try {
            List<CommentTagModel> tags = tagService.getAllCommentTag();
            long count = (tags == null ? 0 : tags.size());
            logger.info("getAll|查询有效标签数量:" + (System.currentTimeMillis() - startTime) + "ms|count" + count);
            return new GetAllTagRes(Boolean.TRUE, String.valueOf(CodeTypes.Success.getCode()), "查询成功", count, tags);
        } catch (Exception e) {
            logger.error("getAll|程序异常|:" + e.getMessage());
        }
        return new GetAllTagRes(Boolean.FALSE, String.valueOf(CodeTypes.SystemError.getCode()), "查询异常", 0, null);
    }

    /*
    *获取所有负面标签
    */
    @RequestMapping("/getAllNegative")
    @ResponseBody
    public GetAllTagRes getAllNegative() {
        long startTime = System.currentTimeMillis();
        try {
            List<CommentTagModel> tags = tagService.getAllNegativeCommentTag();
            long count = (tags == null ? 0 : tags.size());
            logger.info("getAllNegative|查询负面标签数量:" + (System.currentTimeMillis() - startTime) + "ms|count" + count);
            return new GetAllTagRes(Boolean.TRUE, String.valueOf(CodeTypes.Success.getCode()), "查询成功", count, tags);
        } catch (Exception e) {
            logger.error("getAllNegative|程序异常|:" + e.getMessage());
        }
        return new GetAllTagRes(Boolean.FALSE, String.valueOf(CodeTypes.SystemError.getCode()), "查询负面标签数量异常", 0, null);
    }

    /*
    *获取标签信息
    */
    @RequestMapping("/queryTag")
    @ResponseBody
    public GetAllTagRes queryTag(HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        try {
            String commentTagId = request.getParameter("commentTagId");
            logger.info("queryTag|获取标签信息|commentTagId:" + commentTagId);
            if (StringUtils.isEmpty(commentTagId)) {
                return new GetAllTagRes(Boolean.FALSE, String.valueOf(CodeTypes.ParaError.getCode()), "标签ID不能为空", 0, null);
            } else if (!FeedStringUtil.isInteger(commentTagId)) {
                return new GetAllTagRes(Boolean.FALSE, String.valueOf(CodeTypes.ParaError.getCode()), "标签ID格式必须为数字", 0, null);
            }
            Integer tagId = Integer.parseInt(commentTagId);
            CommentTagModel model = tagService.getTag(tagId);
            logger.info("queryTag|获取标签信息:" + (System.currentTimeMillis() - startTime) + "ms");
            if (model == null) {
                return new GetAllTagRes(Boolean.FALSE, String.valueOf(CodeTypes.NoRecord.getCode()), "未找到指定标签", 0, null);
            } else {
                List<CommentTagModel> res = new ArrayList<CommentTagModel>();
                res.add(model);
                return new GetAllTagRes(Boolean.FALSE, String.valueOf(CodeTypes.Success.getCode()), "查询成功", 1, res);
            }
        } catch (Exception e) {
            logger.error("queryTag|程序异常|:" + e.getMessage());
        }
        return new GetAllTagRes(Boolean.FALSE, String.valueOf(CodeTypes.SystemError.getCode()), "获取标签信息异常", 0, null);
    }


    /*
    *获取所有标签(comment_user_tags表)
    */
    @RequestMapping("/getAllCommentUserTags")
    @ResponseBody
    public GetAllUserTagRes getAllCommentUserTags() {
        try {
            List<CommentUserTagModel> tags = userTagService.getAllUserCommentTag();
            long count = (tags == null ? 0 : tags.size());
            logger.info("getAllCommentUserTags|查询所有comment_user_tags标签数量:" + count);
            return new GetAllUserTagRes(Boolean.TRUE, String.valueOf(CodeTypes.Success.getCode()), "查询成功", count, tags);
        } catch (Exception e) {
            logger.error("getAllCommentUserTags|程序异常|:" + e.getMessage());
            return new GetAllUserTagRes(Boolean.FALSE, String.valueOf(CodeTypes.SystemError.getCode()), "取所有标签异常", 0, null);
        }
    }

    /*
     *erp获取标签信息
     */
    @RequestMapping("/getCommentTags")
    @ResponseBody
    public GetAllTagRes getCommentTagsByQuery(HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        try {
            GetTagsReq req = null;
            try {
                req = parseListPara(request);
            } catch (InvalidParameterException e) {
                logger.info("getCommentTagsByQuery|参数错误|", e);
                return new GetAllTagRes(Boolean.FALSE, String.valueOf(CodeTypes.ParaError.getCode()), e.getMessage());
            }
            GetAllTagRes res = null;
            try{
                List<CommentTagModel> tags = tagService.getByQueryString(req);
                res = new GetAllTagRes(Boolean.TRUE, String.valueOf(CodeTypes.Success.getCode()), "success", tags);
                logger.info("getCommentTagsByQuery|" + (System.currentTimeMillis() - startTime) + "ms|returnResult:" + res);
                return res;
            }catch (FeedException e){
                logger.error("getTagsByQueryString|程序异常|:" + e.getMessage());
                res = new GetAllTagRes(Boolean.TRUE, String.valueOf(CodeTypes.Success.getCode()), "程序异常");
            }
            return res;
        } catch (Exception e) {
            logger.error("getTagsByQueryString|程序异常|:" + e.getMessage());
            return new GetAllTagRes(Boolean.FALSE, String.valueOf(CodeTypes.SystemError.getCode()), "程序异常");
        }
    }


    /*
    * 更新commentTag表字段
    */
    @RequestMapping("/updateCommentTag")
    @ResponseBody
    public CommonRes updateCommentTag(@RequestBody UpdateTagsReq updateTagsReq) {
        long startTime = System.currentTimeMillis();
        try {
            logger.info("updateCommentTag|req:" + updateTagsReq);
            try {
                parsePara(updateTagsReq);
            } catch (InvalidParameterException e) {
                logger.error("updateCommentTag|参数校验不通过|" + updateTagsReq, e);
                return new CommonRes(Boolean.FALSE, String.valueOf(CodeTypes.ParaError.getCode()), e.getMessage());
            }
            CommonRes res = null;
            try {
                tagService.update(updateTagsReq);
                res = new CommonRes(Boolean.TRUE, CodeTypes.Success.getCodeStr(), "更新数据成功");
            } catch (FeedException e) {
                logger.error("updateCommentTag|Error|" + e.getMessage());
                res = new CommonRes(Boolean.FALSE, String.valueOf(e.getCode()), e.getMessage());
            }
            logger.info("updateCommentTag|" + (System.currentTimeMillis() - startTime) + "ms|returnResult:" + res);
            return res;
        } catch (Exception e) {
            logger.error("updateCommentTag|程序异常|:" + e.getMessage());
            return new CommonRes(Boolean.FALSE, String.valueOf(CodeTypes.SystemError.getCode()), "更新commentTag异常");
        }

    }

    /*
    *更改备注
    */
    @RequestMapping("/addCommentTag")
    @ResponseBody
    public CommonRes addCommentTag(@RequestBody WebAddCommentTagReq insertReq) {
        long startTime = System.currentTimeMillis();
        try {
            logger.info("addCommentTag|req:" + insertReq);
            AddCommentTagReq req = new AddCommentTagReq();
            try {
                req = parseInsertPara(insertReq);
            } catch (InvalidParameterException e) {
                logger.info("addCommentTag|参数错误|", e);
                return new GetAllTagRes(Boolean.FALSE, String.valueOf(CodeTypes.ParaError.getCode()), e.getMessage());
            }
            CommonRes res = null;
            try {
                tagService.insertTag(req);
                res = new CommonRes(Boolean.TRUE, CodeTypes.Success.getCodeStr(), "新增记录成功");
            } catch (FeedException e) {
                logger.error("addCommentTag|Error|" + e.getMessage());
                res = new CommonRes(Boolean.FALSE, String.valueOf(e.getCode()), "新增数据错误");
            }
            logger.info("addCommentTag|" + (System.currentTimeMillis() - startTime) + "ms|returnResult:" + res);
            return res;
        } catch (Exception e) {
            logger.error("addCommentTag|程序异常|:" + e.getMessage());
        }
        return new CommonRes(Boolean.FALSE, String.valueOf(CodeTypes.SystemError.getCode()), "插入标签程序异常");
    }

    private void parsePara(UpdateTagsReq req) throws InvalidParameterException {

        String commentTagId = req.getCommentTagId();
        String status = req.getStatus();
        String conflictTagId = req.getConflictTagId();
        String score = req.getScore();
        String rank = req.getRank();
        String flag = req.getFlag();

        boolean havaNoparaFlg = true;
        if (commentTagId != null) {
            havaNoparaFlg = false;
            if (!FeedStringUtil.isInteger(commentTagId)) {
                throw new InvalidParameterException("commentTagId必须为数字");
            }
        }else{
            throw new InvalidParameterException("commentTagId不能为空");
        }

        if (conflictTagId != null) {
            havaNoparaFlg = false;
            if (!FeedStringUtil.isInteger(conflictTagId)) {
                throw new InvalidParameterException("conflictTagId必须为数字");
            }
        }

        if (status != null) {
            havaNoparaFlg = false;
            if (!FeedStringUtil.isInteger(status)) {
                throw new InvalidParameterException("status必须为数字");
            }
        }

        if (score != null) {
            havaNoparaFlg = false;
            if (!FeedStringUtil.isInteger(score)) {
                throw new InvalidParameterException("score必须为数字");
            }
        }

        if (rank != null) {
            havaNoparaFlg = false;
            if (!FeedStringUtil.isInteger(rank)) {
                throw new InvalidParameterException("rank");
            }
        }

        if (flag != null) {
            havaNoparaFlg = false;
            if (!FeedStringUtil.isInteger(flag)) {
                throw new InvalidParameterException("flag");
            }
        }

        if (havaNoparaFlg) {
            throw new InvalidParameterException("请求参数不能为空");
        }
    }

    private AddCommentTagReq parseInsertPara(WebAddCommentTagReq insertReq) throws InvalidParameterException {
        AddCommentTagReq req = new AddCommentTagReq();

        if (insertReq.getConflictTagId() == null || !FeedStringUtil.isInteger(insertReq.getConflictTagId())) {
            throw new InvalidParameterException("conflictTagId格式必须为数字");
        } else {
            req.setConflictTagId(Integer.valueOf(insertReq.getConflictTagId()));
        }
        if (insertReq.getTagText() == null) {
            throw new InvalidParameterException("tagText不能为空");
        } else {
            req.setTagText(insertReq.getTagText());
        }
        if (insertReq.getScore() == null || !FeedStringUtil.isInteger(insertReq.getScore())) {
            throw new InvalidParameterException("score格式必须为数字");
        } else {
            req.setScore(Integer.valueOf(insertReq.getScore()));
        }
        if (insertReq.getType() == null || !FeedStringUtil.isInteger(insertReq.getType())) {
            throw new InvalidParameterException("type格式必须为数字");
        } else {
            req.setType(Integer.valueOf(insertReq.getType()));
        }
        if (insertReq.getStatus() == null || !FeedStringUtil.isInteger(insertReq.getStatus())) {
            throw new InvalidParameterException("status格式必须为数字");
        } else {
            req.setStatus(Integer.valueOf(insertReq.getStatus()));
        }
        if (insertReq.getRank() == null || !FeedStringUtil.isInteger(insertReq.getRank())) {
            throw new InvalidParameterException("rank格式必须为数字");
        } else {
            req.setRank(Integer.valueOf(insertReq.getRank()));
        }
        if (insertReq.getOperatorId() == null || !FeedStringUtil.isInteger(insertReq.getOperatorId())) {
            throw new InvalidParameterException("operatorId格式必须为数字");
        } else {
            req.setOperatorId(Integer.valueOf(insertReq.getOperatorId()));
        }
        if (insertReq.getFlag() == null || !FeedStringUtil.isInteger(insertReq.getFlag())) {
            throw new InvalidParameterException("flg格式必须为数字");
        } else {
            req.setFlag(Integer.valueOf(insertReq.getFlag()));
        }
        return req;
    }

    private GetTagsReq parseListPara(HttpServletRequest request) throws InvalidParameterException {
        GetTagsReq req = new GetTagsReq();

        String type = request.getParameter("type");
        String status = request.getParameter("status");
        String conflictTagIds = request.getParameter("conflictTagIds");
        String rank = request.getParameter("rank");
        String tagText = request.getParameter("tagText");

        boolean hasNoParaFlag = true;
        if (type != null) {
            hasNoParaFlag = false;
            if (!FeedStringUtil.isInteger(type)) {
                throw new InvalidParameterException("type格式必须为数字。");
            } else {
                req.setType(Integer.parseInt(type));
            }
        }

        if (status != null) {
            hasNoParaFlag = false;
            String[] temStatus = status.split(",");
            for (String str : temStatus) {
                if (!FeedStringUtil.isInteger(str)) {
                    throw new InvalidParameterException("status格式错误。");
                }
            }
            req.setStatus(temStatus);
        }

        if (conflictTagIds != null) {
            hasNoParaFlag = false;
            String[] ids = conflictTagIds.split(",");
            for (String str : ids) {
                if (!FeedStringUtil.isInteger(str)) {
                    throw new InvalidParameterException("conflictTagIds格式错误。");
                }
            }
            req.setConflictTagIds(ids);
        }

        if (rank != null) {
            hasNoParaFlag = false;
            if (!FeedStringUtil.isInteger(rank)) {
                throw new InvalidParameterException("rank格式必须为数字。");
            } else {
                req.setRank(Integer.parseInt(rank));
            }
        }

        if (tagText != null) {
            hasNoParaFlag = false;
            /*try {
                tagText = new String(request.getParameter("tagText").getBytes("iso8859-1"),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/
            req.setTagText("%" +tagText+"%");
        }

        if (hasNoParaFlag) {
            throw new InvalidParameterException("请求参数不能为空");
        }
        return req;
    }

}
