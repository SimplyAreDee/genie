package click.acme.genius.models;

/**
 * Modèle qui stock la représentation d'un badge
 */
public class Reward {
    private String mId;
    private String mLabel;
    private String mDescription;
    private String mRessourceId;

    public Reward(){}

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }
    
    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getRessourceId() {
        return mRessourceId;
    }

    public void setRessourceId(String ressourceId) {
        mRessourceId = ressourceId;
    }
}
