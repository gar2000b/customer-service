package com.onlineinteract.fileserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
@Configuration
public class CustomerServiceApp {

	public static Connection databaseConnection;

	public static void main(String[] args) {
		connectDB();
		SpringApplication.run(CustomerServiceApp.class, args);
	}

	private static void connectDB() {
		String dbUrl = "mysql.canadacentral.azurecontainer.io";
		String db = "users";
		String username = "social";
		String password = "1234";

		if (databaseConnection != null)
			return;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return;
		}

		System.out.println("\nMySQL JDBC Driver Registered!");
		databaseConnection = null;

		try {
			databaseConnection = DriverManager.getConnection("jdbc:mysql://" + dbUrl + ":3306/" + db
					+ "?rewriteBatchedStatements=true&autoReconnect=true&useSSL=false", username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (databaseConnection != null) {
			System.out.println("You made it, take control of your database now!\n");
		} else {
			System.out.println("Failed to make connection!");
		}
	}

	@Scheduled(fixedRate = 20000)
	public void keepAlive() throws SQLException {
		Statement statement = CustomerServiceApp.databaseConnection.createStatement();
		statement.setQueryTimeout(60);
		statement.setFetchSize(1000);
		ResultSet rs = statement.executeQuery("SELECT 'PONG' AS PING;");
		rs.next();
		if (rs.getString("PING").equals("PONG"))
			System.out.println("*** DB Keep Alive Triggered OK ***");

		rs.close();
		statement.close();
	}
}
