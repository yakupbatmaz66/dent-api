package yqkup.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import yqkup.beans.Hasta;
import yqkup.others.*;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/hasta")
public class HastaController extends DB {

	@GetMapping("/liste")
	public static List<Hasta> getAll() {
		List<Hasta> list = new ArrayList<>();
		String query = "SELECT * FROM " + Variables.TABLE_HASTA + " order by id asc";

		try (Connection connection = baglan(Variables.DB_HOST, Variables.DB_USERNAME, Variables.DB_PASSWORD);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {

			while (resultSet.next()) {
				Hasta hasta = new Hasta();
				hasta.setId(resultSet.getInt("id"));
				hasta.setAdi(resultSet.getString("adi"));
				hasta.setSoyadi(resultSet.getString("soyadi"));
				hasta.setTckimlik(resultSet.getString("tckimlik"));
                hasta.setDogum(resultSet.getDate("dogum"));
                hasta.setKayit(resultSet.getDate("kayit"));
				list.add(hasta);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@PostMapping("/tek")
	public static Hasta getOne(@RequestBody Map<String, String> parametre) {
		String query = "SELECT * FROM " + Variables.TABLE_HASTA + " WHERE id = ?";
		Hasta hasta = new Hasta();

		try (Connection connection = baglan(Variables.DB_HOST, Variables.DB_USERNAME, Variables.DB_PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, Integer.parseInt(parametre.get("id")));

			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					hasta.setId(resultSet.getInt("id"));
					hasta.setAdi(resultSet.getString("adi"));
					hasta.setSoyadi(resultSet.getString("soyadi"));
					hasta.setTckimlik(resultSet.getString("tckimlik"));
	                hasta.setDogum(resultSet.getDate("dogum"));
	                hasta.setKayit(resultSet.getDate("kayit"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hasta;
	}
	
	public static Hasta Tek(Integer id) {
		String query = "SELECT * FROM " + Variables.TABLE_HASTA + " WHERE id = ?";
		Hasta hasta = new Hasta();

		try (Connection connection = baglan(Variables.DB_HOST, Variables.DB_USERNAME, Variables.DB_PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, id);

			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					hasta.setId(resultSet.getInt("id"));
					hasta.setAdi(resultSet.getString("adi"));
					hasta.setSoyadi(resultSet.getString("soyadi"));
					hasta.setTckimlik(resultSet.getString("tckimlik"));
	                hasta.setDogum(resultSet.getDate("dogum"));
	                hasta.setKayit(resultSet.getDate("kayit"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hasta;
	}

	@PostMapping("/ekle")
	private Map<String, String> Ekle(@RequestBody Map<String, String> parametre) throws ParseException {
		String mesaj = "";
		Map<String, String> map = new HashMap<String, String>();
		String query = "INSERT INTO " + Variables.TABLE_HASTA
				+ " (adi,soyadi,tckimlik,dogum,kayit) VALUES (?, ?, ?, ?, ?)";

		try (Connection connection = baglan(Variables.DB_HOST, Variables.DB_USERNAME, Variables.DB_PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			int i = 0;
			preparedStatement.setString(++i, parametre.get("adi"));
			preparedStatement.setString(++i, parametre.get("soyadi"));
			preparedStatement.setString(++i, parametre.get("tckimlik"));
            preparedStatement.setDate(++i, tarihAyarla(parametre.get("dogum")));
            preparedStatement.setDate(++i, tarihAyarla(parametre.get("kayit")));

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows > 0) {
				connection.commit();
				mesaj = "Hasta başarıyla eklendi.";
			} else {
				connection.rollback();
				mesaj = "Hasta eklenirken bir hata oluştu.";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			mesaj = "Hasta eklenirken bir SQL hatası oluştu: " + e.getMessage();
		}
		map = new HashMap<String, String>();
		map.put("mesaj", mesaj);
		return map;
	}

	@PostMapping("/guncelle")
	private Map<String, String> Guncelle(@RequestBody Map<String, String> parametre) throws ParseException {
		String mesaj = "";
		Map<String, String> map = new HashMap<String, String>();
		String query = "UPDATE " + Variables.TABLE_HASTA
				+ " SET adi=?, soyadi=?, tckimlik=?, dogum=?, kayit=? WHERE id=?";

		try (Connection connection = baglan(Variables.DB_HOST, Variables.DB_USERNAME, Variables.DB_PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			int i = 0;
			preparedStatement.setString(++i, parametre.get("adi"));
			preparedStatement.setString(++i, parametre.get("soyadi"));
			preparedStatement.setString(++i, parametre.get("tckimlik"));
            preparedStatement.setDate(++i, Date.valueOf(parametre.get("dogum")));
            preparedStatement.setDate(++i, Date.valueOf(parametre.get("kayit")));
			preparedStatement.setInt(++i, Integer.parseInt(parametre.get("id")));

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows > 0) {
				connection.commit();
				mesaj = "Hasta başarıyla güncellendi.";
			} else {
				connection.rollback();
				mesaj = "Hasta bulunamadı.";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			mesaj = "Hasta güncellenirken bir SQL hatası oluştu: " + e.getMessage();
		}
		map = new HashMap<String, String>();
		map.put("mesaj", mesaj);
		return map;
	}

	@PostMapping("/sil")
	private Map<String, String> Sil(@RequestBody Map<String, String> parametre) {
		String mesaj = "";
		Map<String, String> map = new HashMap<String, String>();
		String query = "DELETE FROM " + Variables.TABLE_HASTA + " WHERE id = ?";

		try (Connection connection = baglan(Variables.DB_HOST, Variables.DB_USERNAME, Variables.DB_PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, Integer.parseInt(parametre.get("id")));

			int rowsAffected = preparedStatement.executeUpdate();

			if (rowsAffected > 0) {
				connection.commit();
				mesaj = "Hasta silme başarılı.";
			} else {
				connection.rollback();
				mesaj = "Hasta bulunamadı.";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			mesaj = "Hasta silinirken bir SQL hatası oluştu: " + e.getMessage();
		}
		map = new HashMap<String, String>();
		map.put("mesaj", mesaj);
		return map;
	}
	
	private Date tarihAyarla(String date) {
        LocalDate tarih = LocalDate.parse(date);
        return java.sql.Date.valueOf(tarih);
	}
}