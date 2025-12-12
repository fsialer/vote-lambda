package votecreate.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteRequest {
    @JsonProperty("pool_id")
    @NotEmpty(message = "pool_id is required.")
    private String poolId;
    @JsonProperty("option_id")
    @NotEmpty(message = "option_id is required.")
    private String optionId;
}
