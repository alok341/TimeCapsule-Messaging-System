package TimeCapsuleMessagingSystem;

import java.sql.*;
import java.time.LocalDateTime;

public class MESSAGES {

    // Send a message
    public static void sendMessage(int senderId, String receiverUsername, String message, LocalDateTime unlockTime) throws Exception {
        int receiverId = USERSERVICE.getUserId(receiverUsername);

        if (receiverId == -1) {
            System.out.println("❌ Receiver not found!");
            return;
        }

        String encrypted = USERSERVICE.encrypt(message);

        String insertQuery = "INSERT INTO messagesystem (SENDER_ID, RECEIVER_ID, MESSAGE, UNLOCK_DATETIME, IS_OPEN) VALUES (?, ?, ?, ?, FALSE)";


        try (Connection conn = DBconnection.getconnection()) {
            PreparedStatement ps = conn.prepareStatement(insertQuery);

            ps.setInt(1, senderId);
            ps.setInt(2, receiverId);
            ps.setString(3, encrypted);
            ps.setTimestamp(4, Timestamp.valueOf(unlockTime));

            ps.executeUpdate();
            System.out.println("✅ Message sent successfully.");
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // View received messages
    public static void viewMessage(int userId, boolean onlyUnread) throws Exception {
        String query = """
            SELECT M.ID, U.USERNAME AS SENDER, M.MESSAGE, M.UNLOCK_DATETIME 
            FROM messagesystem M
            JOIN user U ON M.SENDER_ID = U.ID
            WHERE M.RECEIVER_ID = ?
              AND M.UNLOCK_DATETIME <= NOW()
        """ + (onlyUnread ? " AND M.IS_OPEN = FALSE" : "");

        try (Connection conn = DBconnection.getconnection()) {
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            int count = 0;
            while (rs.next()) {
                count++;
                int messageId = rs.getInt("ID");
                String sender = rs.getString("SENDER");
                String decrypted = USERSERVICE.decrypt(rs.getString("MESSAGE"));
                Timestamp unlocked = rs.getTimestamp("UNLOCK_DATETIME");

                System.out.println("\n📩 FROM: " + sender);
                System.out.println("🔓 UNLOCKED ON: " + unlocked);
                System.out.println("✉️ MESSAGE: " + decrypted);

                // Mark message as read
                markMessageAsRead(messageId);
            }

            if (count == 0) {
                System.out.println("📭 No messages got unlocked.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // Mark message as read
    private static void markMessageAsRead(int messageId) {
        String updateQuery = "UPDATE messagesystem SET IS_OPEN = TRUE WHERE ID = ?";

        try (Connection conn = DBconnection.getconnection();
             PreparedStatement ps = conn.prepareStatement(updateQuery)) {

            ps.setInt(1, messageId);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Error updating read status: " + e.getMessage());
        }
    }
}

