package dev.plotnikov.polystore.entities.DTOs;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonView;
import dev.plotnikov.polystore.util.Views;
import dev.plotnikov.polystore.util.EventType;
import dev.plotnikov.polystore.util.ObjectType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonView(Views.Id.class)
public class WsEventDTO {
    private ObjectType objectType;
    private EventType eventType;
    @JsonRawValue
    private String body;
}
