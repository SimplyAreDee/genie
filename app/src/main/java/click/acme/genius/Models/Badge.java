package click.acme.genius.Models;

public class Badge {
    private int mId;
    private String mLabel;
    private int mRessourceId;

    public Badge(){}

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

    public int getRessourceId() {
        return mRessourceId;
    }

    public void setRessourceId(int ressourceId) {
        mRessourceId = ressourceId;
    }
}
