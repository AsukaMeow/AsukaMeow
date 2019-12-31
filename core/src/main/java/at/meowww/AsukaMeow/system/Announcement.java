package at.meowww.AsukaMeow.system;

import at.meowww.AsukaMeow.AsukaMeow;
import at.meowww.AsukaMeow.user.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.Date;
import java.util.UUID;

public class Announcement {

    private UUID dialogUUID;
    private Date startDate, endDate;

    public void sendAnnouncement(User user) {
        if (new Date().after(startDate)) {
            if (user.getAnnounceReadDate().before(endDate) ||
                    endDate.equals(new Date(0))) {
                AsukaMeow.INSTANCE
                        .getNMSManager()
                        .getDialogFactory()
                        .openDialog(
                                Bukkit.getPlayer(user.getUUID()),
                                AsukaMeow.INSTANCE.getDialogManager().getDialog(dialogUUID)
                        );
                user.updateAnnounceReadDate();
            }
        }
    }

    public void setDialogUUID(UUID uuid) {
        dialogUUID = uuid;
    }

    public void load (MongoCollection systemCol) {
        Document document = (Document) systemCol.find(
                new Document("section", "announcement")).first();
        if (document == null)
            document = new Document("content", new Document());

        Document contentDoc = document.get("content", new Document());

        String uuidString = document.get("dialog_uuid", "");
        dialogUUID = uuidString.split("-").length == 5 ? UUID.fromString(uuidString) : null;
        startDate = contentDoc.get("start_date", new Date());
        endDate = contentDoc.get("end_date", new Date(0));
    }

    public void save (MongoCollection systemCol) {
        systemCol.replaceOne(
                new Document("section", "announcement"),
                new Document("section", "announcement")
                        .append("content", new Document(
                                "dialog_uuid",
                                dialogUUID == null ? "" : dialogUUID.toString()
                                )
                                .append("start_date", startDate)
                                .append("end_date", endDate)
                        ),
                new ReplaceOptions().upsert(true));
    }

}
