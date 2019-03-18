package click.acme.genius.helpers;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import click.acme.genius.models.Answer;

/**
 * Helper pour le modèle {@link click.acme.genius.models.Answer Answer}
 */
public class AnswerHelper {
    /**
     * Recherche toutes les réponses attachées à la question
     * @param questionReference String la référence de la question
     * @return Query la query correspondante à la recherche
     */
    public static Query getAnswersFromDatabase(String questionReference){
        return FirebaseFirestore.getInstance()
                .collection("answers")
                .whereEqualTo("questionReference",questionReference)
                .orderBy("weight")
                .limit(20); //TODO implement infinite scroll
    }

    /**
     * Incrémente le score de la réponse
     * @param answer Answer la réponse à incrémenter
     */
    public static void addCreditToAnswer(Answer answer){
        answer.setWeight( answer.getWeight() + 1 );
        saveState(answer);
    }

    /**
     * Décrémente le score de la réponse
     * @param answer Answer la réponse à décrémenter
     */
    public static void addDiscreditToAnswer(Answer answer){
        answer.setWeight( answer.getWeight() - 1 );
        saveState(answer);
    }

    /**
     * @param answer Answer la réponse à sauvegarder
     */
    public static void saveState(Answer answer) {
        FirebaseFirestore.getInstance().collection("answers").document(answer.getId()).set(answer);
    }
}
