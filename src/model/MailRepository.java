package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MailRepository {
    private final String url = "jdbc:mysql://localhost:3307/maildb?useSSL=false&allowPublicKeyRetrieval=true";
    private final String user = "mail_user";
    private final String password = "Mail1234";

    public MailRepository() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addMail(model.Mail mail) {
        String sql = "INSERT INTO mails (sender, receiver, subject, content, tags, is_spam) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mail.getSender());
            stmt.setString(2, mail.getReceiver());
            stmt.setString(3, mail.getSubject());
            stmt.setString(4, mail.getContent());
            stmt.setString(5, String.join(",", mail.getTags()));
            stmt.setBoolean(6, mail.isSpam());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteMail(String subject) {
        String sql = "DELETE FROM mails WHERE subject = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, subject);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<model.Mail> searchBySubject(String keyword) {
        String sql = "SELECT * FROM mails WHERE LOWER(subject) LIKE ?";
        List<model.Mail> result = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword.toLowerCase() + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(mapRowToMail(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<model.Mail> wildcardSearch(String pattern) {
        String regex = pattern.replace("*", "%");
        String sql = "SELECT * FROM mails WHERE LOWER(subject) LIKE ?";
        List<model.Mail> result = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, regex.toLowerCase());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(mapRowToMail(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public int countMails() {
        String sql = "SELECT COUNT(*) FROM mails";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long countSpamMails() {
        String sql = "SELECT COUNT(*) FROM mails WHERE is_spam = true";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) return rs.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private model.Mail mapRowToMail(ResultSet rs) throws SQLException {
        model.Mail mail = new model.Mail(
                rs.getString("sender"),
                rs.getString("receiver"),
                rs.getString("subject"),
                rs.getString("content")
        );

        String tags = rs.getString("tags");
        if (tags != null && !tags.isEmpty()) {
            Arrays.stream(tags.split(",")).forEach(mail::addTag);
        }

        mail.setSpam(rs.getBoolean("is_spam"));
        return mail;
    }
}
