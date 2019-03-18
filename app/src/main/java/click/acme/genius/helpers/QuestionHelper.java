package click.acme.genius.helpers;

import android.os.Bundle;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import click.acme.genius.models.Question;
import click.acme.genius.models.User;

/**
 * Helper pour le modèle {@link click.acme.genius.models.Question Question}
 */
public class QuestionHelper {

    /**
     * Retourne La liste des questions du SGBD limitée à {@code limit} résultats, les plus récents
     * @param limit int La limite max à renvoyer
     * @return query Query la query correspondante à la recherche
     */
    public static Query getQuestionsFromDatabase(int limit){
        return FirebaseFirestore.getInstance()
                .collection("questions")
                .limit(limit); //TODO implement infinite scroll
    }

    /**
     * Retourne la liste des questions limité à {@code limit} résultats, les plus récents, en tenant
     * compte des filtres à appliquer provenant de {@code bundle}
     * @param limit int La limite max à renvoyer
     * @param bundle Bundle comportant toutes les entrées et leurs valeurs à filtrer
     * @return query Query la query correspondante à la recherche
     */
    public static Query getFilteredQuestionsFromDatabase(int limit, Bundle bundle){

        if(bundle.containsKey("withoutAnswer") && bundle.getBoolean("withoutAnswer")){
            return FirebaseFirestore.getInstance()
                    .collection("questions").whereLessThan("answerCount",1).limit(limit);
        }else if(bundle.containsKey("withCertifiedAnswer") && bundle.getBoolean("withCertifiedAnswer")){
            return FirebaseFirestore.getInstance()
                    .collection("questions").whereEqualTo("certifiedAnswer",true).limit(limit);
        }else if(bundle.containsKey("withAnswer") && bundle.getBoolean("withAnswer")){
            return FirebaseFirestore.getInstance()
                    .collection("questions").whereGreaterThan("answerCount",0).limit(limit);
        }else{
            return FirebaseFirestore.getInstance()
                    .collection("questions").limit(limit);
        }

        //Actuellement sur Firestore on ne peut requêter de cette manière, et toutes nouvelles
        //requête fait perdre les filtres de la précédente, je n'ai pas trouvé le moyen de les enchainer
        //tout en gardant le résultat de la précédente. On vera si ma feature request fait partie
        //des prochains ajouts.
        /*
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
        }*/
    }

    /**
     * Retourne la liste des questions de l'utilisateur {@code authorReference} dans la limite de
     * {@code limit} questions.
     * @param limit int La limite max à renvoyer
     * @param authorReference String la référence de l'utilisateur pour lequel on doit récupérer les
     *                        questions
     * @return query Query la query correspondante à la recherche
     */
    public static Query getQuestionsByUserReferenceFromDatabase(int limit, String authorReference){
        return FirebaseFirestore.getInstance()
                .collection("questions")
                .whereEqualTo("authorReference",authorReference)
                .limit(limit); //TODO implement infinite scroll / pagination
    }

    /**
     * Récupère le document correspondant à la référence {@code questionReference}
     * @param questionReference String la référence de la question à récuperer.
     * @return DocumentReference l'élément récuperé depuis le SGBD
     */
    public static DocumentReference getQuestionByReference(String questionReference){
        return FirebaseFirestore.getInstance()
                .collection("questions")
                .document(questionReference);
    }

    /**
     * Sauvegarde l'état de la question dans le SGBD
     * @param question Question la question à sauvegarder
     */
    public static void saveState(Question question) {
        FirebaseFirestore.getInstance().collection("questions").document(question.getId()).set(question);
    }

    /**
     * Ajoute du poids à la question
     * @param question Question la question à modifier
     */
    public static void addCreditToQuestion(Question question) {
        question.setMajor( question.getMajor() + 1 );
        question.updateWeight();
        saveState(question);
    }

    /**
     * enlève du poids à la question
     * @param question Question la question à modifier
     */
    public static void addDiscreditToQuestion(Question question) {
        question.setMinor( question.getMinor() + 1 );
        question.updateWeight();
        saveState(question);
    }

    /**
     * Ajoute une vue à la question
     * @param question Question la question à modifier
     */
    public static void addViewToQuestion(Question question) {
        String userReference = User.getCurrentUser().getId();

        //TODO sécuriser coté serveur en rajoutant lastViewerReference et en vérifiant que l'utilisateur
        //  existe vraiment.
        //n'enregistre que si l'utilisateur n'a jamais vu la question
        if(question.getViewerReferences() == null || !question.getViewerReferences().contains(userReference)) {
            question.setViewCount(question.getViewCount() + 1);

            question.addViewerReferences(userReference);

            saveState(question);
        }
    }
}