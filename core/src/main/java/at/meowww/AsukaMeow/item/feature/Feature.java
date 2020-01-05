package at.meowww.AsukaMeow.item.feature;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class Feature implements IFeature {

    public static JsonObject serialize(Feature feature) {
        if (feature instanceof FeatureTeleport)
            return FeatureTeleport.serialize(feature);
        else if (feature instanceof FeatureBinding)
            return FeatureBinding.serialize(feature);
        return null;
    }

    public static Feature deserialize(JsonElement jsonEle) {
        JsonObject jsonObj = jsonEle.getAsJsonObject();
        switch (jsonObj.get("name").getAsString().toUpperCase()) {
            case FeatureTeleport.name:
                return FeatureTeleport.deserialize(jsonObj);
            case FeatureBinding.name:
                return FeatureBinding.deserialize(jsonObj);
        }
        return null;
    }

}
