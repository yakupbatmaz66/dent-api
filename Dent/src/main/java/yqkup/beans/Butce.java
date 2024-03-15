package yqkup.beans;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Butce {
	private Integer id;
	private Hasta hasta;
	private double alinacak;
	private double alinan;
	private Date tarih;
}