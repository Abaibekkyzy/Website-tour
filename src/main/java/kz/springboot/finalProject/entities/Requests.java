package kz.springboot.finalProject.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="t_requests")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Requests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Users user;

    @Column(name="phNumber", length = 200)
    private String phNumber;

    @Column(name="date", length = 200)
    private String date;

    @ManyToOne(fetch = FetchType.EAGER)
    private ShopItems shopItems;
}
