package com.bp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Test unit/integration.
 */
@SuppressWarnings({"EmptyMethod"})
@RunWith(SpringRunner.class)
@MybatisTest
@MapperScan("com.bp.repository")
public class PandemicCentreApplication {
	/**
	 * Context loads.
	 */
	@Test
	public void contextLoads() {
	}
}

