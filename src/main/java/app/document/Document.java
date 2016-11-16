package app.document;

/**
 * Created by Admin on 13.11.2016.
 */
public class Document {
    private final long id;
    private final long clientId;
    private final long fromAcc;
    private final long toAcc;
    private final float amount;

    public Document(long id,long clientId, long fromAcc, long toAcc, float amount, long date) {
        this.id = id;
        this.clientId=clientId;
        this.fromAcc = fromAcc;
        this.toAcc = toAcc;
        this.amount = amount;
        this.date = date;
    }

    public Document(DocumentModel model){
        this.id = model.getId();
        this.clientId=model.getClient().getId();
        this.fromAcc = model.getFrom().getAccount_id();
        this.toAcc = model.getTo().getAccount_id();
        this.amount = model.getAmount();
        this.date = model.getDate();
    }

    public long getClientId() {
        return clientId;
    }

    public long getFromAcc() {
        return fromAcc;
    }

    public long getId() {
        return id;
    }

    public long getToAcc() {
        return toAcc;
    }

    public float getAmount() {
        return amount;
    }

    public long getDate() {
        return date;
    }

    private final long date;
}
