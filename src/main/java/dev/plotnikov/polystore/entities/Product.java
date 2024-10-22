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
@Table(name = "products")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE")
@Data
@ToString(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Id.class)
    private Long id;

    @Column(nullable = false)
    @JsonView(Views.MinParams.class)
    protected String name;

    @JsonView(Views.MinParams.class)
    protected String other;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "size_id", nullable = false)
    private Size size;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<AddressProduct> addresses = new ArrayList<>();

    public int getAddressesQty() {
        return addresses.size();
    }

}
