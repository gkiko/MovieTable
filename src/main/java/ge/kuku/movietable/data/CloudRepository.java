package ge.kuku.movietable.data;

import java.util.List;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.Builder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.SaveBehavior;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;

public class CloudRepository implements Repository {

    private static AmazonDynamoDBClient client =
            new AmazonDynamoDBClient(new EnvironmentVariableCredentialsProvider());

    public CloudRepository() {
        client.withRegion(Regions.US_WEST_2);
//        client.withEndpoint("http://localhost:8000");
    }

    @Override
    public void save(MovieItem item) {
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        Builder builder = DynamoDBMapperConfig.builder();
        builder.setSaveBehavior(SaveBehavior.UPDATE_SKIP_NULL_ATTRIBUTES);
        mapper.save(item, builder.build()); 
    }

    @Override
    public List<MovieItem> retrieve(String imdbId) {
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        MovieItem item = new MovieItem();
        item.setImdbId(imdbId);
        DynamoDBQueryExpression<MovieItem> queryExpression = new DynamoDBQueryExpression<MovieItem>()
                .withHashKeyValues(item);
        return mapper.query(MovieItem.class, queryExpression);
    }

}