/*
 * Copyright (c) 2016 Skyon Technology Ltd.
 * All rights reserved.
 *
 * project: pbocAnalysis
 * create: 2016-11-16 下午03:08:46
 * cvs: $Id: $
 */
package com.caafc.pbocAnalysis.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.caafc.pbocAnalysis.dto.RePlmessageheader;
import com.caafc.pbocAnalysis.dto.RePlrecorddetail;
import com.caafc.pbocAnalysis.vo.CardDetailVo;
import com.caafc.pbocAnalysis.vo.LoanDetailVo;
import com.caafc.pbocAnalysis.vo.PlguaranteeinfoVo;
import com.caafc.pbocAnalysis.vo.RePlloanVo;
import com.caafc.pbocAnalysis.vo.RePlloancardVo;

/**
 * TODO 请填写注释.
 * @author ronin 
 * @version $Revision:$
 */
public interface Pbocmanager {
	
	/**
	 * 计算信用卡产品的最近6个月的平均额度使用率
	 * @param reportNo
	 * @return 
	 * @throws Exception 
	 */
    public String getCCardAvgLimitRate6M(List<RePlloancardVo> cardList,List<RePlloanVo> loanList)throws Exception;

    /**
     * 计算最近6个月内信贷产品的最大逾期期数
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getCreditLoanMaxOverdue6M(List<LoanDetailVo> loanDetailList, List<CardDetailVo> cardDetailList, RePlmessageheader header)throws Exception;
    
    /**
     * 计算最近12个月内信贷产品的最大逾期期数
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getCreditLoanMaxOverdue12M(List<LoanDetailVo> loanDetailList, List<CardDetailVo> cardDetailList, RePlmessageheader header)throws Exception;
    
    /**
     * 计算最近24个月信贷产品的最大逾期期数
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getCreditLoanMaxOverdue24M(List<LoanDetailVo> loanDetailList, List<CardDetailVo> cardDetailList, RePlmessageheader header)throws Exception;
    
    /**
     * 计算所有信贷产品最大账龄
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getCreditLoanMaxZL(List<LoanDetailVo> loanDetailList, List<CardDetailVo> cardDetailList, RePlmessageheader header)throws Exception;

    /**
     * 计算最近6个月内信贷产品累计逾期次数
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getCreditLoanOverdueNum6M(List<LoanDetailVo> loanDetailList, List<CardDetailVo> cardDetailList, RePlmessageheader header)throws Exception;
    
    /**
     * 计算最近12个月内信贷产品累计逾期次数
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getCreditLoanOverdueNum12M(List<LoanDetailVo> loanDetailList, List<CardDetailVo> cardDetailList, RePlmessageheader header)throws Exception;

    /**
     * 计算最近24个月内信贷产品累计逾期次数
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getCreditLoanOverdueNum24M(List<LoanDetailVo> loanDetailList, List<CardDetailVo> cardDetailList, RePlmessageheader header)throws Exception;

    /**
     * 计算最近3个月信贷审批查询次数
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getCreditLoanQueryNum3M(List<RePlrecorddetail> recordList, RePlmessageheader header)throws Exception;

    /**
     * 计算最近6个月信贷审批查询次数
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getCreditLoanQueryNum6M(List<RePlrecorddetail> recordList, RePlmessageheader header)throws Exception;

    /**
     * 计算最近12个月信贷审批查询次数
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getCreditLoanQueryNum12M(List<RePlrecorddetail> recordList, RePlmessageheader header)throws Exception;

    /**
     * 计算申请人（征信报告）是否有征信
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getPbocReportFlag(List<LoanDetailVo> loanDetailList, List<CardDetailVo> cardDetailList)throws Exception;

    /**
     * 计算申请人（征信报告）贷款当前逾期期数
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getLoanOverdueNum(List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList)throws Exception;

    /**
     * 计算申请人（征信报告）贷款状态
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getLoanStatus(List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList)throws Exception;

    /**
     * 计算申请人（征信报告）信用卡当期逾期期数
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getCCardOverdueNum(List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList)throws Exception;

    /**
     * 计算申请人（征信报告）信用卡状态
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getCCardStatus(List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList)throws Exception;

    /**
     * 计算申请人（征信报告）近24个月贷款最高逾期期数
     * @param reportNo
     * @return在、
     * @throws Exception
     */
    public String getLoanMaxOverdue24M(List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList,RePlmessageheader header)throws Exception;

