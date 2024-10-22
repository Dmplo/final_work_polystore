package dev.plotnikov.polystore.entities;

import com.fasterxml.jackson.annotation.JsonView;
import dev.plotnikov.polystore.util.Views;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Type {

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

    @Getter
    @Column(unique = true)
    @JsonView(Views.MinParams.class)
    private String name;

    @OneToMany(
            mappedBy = "type",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SizeType> sizes = new ArrayList<>();

    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    public Type(String name) {
        this.name = name;
    }
}
