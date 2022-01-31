package com.nw.sevbanking.generator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * Generator for random account ids
 * 
 * @author nicowickersheim
 *
 */

public class AccountIdGenerator implements IdentifierGenerator {

	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

		Connection connection = session.connection();

		try {
			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery(
					"SELECT (SELECT COUNT(*) FROM `sev-banking-nw`.giro_accounts ) + ( SELECT COUNT(*) FROM `sev-banking-nw`.fixed_deposit_accounts )");

			if (rs.next()) {
				Random rand = new Random();
				long id = 1000000000;
				for (int i = 0; i < 14; i++) {
					int n = rand.nextInt(100000000) + 0;
					id += n;
				}
				return id;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}