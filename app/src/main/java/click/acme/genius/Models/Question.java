package click.acme.genius.Models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@IgnoreExtraProperties
public class Question {
    private String mId;
    private String mAuthorReference;
    private String mSubject;
    private String mTitle;
    private String mPage;
    private String mNumber;
    private String mIban;
    private String mInstruction;
    private int mWeight;
    private List<String> mRevisionReferences;
    private Timestamp mDateCreated;

    public Question(){
    }

    public void generateId(){
        setId( UUID.randomUUID().toString() );
    }

    public void setId(String id){
        mId = id;
    }

    public String getId() {
        return mId;
    }

    public String getSubject() {
        return mSubject;
    }

    public void setSubject(String subject) {
        mSubject = subject;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getPage() {
        return mPage;
    }

    public void setPage(String page) {
        mPage = page;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        mNumber = number;
    }

    public String getIban() {
        return mIban;
    }

    public void setIban(String iban) {
        mIban = iban;
    }

    public String getInstruction() {
        return mInstruction;
    }

    public void setInstruction(String instruction) {
        mInstruction = instruction;
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

    public String getAuthorReference() {
        return mAuthorReference;
    }

    public void setAuthorReference(String userId) {
        mAuthorReference = userId;
    }

    @ServerTimestamp
    public Timestamp getDateCreated() { return mDateCreated; }

    public void setDateCreated(Timestamp dateCreated) {
        mDateCreated = dateCreated;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int weight) {
        mWeight = weight;
    }
}
