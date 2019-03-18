package click.acme.genius.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Modèle utilisé pour représenter une conversation
 */
public class Chat {
    private String mId;
    private String mDateCreated;
    private String mLastMessageTime;
    private String mChatShortcutName;
    private String mChatShortcutImageUrl;
    private List<String> mUsersReferences;
    private List<String> mUsersNames;
    private int mMessageCount;

    public Chat(){
        //Need pour POJO
    }

    /**
     * Constructeur utilisé pour le mappage POJO
     * @param id String la référence du chat
     * @param dateCreated String la date de création
     * @param lastMessageTime String la date du dernier message
     * @param chatShortcutName String le raccourcie visuel du chat
     * @param chatShortcutImageUrl String l'url de l'image du chat
     * @param usersReferences List<String> une liste des participant à la conversation
     * @param usersNames List<String> une liste des pseudos des participants
     * @param messageCount int le nombre de message contenu dans le chat
     */
    public Chat(String id, String  dateCreated, String lastMessageTime, String chatShortcutName,
                String chatShortcutImageUrl, List<String> usersReferences, List<String> usersNames,
                int messageCount){
        setId(id);
        setDateCreated(dateCreated);
        setLastMessageTime(lastMessageTime);
        setChatShortcutName(chatShortcutName);
        setChatShortcutImageUrl(chatShortcutImageUrl);
        setUsersReferences(usersReferences);
        setUsersNames(usersNames);
        setMessageCount(messageCount);
    }

    /**
     * Génère un ID à partir de {@see UUID.randomUUID()}
     */
    public void generateId(){
        setId( UUID.randomUUID().toString() );
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getDateCreated() {
        return mDateCreated;
    }

    public void setDateCreated(String dateCreated) {
        mDateCreated = dateCreated;
    }

    public String getLastMessageTime() {
        return mLastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        mLastMessageTime = lastMessageTime;
    }

    public List<String> getUsersReferences() {
        return mUsersReferences;
    }

    public void setUsersReferences(List<String> usersReferences) {
        mUsersReferences = usersReferences;
    }

    public void addUsersReferences(String userReference){
        if(mUsersReferences == null){
            mUsersReferences = new ArrayList<>();
        }

        mUsersReferences.add(userReference);
    }

    public String getChatShortcutName() {
        return mChatShortcutName;
    }

    public void setChatShortcutName(String chatShortcutName) {
        mChatShortcutName = chatShortcutName;
    }

    public int getMessageCount() {
        return mMessageCount;
    }

    public void setMessageCount(int messageCount) {
        mMessageCount = messageCount;
    }

    public String getChatShortcutImageUrl() {
        return mChatShortcutImageUrl;
    }

    public void setChatShortcutImageUrl(String chatShortcutImageUrl) {
        mChatShortcutImageUrl = chatShortcutImageUrl;
    }

    public List<String> getUsersNames() {
        return mUsersNames;
    }

    public void setUsersNames(List<String> usersNames) {
        mUsersNames = usersNames;
    }

    public void addUsersNames(String userName){
        if(mUsersNames == null){
            mUsersNames = new ArrayList<>();
        }

        mUsersNames.add(userName);
    }
}
