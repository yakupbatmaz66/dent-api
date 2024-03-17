package yqkup.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import yqkup.beans.Hasta;
import yqkup.beans.Islem;
import yqkup.others.DB;
import yqkup.others.Variables;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/islem")
public class IslemController extends DB {
	
	@PostMapping("/tek")
	private List<Islem> getOne(@RequestBody Map<String, String> parametre) {
		String query = "SELECT * FROM " + Variables.TABLE_ISLEM + " WHERE hastaid = ?";
		List<Islem> list = new ArrayList<>();

		try (Connection connection = baglan(Variables.DB_HOST, Variables.DB_USERNAME, Variables.DB_PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, Integer.parseInt(parametre.get("hastaid")));

			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				while (resultSet.next()) {
					Islem islem = new Islem();
					islem.setId(resultSet.getInt("id"));

					Hasta hasta = HastaController.Tek(resultSet.getInt("hastaid"));
					islem.setHasta(hasta);
					
					islem.setIslem(resultSet.getString("islem"));
					list.add(islem);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@PostMapping("/ekle")
	private Map<String, String> Ekle(@RequestBody Map<String, String> parametre) throws ParseException {
		String mesaj = "";
		Map<String, String> map = new HashMap<String, String>();
		String query = "INSERT INTO " + Variables.TABLE_ISLEM
				+ " (hastaid,islem) VALUES (?, ?)";

		try (Connection connection = baglan(Variables.DB_HOST, Variables.DB_USERNAME, Variables.DB_PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			int i = 0;
			preparedStatement.setInt(++i, Integer.parseInt(parametre.get("hastaid")));
			preparedStatement.setString(++i, parametre.get("islem"));

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows > 0) {
				connection.commit();
				mesaj = "İşlem başarıyla eklendi.";
			} else {
				connection.rollback();
				mesaj = "İşlem eklenirken bir hata oluştu.";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			mesaj = "İşlem eklenirken bir SQL hatası oluştu: " + e.getMessage();
		}
		map = new HashMap<String, String>();
		map.put("mesaj", mesaj);
		return map;
	}
	
	@PostMapping("/guncelle")
	private Map<String, String> Guncelle(@RequestBody Map<String, String> parametre) throws ParseException {
		String mesaj = "";
		Map<String, String> map = new HashMap<String, String>();
		String query = "UPDATE " + Variables.TABLE_ISLEM
				+ " SET hastaid=?, islem=? WHERE id=?";

		try (Connection connection = baglan(Variables.DB_HOST, Variables.DB_USERNAME, Variables.DB_PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			int i = 0;
			preparedStatement.setInt(++i, Integer.parseInt(parametre.get("hastaid")));
			preparedStatement.setString(++i, parametre.get("islem"));
			preparedStatement.setInt(++i, Integer.parseInt(parametre.get("id")));

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows > 0) {
				connection.commit();
				mesaj = "İşlem başarıyla eklendi.";
			} else {
				connection.rollback();
				mesaj = "İşlem eklenirken bir hata oluştu.";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			mesaj = "İşlem eklenirken bir SQL hatası oluştu: " + e.getMessage();
		}
		map = new HashMap<String, String>();
		map.put("mesaj", mesaj);
		return map;
	}
	
	@PostMapping("/sil")
	private Map<String, String> Sil(@RequestBody Map<String, String> parametre) {
		String mesaj = "";
		Map<String, String> map = new HashMap<String, String>();
		String query = "DELETE FROM " + Variables.TABLE_ISLEM + " WHERE id = ?";

		try (Connection connection = baglan(Variables.DB_HOST, Variables.DB_USERNAME, Variables.DB_PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, Integer.parseInt(parametre.get("id")));

			int rowsAffected = preparedStatement.executeUpdate();

			if (rowsAffected > 0) {
				connection.commit();
				mesaj = "İşlem silme başarılı.";
			} else {
				connection.rollback();
				mesaj = "İşlem bulunamadı.";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			mesaj = "İşlem silinirken bir SQL hatası oluştu: " + e.getMessage();
		}
		map = new HashMap<String, String>();
		map.put("mesaj", mesaj);
		return map;
	}
}
