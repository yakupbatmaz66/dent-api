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
import yqkup.beans.Butce;
import yqkup.others.DB;
import yqkup.others.Variables;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/butce")
public class ButceController extends DB {

	@PostMapping("/tek")
	private List<Butce> getOne(@RequestBody Map<String, String> parametre) {
		String query = "SELECT * FROM " + Variables.TABLE_BUTCE + " WHERE hastaid = ?";
		List<Butce> list = new ArrayList<>();
		
		try (Connection connection = baglan(Variables.DB_HOST, Variables.DB_USERNAME, Variables.DB_PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, Integer.parseInt(parametre.get("id")));

			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				while (resultSet.next()) {
					Butce butce = new Butce();
					butce.setId(resultSet.getInt("id"));

					Hasta hasta = HastaController.Tek(resultSet.getInt("hastaid"));
					butce.setHasta(hasta);

					butce.setAlinacak(resultSet.getDouble("alinacak"));
					butce.setAlinan(resultSet.getDouble("alinan"));
					
					butce.setTarih(resultSet.getDate("tarih"));
					list.add(butce);
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
		String query = "INSERT INTO " + Variables.TABLE_BUTCE
				+ " (hastaid,alinacak,alinan,tarih) VALUES (?, ?, ?, ?)";

		try (Connection connection = baglan(Variables.DB_HOST, Variables.DB_USERNAME, Variables.DB_PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			int i = 0;
			preparedStatement.setInt(++i, Integer.parseInt(parametre.get("hastaid")));
			preparedStatement.setDouble(++i, Double.parseDouble(parametre.get("alinacak")));
			preparedStatement.setDouble(++i, Double.parseDouble(parametre.get("alinan")));

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
}
