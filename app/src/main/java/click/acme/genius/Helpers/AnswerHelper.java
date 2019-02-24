package click.acme.genius.Helpers;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import click.acme.genius.Models.Answer;
import click.acme.genius.Models.Question;

public class AnswerHelper {
    public static Query getAnswersFromDatabase(String questionReference){
        return FirebaseFirestore.getInstance()
                .collection("answers")
                .whereEqualTo("questionReference",questionReference)
                .orderBy("weight")
                .limit(20); //TODO implement infinite scroll
    }

    public static void addCreditToAnswer(Answer answer){
        answer.setWeight( answer.getWeight() + 1 );
        saveState(answer);
    }

    public static void addDiscreditToAnswer(Answer answer){
        answer.setWeight( answer.getWeight() - 1 );
        saveState(answer);
    }

    public static void saveState(Answer answer) {
        FirebaseFirestore.getInstance().collection("answers").document(answer.getId()).set(answer);
    }
}
