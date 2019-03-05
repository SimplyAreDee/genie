package click.acme.genius.Models;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import click.acme.genius.Utils.AccountStatus;

public class User {
    private static User sCurrentUser;

    private String mId;
    private String mAvatarUrl;
    private String mUserName;
    private String mUserFirstName;
    private String mUserLastName;
    private String mEmailAddress;
    private int mScholarLevel;
    private String mEstablishment;
    private int mAskedCount;
    private int mAnsweredCount;
    private AccountStatus mAccountStatus;
    private List<Integer> mRewardBadges;

    public User(){

    }

    public User(FirebaseUser user){
        setAvatarUrl(user.getPhotoUrl().toString());
        setUserName(user.getDisplayName());
        setEmailAdress(user.getEmail());
        setId(user.getUid());
    }

    public static User getCurrentUser(){
        return sCurrentUser;
    }

    public static void setCurrentUser(User user){
        sCurrentUser = user;
    }


    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        mAvatarUrl = avatarUrl;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getUserFirstName() {
        return mUserFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        mUserFirstName = userFirstName;
    }

    public String getUserLastName() {
        return mUserLastName;
    }

    public void setUserLastName(String userLastName) {
        mUserLastName = userLastName;
    }

    public String getEmailAddress() {
        return mEmailAddress;
    }

    public void setEmailAdress(String emailAdress) {
        mEmailAddress = emailAdress;
    }

    public int getScholarLevel() {
        return mScholarLevel;
    }

    public void setScholarLevel(int aClass) {
        mScholarLevel = aClass;
    }

    public String getEstablishment() {
        return mEstablishment;
    }

    public void setEstablishment(String establishment) {
        mEstablishment = establishment;
    }

    public int getAskedCount() {
        return mAskedCount;
    }

    public void setAskedCount(int asked) {
        mAskedCount = asked;
    }

    public void incrementAskedCount() { mAskedCount++; }

    public int getAnsweredCount() {
        return mAnsweredCount;
    }

    public void setAnsweredCount(int answered) {
        mAnsweredCount = answered;
    }

    public void incrementAnsweredCount(){ mAnsweredCount++; }

    public AccountStatus getAccountStatus() {
        return mAccountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        mAccountStatus = accountStatus;
    }

    public List<Integer> getRewardBadges() {
        return mRewardBadges;
    }

    public void setRewardBadges(List<Integer> rewardBadges) {
        mRewardBadges = rewardBadges;
    }

    public void addRewardBadge(int rewardBadgeReference){
        if(mRewardBadges == null){
            mRewardBadges = new ArrayList<>();
        }

        mRewardBadges.add(rewardBadgeReference);
    }

    public int getRewardBadge(int index){
        return mRewardBadges.get(index);
    }
}
