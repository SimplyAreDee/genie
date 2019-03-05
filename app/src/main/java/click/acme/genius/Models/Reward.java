package click.acme.genius.Models;

public class Reward {
    private int mId;
    private String mLabel;
    private String mDescription;
    private int mRessourceId;

    public Reward(){}

    public int getId() {
        return mId;
    }

    public void setId(int id) {
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

    public int getRessourceId() {
        return mRessourceId;
    }

    public void setRessourceId(int ressourceId) {
        mRessourceId = ressourceId;
    }
}
