package at.meowww.AsukaMeow.item.feature;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class Feature implements IFeature {

    public static JsonObject serialize(Feature feature) {
        return null;
    }

    public static Feature deserialize(JsonElement jsonEle) {
        JsonObject jsonObj = jsonEle.getAsJsonObject();
        return null;
    }

}
