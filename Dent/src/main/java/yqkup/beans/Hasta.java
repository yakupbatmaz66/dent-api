package yqkup.beans;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hasta {
	private Integer id;
	private String adi;
	private String soyadi;
	private String tckimlik;
	private Date dogum;
	private Date kayit;
}
