package click.acme.genius.models;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Modèle utilisé pour stocker les messages d'une conversation
 */
public class ChatMessage {
    private String mId;
    private String mChatReference;
    private String mSenderReference;
    private String mContent;
    private String mContentType;
    private String mDateCreated;
    private boolean mFromCertifiedProfil;

    public ChatMessage(){
        //Need pour POJO
    }

    /**
     * Génère un ID à partir de {@see UUID.randomUUID()}
     */
    public void generateId(){
        setId( UUID.randomUUID().toString() );
    }

    public void markStamp(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        setDateCreated(timestamp.toString());
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getSenderReference() {
        return mSenderReference;
    }

    public void setSenderReference(String senderType) {
        mSenderReference = senderType;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getContentType() {
        return mContentType;
    }

    public void setContentType(String contentType) {
        mContentType = contentType;
    }

    public String getDateCreated() {
        return mDateCreated;
    }

    public void setDateCreated(String dateCreated) {
        mDateCreated = dateCreated;
    }

    public boolean isSenderIsMe(){
        if(mSenderReference != null) {
            return mSenderReference.equalsIgnoreCase(User.getCurrentUser().getId());
        }

        return false;
    }

    public String getChatReference() {
        return mChatReference;
    }

    public void setChatReference(String chatReference) {
        mChatReference = chatReference;
    }

    public boolean isFromCertifiedProfil() {
        return mFromCertifiedProfil;
    }

    public void setFromCertifiedProfil(boolean fromCertifiedProfil) {
        mFromCertifiedProfil = fromCertifiedProfil;
    }
}
