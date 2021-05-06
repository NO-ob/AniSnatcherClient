package UITools;

import APITools.Utilz;
import MetaData.Episode;
import MetaData.Show;
import MetaData.TorrentFile;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.concurrent.atomic.AtomicReference;


public class ElementBuilder {

    public static HBox createShowTile(Show show, int index){
        HBox hbox = new HBox();
        Image image = new Image(show.posterURL);
        if (image.isError()) {
            System.out.println("Image error getting png instead");
            image = new Image(Utilz.StoreImageAsPNG(show.posterURL,show.sourceID));
        }
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(200);
        hbox.getChildren().add(imageView);
        VBox vbox = new VBox();
        if(!show.title_en.isEmpty()){
            Text title_en = new Text(show.title_en);
            vbox.getChildren().add(title_en);
        }
        if(!show.title_romaji.isEmpty()){
            Text title_romaji = new Text(show.title_romaji);
            vbox.getChildren().add(title_romaji);
        }
        if(!show.title_jp.isEmpty()){
            Text title_jp = new Text(show.title_jp);
            vbox.getChildren().add(title_jp);
        }
        Text synopsis = new Text(show.synopsis);
        synopsis.setTextAlignment(TextAlignment.LEFT);
        synopsis.setWrappingWidth(600);
        if(show.torrentMeta != null){
            HBox infoRow = new HBox();
            infoRow.alignmentProperty().setValue(Pos.BOTTOM_RIGHT);
            if (!show.torrentMeta.resolution.isEmpty()){
                Text resolution = new Text(show.torrentMeta.resolution);
                infoRow.getChildren().add(resolution);
            }
            if (!show.torrentMeta.sourceType.isEmpty()){
                Text sourceType = new Text(show.torrentMeta.sourceType);
                infoRow.getChildren().add(sourceType);
            }
            vbox.getChildren().add(infoRow);
        }
        vbox.getChildren().add(synopsis);
        hbox.getChildren().add(vbox);
        return hbox;
    }
    public static HBox createTorrentTile(Show show, int index){
        HBox hbox = new HBox();
        VBox vbox = new VBox();
        if(!show.torrentMeta.releaseName.isEmpty()){
            Text torrentName = new Text(show.torrentMeta.releaseName);
            vbox.getChildren().add(torrentName);
        }
        if(!show.torrentMeta.status.isEmpty()){
            Text torrentStatus = new Text("Status: " +show.torrentMeta.status);
            vbox.getChildren().add(torrentStatus);
        }
        Text percentText = new Text("Percent Done: "+ show.torrentMeta.percentDone);
        vbox.getChildren().add(percentText);
        hbox.getChildren().add(vbox);
        return hbox;
    }
    public static HBox createFileTile(Show show, int fileListIndex){
        HBox hbox = new HBox();
        VBox vbox = new VBox();
        TorrentFile file = show.torrentMeta.fileList.get(fileListIndex);
        if(!file.fileName.isEmpty()){
            Text fileName = new Text("File Name: " + file.fileName);
            vbox.getChildren().add(fileName);
        }
        AtomicReference<Episode> episode = new AtomicReference<>();
        if (show.episodes.get(fileListIndex).DBID == file.episodeDBID){
            episode.set(show.episodes.get(fileListIndex));
        } else {
            show.episodes.forEach(ep -> {
                if (ep.DBID == file.episodeDBID){
                    episode.set(ep);
                }
            });
        }
        String epName = "";
        if(!episode.get().title_en.isEmpty()){
            epName = episode.get().title_en;
        } else if(!episode.get().title_romaji.isEmpty()){
            epName = episode.get().title_romaji;
        } else {
            epName = episode.get().title_jp;
        }
        Text episodeName = new Text("Episode Name: " +epName);
        vbox.getChildren().add(episodeName);
        Text episodeNum = new Text("Episode Number: " +episode.get().number);
        vbox.getChildren().add(episodeNum);
        hbox.getChildren().add(vbox);
        return hbox;
    }
}
