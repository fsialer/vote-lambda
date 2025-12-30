package votecreate.models;

import lombok.*;

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
