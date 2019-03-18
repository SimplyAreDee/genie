package click.acme.genius.models;

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
    private String mCapturedImage;
    private int mWeight;
    private int mMinor;
    private int mMajor;
    private int mViewCount;
    private int mAnswerCount;
    private int mScolarLevel;
    private boolean mCertifiedAnswer;
    private List<String> mAnswerReferences;
    private List<String> mRevisionReferences;
    private List<String> mViewerReferences;
    private Timestamp mDateCreated;

    public Question(){
    }

    /**
     * Génère un ID à partir de {@see UUID.randomUUID()}
     */
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
        return mMajor - mMinor;
    }

    public void setWeight(int weight) {
        mWeight = weight;
    }

    public int getMinor() {
        return mMinor;
    }

    public void setMinor(int minor) {
        mMinor = minor;
    }

    public int getMajor() {
        return mMajor;
    }

    public void setMajor(int major) {
        mMajor = major;
    }

    public void updateWeight() {
        setWeight( getWeight() );
    }

    public List<String> getAnswerReferences() {
        return mAnswerReferences;
    }

    public void setAnswerReferences(List<String> mAnswerReferences) {
        this.mAnswerReferences = mAnswerReferences;
    }

    public void addAnswerReference(String reference){
        if(mAnswerReferences == null){
            mAnswerReferences = new ArrayList<String>();
        }

        mAnswerReferences.add(reference);
    }

    public boolean isCertifiedAnswer() {
        return mCertifiedAnswer;
    }

    public void setCertifiedAnswer(boolean certifiedAnswer) {
        this.mCertifiedAnswer = certifiedAnswer;
    }

    public int getViewCount() {
        return mViewCount;
    }

    public void setViewCount(int mViewCount) {
        this.mViewCount = mViewCount;
    }

    public int getAnswerCount() {
        return mAnswerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.mAnswerCount = answerCount;
    }

    public List<String> getViewerReferences() {
        return mViewerReferences;
    }

    public void setViewerReferences(List<String> mViewerReferences) {
        this.mViewerReferences = mViewerReferences;
    }

    public void addViewerReferences(String userId){
        if(mViewerReferences == null){
            mViewerReferences = new ArrayList<>();
        }

        mViewerReferences.add(userId);
    }

    public int getScolarLevel() {
        return mScolarLevel;
    }

    public void setScolarLevel(int mScolarLevel) {
        this.mScolarLevel = mScolarLevel;
    }

    public String getCapturedImage() {
        return mCapturedImage;
    }

    public void setCapturedImage(String capturedImage) {
        mCapturedImage = capturedImage;
    }
}