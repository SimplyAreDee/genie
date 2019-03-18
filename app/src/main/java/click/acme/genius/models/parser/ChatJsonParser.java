package click.acme.genius.models.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import click.acme.genius.models.Chat;

/**
 * @version Parser correspondant au modèle de {@link click.acme.genius.models.Chat Chat} de la version 1
 * Permet de parser le résultat de recherche sur l'index de Chat d'Aglolia
 */
public class ChatJsonParser {

    /**
     * Parse l'objet json
     * @param jsonObject JSONObject l'object à parser
     * @return Chat chat l'objet POJO {@link click.acme.genius.models.Chat Chat}
     */
    public Chat parse(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        String id = jsonObject.optString("id");
        String dateCreated = jsonObject.optString("dateCreated");
        String lastMessageTime = jsonObject.optString("lastMessageTime");
        String chatShortcutName = jsonObject.optString("chatShortcutName");
        String chatShortcutImageUrl = jsonObject.optString("chatShortcutImageUrl");
        ArrayList<String> usersReferences = new ArrayList<String>();
        ArrayList<String> usersNames = new ArrayList<String>();
        int messageCount = jsonObject.optInt("title",0);

        JSONArray jArray = null;
        try {
            jArray = jsonObject.getJSONArray("usersReferences");
            if (jArray != null) {
                for (int i=0;i<jArray.length();i++){
                    usersReferences.add(jArray.getString(i));
                }
            }
            jArray = jsonObject.getJSONArray("usersNames");
            if (jArray != null) {
                for (int i=0;i<jArray.length();i++){
                    usersReferences.add(jArray.getString(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (id != null) {
            return new Chat(id, dateCreated, lastMessageTime, chatShortcutName, chatShortcutImageUrl,
                    usersReferences, usersNames, messageCount);
        }
        return null;
    }
}
