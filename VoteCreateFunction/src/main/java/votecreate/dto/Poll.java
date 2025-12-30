package votecreate.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Poll {
    private String pollId;
    private String question;
    private Boolean active;
    private String dateClosed;
    private List<Option> options;

    @Override
    public String toString() {
        return "Poll{" +
                "pollId='" + pollId + '\'' +
                ", question='" + question + '\'' +
                ", active=" + active +
                ", dateClosed='" + dateClosed + '\'' +
                '}';
    }
}
