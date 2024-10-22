package dev.plotnikov.polystore.entities;

import com.fasterxml.jackson.annotation.JsonView;
import dev.plotnikov.polystore.util.Views;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "powers")
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Power {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Id.class)
    private Long id;

    @Column(unique = true, nullable = false)
    @JsonView(Views.MinParams.class)
    private double name;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    @JsonView(Views.Full.class)
    private LocalDate updatedAt;

    public Power(double name) {
        this.name = name;
    }
}
