package kr.co.posventure.batch.test;

import kr.co.posventure.batch.service.SampleService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TestCRUD {

	@Autowired
	private SampleService sampleService;

	@Test
	public void create() {
	}
	
	@Test
	public void read() {
		sampleService.excute("");
	}

	@Test
	public void update() {
	}

	@Test
	public void delete() {
	}

}
