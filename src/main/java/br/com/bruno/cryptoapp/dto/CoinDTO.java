package br.com.bruno.cryptoapp.dto;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoinDTO {

    private int id;
    private String name;
    private BigDecimal price;
    private BigDecimal quantity;
    private Timestamp createDateTime;
    private Timestamp updateDateTime;

}
