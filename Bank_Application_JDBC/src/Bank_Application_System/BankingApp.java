package Bank_Application_System;

import java.sql.Connection;
import java.util.Scanner;

public class BankingApp {

	public static void main(String[] args) {

		try {
			Connection connection = MyConnection.getConnection();
			Scanner sc = new Scanner(System.in);

			User user = new User(connection, sc);

			Account accounts = new Account(connection, sc);

			AccountManager accountManager = new AccountManager(connection, sc);

			String email;
			long account_number;

			while (true) {
				System.out.println("****WELCOME TO BANKING SYSTEM****");
				System.out.println();
				System.out.println("1. Register");
				System.out.println("2. Login");
				System.out.println("3. Exit");
				System.out.println("Enter Your Choice: ");
				int choice1 = sc.nextInt();

				switch (choice1) {
				case 1:
					user.register();
					break;

				case 2:
					email = user.login();
					if (email != null) {
						System.out.println();
						System.out.println("User Logged In!");
						if (!accounts.account_exist(email)) {
							System.out.println();
							System.out.println("1. Open a new Bank Account");
							System.out.println("2. Exit");
							if (sc.nextInt() == 1) {
								account_number = accounts.open_account(email);
								System.out.println("Account Created Successfully!");
								System.out.println("Your Account Number is: " + account_number);
							} else {
								break;
							}
						}
						account_number = accounts.getAccount_number(email);

						int choice2 = 0;
						while (choice2 != 5) {
							System.out.println();
							System.out.println("1. Debit Money");
							System.out.println("2. Credit Money");
							System.out.println("3. Transfer Money");
							System.out.println("4. Check Balance");
							System.out.println("5. Log Out");
							System.out.println("Enter your choice: ");
							choice2 = sc.nextInt();
							switch (choice2) {
							case 1:
								accountManager.debit_money(account_number);
								break;

							case 2:
								accountManager.credit_money(account_number);
								break;

							case 3:
								accountManager.transfer_money(account_number);
								break;

							case 4:
								accountManager.getBalance(account_number);
								break;

							case 5:
								break;

							default:
								System.out.println("Enter Valid Choise.");
								break;
							}
						}

					} else {
						System.out.println("Incorrect email or Password!");
					}
				case 3:
					System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
					System.out.println("Exiting System!");
					return;

				default:
					System.out.println("Enter Valid Choice.");
					break;

				}

			}

		} catch (Exception e) {

			System.out.println(e);
		}

	}

}
