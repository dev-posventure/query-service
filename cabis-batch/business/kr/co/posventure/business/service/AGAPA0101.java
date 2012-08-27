package kr.co.posventure.business.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import kr.co.daewoobrenic.service.AbstractSqlQueryService;
import kr.co.posventure.business.model.Excel;
import kr.co.posventure.business.model.PayReqModel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author hdlee
 * @see : 지급 요청
 */
@Service
public class AGAPA0101 extends AbstractSqlQueryService{
	
	private static final Logger logger = Logger.getLogger(AGAPA0101.class);

	List<Excel> excelList = new ArrayList<Excel>();

	public AGAPA0101() {
		Excel excel = new Excel("지급처","1","아라산업","6078136064","","003020","우리","020","(주)아라산업","96023101230","232,321,323","수당","연구비 수당");
		Excel excel2 = new Excel("사원","2","이학도","12X00006","마케팅팀","003020","우리","020","이학도","76070104038537","2,321,321,323","월급","프로젝트 수행 수당");
		excelList.add(excel);
		excelList.add(excel2);
	}

	/**
	 * UPLOAD한 EXCEL데이터
	 */
	@Transactional
	public void doPayDtrm() {
		if (logger.isDebugEnabled()) {
			logger.debug("doPayDtrm() - start"); //$NON-NLS-1$
		}
		validationAndConvertPayReqModelExcelList(excelList);
		deleteHtSalrBase();
		for (Excel excel : excelList) {
			int count = countAtPayPlan(excel);
			if (count > 0) {
				logger.info("doPayDtrm() - 존재하는 거래처/사원번호 = " + excel.getA());
				insertHtSalrBase(excel);
				String seq = selectPaySeq();
				insertAtPayPlan(seq, excel);
				insertCtPayRqst(seq, excel);
			}else{
				logger.info("doPayDtrm() - 존재하지 않는 거래처/사원번호 = " + excel.getA());
			}
        }
		if (logger.isDebugEnabled()) {
			logger.debug("doPayDtrm() - end"); //$NON-NLS-1$
		}
	}

	/**
	 * @param list
	 */
	private List<PayReqModel> validationAndConvertPayReqModelExcelList(List<Excel> list) {
		List<PayReqModel> list2 = new ArrayList<PayReqModel>();
		for (Excel excel : list) {
			PayReqModel payReqModel  = new PayReqModel();
			list2.add(payReqModel);
        }
		return list2;
		
    }

	public String COUNT_AT_PAY_PLAN = "\n"
			+ " SELECT SUM(CNT) FROM (        \n"
			+ "	  SELECT count(*) AS CNT      \n"
			+ "	    FROM CBSCOMM.HT_EMP_BASE  \n"
			+ "	   WHERE EMP_NO = ?           \n"
			+ "	   UNION                      \n"
			+ "	  SELECT count(*) AS CNT      \n"
			+ "	    FROM CBSACCT.AT_BUSI      \n"
			+ "	   WHERE BUSI_CD = ?          \n"
			+ ") A                            \n";
	
	/**
	 * @category : 지급처 구분이 사원인 경우 직원 테이블의 사원번호와 엑셀의 거래처/사원번호를 비교하여 오류체크를 하고
	 * @category : 지급처 구분이 거래처인 경우 CBSACCT.AT_BUSI 테이블의 거래처번호와 엑셀의 거래처/사원번호를 비교하여 오류체크를 한다.
	 * @param excel
	 * @return
	 */
	private int countAtPayPlan(Excel excel) {
		Object[] objs = new Object[]{
			excel.getA()
		   ,excel.getA()
		};
	    return getJdbcTemplate().queryForInt(COUNT_AT_PAY_PLAN, objs);
    }
	
