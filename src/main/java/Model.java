import APITools.ServerHandler;
import MetaData.Show;
import Utils.SettingsHandler;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.ArrayList;

public class Model {
    ArrayList<Show> libraryShows = new ArrayList<Show>();
    ArrayList<Show> searchShows = new ArrayList<Show>();
    public void getShows(String query){
        ArrayList args = new ArrayList();
        args.add(query);
        searchShows = ServerHandler.talkToApi(args, ServerHandler.apiReqType.QUERY);
    }
    public void getLibrary(boolean forceUpdate){
        ArrayList args = new ArrayList();
        args.add(forceUpdate);
        libraryShows = ServerHandler.talkToApi(args, ServerHandler.apiReqType.LIBRARY);
    }
    public void snatchShow(int searchShowIndex){
        ServerHandler.snatch(searchShows.get(searchShowIndex));
    }
    public void openShowDirectory(int libraryShowIndex){
        try {
            if (!SettingsHandler.downloadPath.isEmpty()){
                String command = "xdg-open";
                String path = ""+ SettingsHandler.Inst().downloadPath+"/"+libraryShows.get(libraryShowIndex).torrentMeta.torrentDirectory + "";
                System.out.println(command);
                Process p = Runtime.getRuntime().exec(
                        new String[] { command, path});
                System.out.println(command+path.replaceAll(" ","\\ "));
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Download path is null, add it to the config file in \n ~/.AniSnatcher/client/settings.conf \n and restart the program");
                alert.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
