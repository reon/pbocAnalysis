/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package com.caafc.pbocAnalysis.webService.pbocService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caafc.pbocAnalysis.common.Constant;
import com.caafc.pbocAnalysis.dao.jdbc.DBQuery;
import com.caafc.pbocAnalysis.dto.RePlawardcreditinfo;
import com.caafc.pbocAnalysis.dto.RePlcontractinfo;
import com.caafc.pbocAnalysis.dto.RePlcurraccountinfo;
import com.caafc.pbocAnalysis.dto.RePlcurroverdue;
import com.caafc.pbocAnalysis.dto.RePlguarantee;
import com.caafc.pbocAnalysis.dto.RePlguaranteeinfo;
import com.caafc.pbocAnalysis.dto.RePlidentity;
import com.caafc.pbocAnalysis.dto.RePllatest24monthpaymentstate;
import com.caafc.pbocAnalysis.dto.RePlmessageheader;
import com.caafc.pbocAnalysis.dto.RePloverduerecord;
import com.caafc.pbocAnalysis.dto.RePlqueryreq;
import com.caafc.pbocAnalysis.dto.RePlrecorddetail;
import com.caafc.pbocAnalysis.dto.RePlrepayinfo;
import com.caafc.pbocAnalysis.service.Pbocmanager;
import com.caafc.pbocAnalysis.util.AnalysisJsonUtil;
import com.caafc.pbocAnalysis.util.HttpClientUtil;
import com.caafc.pbocAnalysis.util.ParameterUtil;
import com.caafc.pbocAnalysis.util.Parameters;
import com.caafc.pbocAnalysis.vo.CardDetailVo;
import com.caafc.pbocAnalysis.vo.LoanDetailVo;
import com.caafc.pbocAnalysis.vo.PlguaranteeinfoVo;
import com.caafc.pbocAnalysis.vo.RePlloanVo;
import com.caafc.pbocAnalysis.vo.RePlloancardVo;
import com.skyon.core.util.StringUtil;

/**
 * This class was generated by Apache CXF 2.5.2 2016-11-16T15:06:09.558+08:00 Generated source version: 2.5.2
 * 
 */

@javax.jws.WebService(serviceName = "PbocService", portName = "PbocServicePort", targetNamespace = "http://www.caafc.com/pbocAnalysis",
// wsdlLocation = "PbocService.wsdl",
endpointInterface = "com.caafc.pbocAnalysis.webService.pbocService.PbocService")
public class PbocServiceImpl implements PbocService {

	private static final Logger LOG = Logger.getLogger(PbocServiceImpl.class.getName());

