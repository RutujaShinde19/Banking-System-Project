package Bank_Application_System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {

	private Connection connection;
	private Scanner sc;

	AccountManager(Connection connection, Scanner sc) {
		this.connection = connection;
		this.sc = sc;
	}

	public void credit_money(long account_number) throws SQLException {
		sc.nextLine();
		System.out.println("Enter Amount: ");
		double amount = sc.nextDouble();
		sc.nextLine();

		System.out.println("Enter Security Pin: ");
		String security_pin = sc.nextLine();
		try {

			connection.setAutoCommit(false);
			String query = "select * from accounts where account_number=? and security_pin=?";

			if (account_number != 0) {
				PreparedStatement psmt = connection.prepareStatement(query);
				psmt.setLong(1, account_number);
				psmt.setString(2, security_pin);

				ResultSet rs = psmt.executeQuery();

				if (rs.next()) {
					String credit_query = "update accounts set balance=balance+? where account_number=?";
					PreparedStatement psmt1 = connection.prepareStatement(credit_query);
					psmt1.setDouble(1, amount);
					psmt1.setLong(2, account_number);
					int rowsAffected = psmt1.executeUpdate();
					if (rowsAffected > 0) {
						System.out.println("Rs." + amount + " credited Successfully");
						connection.commit();
						connection.setAutoCommit(true);
						return;
					} else {
						System.out.println("Transaction Failed!!!");
						connection.rollback();
						connection.setAutoCommit(true);
					}
				} else {
					System.out.println("Invalid Security Pin!!!");
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		connection.setAutoCommit(true);
	}

	public void debit_money(long account_number) throws SQLException {
		sc.nextLine();
		System.out.print("Enter Amount: ");
		double amount = sc.nextDouble();
		sc.nextLine();
		System.out.print("Enter Security Pin: ");
		String security_pin = sc.nextLine();
		try {
			connection.setAutoCommit(false);
			if (account_number != 0) {
				String query = "select * from accounts where account_number = ? and security_pin = ? ";
				PreparedStatement psmt = connection.prepareStatement(query);
				psmt.setLong(1, account_number);
				psmt.setString(2, security_pin);
				ResultSet rs = psmt.executeQuery();

				if (rs.next()) {
					double current_balance = rs.getDouble("balance");
					if (amount <= current_balance) {
						String debit_query = "update accounts set balance = balance - ? where account_number = ?";
						PreparedStatement psmt1 = connection.prepareStatement(debit_query);
						psmt1.setDouble(1, amount);
						psmt1.setLong(2, account_number);
						int rowsAffected = psmt1.executeUpdate();
						if (rowsAffected > 0) {
							System.out.println("Rs." + amount + " debited Successfully!");
							connection.commit();
							connection.setAutoCommit(true);
							return;
						} else {
							System.out.println("Transaction Failed!");
							connection.rollback();
							connection.setAutoCommit(true);
						}
					} else {
						System.out.println("Insufficient Balance!");
					}
				} else {
					System.out.println("Invalid Pin!");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		connection.setAutoCommit(true);
	}

	public void transfer_money(long sender_account_number) throws SQLException {
		sc.nextLine();
		System.out.print("Enter Receiver Account Number: ");
		long receiver_account_number = sc.nextLong();
		System.out.print("Enter Amount: ");
		double amount = sc.nextDouble();
		sc.nextLine();
		System.out.print("Enter Security Pin: ");
		String security_pin = sc.nextLine();
		try {
			String query = "select * from accounts where account_number = ? and security_pin = ?";
			connection.setAutoCommit(false);
			if (sender_account_number != 0 && receiver_account_number != 0) {

				PreparedStatement psmt = connection.prepareStatement(query);
				psmt.setLong(1, sender_account_number);
				psmt.setString(2, security_pin);
				ResultSet rs = psmt.executeQuery();

				if (rs.next()) {
					double current_balance = rs.getDouble("balance");
					if (amount <= current_balance) {

						// Write debit and credit queries
						String debit_query = "update accounts set balance = balance - ? where account_number = ?";
						String credit_query = "update accounts set balance = balance + ? where account_number = ?";

						// Debit and Credit prepared Statements
						PreparedStatement creditPsmt = connection.prepareStatement(credit_query);
						PreparedStatement debitPsmt = connection.prepareStatement(debit_query);

						// Set Values for debit and credit prepared statements
						creditPsmt.setDouble(1, amount);
						creditPsmt.setLong(2, receiver_account_number);
						debitPsmt.setDouble(1, amount);
						debitPsmt.setLong(2, sender_account_number);
						int rowsAffected1 = debitPsmt.executeUpdate();
						int rowsAffected2 = creditPsmt.executeUpdate();
						if (rowsAffected1 > 0 && rowsAffected2 > 0) {
							System.out.println("Transaction Successful!!!");
							System.out.println("Rs." + amount + " Transferred Successfully");
							connection.commit();
							connection.setAutoCommit(true);
							return;
						} else {
							System.out.println("Transaction Failed!!!");
							connection.rollback();
							connection.setAutoCommit(true);
						}
					} else {
						System.out.println("Insufficient Balance!!");
					}
				} else {
					System.out.println("Invalid Security Pin!!!");
				}
			} else {
				System.out.println("Invalid account number...");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		connection.setAutoCommit(true);
	}

	public void getBalance(long account_number) {
		sc.nextLine();
		System.out.print("Enter Security Pin: ");
		String security_pin = sc.nextLine();
		try {
			String query = "select balance from accounts where account_number = ? and security_pin = ?";
			PreparedStatement psmt = connection.prepareStatement(query);
			psmt.setLong(1, account_number);
			psmt.setString(2, security_pin);
			ResultSet rs = psmt.executeQuery();
			if (rs.next()) {
				double balance = rs.getDouble("balance");
				System.out.println("Balance: " + balance);
			} else {
				System.out.println("Invalid Pin!!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
