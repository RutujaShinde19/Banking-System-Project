package Bank_Application_System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class User {

	private Connection connection;
	private Scanner sc;

	public User(Connection connection, Scanner sc) {
		super();
		this.connection = connection;
		this.sc = sc;
	}

	public void register() {

		try {

			
			String query = "insert into user values(?,?,?)";

			PreparedStatement psmt = connection.prepareStatement(query);
			sc.nextLine();
			System.out.println("Enter Name: ");
			psmt.setString(1, sc.nextLine());

			System.out.println("Enter Email: ");
			String email = sc.nextLine();
			psmt.setString(2, email);

			System.out.println("Enter Password: ");
			psmt.setString(3, sc.nextLine());

			if (user_exist(email)) {
				System.out.println("User Already Exists for this Email Address!!");
				return;
			}

			int affectedRows = psmt.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("Registration Successful!!!");
			} else {
				System.out.println("Registration Failed");
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public String login() {
		sc.nextLine();
		try {
			String login_query = "select * from user where email=? and password=?";
			PreparedStatement psmt = connection.prepareStatement(login_query);
			System.out.println("Enter Email: ");
			String email = sc.nextLine();
			psmt.setString(1, email);

			System.out.println("Enter Password: ");
			psmt.setString(2, sc.nextLine());

			ResultSet rs = psmt.executeQuery();
			if (rs.next()) {
				return email;
			} else {
				return null;
			}

		} catch (Exception e) {

			System.out.println(e);
		}
		return null;

	}

	public boolean user_exist(String email) {

		try {
			String query = "select * from user where email=?";

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
