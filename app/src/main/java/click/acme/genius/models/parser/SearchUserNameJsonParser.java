package click.acme.genius.models.parser;

import org.json.JSONObject;
import click.acme.genius.models.SearchUserName;

/**
 * @version Parser correspondant au modèle de {@link click.acme.genius.models.SearchUserName SearchUserName} de la version 1
 * Permet de parser le résultat de recherche sur l'index de utilisateur d'Aglolia
 */
public class SearchUserNameJsonParser {

    /**
     * Parse l'objet json
     * @param jsonObject JSONObject l'object à parser
     * @return SearchUserName searchUserName l'objet POJO {@link click.acme.genius.models.SearchUserName SearchUserName}
     */
    public SearchUserName parse(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        String id = jsonObject.optString("id");
        String userName = jsonObject.optString("userName");


        if (id != null) {
            return new SearchUserName(id, userName);
        }
        return null;
    }
}
