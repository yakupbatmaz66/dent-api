package yqkup.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
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

import yqkup.beans.Kullanici;
import yqkup.others.*;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/kullanici")
public class KullaniciController extends DB {

	@GetMapping("/liste")
	private List<Kullanici> getAll() {
		List<Kullanici> list = new ArrayList<>();
		String query = "SELECT * FROM " + Variables.TABLE_PERSONEL + " order by id asc";

		try (Connection connection = baglan(Variables.DB_HOST, Variables.DB_USERNAME, Variables.DB_PASSWORD);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {

			while (resultSet.next()) {
				Kullanici kullanici = new Kullanici();
				kullanici.setId(resultSet.getInt("id"));
				kullanici.setAdi(resultSet.getString("adi"));
				kullanici.setKadi(resultSet.getString("kadi"));
				kullanici.setSifre(resultSet.getString("sifre"));
				kullanici.setYetki(resultSet.getString("yetki"));
				kullanici.setHatirlaticisoru(resultSet.getString("hatirlaticisoru"));
				kullanici.setHatirlaticicevap(resultSet.getString("hatirlaticicevap"));
				list.add(kullanici);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@PostMapping("/tek")
	private Kullanici getOne(@RequestBody Map<String, String> parametre) {
		String query = "SELECT * FROM " + Variables.TABLE_PERSONEL + " WHERE id = ?";
		Kullanici kullanici = new Kullanici();

		try (Connection connection = baglan(Variables.DB_HOST, Variables.DB_USERNAME, Variables.DB_PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, Integer.parseInt(parametre.get("id")));

			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					kullanici.setId(resultSet.getInt("id"));
					kullanici.setAdi(resultSet.getString("adi"));
					kullanici.setKadi(resultSet.getString("kadi"));
					kullanici.setSifre(resultSet.getString("sifre"));
					kullanici.setYetki(resultSet.getString("yetki"));
					kullanici.setHatirlaticisoru(resultSet.getString("hatirlaticisoru"));
					kullanici.setHatirlaticicevap(resultSet.getString("hatirlaticicevap"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kullanici;
	}

	@PostMapping("/ekle")
	private Map<String, String> Ekle(@RequestBody Map<String, String> parametre) throws ParseException {
		String mesaj = "";
		Map<String, String> map = new HashMap<String, String>();
		String query = "INSERT INTO " + Variables.TABLE_PERSONEL
				+ " (adi,kadi,sifre,yetki,hatirlaticisoru,hatirlaticicevap) VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection connection = baglan(Variables.DB_HOST, Variables.DB_USERNAME, Variables.DB_PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			int i = 0;
			preparedStatement.setString(++i, parametre.get("adi"));
			preparedStatement.setString(++i, parametre.get("kadi"));
			preparedStatement.setString(++i, parametre.get("sifre"));
			preparedStatement.setString(++i, parametre.get("yetki"));
			preparedStatement.setString(++i, parametre.get("hatirlaticisoru"));
			preparedStatement.setString(++i, parametre.get("hatirlaticicevap"));

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows > 0) {
				connection.commit();
				mesaj = "Kullanıcı başarıyla eklendi.";
			} else {
				connection.rollback();
				mesaj = "Kullanıcı eklenirken bir hata oluştu.";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			mesaj = "Kullanıcı eklenirken bir SQL hatası oluştu: " + e.getMessage();
		}
		map = new HashMap<String, String>();
		map.put("mesaj", mesaj);
		return map;
	}

	@PostMapping("/guncelle")
	private Map<String, String> Guncelle(@RequestBody Map<String, String> parametre) throws ParseException {
		String mesaj = "";
		Map<String, String> map = new HashMap<String, String>();
		String query = "UPDATE " + Variables.TABLE_PERSONEL
				+ " SET adi=?, kadi=?, sifre=?, yetki=?, hatirlaticisoru=?, hatirlaticicevap=? WHERE id=?";

		try (Connection connection = baglan(Variables.DB_HOST, Variables.DB_USERNAME, Variables.DB_PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			int i = 0;
			preparedStatement.setString(++i, parametre.get("adi"));
			preparedStatement.setString(++i, parametre.get("kadi"));
			preparedStatement.setString(++i, parametre.get("sifre"));
			preparedStatement.setString(++i, parametre.get("yetki"));
			preparedStatement.setString(++i, parametre.get("hatirlaticisoru"));
			preparedStatement.setString(++i, parametre.get("hatirlaticicevap"));
			preparedStatement.setInt(++i, Integer.parseInt(parametre.get("id")));

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows > 0) {
				connection.commit();
				mesaj = "Kullanıcı başarıyla güncellendi.";
			} else {
				connection.rollback();
				mesaj = "Kullanıcı bulunamadı.";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			mesaj = "Kullanıcı güncellenirken bir SQL hatası oluştu: " + e.getMessage();
		}
		map = new HashMap<String, String>();
		map.put("mesaj", mesaj);
		return map;
	}

	@PostMapping("/sil")
	private Map<String, String> Sil(@RequestBody Map<String, String> parametre) {
		String mesaj = "";
		Map<String, String> map = new HashMap<String, String>();
		String query = "DELETE FROM " + Variables.TABLE_PERSONEL + " WHERE id = ?";

		try (Connection connection = baglan(Variables.DB_HOST, Variables.DB_USERNAME, Variables.DB_PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, Integer.parseInt(parametre.get("id")));

			int rowsAffected = preparedStatement.executeUpdate();

			if (rowsAffected > 0) {
				connection.commit();
				mesaj = "Kullanıcı silme başarılı.";
			} else {
				connection.rollback();
				mesaj = "Kullanıcı bulunamadı.";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			mesaj = "Kullanıcı silinirken bir SQL hatası oluştu: " + e.getMessage();
		}
		map = new HashMap<String, String>();
		map.put("mesaj", mesaj);
		return map;
	}
}