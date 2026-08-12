package com.springboot.others;

import com.helloworld.starter.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MystarterApplicationTests {
	@Autowired
	private PersonService personService;
	
	@Test
	public void testHelloWorld() {
		personService.sayHello();
	}
}
