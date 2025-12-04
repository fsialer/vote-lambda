package surveycreate.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Option {
    @JsonProperty("option_id")
    @NotNull(message = "option_id is required.")
    private Integer optionId;
    @JsonProperty("description")
    @NotBlank(message = "description is required.")
    private String description;
}
