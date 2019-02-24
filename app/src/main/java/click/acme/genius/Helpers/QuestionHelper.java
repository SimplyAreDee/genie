package click.acme.genius.Helpers;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import click.acme.genius.Models.Question;

public class QuestionHelper {
    public static Query getQuestionsFromDatabase(int limit){
        return FirebaseFirestore.getInstance()
                .collection("questions")
                .limit(limit); //TODO implement infinite scroll
    }

    public static DocumentReference getQuestionByReference(String questionReference){
        return FirebaseFirestore.getInstance()
                .collection("questions")
                .document(questionReference);
    }

    public static void saveState(Question question) {
        FirebaseFirestore.getInstance().collection("questions").document(question.getId()).set(question);
    }

    public static void addCreditToQuestion(Question question) {
        question.setWeight( question.getWeight() + 1 );
        saveState(question);
    }

    public static void addDiscreditToQuestion(Question question) {
        question.setWeight( question.getWeight() - 1 );
        saveState(question);
    }
}