	private Pbocmanager pbocmanager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.caafc.pbocAnalysis.webService.PbocService#pbocTarget(com.caafc.pbocAnalysis.webService.PbocRequest
	 * pbocRequest )*
	 */
	public com.caafc.pbocAnalysis.webService.pbocService.PbocResponse pbocTarget(PbocRequest pbocRequest) {
		PbocResponse pbocResponse = new PbocResponse();
		/**
		 * 步骤1：调用人行征信解析程序的webservice接口，解析征信报告并入库
		 */
		String url = ParameterUtil.getParameter(Parameters.PBOC_URL);
		String filePath = pbocRequest.getPbocFilePath();
		String outFilePath = ParameterUtil.getParameter(Parameters.HTML_PATH);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		int date = Integer.valueOf(dateFormat.format(calendar.getTime()));
		
		String saveFilePath = outFilePath + "/" + date;
		
		if (StringUtil.isEmpty(filePath)) {
			pbocResponse.setResultCode("99999");
			pbocResponse.setResultMessage("待解析人行报告文件路径为空");
			return pbocResponse;
		}
		if (StringUtil.isEmpty(saveFilePath)) {
			pbocResponse.setResultCode("99999");
			pbocResponse.setResultMessage("解析后人行报告文件存放路径为空");
			return pbocResponse;
		}

		LOG.info("path=" + filePath + "&savepath=" + saveFilePath + "");

		String params = "type=single&path=" + filePath + "&savepath=" + saveFilePath + "";
		String message = "";
		String reportNo = "";
		String responseCode = "";
		JSONObject pbocReport = new JSONObject();
		try {
			String pbocAnalysis = HttpClientUtil.getInstance().sendHttpPost(url, params);
//			String pbocAnalysis = PbocAnalysisDoPost.post(url, params);
			if(StringUtils.isBlank(pbocAnalysis)){
				pbocResponse.setResultCode("E0002");
				pbocResponse.setResultMessage("征信解析系统异常,请查看征信解析系统日志");
				return pbocResponse;
			}
			System.out.println(pbocAnalysis);
			JSONObject obj = JSON.parseObject(pbocAnalysis);
			message = obj.getString("message");
			reportNo = obj.getString("reportNo");
			responseCode = obj.getString("responseCode");
			
			//解析错误
			if (responseCode.equals("99999999")) {
				pbocResponse.setResultCode("E0002");
				pbocResponse.setResultMessage("解析人行征信报告文件错误: " + message + "");
				LOG.error(pbocAnalysis);
				return pbocResponse;
			} else if (responseCode.equals("99999998")) {//查询信息有误，无征信报告
				pbocResponse.setResultCode("00000");
				pbocResponse.setResultMessage("无征信报告");
				//赋值为无征信报告
				initPbocResponse(pbocResponse);
				return pbocResponse;
			}
			
			pbocReport = obj.getJSONObject("pbocReport");
			reportNo = pbocReport.getString("reportNo");
			pbocResponse.setReportNo(reportNo);
			
		} catch (Exception e) {
			e.printStackTrace();
			pbocResponse.setResultCode("E0002");
			pbocResponse.setResultMessage(e.getMessage());
			return pbocResponse;
		}

		/**
		 * 步骤2：衍生变量数据加工逻辑处理
		 */
		try {
			pbocResponseProcess(pbocResponse, pbocReport);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e);
			pbocResponse.setResultCode("E0003");
			pbocResponse.setResultMessage("衍生变量加工处理时发送错误");
			return pbocResponse;
		}

		/**
		 * 步骤3 存储数据
		 */
		try {
			pbocResponse.setResultCode("00000");
			pbocResponse.setResultMessage("解析成功");
			saveResult(pbocResponse);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e);
			pbocResponse.setResultCode("E0001");
			pbocResponse.setResultMessage("数据库操作失败");
			return pbocResponse;
		}
		return pbocResponse;
	}
	
	/**
	 * 衍生变量加工处理
	 * @param pbocResponse
	 * @param reportNo
	 * @throws Exception
	 */
	private void pbocResponseProcess(PbocResponse pbocResponse, JSONObject pbocReport) throws Exception{
		
		// 获取贷款
		List<RePlloanVo> loanList = AnalysisJsonUtil.getRePlloan(pbocReport);
		// 获取信用卡
		List<RePlloancardVo> cardList = AnalysisJsonUtil.getCreditCard(pbocReport);
		
		//获取贷记卡
		List<RePlloancardVo> DcardList = AnalysisJsonUtil.getRePlloancard(pbocReport);
		
		//获取准贷记卡
		List<RePlloancardVo> ZcardList = AnalysisJsonUtil.getRePlstandardloancard(pbocReport);
		
		//获取征信报告表头信息
		RePlmessageheader header = AnalysisJsonUtil.getRePlmessageheader(pbocReport);
		//获取信贷审批查询信息
		List<RePlrecorddetail> recordList = AnalysisJsonUtil.getRePlrecorddetail(pbocReport);
		
		//获取贷款对外担保信息
		List<RePlguaranteeinfo> Plguaranteeinfo = AnalysisJsonUtil.getRePlguaranteeinfo(pbocReport);
		
		//贷款
		List<LoanDetailVo> loanDetailList = loanDetailProcess(loanList);
		//信用卡
		List<CardDetailVo> cardDetailList = cardDetailProcess(cardList);
		//贷记卡
		List<CardDetailVo> DcardDetailList = cardDetailProcess(DcardList);
		//准贷记卡
		List<CardDetailVo> ZcardDetailList = cardDetailProcess(ZcardList);
		
		//五级分类
		List<PlguaranteeinfoVo> guaranteeList = guaranteeProcess(Plguaranteeinfo);
		
		
		//根据报告编号获得查询人姓名、证据号码
		RePlqueryreq req = AnalysisJsonUtil.getRePlqueryreq(pbocReport);
		pbocResponse.setCustName(req.getName());
		pbocResponse.setIdNumber(req.getCertno());
		
		//获取身份信息
		RePlidentity identity = AnalysisJsonUtil.getRePlidentity(pbocReport);
		if(identity != null){
			String mobile = identity.getMobile();
			if(StringUtils.isNotEmpty(mobile) && !"--".equals(mobile)){
				pbocResponse.setMobile(mobile);
			}else{
				pbocResponse.setMobile(Constant.NO);
			}
		}else{
			pbocResponse.setMobile(Constant.NO);
		}

		//计算信用卡产品的最近6个月的平均额度使用率
		String cCardAvgLimitRate6M = pbocmanager.getCCardAvgLimitRate6M(cardList);
		pbocResponse.setCCardAvgLimitRate6M(cCardAvgLimitRate6M);
		
	    //计算最近6个月内信贷产品的最大逾期期数
		String creditLoanMaxOverdue6M = pbocmanager.getCreditLoanMaxOverdue6M(loanDetailList, cardDetailList, header);
		pbocResponse.setCreditLoanMaxOverdue6M(creditLoanMaxOverdue6M);
	    
	    //计算最近12个月内信贷产品的最大逾期期数
		String creditLoanMaxOverdue12M = pbocmanager.getCreditLoanMaxOverdue12M(loanDetailList, cardDetailList, header);
	    pbocResponse.setCreditLoanMaxOverdue12M(creditLoanMaxOverdue12M);
	    
	    //计算最近24个月内信贷产品的最大逾期期数
	    String creditLoanMaxOverdue24M = pbocmanager.getCreditLoanMaxOverdue24M(loanDetailList, cardDetailList, header);
	    pbocResponse.setCreditLoanMaxOverdue24M(creditLoanMaxOverdue24M);
	    
	    //计算所有信贷产品最大账龄
	    String reditLoanMaxZL = pbocmanager.getCreditLoanMaxZL(loanDetailList, cardDetailList, header);
	    pbocResponse.setCreditLoanMaxZL(reditLoanMaxZL);

	    //计算最近6个月内信贷产品累计逾期次数
	    String creditLoanOverdueNum6M = pbocmanager.getCreditLoanOverdueNum6M(loanDetailList, cardDetailList, header);
	    pbocResponse.setCreditLoanOverdueNum6M(creditLoanOverdueNum6M);
	    
	    //计算最近12个月内信贷产品累计逾期次数
	    String creditLoanOverdueNum12M = pbocmanager.getCreditLoanOverdueNum12M(loanDetailList, cardDetailList, header);
	    pbocResponse.setCreditLoanOverdueNum12M(creditLoanOverdueNum12M);

	    //计算最近24个月内信贷产品累计逾期次数
	    String creditLoanOverdueNum24M = pbocmanager.getCreditLoanOverdueNum24M(loanDetailList, cardDetailList, header);
	    pbocResponse.setCreditLoanOverdueNum24M(creditLoanOverdueNum24M);

	    //计算最近3个月信贷审批查询次数
	    String creditLoanQueryNum3M = pbocmanager.getCreditLoanQueryNum3M(recordList, header);
	    pbocResponse.setCreditLoanQueryNum3M(creditLoanQueryNum3M);

	    //计算最近6个月信贷审批查询次数
	    String creditLoanQueryNum6M = pbocmanager.getCreditLoanQueryNum6M(recordList, header);
	    pbocResponse.setCreditLoanQueryNum6M(creditLoanQueryNum6M);

	    //计算最近12个月信贷审批查询次数
	    String creditLoanQueryNum12M = pbocmanager.getCreditLoanQueryNum12M(recordList, header);
	    pbocResponse.setCreditLoanQueryNum12M(creditLoanQueryNum12M);

	    //计算申请人（征信报告）是否有征信
	    String pbocReportFlag = pbocmanager.getPbocReportFlag(loanDetailList, cardDetailList);
	    pbocResponse.setPbocReportFlag(pbocReportFlag);

	    //计算申请人（征信报告）贷款当前逾期期数
	    String loanOverdueNum = pbocmanager.getLoanOverdueNum(loanDetailList);
	    pbocResponse.setLoanOverdueNum(loanOverdueNum);

	    //计算申请人（征信报告）贷款状态
	    String loanStatus = pbocmanager.getLoanStatus(loanDetailList);
	    pbocResponse.setLoanStatus(loanStatus);

	    //计算申请人（征信报告）信用卡当期逾期期数
	    String cCardOverdueNum = pbocmanager.getCCardOverdueNum(cardDetailList);
	    pbocResponse.setCCardOverdueNum(cCardOverdueNum);

	    //计算申请人（征信报告）信用卡状态
	    String cCardStatus = pbocmanager.getCCardStatus(cardDetailList);
	    pbocResponse.setCCardStatus(cCardStatus);

	    //计算申请人（征信报告）近24个月贷款最高逾期期数
	    String loanMaxOverdue24M = pbocmanager.getLoanMaxOverdue24M(loanDetailList, header);
	    pbocResponse.setLoanMaxOverdue24M(loanMaxOverdue24M);

	    //计算申请人（征信报告）近24个月贷款累计逾期期数
	    String loanSumOverdue24M = pbocmanager.getLoanSumOverdue24M(loanDetailList, header);
	    pbocResponse.setLoanSumOverdue24M(loanSumOverdue24M);

	    //计算申请人（征信报告）近24个月信用卡最高逾期期数
	    String cCardMaxOverdue24M = pbocmanager.getCCardMaxOverdue24M(cardDetailList, header);
	    pbocResponse.setCCardMaxOverdue24M(cCardMaxOverdue24M);

	    //计算申请人（征信报告）近24个月信用卡累计逾期期数
	    String cCardSumOverdue24M = pbocmanager.getCCardSumOverdue24M(cardDetailList, header);
	    pbocResponse.setCCardSumOverdue24M(cCardSumOverdue24M);

	    //计算申请人（征信报告）额度使用率超过80%的信用卡的张数
	    String cCardOut80Rate = pbocmanager.getCCardOut80Rate(cardDetailList);
	    pbocResponse.setCCardOut80Rate(cCardOut80Rate);

	    //计算申请人（征信报告）呆账信息汇总笔数
	    String badAccountNum = pbocmanager.getBadAccountNum(pbocReport);
	    pbocResponse.setBadAccountNum(badAccountNum);

	    //计算申请人（征信报告）资产处置信息汇总笔数
	    String assetDisposals = pbocmanager.getAssetDisposals(pbocReport);
	    pbocResponse.setAssetDisposals(assetDisposals);

	    //计算申请人（征信报告）是否存在强制执行记录
	    String courtExecutions = pbocmanager.getCourtExecutions(pbocReport);
	    pbocResponse.setCourtExecutions(courtExecutions);

	    //计算申请人（征信报告）是否存在行政处罚记录
	    String apRecordFlag = pbocmanager.getApRecordFlag(pbocReport);
	    pbocResponse.setApRecordFlag(apRecordFlag);

	    //计算准贷记卡
	    String semiCreditCard = pbocmanager.getSemiCreditCard(pbocReport);
	    pbocResponse.setSemiCreditCard(semiCreditCard);

	    //计算贷款历史逾期比例
	    String loanHisOverdueRate = pbocmanager.getLoanHisOverdueRate(loanDetailList, header);
	    pbocResponse.setLoanHisOverdueRate(loanHisOverdueRate);

	    //计算单张信用卡历史逾期比例
	    String oneCcardHisOverdueRate = pbocmanager.getOneCcardHisOverdueRate(cardDetailList, header);
	    pbocResponse.setOneCcardHisOverdueRate(oneCcardHisOverdueRate);

	    //计算多张信用卡历史逾期比例
	    String manyCcardHisOverdueRate = pbocmanager.getManyCcardHisOverdueRate(cardDetailList, header);
	    pbocResponse.setManyCcardHisOverdueRate(manyCcardHisOverdueRate);

	    //计算申请人（征信报告）贷款月负债
	    String loanMonthLiabilities = pbocmanager.getLoanMonthLiabilities(loanDetailList);
	    pbocResponse.setLoanMonthLiabilities(loanMonthLiabilities);
	    
	    //信用卡产品最大账龄
		String RESERVED_1 = pbocmanager.getRESERVED_1(cardDetailList, header);
		pbocResponse.setRESERVED_1(RESERVED_1);
		
		//住房贷款笔数
		String RESERVED_2 = pbocmanager.getRESERVED_2(loanDetailList);
		pbocResponse.setRESERVED_2(RESERVED_2);
		
		//信用卡产品的最近6个月的平均额度使用率（授信概要）
		String RESERVED_3 = pbocmanager.getRESERVED_3(pbocReport);
		pbocResponse.setRESERVED_3(RESERVED_3);
		
		//最近24个月内准贷记卡产品的最大逾期期数（不考虑呆账）
		String PO_SEMICREDITCARDNO = pbocmanager.getPO_SEMICREDITCARDNO(ZcardDetailList, header);
		pbocResponse.setPO_SEMICREDITCARDNO(PO_SEMICREDITCARDNO);
		
		//最近24个月内贷款产品的最大逾期期数（不考虑呆账）
		String PO_LOANTOPDUENUM24NO = pbocmanager.getPO_LOANTOPDUENUM24NO(loanDetailList, header);
		pbocResponse.setPO_LOANTOPDUENUM24NO(PO_LOANTOPDUENUM24NO);
		
		//最近24个月内单个贷款产品的累计逾期期数（不考虑呆账）
		String PO_LOANSUMDUENUM24NO = pbocmanager.getPO_LOANSUMDUENUM24NO(loanDetailList, header);
		pbocResponse.setPO_LOANSUMDUENUM24NO(PO_LOANSUMDUENUM24NO);
		
		//最近24个月内单张贷记卡产品的最大逾期期数（不考虑呆账）
		String PO_DEBITCARDTOP24DUENUMNO = pbocmanager.getPO_DEBITCARDTOP24DUENUMNO(DcardDetailList, header);
		pbocResponse.setPO_DEBITCARDTOP24DUENUMNO(PO_DEBITCARDTOP24DUENUMNO);
		
		//最近24个月内单张贷记卡产品的累计逾期期数（不考虑呆账）
		String PO_DEBITCARDSUM24DUENUMNO = pbocmanager.getPO_DEBITCARDSUM24DUENUMNO(DcardDetailList, header);
		pbocResponse.setPO_DEBITCARDSUM24DUENUMNO(PO_DEBITCARDSUM24DUENUMNO);
		
		//担保贷款五级分类
		String PO_ASSUREFIVE = pbocmanager.getPO_ASSUREFIVE(guaranteeList);
		pbocResponse.setPO_ASSUREFIVE(PO_ASSUREFIVE);
		
		//申请人（征信报告）呆账信息汇总笔数
		String PO_BADDEBTSNUM = pbocmanager.getPO_BADDEBTSNUM(pbocReport,loanDetailList,cardDetailList);
		pbocResponse.setPO_BADDEBTSNUM(PO_BADDEBTSNUM);
		
		//信用卡当前逾期期数（贷记卡）
		String PO_CREDITCARDCURDUED = pbocmanager.getPO_CREDITCARDCURDUED(DcardDetailList);
		pbocResponse.setPO_CREDITCARDCURDUED(PO_CREDITCARDCURDUED);
		
	}
	
	/**
	 * 处理五级分类所需信息
	 * @param Plguaranteeinfo
	 * @return
	 */
	private List<PlguaranteeinfoVo> guaranteeProcess(List<RePlguaranteeinfo> Plguaranteeinfo){
		List<PlguaranteeinfoVo> guaranteeList = new ArrayList<PlguaranteeinfoVo>();
		if(CollectionUtils.isEmpty(Plguaranteeinfo)){
			return guaranteeList;
		}
		for(RePlguaranteeinfo info : Plguaranteeinfo){
			PlguaranteeinfoVo detail = new PlguaranteeinfoVo();
			//获取五级分类
			RePlguarantee guarantee = AnalysisJsonUtil.getRePlguarantee(info);
			detail.setRePlguarantee(guarantee);
			guaranteeList.add(detail);
		}
		
		return guaranteeList;
	}
	
	/**
	 * 处理贷款所需信息
	 * @param loanList
	 * @return
	 */
	private List<LoanDetailVo> loanDetailProcess(List<RePlloanVo> loanList) {
		List<LoanDetailVo> loanDetailList = new ArrayList<LoanDetailVo>();
		if (CollectionUtils.isEmpty(loanList)) {
			return loanDetailList;
		}

		for (RePlloanVo loanVo : loanList) {
			LoanDetailVo detail = new LoanDetailVo();
			// 获取贷款合同信息
			RePlcontractinfo contract = AnalysisJsonUtil.getRePlcontractinfo(loanVo);
			// 获取贷款当前账户信息
			RePlcurraccountinfo account = AnalysisJsonUtil.getRePlcurraccountinfo(loanVo);
			// 获取逾期/透支记录信息描述
			List<RePloverduerecord> overdueList = AnalysisJsonUtil
					.getRePloverduerecord(loanVo.getRePllatest5yearoverduerecordSet());
			// 获取最近24个月还款状态
			RePllatest24monthpaymentstate paymentstate = AnalysisJsonUtil
					.getRePllatest24monthpaymentstate(loanVo.getRePllatest24monthpaymentstateSet());
			// 获取当前逾期信息
			List<RePlcurroverdue> curroverdueList = AnalysisJsonUtil.getRePlcurroverdue(loanVo);
			
			// 贷款状态
			String state = "";
			if (null != contract) {
				// 获取贷款合同信息里面的状态
				state = contract.getStateStr();
			}
			// 如果贷款合同里面的状态为空，则去获取当前账户信息里面的状态
			if (StringUtils.isBlank(state)) {
				if (null != account) {
					state = account.getState();
				}
			}
			// 最终状态为空 ，赋值为未结清
			if (StringUtils.isBlank(state)) {
				state = Constant.STATE_DEFAULT;
			}

			detail.setContract(contract);
			detail.setAccount(account);
			detail.setState(state);
			detail.setOverdueList(overdueList);
			detail.setPaymentstate(paymentstate);
			detail.setCurroverdueList(curroverdueList);
			loanDetailList.add(detail);
		}

		return loanDetailList;
	}
	
	
	/**
	 * 处理信用卡所需信息
	 * @param cardList
	 * @return
	 */
	private List<CardDetailVo> cardDetailProcess(List<RePlloancardVo> cardList){
		List<CardDetailVo> cardDetailList = new ArrayList<CardDetailVo>();
		if (CollectionUtils.isEmpty(cardList)) {
			return cardDetailList;
		}
		
		for (RePlloancardVo cardVo : cardList) {
			CardDetailVo detail = new CardDetailVo();
			// 获取信用卡授信信息
			RePlawardcreditinfo awardInfo = AnalysisJsonUtil.getRePlawardcreditinfo(cardVo);
			// 获取信用卡使用/还款记录
			RePlrepayinfo repayInfo = AnalysisJsonUtil.getRePlrepayinfo(cardVo);
			// 获取逾期/透支记录信息描述
			List<RePloverduerecord> overdueList = AnalysisJsonUtil
					.getRePloverduerecord(cardVo.getRePllatest5yearoverduerecordSet());
			// 获取最近24个月还款状态
			RePllatest24monthpaymentstate paymentstate = AnalysisJsonUtil
					.getRePllatest24monthpaymentstate(cardVo.getRePllatest24monthpaymentstateSet());
			// 信用卡状态
			String state = "";
			if(null != awardInfo){
				//获取信用卡授信信息里面的状态
				state = awardInfo.getStateStr();
			}
			// 若信用卡授信信息息里面的状态为空，则去获取使用还款记录信息里面的状态
			if (StringUtils.isBlank(state)) {
				if(null != repayInfo){
					state = repayInfo.getState();
				}
			}

			//最终状态为空 ，赋值为未默认值
			if(StringUtils.isBlank(state)){
				state = Constant.STATE_DEFAULT;
			}
			detail.setAwardInfo(awardInfo);
			detail.setRepayInfo(repayInfo);
			detail.setState(state);
			detail.setOverdueList(overdueList);
			detail.setPaymentstate(paymentstate);
			cardDetailList.add(detail);
		}
		
		return cardDetailList;
	}
	
	
	/**
	 * 衍生变量初始化
	 * @param pbocResponse
	 */
	private void initPbocResponse(PbocResponse pbocResponse){
		
		//获取手机号码
		pbocResponse.setMobile(Constant.NO);
		
		//计算信用卡产品的最近6个月的平均额度使用率
		pbocResponse.setCCardAvgLimitRate6M(Constant.SPECIAL_VAL_NONE_99);
		
	    //计算最近6个月内信贷产品的最大逾期期数
		pbocResponse.setCreditLoanMaxOverdue6M(Constant.SPECIAL_VAL_NONE_99);
	    
	    //计算最近12个月内信贷产品的最大逾期期数
	    pbocResponse.setCreditLoanMaxOverdue12M(Constant.SPECIAL_VAL_NONE_99);
	    
	    //计算最近24个月内信贷产品的最大逾期期数
	    pbocResponse.setCreditLoanMaxOverdue24M(Constant.SPECIAL_VAL_NONE_99);
	    
	    //计算所有信贷产品最大账龄
	    pbocResponse.setCreditLoanMaxZL(Constant.SPECIAL_VAL_NONE_99);

	    //计算最近6个月内信贷产品累计逾期次数
	    pbocResponse.setCreditLoanOverdueNum6M(Constant.SPECIAL_VAL_NONE_99);
	    
	    //计算最近12个月内信贷产品累计逾期次数
	    pbocResponse.setCreditLoanOverdueNum12M(Constant.SPECIAL_VAL_NONE_99);

	    //计算最近24个月内信贷产品累计逾期次数
	    pbocResponse.setCreditLoanOverdueNum24M(Constant.SPECIAL_VAL_NONE_99);

	    //计算最近3个月信贷审批查询次数
	    pbocResponse.setCreditLoanQueryNum3M(Constant.SPECIAL_VAL_NONE_99);

	    //计算最近6个月信贷审批查询次数
	    pbocResponse.setCreditLoanQueryNum6M(Constant.SPECIAL_VAL_NONE_99);

	    //计算最近12个月信贷审批查询次数
	    pbocResponse.setCreditLoanQueryNum12M(Constant.SPECIAL_VAL_NONE_99);

	    //计算申请人（征信报告）是否有征信
	    pbocResponse.setPbocReportFlag("0");

	    //计算申请人（征信报告）贷款当前逾期期数
	    pbocResponse.setLoanOverdueNum(Constant.SPECIAL_VAL_NONE_99);

	    //计算申请人（征信报告）贷款状态
	    pbocResponse.setLoanStatus(Constant.SPECIAL_VAL_NONE_99);

	    //计算申请人（征信报告）信用卡当期逾期期数
	    pbocResponse.setCCardOverdueNum(Constant.SPECIAL_VAL_NONE_99);

	    //计算申请人（征信报告）信用卡状态
	    pbocResponse.setCCardStatus(Constant.SPECIAL_VAL_NONE_99);

	    //计算申请人（征信报告）近24个月贷款最高逾期期数
	    pbocResponse.setLoanMaxOverdue24M(Constant.SPECIAL_VAL_NONE_99);

	    //计算申请人（征信报告）近24个月贷款累计逾期期数
	    pbocResponse.setLoanSumOverdue24M(Constant.SPECIAL_VAL_NONE_99);

	    //计算申请人（征信报告）近24个月信用卡最高逾期期数
	    pbocResponse.setCCardMaxOverdue24M(Constant.SPECIAL_VAL_NONE_99);

	    //计算申请人（征信报告）近24个月信用卡累计逾期期数
	    pbocResponse.setCCardSumOverdue24M(Constant.SPECIAL_VAL_NONE_99);

	    //计算申请人（征信报告）额度使用率超过80%的信用卡的张数
	    pbocResponse.setCCardOut80Rate(Constant.SPECIAL_VAL_NONE_99);

	    //计算申请人（征信报告）呆账信息汇总笔数
	    pbocResponse.setBadAccountNum(Constant.SPECIAL_VAL_NONE_99);

	    //计算申请人（征信报告）资产处置信息汇总笔数
	    pbocResponse.setAssetDisposals(Constant.SPECIAL_VAL_NONE_99);

	    //计算申请人（征信报告）是否存在强制执行记录
	    pbocResponse.setCourtExecutions(Constant.NO);

	    //计算申请人（征信报告）是否存在行政处罚记录
	    pbocResponse.setApRecordFlag(Constant.NO);

	    //计算准贷记卡
	    pbocResponse.setSemiCreditCard(Constant.SPECIAL_VAL_NONE_99);

	    //计算贷款历史逾期比例
	    pbocResponse.setLoanHisOverdueRate(Constant.SPECIAL_VAL_NONE_99);

	    //计算单张信用卡历史逾期比例
	    pbocResponse.setOneCcardHisOverdueRate(Constant.SPECIAL_VAL_NONE_99);

	    //计算多张信用卡历史逾期比例
	    pbocResponse.setManyCcardHisOverdueRate(Constant.SPECIAL_VAL_NONE_99);

	    //计算申请人（征信报告）贷款月负债
	    pbocResponse.setLoanMonthLiabilities(Constant.SPECIAL_VAL_NONE_99);
	    
	    //信用卡产品最大账龄
		pbocResponse.setRESERVED_1(Constant.SPECIAL_VAL_NONE_99);
		
		//住房贷款笔数
		pbocResponse.setRESERVED_2(Constant.SPECIAL_VAL_NONE_99);
		
		//信用卡产品的最近6个月的平均额度使用率（授信概要）
		pbocResponse.setRESERVED_3(Constant.SPECIAL_VAL_NONE_99);
		
		//最近24个月内准贷记卡产品的最大逾期期数（不考虑呆账）
		pbocResponse.setPO_SEMICREDITCARDNO(Constant.SPECIAL_VAL_NONE_99);
		
		//最近24个月内贷款产品的最大逾期期数（不考虑呆账）
		pbocResponse.setPO_LOANTOPDUENUM24NO(Constant.SPECIAL_VAL_NONE_99);
		
		//最近24个月内单个贷款产品的累计逾期期数（不考虑呆账）
		pbocResponse.setPO_LOANSUMDUENUM24NO(Constant.SPECIAL_VAL_NONE_99);
		
		//最近24个月内单张贷记卡产品的最大逾期期数（不考虑呆账）
		pbocResponse.setPO_DEBITCARDTOP24DUENUMNO(Constant.SPECIAL_VAL_NONE_99);
		
		//最近24个月内单张贷记卡产品的累计逾期期数
		pbocResponse.setPO_DEBITCARDSUM24DUENUMNO(Constant.SPECIAL_VAL_NONE_99);
		
		//担保贷款五级分类
		pbocResponse.setPO_ASSUREFIVE(Constant.SPECIAL_VAL_NONE_99);
		
		//申请人（征信报告）呆账信息汇总笔数
		pbocResponse.setPO_BADDEBTSNUM(Constant.SPECIAL_VAL_NONE_99);
		
		//申请人（征信报告）信用卡当期逾期期数
		pbocResponse.setPO_CREDITCARDCURDUED(Constant.SPECIAL_VAL_NONE_99);
	}
	
	/**
	 * 保存数据
	 * @param pbocResponse
	 * @throws Exception 
	 */
	private void saveResult(PbocResponse pbocResponse) throws Exception{
		String reportNo = pbocResponse.getReportNo();
		int count = DBQuery.countPbocResponse(reportNo);
		if(count == 0){
			//新增操作
			DBQuery.insertPbocResponse(pbocResponse);
			
		}else if(count > 0){
			//更新操作
			DBQuery.updatePbocResponse(pbocResponse);
		}
	}

	/**
	 * @param pbocmanager the pbocmanager to set
	 */
	public void setPbocmanager(Pbocmanager pbocmanager) {
		this.pbocmanager = pbocmanager;
	}
	
	
}
