package votecreate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteRequest {
    @JsonProperty("pool_id")
    private String poolId;
    @JsonProperty("option_id")
    private String optionId;
}
