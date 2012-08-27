package kr.co.posventure.batch.main;

import org.apache.log4j.Logger;
import kr.co.daewoobrenic.main.AbstractMain;
import kr.co.posventure.batch.service.SampleService;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hdlee
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SampleMain extends AbstractMain {
	private static final Logger logger = Logger.getLogger(SampleMain.class);
 
	//console로 받는 전역 변수 
	private String date;

	/**
	 * 1
	 */
	public static void main(String[] args) {
		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - start");
		}
		//필수값 체크
	     if ((args == null) || (args.length < 1)) {
	    	 logger.info("배치 필수 파라미터를 확인해 주십시오.");
	    	 logger.info("필수파라미터: 기준일자(1)");
             System.exit(-1);
         }
		SampleMain main = new SampleMain();//AbstractMain의 상속을 받아 오기 위해 인스턴스를 만든다.
		main.setDate(args[0]);
		main.execute();
		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - end");
		}
	}

	/**
	 * 2
	 */
	public SampleMain() {
		super("applicationContext.xml", "샘플배치");
	}

	/**
	 * 3
	 */
	private void execute() {
		System.out.println(getDate());
		if (logger.isDebugEnabled()) {
			logger.debug("execute() - start");
		}
		SampleService sampleService = (SampleService) getServiceClass(new SampleService(), SampleService.class);
		boolean isSuccess = sampleService.excute(getDate());//스프링 서비스 실행 - transction을 여기서 탄다
		if (isSuccess) {
			logger.info(getBatchName() + " 배치 실행 성공");
        }else {
			logger.info(getBatchName() + " 배치 실행 실패");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("execute() - end");
		}
	}
}
