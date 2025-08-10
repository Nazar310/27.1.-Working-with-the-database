package org.example;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:my_database.db";

        try {
            try (Connection conn = DriverManager.getConnection(url)) {
                System.out.println("✅ Підключення до БД успішне!");

                String createTable = """
                    CREATE TABLE IF NOT EXISTS users (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT,
                        age INTEGER,
                        email TEXT
                    );
                    """;
                conn.createStatement().execute(createTable);

                String insertSQL = "INSERT INTO users(name, age, email) VALUES (?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    pstmt.setString(1, "John");
                    pstmt.setInt(2, 30);
                    pstmt.setString(3, "john@example.com");
                    pstmt.executeUpdate();

                    pstmt.setString(1, "Alice");
                    pstmt.setInt(2, 25);
                    pstmt.setString(3, "alice@example.com");
                    pstmt.executeUpdate();

                    pstmt.setString(1, "Bob");
                    pstmt.setInt(2, 35);
                    pstmt.setString(3, "bob@example.com");
                    pstmt.executeUpdate();
                }

                System.out.println("\n📋 Всі користувачі:");
                try (ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM users")) {
                    while (rs.next()) {
                        System.out.println(rs.getInt("id") + " | " +
                                rs.getString("name") + " | " +
                                rs.getInt("age") + " | " +
                                rs.getString("email"));
                    }
                }

                String deleteSQL = "DELETE FROM users WHERE name = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
                    pstmt.setString(1, "Bob");
                    pstmt.executeUpdate();
                }

                System.out.println("\n📋 Після видалення Bob:");
                try (ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM users")) {
                    while (rs.next()) {
                        System.out.println(rs.getInt("id") + " | " +
                                rs.getString("name") + " | " +
                                rs.getInt("age") + " | " +
                                rs.getString("email"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Помилка роботи з БД: " + e.getMessage());
        }
    }
}
