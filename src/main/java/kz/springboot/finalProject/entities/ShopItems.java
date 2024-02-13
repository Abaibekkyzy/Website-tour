package kz.springboot.finalProject.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "t_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name", length = 200)
    private String name;

    @Column(name="price", length = 200)
    private int price;

    @Column(name="amount", length = 200)
    private int amount;

    @ManyToOne(fetch = FetchType.EAGER)
    private Countries country;

}
