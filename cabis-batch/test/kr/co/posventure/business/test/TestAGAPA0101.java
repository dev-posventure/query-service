package kr.co.posventure.business.test;

import kr.co.posventure.business.service.AGAPA0101;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TestAGAPA0101 {

	@Autowired
	private AGAPA0101 agapa0101;
	
	@Test
	public void doPayDtrm() {
		agapa0101.doPayDtrm();
    }
}
