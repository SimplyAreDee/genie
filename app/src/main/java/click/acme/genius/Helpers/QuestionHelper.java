package click.acme.genius.Helpers;

import android.os.Bundle;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import click.acme.genius.Models.Question;
import click.acme.genius.Models.User;

public class QuestionHelper {
    public static Query getQuestionsFromDatabase(int limit){
        return FirebaseFirestore.getInstance()
                .collection("questions")
                .limit(limit); //TODO implement infinite scroll
    }

    public static Query getFilteredQuestionsFromDatabase(int limit, Bundle bundle){
        Query query = FirebaseFirestore.getInstance()
                .collection("questions");

        if(bundle.containsKey("withoutAnswer") && bundle.getBoolean("withoutAnswer")){
            query.whereLessThan("answerReference",1);
        }else if(bundle.containsKey("withCertifiedAnswer") && bundle.getBoolean("withCertifiedAnswer")){
            query.whereGreaterThan("answerReference",1)
                    .whereEqualTo("certifiedAnswer",true);
        }else if(bundle.containsKey("withAnswer") && bundle.getBoolean("withAnswer")){
            query.whereGreaterThan("answerReferences",0);
        }

        int userClass = User.getCurrentUser().getScholarLevel();
        if(bundle.containsKey("lowLvlQuestion") && userClass > 0 ){
            query.whereLessThanOrEqualTo("scholarclass",userClass);
        }else if(bundle.containsKey("scholarclass")){
            userClass = bundle.getInt("scholarclass");
            query.whereEqualTo("scholarclass",userClass);
        }

        if(bundle.containsKey("subject")){
            query.whereEqualTo("subject", bundle.getString("subject"));
        }

        if(bundle.containsKey("interval")){
            String interval = bundle.getString("interval");

            //Calendar set to the current date
            Calendar calendar = Calendar.getInstance();
            Timestamp time = null;

            switch(interval){
                case "Aujourd'hui":
                    calendar.add(Calendar.DATE, 1);

                    break;
                case "Depuis 2 jours":
                    calendar.add(Calendar.DAY_OF_YEAR, -2);

                    break;
                case "Depuis 1 semaine":
                    calendar.add(Calendar.DAY_OF_YEAR, -7);

                    break;
                case "Ce mois-ci":
                    calendar.add(Calendar.DAY_OF_MONTH, 1);

                    break;
            }

            time = new Timestamp( calendar.getTimeInMillis() );

            query.whereGreaterThanOrEqualTo("createAt",time.toString());
        }

        query.limit(limit);

        return query;
    }

    public static Query getQuestionsByUserReferenceFromDatabase(int limit, String authorReference){
        return FirebaseFirestore.getInstance()
                .collection("questions")
                .whereEqualTo("authorReference",authorReference)
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
        question.setMajor( question.getMajor() + 1 );
        question.updateWeight();
        saveState(question);
    }

    public static void addDiscreditToQuestion(Question question) {
        question.setMinor( question.getMinor() + 1 );
        question.updateWeight();
        saveState(question);
    }
}