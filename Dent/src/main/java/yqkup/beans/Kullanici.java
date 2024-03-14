package yqkup.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kullanici {
    private Integer id;
    private String adi;
    private String kadi;
    private String sifre;
    private String yetki;
    private String hatirlaticisoru;
    private String hatirlaticicevap;
}