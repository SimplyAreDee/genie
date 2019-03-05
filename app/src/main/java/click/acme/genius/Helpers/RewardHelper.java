package click.acme.genius.Helpers;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import click.acme.genius.Models.Question;

public class RewardHelper {
    public static Query getUserRewardsFromDatabase(int limit, String userId){
        return FirebaseFirestore.getInstance()
                .collection("rewards")
                .whereEqualTo("userReference",userId);
    }
}