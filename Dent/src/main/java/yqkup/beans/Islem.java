package yqkup.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Islem {
	private Integer id;
	private Hasta hasta;
	private String islem;
}