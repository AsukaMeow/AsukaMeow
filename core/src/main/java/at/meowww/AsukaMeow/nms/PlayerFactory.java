package at.meowww.AsukaMeow.nms;

import at.meowww.AsukaMeow.user.UserInventory;

import java.util.UUID;

public abstract class PlayerFactory {

    public abstract UserInventory getOfflinePlayerInventory(UUID uuid);

}
