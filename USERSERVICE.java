package TimeCapsuleMessagingSystem;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.sql.*;
import java.util.Base64;

public class USERSERVICE {

    private static final String SECRET_KEY = System.getenv("AES_SECRET_KEY") != null
            ? System.getenv("AES_SECRET_KEY")
            : "1234567890abcdef";


    public static String hashpassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes());
            byte[] result = messageDigest.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : result) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("❌ Hashing error: " + e.getMessage());
        }
        return "";
    }

    // ✅ Encrypt using AES
    public static String encrypt(String message) throws Exception {
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes()));
    }

    // ✅ Decrypt using AES
    public static String decrypt(String encryptedMessage) throws Exception {
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedMessage)));
    }

    // ✅ Login method
    public static boolean Login(String userName, String password) {
        String query = "SELECT * FROM user WHERE USERNAME = ? AND PASSWORD = ?";
        try (Connection connection = DBconnection.getconnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, userName);
            ps.setString(2, hashpassword(password));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("✅ Login successful!");
                    return true;
                } else {
                    System.out.println("❌ Login failed! Incorrect username or password.");
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Login error: " + e.getMessage());
        }
        return false;
    }

    // ✅ Register method
    public static boolean Register(String userName, String password) {
        if (password.length() != 6) {
            System.out.println("❌ Password must be exactly 6 characters.");
            return false;
        }

        String query = "INSERT INTO user (USERNAME, PASSWORD) VALUES (?, ?)";
        try (Connection connection = DBconnection.getconnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, userName);
            ps.setString(2, hashpassword(password));

            int row = ps.executeUpdate();
            if (row > 0) {
                System.out.println("✅ Registration successful!");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("❌ Registration error: " + e.getMessage());
        }
        return false;
    }

    // ✅ Get user ID by username
    public static int getUserId(String username) {
        String query = "SELECT ID FROM user WHERE USERNAME = ?";
        try (Connection connection = DBconnection.getconnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID");
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ getUserId error: " + e.getMessage());
        }
        return -1;
    }

    // ✅ Get username by ID
    public static String getUserNameByID(int ID) {
        String query = "SELECT USERNAME FROM user WHERE ID = ?";
        try (Connection connection = DBconnection.getconnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, ID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("USERNAME");
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ getUserNameByID error: " + e.getMessage());
        }
        return null;
    }
}


