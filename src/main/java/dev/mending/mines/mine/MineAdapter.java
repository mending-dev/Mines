package dev.mending.mines.mine;

import com.google.gson.*;
import org.bukkit.Location;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MineAdapter implements JsonSerializer<Mine>, JsonDeserializer<Mine> {

    @Override
    public JsonElement serialize(Mine mine, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject obj = new JsonObject();

        // Meta
        obj.addProperty("displayName", mine.getDisplayName());
        obj.addProperty("resetInterval", mine.getResetInterval());

        // Content
        JsonArray contentArray = new JsonArray();
        for (MineContent content : mine.getContent()) {
            JsonObject contentObj = new JsonObject();
            contentObj.addProperty("blockType", content.getBlockData().getMaterial().toString());
            contentObj.addProperty("chance", content.getChance());
            contentArray.add(contentObj);
        }
        obj.add("content", contentArray);

        // Positions
        JsonObject positions = new JsonObject();
        positions.add(String.valueOf(1), context.serialize(mine.getPos1()));
        positions.add(String.valueOf(2), context.serialize(mine.getPos2()));
        obj.add("positions", positions);

        // Teleport Location
        obj.add("teleportLocation", context.serialize(mine.getTeleportLocation()));

        // Result
        return obj;
    }

    @Override
    public Mine deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();

        // Meta
        String displayName = obj.has("displayName") ? obj.get("displayName").getAsString() : null;
        long resetInterval = obj.has("resetInterval") ? obj.get("resetInterval").getAsLong() : 60;

        // Content
        List<MineContent> content = new ArrayList<>();
        if (obj.has("content")) {
            JsonArray contentArray = obj.getAsJsonArray("content");
            for (JsonElement el : contentArray) {
                JsonObject contentObj = el.getAsJsonObject();
                String blockType = contentObj.get("blockType").getAsString();
                int chance = contentObj.get("chance").getAsInt();
                content.add(new MineContent(blockType, chance));
            }
        }

        // Positions
        List<Location> positions = new ArrayList<>();
        JsonObject posObj = obj.getAsJsonObject("positions");

        for (Map.Entry<String, JsonElement> entry : posObj.entrySet()) {
            Location loc = context.deserialize(entry.getValue(), Location.class);
            positions.add(loc);
        }

        // Teleport Location
        Location teleportLocation = obj.has("teleportLocation") ? context.deserialize(obj.get("teleportLocation"), Location.class) : null;

        // Result

        Mine mine = new Mine(null, positions.getFirst(), positions.getLast());

        mine.setDisplayName(displayName);
        mine.setResetInterval(resetInterval);
        mine.setContent(content);
        mine.setTeleportLocation(teleportLocation);

        return mine;
    }
}