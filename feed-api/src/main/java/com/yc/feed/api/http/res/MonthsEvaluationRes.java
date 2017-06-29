package com.yc.feed.api.http.res;

import java.util.List;

import com.yc.feed.domain.model.MonthsEvalutionInfo;

/**
 * Created by ke on 16-12-5.
 * 星火项目自然月的好评率和差评次数
 */
public class MonthsEvaluationRes extends  CommonRes{

	private List<MonthsEvalutionInfo> list;

	public MonthsEvaluationRes(boolean success, String code, String msg) {
        super(success, code, msg);
    }

    public MonthsEvaluationRes(boolean success, String code, String msg, List list) {
        super(success, code, msg);
        this.list = list;
    }

    public List<MonthsEvalutionInfo> getList() {
		return list;
	}

	public void setList(List<MonthsEvalutionInfo> list) {
		this.list = list;
	}


}
