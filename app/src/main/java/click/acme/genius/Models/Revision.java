package click.acme.genius.Models;

import java.util.UUID;

public class Revision {

    public String mId;
    public String mOldContent;
    public String mNewContent;
    public String mAuthorReference;

    public Revision(){
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
}
