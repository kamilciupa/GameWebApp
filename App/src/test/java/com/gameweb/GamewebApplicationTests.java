package com.gameweb;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GamewebApplicationTests {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Test
	public void contextLoads() {
	}

	@Test
	public void databaseTest(){

	int isInserted = jdbcTemplate.update("INSERT INTO categories (id, name) values (?,?)",99999,"test");
		Assert.assertNotEquals(isInserted, 0);
		jdbcTemplate.execute("DELETE FROM categories where id = 99999");
	}

}
