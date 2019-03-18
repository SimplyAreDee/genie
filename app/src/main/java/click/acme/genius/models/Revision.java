package click.acme.genius.models;

import java.util.UUID;

/**
 * Modèle qui stock toutes une modification apportée à une question/réponse
 */
public class Revision {

    private String mId;
    private String mOldContent;
    private String mNewContent;
    private String mAuthorReference;
    private String mQuestionReference;

    public Revision(){
    }

    /**
     * Génère un ID à partir de {@see UUID.randomUUID()}
     */
    public void generateId(){
        mId = UUID.randomUUID().toString();
    }

    public String getId() {
        return mId;
    }

    public String getOldContent() {
        return mOldContent;
    }

    public void setOldContent(String oldContent) {
        mOldContent = oldContent;
    }

    public String getNewContent() {
        return mNewContent;
    }

    public void setNewContent(String newContent) {
        mNewContent = newContent;
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
}
