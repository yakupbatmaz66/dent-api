package yqkup.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import yqkup.others.DB;
import yqkup.others.Variables;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/analiz")
public class AnalizController extends DB {

	
	
	@GetMapping("/ay")
	private  Map<Integer, Object> getAy() {
//		String query = "SELECT EXTRACT(MONTH FROM TO_DATE(TO_CHAR(tarih, 'YYYY-MM-DD'), 'YYYY-MM-DD')) AS zaman, SUM(alinacak) AS toplam_alinacak, SUM(alinan) AS toplam_alinan FROM " + Variables.TABLE_BUTCE + " GROUP BY EXTRACT(MONTH FROM TO_DATE(TO_CHAR(tarih, 'YYYY-MM-DD'), 'YYYY-MM-DD'))";
		
		String query = "WITH years AS ( SELECT DISTINCT EXTRACT(YEAR FROM TO_DATE(TO_CHAR(tarih, 'DD-MM-YYYY'), 'DD-MM-YYYY')) AS year FROM " + Variables.TABLE_BUTCE + ")"
				+ "SELECT year,"
				+ "    EXTRACT(MONTH FROM TO_DATE(TO_CHAR(tarih, 'DD-MM-YYYY'), 'DD-MM-YYYY')) AS month,"
				+ "    SUM(CASE WHEN EXTRACT(YEAR FROM TO_DATE(TO_CHAR(tarih, 'DD-MM-YYYY'), 'DD-MM-YYYY')) = year THEN alinacak END) AS alinacak,"
				+ "    SUM(CASE WHEN EXTRACT(YEAR FROM TO_DATE(TO_CHAR(tarih, 'DD-MM-YYYY'), 'DD-MM-YYYY')) = year THEN alinan END) AS alinan "
				+ "FROM " + Variables.TABLE_BUTCE + ", years "
				+ "GROUP BY year,EXTRACT(MONTH FROM TO_DATE(TO_CHAR(tarih, 'DD-MM-YYYY'), 'DD-MM-YYYY'))"
				+ "ORDER BY year, month;";
		
//		String query = "SELECT alinan, alinacak, EXTRACT(YEAR FROM tarih) AS year, EXTRACT(MONTH FROM tarih) AS month FROM " + Variables.TABLE_BUTCE;
		System.out.println(query);
		Map<Integer, Object> list = new HashMap<Integer, Object>();
		
		try (Connection connection = baglan(Variables.DB_HOST, Variables.DB_USERNAME, Variables.DB_PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			int a = 0;
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				while (resultSet.next()) {
					if(resultSet.getDouble("alinacak") > 0 && resultSet.getDouble("alinan") > 0) {
						Map<String, Object> map = new HashMap<String, Object>();
						
						map.put("alinacak", resultSet.getDouble("alinacak"));
						map.put("alinan", resultSet.getDouble("alinan"));
						map.put("ay", resultSet.getInt("month"));
						map.put("yil", resultSet.getInt("year"));
						
						list.put(a++, map);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	@GetMapping("/hasta")
	private List<Map<String, Object>> getHasta() {
		String query = "SELECT EXTRACT(YEAR FROM kayit) AS yil, EXTRACT(MONTH FROM kayit) AS ay, COUNT(*) AS sayi FROM " + Variables.TABLE_HASTA + " GROUP BY EXTRACT(YEAR FROM kayit), EXTRACT(MONTH FROM kayit) ORDER BY yil, ay;";
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		try (Connection connection = baglan(Variables.DB_HOST, Variables.DB_USERNAME, Variables.DB_PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				while (resultSet.next()) {
	        		Map<String, Object> map = new HashMap<String, Object>();
					map.put("yil", resultSet.getInt("yil"));
					map.put("ay", resultSet.getInt("ay"));
					map.put("sayi", resultSet.getInt("sayi"));
					list.add(map);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
