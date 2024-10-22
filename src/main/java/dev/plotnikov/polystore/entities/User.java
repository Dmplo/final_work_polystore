package dev.plotnikov.polystore.entities;

import com.fasterxml.jackson.annotation.JsonView;
import dev.plotnikov.polystore.util.Views;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Id.class)
    private Long id;

    @Column(nullable = false)
    @JsonView(Views.MinParams.class)
    private String firstname;

    @Column(nullable = false)
    @JsonView(Views.MinParams.class)
    private String lastname;

    @Column(unique = true, nullable = false)
    @JsonView(Views.MinParams.class)
    private String username;

    @Column(nullable = false)
    private String password;

    @JsonView(Views.MinParams.class)
    private LocalDate birthDate;

    @JsonView(Views.MinParams.class)
    private String phone;

    @JsonView(Views.MinParams.class)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonView(Views.Full.class)
    private List<Role> roles = new ArrayList<>();

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

}
