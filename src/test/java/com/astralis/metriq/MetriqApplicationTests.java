package com.astralis.metriq;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MetriqApplicationTests {

	@Autowired
	private DataSource dataSource;

	@Test
	void contextLoads() throws Exception {
		try (var connection = dataSource.getConnection()) {
			assertEquals("H2", connection.getMetaData().getDatabaseProductName());
		}
	}

}
