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
@Table(name = "flanges")
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Flange {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Id.class)
    private Long id;

    @Column(unique = true, nullable = false)
    @JsonView(Views.MinParams.class)
    private int name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonView(Views.Full.class)
    @JoinColumn(name = "type_id", nullable = false)
    private Type type;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    @JsonView(Views.Full.class)
    private LocalDate updatedAt;

    public Flange(int name, Type type) {
        this.name = name;
        this.type = type;
    }
}
