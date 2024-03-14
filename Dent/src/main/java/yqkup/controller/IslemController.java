package yqkup.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import yqkup.beans.Islem;
import yqkup.others.DB;
import yqkup.others.Variables;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/islem")
public class IslemController extends DB {

	@GetMapping("/liste")
	private List<Islem> getAll() {
		List<Islem> list = new ArrayList<>();
		String query = "SELECT * FROM " + Variables.TABLE_ISLEM + " order by id asc";

		try (Connection connection = baglan(Variables.DB_HOST, Variables.DB_USERNAME, Variables.DB_PASSWORD);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {

			while (resultSet.next()) {
				Islem islem = new Islem();
				islem.setId(resultSet.getInt("id"));
				islem.setHastaid(resultSet.getInt("hastaid"));
				islem.setIslem(resultSet.getString("islem"));
				list.add(islem);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
