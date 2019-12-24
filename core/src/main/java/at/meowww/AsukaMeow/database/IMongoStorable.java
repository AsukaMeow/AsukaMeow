package at.meowww.AsukaMeow.database;

import com.mongodb.client.MongoDatabase;

public interface IMongoStorable {

    void initCollection (MongoDatabase db);

}
