package APITools;

import MetaData.Show;
import Utils.SettingsHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ServerHandler {
    public enum apiReqType {
        QUERY,
        LIBRARY,
    }

    private static String baseURL = "http://"+ SettingsHandler.Inst().serverIP +":"+SettingsHandler.Inst().serverPort;
    public static ArrayList<Show> talkToApi(ArrayList args, apiReqType method){
        ArrayList<Show> shows = new ArrayList<Show>();
        try{
            URL url = null;
            System.out.println(baseURL);
            switch (method){
                case QUERY:
                    String query = (String) args.get(0);
                    query = query.replaceAll(" ", "+");
                    url = new URL(baseURL + "/api/search?query="+query);
                    break;
                case LIBRARY:
                    Boolean forceUpdate = (Boolean) args.get(0);
                    url = new URL(baseURL + "/api/library?update="+forceUpdate);
                    break;
            }
            if(url != null){
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                String response = bufferedReader.lines().collect(Collectors.joining("\n"));
                shows = responseToShowList(response);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shows;
    }
    public static void snatch(Show show){
        try{
            URL url = new URL(baseURL + "/api/snatch");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            String showJson = show.toJSON();
            byte[] out = showJson.getBytes(StandardCharsets.UTF_8);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setFixedLengthStreamingMode(out.length);
            conn.setDoOutput(true);
            conn.connect();
            conn.getOutputStream().write(out);
            System.out.println(conn.getResponseCode());
            conn.disconnect();
        } catch (IOException e){
            System.out.println(e);
        }
    }
    private static ArrayList<Show> responseToShowList(String response){
        ArrayList<Show> shows = new ArrayList<Show>();
        JsonArray jsonArray = null;
        if (response.length() > 0) {
            JsonObject jsonObj = JsonParser.parseString(response).getAsJsonObject();
            jsonArray = jsonObj.get("shows").getAsJsonArray();
        }
        if (jsonArray != null){
            for (int i = 0; i < jsonArray.size(); i++){
                shows.add(new Show((JsonObject) jsonArray.get(i)));
            }
        }
        return shows;
    }
}
