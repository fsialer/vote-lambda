package surveycreate.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteEvent {
    @JsonProperty("question")
    @NotEmpty(message = "Question is required.")
    private String question;
    @JsonProperty("options")
    @Size(min = 2, message = "At least two options are required.")
    @Valid
    private Set<Option> options;
}
