package br.com.bruno.cryptoapp.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "coin")
public class CoinEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 100)
    private String name;

    @Column(columnDefinition="Decimal(15,8)")
    @Type(type = "big_decimal")
    private BigDecimal price;

    @Column(columnDefinition="Decimal(15,8)")
    private BigDecimal quantity;

    private Timestamp createDateTime;
    private Timestamp updateDateTime;

    @PrePersist
    @PreUpdate
    public void PrecisionConvertion() {
        if (this.price != null) {
            this.price = this.price.setScale(8, RoundingMode.HALF_UP);
        }
        if (this.quantity != null) {
            this.quantity = this.quantity.setScale(8, RoundingMode.HALF_UP);
        }
    }

}