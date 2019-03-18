package click.acme.genius.helpers;

import android.content.res.Resources;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import click.acme.genius.R;

/**
 * Helper pour le modèle {@link click.acme.genius.models.Reward Reward}
 */
public class RewardHelper {

    /**
     * Retourne la liste des badges que l'utilisateur possède.
     * @param limit int la limite à récuperer
     * @param userReference String la référence de l'utilisateur dont on doit récupérer les badges
     * @return query Query la query correspondante à la recherche
     */
    public static Query getUserRewardsFromDatabase(int limit, String userReference){
        return FirebaseFirestore.getInstance()
                .collection("rewards")
                .whereArrayContains("rewardedUsers",userReference);
    }

    /**
     * Retourne le drawable correspondant à la {@code ressourceName} du badge
     * Si aucun drawable de trouvé on retourne {@code R.drawable.badge_default}
     * @param ressourceName String l'id du drawable à récupérer
     * @return la référence correspondante au drawable.
     */
    public static int getRessouceId(String ressourceName){
        try {
            return R.drawable.class.getField(ressourceName).getInt(null);
        }catch (Resources.NotFoundException ex){
            return R.drawable.badge_default;
        } catch (IllegalAccessException e) {
            return R.drawable.badge_default;
        } catch (NoSuchFieldException e) {
            return R.drawable.badge_default;
        }
    }
}