	public String INSERT_CT_PAY_RQST = "\n"
			 + " INSERT INTO CBSCOMM.CT_PAY_RQST(                 \n "
			 + "     ACNT_NO           /* 계좌_번호          */   \n "
			 + "   , BANK_CD           /* 은행_코드          */   \n "
			 + "   , DEPO_OWN_NM       /* 예금주_명          */   \n "
			 + "   , PAY_AMT           /* 지급_금액          */   \n "
			 + "   , PAY_NO            /* 지급_번호          */   \n "
			 + "   , RQST_CUST_NM      /* 지급_고객_명       */   \n "
			 + "   , RQST_CUST_NO      /* 지급_고객_번호     */   \n "    
			 + "   , FRST_REG_DT       /* 최초_등록_일자     */   \n "
			 + "   , FRST_REG_EMP_NO   /* 최초_등록_사원번호 */   \n "
			 + "   , FRST_REG_TM       /* 최초_등록_시간     */   \n "
			 + " ) VALUES(                                        \n "
			 + "     ? /* ACNT_NO           */                    \n "
			 + "   , ? /* BANK_CD           */                    \n "
			 + "   , ? /* DEPO_OWN_NM       */                    \n "
			 + "   , ? /* PAY_AMT           */                    \n "
			 + "   , ? /* PAY_NO            */                    \n "
			 + "   , ? /* RQST_CUST_NM      */                    \n "
			 + "   , ? /* RQST_CUST_NO      */                    \n "
			 + "   , ? /* FRST_REG_DT       */                    \n "
			 + "   , ? /* FRST_REG_EMP_NO   */                    \n "
			 + "   , ? /* FRST_REG_TM       */                    \n "
			 + " )                                                \n ";
	/**
	 * 지급요청 INSERT
	 * @param excel 
	 * @param seq 
	 * @category CBSCOMM
	 */
	public void insertCtPayRqst(String seq, Excel excel) {
		Object[] objs = new Object[]{
				   ""   /* ACNT_NO           */
				 , ""   /* BANK_CD           */
				 , ""   /* DEPO_OWN_NM       */
				 , ""   /* PAY_AMT           */
				 , ""   /* PAY_NO            */
				 , ""   /* RQST_CUST_NM      */
				 , ""   /* RQST_CUST_NO      */
				 , ""   /* FRST_REG_DT       */
				 , ""   /* FRST_REG_EMP_NO   */
				 , ""   /* FRST_REG_TM       */
			};
		getJdbcTemplate().update(INSERT_CT_PAY_RQST, objs );		
    }

	public String INSERT_AT_PAY_PLAN = ""
			+ " INSERT INTO CBSACCT.AT_PAY_PLAN (                                                                                           \n "
			+ " 	 ACNT_NO           /* 계좌_번호                                                          */                               \n "
			+ " 	,BANK_CD           /* 은행_코드                                                          */                               \n "
			+ " 	,DEPO_OWNNM        /* 예금주명                                                           */                               \n "
			+ " 	,PAY_OBJ_DIV_CD    /* 지급_대상_구분(1-거래처,2-사원)                                    */                               \n "
			+ " 	,PAY_RQST_DEPT_CD  /* 지급_요청_부서_코드                                                */ /* 지급요청부서 '003020' */   \n "
			+ " 	,PAY_RQST_DT       /* 지급_요청_일자                                                     */                               \n "
			+ " 	,PAY_RQST_METH_CD  /* 지급_요청_수단_코드(10-현금,20-제예금,30-지급어음,40-자동이체)     */ /* 지급요청수단 '20'     */   \n "
			+ " 	,PAY_STAT_CD       /* 지급_상태_코드(10-보류, 20-대기,30-결정,90-확정, 94-삭제, DD-취소) */ /* 지급상태코드 '20'     */   \n "
			+ " 	,RMRK              /* 비고                                                               */ /* 적요로 쓰는 건가?     */   \n "
			+ " 	,FRST_REG_DT       /* 최초_등록_일자                                                     */                               \n "
			+ " 	,FRST_REG_EMP_NO   /* 최초_등록_사원_번호                                                */                               \n "
			+ " 	,FRST_REG_TM       /* 최초_등록_시간                                                     */                               \n "
			+ " )VALUES(                                                                                                                    \n "
			+ "    ?                 /* ACNT_NO          */                                                                                 \n "
			+ "   ,?                 /* BANK_CD          */                                                                                 \n "
			+ "   ,?                 /* DEPO_OWNNM       */                                                                                 \n "
			+ "   ,?                 /* PAY_OBJ_DIV_CD   */                                                                                 \n "
			+ "   ,?                 /* PAY_RQST_DEPT_CD */                                                                                 \n "
			+ "   ,?                 /* PAY_RQST_DT      */                                                                                 \n "
			+ "   ,?                 /* PAY_RQST_METH_CD */                                                                                 \n "
			+ "   ,?                 /* PAY_STAT_CD      */                                                                                 \n "
			+ "   ,?                 /* RMRK             */                                                                                 \n "
			+ "   ,?                 /* FRST_REG_DT      */                                                                                 \n "
			+ "   ,?                 /* FRST_REG_EMP_NO  */                                                                                 \n "
			+ "   ,?                 /* FRST_REG_TM      */                                                                                 \n "
			+ " )                                                                                                                           \n ";
	/**
	 * 지급계획 INSERT
	 * @param excel 
	 * @param seq 
	 * @category CBSACCT
	 */
	public void insertAtPayPlan(String seq, Excel excel) {
		Object[] objs = new Object[]{
				  ""                                                    /* ACNT_NO          */ 
				, ""                                                    /* BANK_CD          */ 
				, ""                                                    /* DEPO_OWNNM       */ 
				, ""                                                    /* PAY_OBJ_DIV_CD   */ 
				, "003020"                                              /* PAY_RQST_DEPT_CD */ 
				, new SimpleDateFormat("yyyy-MM-dd").format(new Date()) /* PAY_RQST_DT      */ 
				, "20"                                                  /* PAY_RQST_METH_CD */ 
				, "20"                                                  /* PAY_STAT_CD      */ 
				, ""                                                    /* RMRK             */ 
				, new SimpleDateFormat("yyyy-MM-dd").format(new Date()) /* FRST_REG_DT      */ 
				, ""                                                    /* FRST_REG_EMP_NO  */ 
				, new SimpleDateFormat("hh:mm:ss").format(new Date())   /* FRST_REG_TM      */ 
			};
			getJdbcTemplate().update(INSERT_AT_PAY_PLAN, objs );
    }

