package ge.kuku.movietable.data;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;

import java.util.List;

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
        mapper.save(item, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.UPDATE_SKIP_NULL_ATTRIBUTES));
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