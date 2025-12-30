package votecreate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteRequest {
    @JsonProperty("poll_id")
    private String pollId;
    @JsonProperty("option_id")
    private String optionId;
}
