package click.acme.genius.helpers;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import click.acme.genius.models.User;

/**
 * Helper pour le modèle {@link click.acme.genius.models.User User}
 */
public class UserHelper {

    /**
     * Sauvegarde l'état dans le SGBD
     * @param user User l'objet représentant l'utilisateur {@see click.acme.genius.models.User}
     */
    public static void saveState(User user) {
        FirebaseFirestore.getInstance().collection("users").document(user.getId()).set(user)
                .addOnSuccessListener(aVoid -> User.setCurrentUser(user));
    }

    /**
     * Retourne l'utilisateur enregistré dans le SGBD
     * @param userId String la référence de l'utilisateur
     * @return DocumentReference la reference du document trouvé dans le SGBD (appelé .get() pour le récupérer)
     */
    public static DocumentReference getUserById(String userId){
        return FirebaseFirestore.getInstance().collection("users").document(userId);
    }

}
