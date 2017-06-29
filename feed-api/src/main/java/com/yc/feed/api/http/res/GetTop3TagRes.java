package com.yc.feed.api.http.res;

import com.yc.feed.domain.model.CommentTagResults;

import java.util.HashMap;
import java.util.List;

public class GetTop3TagRes extends CommonRes{

    private HashMap<String,String> resultData;

    public GetTop3TagRes() {
    }

    public GetTop3TagRes(boolean success, String code, String msg, HashMap<String,String> resultData) {
        super(success, code, msg);
        this.resultData = resultData;
    }

    public GetTop3TagRes(boolean success, String code, String msg) {
        super(success, code, msg);
    }

    public HashMap<String, String> getResultData() {
        return resultData;
    }

    public void setResultData(HashMap<String, String> resultData) {
        this.resultData = resultData;
    }
}