	public String SELECT_SALR_BASE = "\n"
			+ " SELECT ZZZ FROM XXX.YYY";
	/**
	 * 지급 번호 채번
	 * @return 
	 */
	public String selectPaySeq() {
		return "";
    }

	
	public String INSERT_HT_SALR_BASE = ""                        
			+ " INSERT INTO CBSCOMM.HT_SALR_BASE(                    \n " 
			+ "      EMP_NO	                /* 사원번호             */  \n " 
			+ "     ,SALR_ACNT_BANK_CD	    /* 급여_계좌_은행_코드  */  \n " 
			+ "     ,SALR_ACNT_DEPO_OWNNM	/* 급여_계좌_예금주명   */  \n " 
			+ "     ,SALR_ACNT_NO	        /* 급여_계좌_번호       */  \n " 
			+ "     ,FRST_REG_DT	        /* 최초_등록_일자       */  \n " 
			+ "     ,FRST_REG_TM	        /* 최초_등록_시각       */  \n " 
			+ "     ,FRST_REG_EMP_NO	    /* 최초_등록_사원번호   */  \n " 
			+ " ) VALUES(                                             \n " 
			+ "      ?                    /* EMP_NO	              */  \n " 
			+ "     ,?                    /* SALR_ACNT_BANK_CD	  */  \n " 
			+ "     ,?                    /* SALR_ACNT_DEPO_OWNNM */  \n " 
			+ "     ,?                    /* SALR_ACNT_NO	      */  \n " 
			+ "     ,?                    /* FRST_REG_DT	      */  \n " 
			+ "     ,?                    /* FRST_REG_TM	      */  \n " 
			+ "     ,?                    /* FRST_REG_EMP_NO	  */  \n " 
			+ " )                                                     \n ";
	/**
	 * 급여기본 INSERT
	 * @category CBSCOMM
	 */
	public void insertHtSalrBase(Excel excel) {
	 Object[] objs = new Object[]{
				   ""                                                    /* EMP_NO	             */ 
				 , ""                                                    /* SALR_ACNT_BANK_CD	 */ 
				 , ""                                                    /* SALR_ACNT_DEPO_OWNNM */ 
				 , ""                                                    /* SALR_ACNT_NO	     */ 
				 , new SimpleDateFormat("yyyy-MM-dd").format(new Date()) /* FRST_REG_DT	         */ 
				 , new SimpleDateFormat("hh:mm:ss").format(new Date())   /* FRST_REG_TM	         */ 
				 , ""                                                    /* FRST_REG_EMP_NO	     */ 
			 };
		getJdbcTemplate().update(INSERT_HT_SALR_BASE, objs );
    }

	public String DELETE_HT_SALR_BASE = "DELETE FROM CBSCOMM.HT_SALR_BASE";
	
	/**
	 * CBSCOMM.HT_SALR_BASE 테이블(급여기본)삭제 
	 */
	public void deleteHtSalrBase() {
		getJdbcTemplate().update(DELETE_HT_SALR_BASE );
    }

}
