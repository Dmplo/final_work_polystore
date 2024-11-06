package dev.plotnikov.polystore.entities;

import com.fasterxml.jackson.annotation.JsonView;
import dev.plotnikov.polystore.util.Views;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    @JsonView(Views.Id.class)
    private Long id;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    @JsonView(Views.Full.class)
    private LocalDate updatedAt;

    @Column(unique = true, nullable = false)
    @JsonView(Views.MinParams.class)
    private String name;

    @Column(nullable = false)
    @JsonView(Views.MinParams.class)
    @Min(value = 1, message = "Количество ярусов должно быть не меньше 1")
    private int capacity;

    @OneToMany(
            mappedBy = "address",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<AddressProduct> products = new ArrayList<>();

    public Address(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }
}
