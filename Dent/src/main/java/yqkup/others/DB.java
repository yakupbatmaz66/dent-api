package yqkup.others;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DB {
	private static final ThreadLocal<Connection> threadLocalConnection = new ThreadLocal<>();

	public static Connection baglan(String host, String kullaniciAdi, String sifre) throws SQLException {
		Connection connection = DriverManager.getConnection(host, kullaniciAdi, sifre);
		connection.setAutoCommit(false); // setAutoCommit ayarı
		threadLocalConnection.set(connection);
		return connection;
	}

	public static Connection getCurrentConnection() {
		return threadLocalConnection.get();
	}

	public static void kapat() {
		Connection connection = threadLocalConnection.get();
		if (connection != null) {
			try {
				connection.close();
				threadLocalConnection.remove();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void preparedStatementKapat(PreparedStatement preparedStatement) {
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}