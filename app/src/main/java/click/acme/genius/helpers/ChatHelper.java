package click.acme.genius.helpers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import click.acme.genius.models.Chat;
import click.acme.genius.models.User;

/**
 * Helper pour le modèle {@link click.acme.genius.models.Chat Chat}
 */
public class ChatHelper {
    /**
     * Recherche toutes les réponses attachées à la question
     * @param limit int le nombre max de conversation à récuperer
     * @return Query la query correspondante à la recherche
     */
    public static Query getChatsFromDatabase(int limit) {
        Query query = FirebaseFirestore.getInstance()
                .collection("chats")
                .whereArrayContains("usersReferences", User.getCurrentUser().getId())
                .limit(limit);

        return query;
    }

    /**
     * Récupère les conversations qui ne concerne que l'utilisateur séléctionné
     * @param userReference String la référence de l'utilisateur
     * @param limit int la limite de conversation à récupérer
     * @return Query la query correpondante à la recherche
     */
    public static Query getChatsFilterByUsernameFromDatabase(String userReference, int limit) {
        return FirebaseFirestore.getInstance()
                .collection("chats")
                .whereArrayContains("usersReferences", userReference )
                .limit(limit);
    }

    /**
     * Sauvegarde l'état de la conversation dans le SGBD
     * @param chat Chat la conversation à sauvegarder
     * @return la tache d'insertion pour poursuivre vers autre chose une fois l'insertion faite
     */
    public static Task<Void> saveState(Chat chat) {
        return FirebaseFirestore.getInstance().collection("chats")
                .document(chat.getId()).set(chat);
    }
}
