package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "product",
        indexes = {
                @Index(name = "barcode_idx", columnList = "barcode",unique= true),
        })
public class ProductPojo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String barcode;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Double mrp;
    @Column(nullable = false)
    private Integer brandId;

    @Override
    public String toString() {
        return "ProductPojo{" +
                "id=" + id +
                ", barcode='" + barcode + '\'' +
                ", name='" + name + '\'' +
                ", mrp=" + mrp +
                ", brandId=" + brandId +
                '}';
    }
}
