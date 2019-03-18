package click.acme.genius.models;

/**
 * Modèlé utilisé pour simplifier le modèle {@link User User} pour les recherches
 */
public class SearchUserName {
    private String mId;
    private String mUserName;

    public SearchUserName(String id, String userName) {
        setId(id);
        setUserName(userName);
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    //Pour que l'on puisse utiliser l'objet dans un Spinner par exemple
    @Override
    public String toString() {
        return getUserName();
    }

    //Pour effectuer une recherche dans un SearchTextView par exemple
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SearchUserName){
            SearchUserName searchUserName = (SearchUserName )obj;
            return searchUserName.getUserName().equals(getUserName())
                    && searchUserName.getId().equals(getId());
        }

        return false;
    }

}
