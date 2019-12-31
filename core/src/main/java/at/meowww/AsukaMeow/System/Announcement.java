package at.meowww.AsukaMeow.System;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.user.User;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.Date;
import java.util.UUID;

public class Announcement {

    private UUID dialogUUID;
    private Date startDate, endDate;

    public void sendAnnouncement(User user) {
        if (user.getAnnounceReadDate().before(startDate)) {
            AsukaMeow.INSTANCE.getNMSManager().getDialogFactory().openDialog(
                    Bukkit.getPlayer(user.getUUID()),
                    AsukaMeow.INSTANCE.getDialogManager().getDialog(dialogUUID)
            );
            user.updateAnnounceReadDate();
        }
    }

    public void setDialogUUID(UUID uuid) {
        this.dialogUUID = uuid;
    }

    public void load (Document document) {
        Document content = document.get("content", new Document());
        String uuidString = content.get("dialog_uuid", "");
        dialogUUID = uuidString.split("-").length == 5 ? UUID.fromString(uuidString) : null;
        startDate = content.get("start_date", new Date(0));
        endDate = content.get("end_date", new Date(0));
    }

    public Document save () {
        return new Document("section", "announcement")
                .append("content", new Document(
                        "dialog_uuid",
                        dialogUUID == null ? "" : dialogUUID.toString())
                        .append("start_date", startDate)
                        .append("end_date", endDate)
                );
    }

}
