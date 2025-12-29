package votecreate.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Poll {
    private String poolId;
    private String question;
    private Boolean active;
    private String dateClosed;
}
