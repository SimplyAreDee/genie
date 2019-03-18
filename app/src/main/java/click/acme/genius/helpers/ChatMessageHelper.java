package click.acme.genius.helpers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import click.acme.genius.models.ChatMessage;

/**
 * Helper pour le modèle {@link click.acme.genius.models.ChatMessage ChatMessage}
 */
public class ChatMessageHelper {
    /**
     * Retourne la liste des messages concernant la conversation
     * @param chatReference String la référence de la conversation
     * @return Query la query correspondante à la recherche
     */
    public static Query getChatMessageFromDatabase(String chatReference) {
        return FirebaseFirestore.getInstance()
                .collection("chats/" + chatReference + "/messages").orderBy("dateCreated", Query.Direction.ASCENDING);
    }

    /**
     * Sauvegarde le message dans le SGBD
     * @param message ChatMessage le message à sauvegarder
     * @return la tache d'insertion pour poursuivre vers autre chose une fois l'insertion faite
     */
    public static Task<Void> saveState(ChatMessage message) {
        return FirebaseFirestore.getInstance().collection("chats/" + message.getChatReference() + "/messages").document(message.getId()).set(message);
    }
}
