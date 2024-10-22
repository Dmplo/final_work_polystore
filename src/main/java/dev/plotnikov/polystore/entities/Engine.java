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
@DiscriminatorValue("E")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Engine extends Product {
    @ManyToOne
    @JoinColumn(name = "speed_id")
    private Speed speed;

    @ManyToOne
    @JoinColumn(name = "power_id")
    private Power power;

    @ManyToOne
    @JoinColumn(name = "flange_id")
    private Flange flange;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Engine engine = (Engine) o;
        return Objects.equals(speed, engine.speed) && Objects.equals(power, engine.power) && Objects.equals(flange, engine.flange) && Objects.equals(super.getSize().getId(), engine.getSize().getId()) && Objects.equals(super.getName(), engine.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getSize().getId(), speed, power, flange);
    }
}
