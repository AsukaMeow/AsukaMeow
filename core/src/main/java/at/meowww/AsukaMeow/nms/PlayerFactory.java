package at.meowww.AsukaMeow.nms;

import at.meowww.AsukaMeow.user.UserInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * For operate player saves with NMS
 *
 * This abstract class should be extends by relative version
 *  of NMS class.
 * @author clooooode
 * @since 0.0.3-SNAPSHOT
 */
public abstract class PlayerFactory {

    /**
     * Get player's .data saves into Object
     *
     * NMS should cast the return object to NBTBase object, because
     *  NBTBase only provide in craftbukkit.
     * @param uuid
     * @return null if no such uuid saves exists.
     */
    @Nullable
    public abstract Object getPlayerSaves (UUID uuid);

    /**
     * Get player's inventory data from saves.
     * @param uuid
     * @return
     */
    @NotNull
    public abstract UserInventory getPlayerInventory(UUID uuid);

}
