import APITools.ServerHandler;
import MetaData.Show;
import UITools.ElementBuilder;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Controller {
    Model model = new Model();
    boolean listenerAdded = false;
    @FXML
    private TextField searchField;
    @FXML
    private ListView libraryResultGrid,torrentList,fileList,searchResultGrid;
    @FXML
    private CheckBox forceUpdateBox;
    @FXML
    public void handleSearch(){
        Thread taskThread = new Thread(() -> {
            model.getShows(searchField.getText());
            Platform.runLater(() -> {
                try{
                    searchResultGrid.getItems().clear();
                }finally{
                    for(int i = 0; i < model.searchShows.size(); i++){
                        HBox hbox = ElementBuilder.createShowTile(model.searchShows.get(i), i);
                        Button snatchButton = new Button("Snatch");
                        snatchButton.setId("snatch_" + i);
                        snatchButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent e) {
                              int id = Integer.parseInt(((Button)e.getSource()).getId().split("_")[1]);
                              model.snatchShow(id);
                            }
                        });
                        hbox.getChildren().add(snatchButton);
                        searchResultGrid.getItems().add(hbox);
                    }
                }
            });

        });
        taskThread.start();
    }
    public void handleLibrary(){
        Thread taskThread = new Thread(() -> {
            model.getLibrary(forceUpdateBox.isSelected());
            Platform.runLater(() -> {
                try{
                    libraryResultGrid.getItems().clear();
                }finally{
                    for(int i = 0; i < model.libraryShows.size(); i++){
                        HBox hbox = ElementBuilder.createShowTile(model.libraryShows.get(i), i);
                        Button openDirButton = new Button("Open");
                        openDirButton.setId("snatch_" + i);
                        openDirButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent e) {
                                int id = Integer.parseInt(((Button)e.getSource()).getId().split("_")[1]);
                                model.openShowDirectory(id);
                            }
                        });
                        hbox.getChildren().add(openDirButton);
                        libraryResultGrid.getItems().add(hbox);
                    }
                }
            });
        });
        taskThread.start();
    }
    public void handleTorrents(){
        Thread taskThread = new Thread(() -> {
            model.getLibrary(forceUpdateBox.isSelected());
            Platform.runLater(() -> {
                try{
                    torrentList.getItems().clear();
                }finally{
                    for(int i = 0; i < model.libraryShows.size(); i++){
                        if(model.libraryShows.get(i).torrentMeta != null){
                            HBox hbox = ElementBuilder.createTorrentTile(model.libraryShows.get(i), i);
                            hbox.setId("torrent_"+i);
                            torrentList.getItems().add(hbox);
                        }
                    }
                }
            });
        });
        taskThread.start();
        if (!listenerAdded){
            listenerAdded = true;
            torrentList.getSelectionModel().selectedItemProperty().addListener((ChangeListener<HBox>) (observable, oldValue, newValue) -> {
                // Your action here
                if (newValue != null){
                    int showIndex = Integer.parseInt(newValue.getId().split("_")[1]);
                    fileList.getItems().clear();
                    for (int x = 0; x < model.libraryShows.get(showIndex).torrentMeta.fileList.size(); x++){
                        HBox filehbox = ElementBuilder.createFileTile(model.libraryShows.get(showIndex), x);
                        fileList.getItems().add(filehbox);
                    }
                }
            });
        }
    }
}
