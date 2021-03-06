package click.acme.genius.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Modèle pour la réponse apporté à une question
 */
public class Answer {
    private String mId;
    private String mAuthorReference;
    private String mQuestionReference;
    private String mExplanation;
    private int mWeight;
    private boolean mCertified;
    private List<String> mRevisionReferences;
    private Timestamp mDateCreated;

    public Answer(){
    }

    /**
     * Génère un ID à partir de {@see UUID.randomUUID()}
     */
    public void generateId(){
        setId(UUID.randomUUID().toString());
    }

    public void setId(String id){
        mId = id;
    }

    public String getId() {
        return mId;
    }

    public String getAuthorReference() {
        return mAuthorReference;
    }

    public void setAuthorReference(String authorReference) {
        mAuthorReference = authorReference;
    }

    public String getQuestionReference() {
        return mQuestionReference;
    }

    public void setQuestionReference(String questionReference) {
        mQuestionReference = questionReference;
    }

    public String getExplanation() {
        return mExplanation;
    }

    public void setExplanation(String explanation) {
        mExplanation = explanation;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int weight) {
        mWeight = weight;
    }

    public List<String> getRevisionReferences() {
        return mRevisionReferences;
    }

    public void setRevision(List<String> revisionReferences) {
        mRevisionReferences = revisionReferences;
    }

    public void addRevisionReferences(String revisionReference){
        if(mRevisionReferences == null){
            mRevisionReferences = new ArrayList<>();
        }

        mRevisionReferences.add(revisionReference);
    }

    @ServerTimestamp
    public Timestamp getDateCreated() { return mDateCreated; }

    public void setDateCreated(Timestamp dateCreated) {
        mDateCreated = dateCreated;
    }

    public boolean isCertified() {
        return mCertified;
    }

    public void setCertified(boolean certified) {
        mCertified = certified;
    }
}