    /**
     * 计算申请人（征信报告）近24个月贷款累计逾期期数
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getLoanSumOverdue24M(List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList, RePlmessageheader header)throws Exception;

    /**
     * 计算申请人（征信报告）近24个月信用卡最高逾期期数
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getCCardMaxOverdue24M(List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList, RePlmessageheader header)throws Exception;

    /**
     * 计算申请人（征信报告）近24个月信用卡累计逾期期数
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getCCardSumOverdue24M(List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList, RePlmessageheader header)throws Exception;

    /**
     * 计算申请人（征信报告）额度使用率超过80%的信用卡的张数
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getCCardOut80Rate(List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList)throws Exception;

    /**
     * 计算申请人（征信报告）呆账信息汇总笔数
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getBadAccountNum(JSONObject pbocReport)throws Exception;

    /**
     * 计算申请人（征信报告）资产处置信息汇总笔数
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getAssetDisposals(List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList,JSONObject pbocReport)throws Exception;

    /**
     * 计算申请人（征信报告）是否存在强制执行记录
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getCourtExecutions(JSONObject pbocReport)throws Exception;

    /**
     * 计算申请人（征信报告）是否存在行政处罚记录
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getApRecordFlag(JSONObject pbocReport)throws Exception;

    /**
     * 计算准贷记卡
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getSemiCreditCard(List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList,JSONObject pbocReport)throws Exception;

    /**
     * 计算贷款历史逾期比例
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getLoanHisOverdueRate(List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList, RePlmessageheader header)throws Exception;

    /**
     * 计算单张信用卡历史逾期比例
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getOneCcardHisOverdueRate(List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList, RePlmessageheader header)throws Exception;
    
    /**
     * 计算多张信用卡历史逾期比例
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getManyCcardHisOverdueRate(List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList, RePlmessageheader header)throws Exception;
    
    /**
     * 计算申请人（征信报告）贷款月负债
     * @param reportNo
     * @return
     * @throws Exception
     */
    public String getLoanMonthLiabilities(List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList)throws Exception;
	
    /**
     * 信用卡产品最大账龄
     * @param pbocReport
     * @return
     * @throws Exception
     */
    public String getRESERVED_1(List<CardDetailVo> cardDetailList,List<LoanDetailVo> loanDetailList, RePlmessageheader header) throws Exception;
    
    
    /**
     * 住房贷款笔数
     * @param pbocReport
     * @return
     * @throws Exception
     */
    public String getRESERVED_2(List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList) throws Exception;
    
    
    /**
     * 信用卡产品的最近6个月的平均额度使用率（授信概要）
     * @param pbocReport
     * @return
     * @throws Exception
     */
    public String getRESERVED_3(JSONObject pbocReport,List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList) throws Exception;
    
    /**
     * 最近24个月内准贷记卡产品的最大逾期期数（不考虑呆账）
     * @return
     * @throws Exception
     */
    public String getPO_SEMICREDITCARDNO(List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList,List<CardDetailVo> ZcardDetailList, RePlmessageheader header) throws Exception;
    
    /**
     * 最近24个月内贷款产品的最大逾期期数（不考虑呆账）
     * @param loanDetailList
     * @param header
     * @return
     * @throws Exception
     */
    public String getPO_LOANTOPDUENUM24NO(List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList, RePlmessageheader header) throws Exception;
    
    /**
     * 最近24个月内单个贷款产品的累计逾期期数（不考虑呆账）
     * @param loanDetailList
     * @param header
     * @return
     * @throws Exception
     */
    public String getPO_LOANSUMDUENUM24NO(List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList, RePlmessageheader header) throws Exception;
    
    /**
     * 最近24个月内单张贷记卡产品的最大逾期期数（不考虑呆账）
     * @param DcardDetailList
     * @param header
     * @return
     * @throws Exception
     */
    public String getPO_DEBITCARDTOP24DUENUMNO(List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList,List<CardDetailVo> DcardDetailList,RePlmessageheader header) throws Exception;
    
    /**
     * 最近24个月内单张贷记卡产品的累计逾期期数（不考虑呆账）
     * @param DcardDetailList
     * @param header
     * @return
     * @throws Exception
     */
    public String getPO_DEBITCARDSUM24DUENUMNO(List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList,List<CardDetailVo> DcardDetailList,RePlmessageheader header) throws Exception;
    
    /**
     * 担保贷款五级分类
     * @param pbocReport
     * @return
     * @throws Exception
     */
    public String getPO_ASSUREFIVE(List<PlguaranteeinfoVo> guaranteeList) throws Exception;
    
    /**
     * 申请人（征信报告）呆账信息汇总笔数
     * @param pbocReport
     * @return
     * @throws Exception
     */
    public String getPO_BADDEBTSNUM(JSONObject pbocReport,List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList) throws Exception;
    
    /**
     * 申请人（征信报告）信用卡当期逾期期数
     * @param DcardDetailList
     * @return
     * @throws Exception
     */
    public String getPO_CREDITCARDCURDUED(List<LoanDetailVo> loanDetailList,List<CardDetailVo> cardDetailList,List<CardDetailVo> DcardDetailList) throws Exception;
    
}
