package click.acme.genius.models;

import com.google.firebase.auth.FirebaseUser;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import click.acme.genius.utils.AccountStatus;

/**
 * Modèle pour représenté un utilisateur tel qu'il existe dans le SGBD
 */
public class User {
    //Pour pouvoir accèder à l'utilisateur actuellement connecté dans toutes l'application
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
    private int mTotalScore;
    private AccountStatus mAccountStatus;
    private List<String> mRewardBadges;

    public User(){

    }

    /**
     * Parse les données utilisateur de Firebase.Auth dans un objet User
     * @param user FirebaseUser l'utilisateur authentifié à l'aide de Firebase.Auth
     */
    public User(FirebaseUser user){
        if(user.getPhotoUrl() != null) {
            setAvatarUrl(user.getPhotoUrl().toString());
        }else{
            String gravatarUrl = "https://www.gravatar.com/avatar/" + md5(user.getEmail());
            setAvatarUrl(gravatarUrl);
        }
        setUserName(user.getDisplayName());
        setEmailAddress(user.getEmail());
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

    public void setEmailAddress(String emailAdress) {
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

    public List<String> getRewardBadges() {
        return mRewardBadges;
    }

    public void setRewardBadges(List<String> rewardBadges) {
        mRewardBadges = rewardBadges;
    }

    public void addRewardBadge(String rewardBadgeReference){
        if(mRewardBadges == null){
            mRewardBadges = new ArrayList<>();
        }

        mRewardBadges.add(rewardBadgeReference);
    }

    public String getRewardBadge(int index){
        return mRewardBadges.get(index);
    }

    //Utilisé pour la génération d'un avatar par défaut à l'aide de gravatar.
    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public int getTotalScore() {
        return mTotalScore;
    }

    public void setTotalScore(int totalScore) {
        mTotalScore = totalScore;
    }
}
