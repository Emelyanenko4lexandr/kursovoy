package dev.kursovoy.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "photos")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "automobile_id", nullable = false)  // Столбец для связи с автомобилем
    private Automobile automobile;  // Ссылка на автомобиль

    @Column(columnDefinition = "TEXT")
    private String photo;

    private Integer position;

    public Photo(String base64Photo, Automobile automobile, Integer position) {
        this.photo = base64Photo;
        this.automobile = automobile;
        this.position = position;
    }
}
