package at.meowww.AsukaMeow.nms;

import at.meowww.AsukaMeow.AsukaMeow;

public class NMSManager {

    private ItemFactory itemFactory;
    private PlayerFactory playerFactory;
    private DialogFactory dialogFactory;

    public NMSManager () {
        try {
            this.itemFactory = initItemFactory();
            this.playerFactory = initPlayerFactory();
            this.dialogFactory = initDialogFactory();
        } catch (Exception e) {
            AsukaMeow.INSTANCE.getLogger().warning(
                    e.getMessage() + " could not find a valid implementation for this version."
            );
        }
    }

    private String classAbsolutePath (Class clazz) {
        return clazz.getPackage().getName() +
                "." + AsukaMeow.NMS_VERSION +
                "." + clazz.getSimpleName();
    }

    public ItemFactory getItemFactory () {
        return this.itemFactory;
    }

    public PlayerFactory getPlayerFactory () {
        return this.playerFactory;
    }


    public DialogFactory getDialogFactory () {
        return this.dialogFactory;
    }

    private ItemFactory initItemFactory () throws Exception {
        try {
            return (ItemFactory) Class.forName(
                    classAbsolutePath(ItemFactory.class)).newInstance();
        } catch (Exception e) {
            throw new Exception("ItemFactory");
        }
    }

    private PlayerFactory initPlayerFactory () throws Exception {
        try {
            return (PlayerFactory) Class.forName(
                    classAbsolutePath(PlayerFactory.class)).newInstance();
        } catch (Exception e) {
            throw new Exception("PlayerFactory");
        }
    }

    private DialogFactory initDialogFactory () throws Exception {
        try {
            return (DialogFactory) Class.forName(
                    classAbsolutePath(DialogFactory.class)).newInstance();
        } catch (Exception e) {
            throw new Exception("DialogFactory");
        }
    }

}
