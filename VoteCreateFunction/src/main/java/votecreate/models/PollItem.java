package votecreate.models;

import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Setter
@DynamoDbBean
public class PollItem {
    private String pk;
    private String sk;
    private String question;
    private Boolean active;
    private String dateClosed;
    private String text;
    private String votes;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("PK")
    public String getPk() {
        return pk;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("SK")
    public String getSk() {
        return sk;
    }


    @DynamoDbAttribute("question")
    public String getQuestion() {
        return question;
    }

    @DynamoDbAttribute("active")
    public Boolean getActive() {
        return active;
    }

    @DynamoDbAttribute("text")
    public String getText() {
        return text;
    }

    @DynamoDbAttribute("votes")
    public String getVotes() {
        return votes;
    }

    @DynamoDbAttribute("dateClosed")
    public String getDateClosed() {
        return dateClosed;
    }

}
