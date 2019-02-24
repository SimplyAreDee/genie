package click.acme.genius.Helpers;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import click.acme.genius.Models.User;
import click.acme.genius.Utils.Exceptions.NotImplementedException;
import click.acme.genius.Utils.UserUpdateOperation;

public class UserHelper {

    public static void updateUserProfile(User user, UserUpdateOperation operation) throws NotImplementedException {
        switch (operation) {
            case IncrementAnsweredQuestions:
                incrementAnsweredQuestions(user);
                break;
            case IncrementAskedQuestions:
                incrementAskedQuestion(user);
                break;
            default:
                throw new NotImplementedException();
        }
    }

    public static void saveState(User user) {
        FirebaseFirestore.getInstance().collection("users").document(user.getId()).set(user)
                .addOnSuccessListener(aVoid -> User.setCurrentUser(user));
    }

    public static DocumentReference getUserById(String userId){
        return FirebaseFirestore.getInstance().collection("users").document(userId);
    }

    private static User incrementAnsweredQuestions(User user) {
        user.incrementAnsweredCount();
        saveState(user);
        return user;
    }

    private static User incrementAskedQuestion(User user) {
        user.incrementAskedCount();
        saveState(user);
        return user;
    }
}
