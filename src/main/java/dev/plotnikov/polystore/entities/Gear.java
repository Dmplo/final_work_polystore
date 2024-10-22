package dev.plotnikov.polystore.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@DiscriminatorValue("G")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Gear extends Product {

    private Double ratio;

    @ManyToOne
    @JoinColumn(name = "pam_id")
    private Pam pam;

    @ManyToOne
    @JoinColumn(name = "flange_id")
    private Flange flange;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Gear gear = (Gear) o;
        return Objects.equals(ratio, gear.ratio) && Objects.equals(pam, gear.pam) && Objects.equals(flange, gear.flange) && Objects.equals(super.getSize().getId(), gear.getSize().getId()) && Objects.equals(super.getName(), gear.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getSize().getId(), ratio, pam, flange);
    }
}
