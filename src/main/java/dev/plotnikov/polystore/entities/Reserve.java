package dev.plotnikov.polystore.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "reserves")
@NoArgsConstructor
@AllArgsConstructor
public class Reserve {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @EqualsAndHashCode.Include
  private Long id;

  @CreationTimestamp
  private LocalDate createdAt;

  @UpdateTimestamp
  private LocalDate updatedAt;

  @Column(nullable = false)
  @Min(value = 1, message = "Количество должно быть не меньше 1")
  private int qty;

  private String comment;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "address_id", nullable = false, updatable = false)
  private AddressProduct addressProduct;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", nullable = false, updatable = false)
  private User user;

}
