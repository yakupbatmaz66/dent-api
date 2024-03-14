package yqkup.others;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("file:./db.properties")
public class Variables {
	static AnnotationConfigApplicationContext context;
	static Environment environment;

	public static String DB_HOST = null;
	public static String DB_USERNAME = null;
	public static String DB_PASSWORD = null;
	
	public static String TABLE_KULLANICI = null;
	public static String TABLE_HASTA = null;
	public static String TABLE_ISLEM = null;
	public static String TABLE_BUTCE = null;
	
	public static void GetVariables() {
		context = new AnnotationConfigApplicationContext();
		try {
			context.register(Variables.class);
			context.refresh();
			environment = context.getEnvironment();

			DB_HOST = environment.getProperty("db_host");
			DB_USERNAME = environment.getProperty("db_username");
			DB_PASSWORD = environment.getProperty("db_password");
			
			TABLE_KULLANICI = environment.getProperty("tbl_kullanicilar");
			TABLE_HASTA = environment.getProperty("tbl_hastalar");
			TABLE_ISLEM = environment.getProperty("tbl_islemler");
			TABLE_BUTCE = environment.getProperty("tbl_butce");
			
			System.out.println("Değişkenler Alındı.");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}