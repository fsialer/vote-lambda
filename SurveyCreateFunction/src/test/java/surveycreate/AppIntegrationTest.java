package surveycreate;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import surveycreate.models.Option;
import surveycreate.models.VoteEvent;
import surveycreate.repository.impl.QuestionRepositoryImpl;
import surveycreate.services.SaveQuestionUseCase;
import surveycreate.services.impl.VoteService;

import java.util.Set;

import static org.junit.Assert.*;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB;

public class AppIntegrationTest {

    @ClassRule
    public static LocalStackContainer localstack = new LocalStackContainer(DockerImageName.parse("localstack/localstack:2.3"))
            .withServices(DYNAMODB);

    @Mock
    private Context mockContext;
    
    private App app;
    private ObjectMapper objectMapper;
    private static AmazonDynamoDB dynamoDB;

    @BeforeClass
    public static void setUpClass() {
        dynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                localstack.getEndpointOverride(DYNAMODB).toString(),
                                localstack.getRegion()
                        )
                )
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(localstack.getAccessKey(), localstack.getSecretKey())
                        )
                )
                .build();

        // Create test table
        createTestTable();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        
        // Create App with test DynamoDB client
        app = new App() {
            @Override
            protected SaveQuestionUseCase createVoteService() {
                QuestionRepositoryImpl questionRepo = new QuestionRepositoryImpl() {
                    @Override
                    protected AmazonDynamoDB getDynamoDBClient() {
                        return dynamoDB;
                    }
                    
                    @Override
                    protected String getTableName() {
                        return "TestVoteTable";
                    }
                };
                return new VoteService(questionRepo, null);
            }
        };
    }

    private static void createTestTable() {
        try {
            CreateTableRequest request = new CreateTableRequest()
                    .withTableName("TestVoteTable")
                    .withKeySchema(
                            new KeySchemaElement("PK", KeyType.HASH),
                            new KeySchemaElement("SK", KeyType.RANGE)
                    )
                    .withAttributeDefinitions(
                            new AttributeDefinition("PK", ScalarAttributeType.S),
                            new AttributeDefinition("SK", ScalarAttributeType.S)
                    )
                    .withBillingMode(BillingMode.PAY_PER_REQUEST);

            dynamoDB.createTable(request);
        } catch (Exception e) {
            // Table might already exist
        }
    }

    @Test
    public void testSuccessfulSurveyCreation() throws Exception {
        VoteEvent voteEvent = VoteEvent.builder()
            .question("What's your favorite cloud provider?")
            .options(Set.of(
                Option.builder().optionId(1).description("AWS").build(),
                Option.builder().optionId(2).description("Azure").build()
            ))
            .build();

        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setBody(objectMapper.writeValueAsString(voteEvent));

        APIGatewayProxyResponseEvent response = app.handleRequest(request, mockContext);

        assertEquals(201, response.getStatusCode().intValue());
        assertTrue(response.getBody().contains("Survey created successfully"));
        assertTrue(response.getBody().contains("surveyId"));
    }
}