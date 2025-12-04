package votecreate.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionEvent {
    @JsonProperty("survey_id")
    @NotEmpty(message = "survey_id is required.")
    private String surveyId;
    @JsonProperty("option_id")
    @NotEmpty(message = "option_id is required.")
    private String optionId;
}
