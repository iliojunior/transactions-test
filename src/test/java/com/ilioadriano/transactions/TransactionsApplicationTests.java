package com.ilioadriano.transactions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TransactionsApplicationTests {

	/**
	 * Assert the application is up without configuration error
	 */
	@Test
	void contextLoads() {
		String[] args = {"--spring.profiles.active=mysql-test,test", "--server.port=0"};
		TransactionsApplication.main(args);

		Assertions.assertTrue(true);
	}

}
