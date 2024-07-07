package Bank_Application_System;

import java.awt.Taskbar.State;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Account {

	private Connection connection;
	private Scanner sc;

	public Account(Connection connection, Scanner sc) {
		super();
		this.connection = connection;
		this.sc = sc;
	}

	public long open_account(String email) {
		if (!account_exist(email)) {
			sc.nextLine();
			System.out.println("Enter Name: ");
			String name=sc.nextLine();
		
			System.out.println("Enter Initial Amount: ");
			Double balance=sc.nextDouble();
			
			System.out.println("Enter Security Pin: ");
			String security_pin=sc.next();
			try {
				sc.nextLine();
				long account_number = generateAccountNumber();
				String open_account_query = "insert into accounts values(?,?,?,?,?)";

				PreparedStatement psmt = connection.prepareStatement(open_account_query);
				psmt.setLong(1, account_number);
				psmt.setString(2, name);
				psmt.setString(3, email);
				psmt.setDouble(4,balance);
				psmt.setString(5, security_pin);

				int rowsAffected = psmt.executeUpdate();
				if (rowsAffected > 0) {
					return account_number;
				} else {
					throw new RuntimeException("Account Creation Failed!!!");
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		throw new RuntimeException("Account Already Exist...");
	}

	public long getAccount_number(String email) {
		try {

			String query = "select account_number from accounts where email=?";

			PreparedStatement psmt = connection.prepareStatement(query);
			psmt.setString(1, email);

			ResultSet rs = psmt.executeQuery();

			if (rs.next()) {
				return rs.getLong("account_number");

			}

		} catch (Exception e) {
			System.out.println(e);
		}
		throw new RuntimeException("Account Number Doesn't Exit!");
	}

	private long generateAccountNumber() {
		try {

			Statement smt = connection.createStatement();
			ResultSet rs = smt.executeQuery("select account_number from accounts order by account_number desc limit 1");

			if (rs.next()) {
				long last_account_number = rs.getLong("account_number");
				return last_account_number + 1;
			} else {
				return 10000100;
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return 10000100;
	}

	public boolean account_exist(String email) {
		try {

			String query = "select account_number from accounts where email=?";
			PreparedStatement psmt = connection.prepareStatement(query);

			psmt.setString(1, email);

			ResultSet rs = psmt.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			System.out.println(e);
		}

		return false;
	}
}
