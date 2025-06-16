package TimeCapsuleMessagingSystem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MAIN {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n----- Time Capsule Messaging System -----");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {
                case 1: // Register
                    System.out.print("Enter username: ");
                    String newUsername = sc.nextLine();
                    System.out.print("Enter 6-character password: ");
                    String newPassword = sc.nextLine();
                    USERSERVICE.Register(newUsername, newPassword);
                    break;

                case 2: // Login
                    System.out.print("Enter username: ");
                    String username = sc.nextLine();
                    System.out.print("Enter password: ");
                    String password = sc.nextLine();

                    if (USERSERVICE.Login(username, password)) {
                        int userId = USERSERVICE.getUserId(username);

                        boolean loggedIn = true;
                        while (loggedIn) {
                            System.out.println("\n--- Welcome, " + username + " ---");
                            System.out.println("1. Send Message");
                            System.out.println("2. View Unlocked Messages");
                            System.out.println("3. Logout");
                            System.out.print("Enter your choice: ");
                            int userChoice = sc.nextInt();
                            sc.nextLine(); // clear buffer

                            switch (userChoice) {
                                case 1: // Send message
                                    System.out.print("Enter receiver's username: ");
                                    String receiver = sc.nextLine();

                                    System.out.print("Enter message: ");
                                    String message = sc.nextLine();

                                    System.out.print("Enter unlock time (yyyy/MM/dd HH:mm): ");
                                    String unlockInput = sc.nextLine();



                                    try {
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
                                        LocalDateTime unlockTime = LocalDateTime.parse(unlockInput, formatter);
                                        MESSAGES.sendMessage(userId, receiver, message, unlockTime);
                                    } catch (Exception e) {
                                        System.out.println("Invalid date format. Use: yyyy/MM/dd HH:mm");
                                    }

                                    break;

                                case 2: // View unlocked messages
                                    try {
                                        MESSAGES.viewMessage(userId, false);
                                    } catch (Exception e) {
                                        System.out.println("Error viewing messages: " + e.getMessage());
                                    }
                                    break;

                                case 3: // Logout
                                    loggedIn = false;
                                    System.out.println("Logged out.");
                                    break;

                                default:
                                    System.out.println("Invalid option. Try again.");
                            }
                        }
                    }
                    break;

                case 3: // Exit
                    running = false;
                    System.out.println("Exiting system. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
        sc.close();
    }
}

