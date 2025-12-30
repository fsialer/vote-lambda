package votecreate.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Option {
    private String optionId;
    private String text;
    private long votes;
}
