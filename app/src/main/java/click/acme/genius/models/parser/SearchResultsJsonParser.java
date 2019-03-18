package click.acme.genius.models.parser;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import click.acme.genius.models.Chat;
import click.acme.genius.models.SearchUserName;

/**
 * @version Parser correspondant au modèle de la version 1
 * Permet de parser le résultat de recherche sur l'index de Chat ou User d'Aglolia
 */
public class SearchResultsJsonParser {
    private ChatJsonParser mChatParser = new ChatJsonParser();
    private SearchUserNameJsonParser mUserNameParser = new SearchUserNameJsonParser();

    /**
     * Retourne la liste des chats trouvé lors de la requête
     * @param jsonObject JSONObject le json retourné par Aglolia
     * @return List<Chat> La liste des objets POJO retournés par Algolia
     */
    public List<Chat> parseChatResults(JSONObject jsonObject) {
        if (jsonObject == null) return null;

        List<Chat> results = new ArrayList<>();

        JSONArray hits = jsonObject.optJSONArray("hits");

        if (hits == null) return null;

        for (int i = 0; i < hits.length(); ++i) {
            JSONObject hit = hits.optJSONObject(i);
            if (hit == null) continue;

            Chat chat = mChatParser.parse(hit);
            if (chat == null) continue;

            results.add(chat);
        }

        return results;
    }

    /**
     * Retourne la liste des User simplifié {@see SearchUserName} trouvé lors de la requête
     * @param jsonObject JSONObject le json retourné par Aglolia
     * @return List<SearchUserName> La liste des objets POJO retournés par Algolia
     */
    public List<SearchUserName> parseUserResults(JSONObject jsonObject) {
        if (jsonObject == null) return null;

        List<SearchUserName> results = new ArrayList<SearchUserName>();

        JSONArray hits = jsonObject.optJSONArray("hits");

        if (hits == null) return null;

        for (int i = 0; i < hits.length(); ++i) {
            JSONObject hit = hits.optJSONObject(i);
            if (hit == null) continue;

            SearchUserName searchUserName = mUserNameParser.parse(hit);
            if (searchUserName == null) continue;

            results.add(searchUserName);
        }

        return results;
    }
}