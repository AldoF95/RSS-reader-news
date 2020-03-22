package sample;

import com.sun.syndication.io.FeedException;
import javafx.event.EventHandler;
import java.io.File;
import javafx.scene.input.ContextMenuEvent;
import java.net.URL;

import javafx.scene.control.Button;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Utils extends Constants {

    public ArrayList<String> listOfNames = new ArrayList<>();

    public Utils() throws MalformedURLException {}

    /**
     * open in browser (default browser) the clicked news feed
     * @param url the url link for the article
     * @throws URISyntaxException
     * @throws IOException
     */
    public void openInBrowser(String url) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI(url));
    }

    /**
     * dinamicly created buttons
     * text of the button: news portal Name
     * userData of the button: url of the rss
     * @param btnText title of the button
     * @param btnValue url of the rss
     * @return created button
     */
    public Button creatNewsNameButton(String btnText, String btnValue, String type){
        //create new button
        Button newsButton = new Button();
        newsButton.setText(btnText);
        newsButton.setUserData(btnValue);
        newsButton.setMinHeight(50);
        newsButton.setMinWidth(250);
        //hover effect
        newsButton.setStyle(BUTTON_NORMAL_STATE);
        newsButton.setOnMouseEntered(event -> newsButton.setStyle(BUTTON_HOVER_STATE));
        newsButton.setOnMouseExited(event -> newsButton.setStyle(BUTTON_NORMAL_STATE));
        //create a context menu -> for saving buttons
        ContextMenu contextMenu = saveButtonPopUp(btnText, btnValue);
        //add context menu only if button od type 'All'
        if (type.equals("All")){
            newsButton.setOnContextMenuRequested(event -> {
                contextMenu.show(newsButton, event.getScreenX(), event.getScreenY());
            });
        }
        //return created button
        return newsButton;
    }

    /**
     * component for the feeds display
     * @param title
     * @param description
     * @param publishedDate
     * @param link
     * @param index
     * @return grid pane
     */
    public GridPane showFeeds(String title, String description, Date publishedDate, String link, int index) {
        //a grid pane with title, description and date
        GridPane feedPane =  new GridPane();
        feedPane.setMinHeight(80); feedPane.setMinWidth(400);
        //title of the feed
        Label lblTitle = new Label(title);
        lblTitle.setStyle(TITLE_NORMAL_STATE);
        lblTitle.setMaxWidth(400);
        lblTitle.setWrapText(true);
        //description of the feed
        Label lblDescription = new Label(description);
        lblDescription.setWrapText(true);
        lblDescription.setStyle(DESCRIPTION_NORMAL_STATE);
        //date of the feed
        SimpleDateFormat formatDate = new SimpleDateFormat("HH:mm dd.MM.YYYY");
        String date = formatDate.format(publishedDate);
        Label lblDate = new Label(date);
        lblDate.setStyle("-fx-padding: 5");
        //hover effect
        feedPane.setStyle(FEED_NORMAL_STATE);
        feedPane.setOnMouseEntered(event -> {
            feedPane.setStyle(FEED_HOVER_STATE);
            lblTitle.setStyle(TITLE_HOVER_STATE);
        });
        feedPane.setOnMouseExited(event -> {
            feedPane.setStyle(FEED_NORMAL_STATE);
            lblTitle.setStyle(TITLE_NORMAL_STATE);
        });

        feedPane.add(lblTitle, 0, 0);
        feedPane.add(lblDescription, 0, 1);
        feedPane.add(lblDate, 0, 2);
        feedPane.setOnMouseClicked(event -> {
            try {
                openInBrowser(link);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //return created grid pane
        return  feedPane;
    }

    /**
     * write to a txt file
     * name of the news feed and its url to the feed
     * add to list only if not already present in the list
     * @param name
     * @param url
     * @throws IOException
     */
    public void addToSavedLinks(String name, String url) throws IOException {
        if(!listOfNames.contains(name)){
            String toWrite = name+","+url;
            FileWriter fileToWrite = new FileWriter(SAVE_LINK, true);
            BufferedWriter writer = new BufferedWriter(fileToWrite);
            writer.write(toWrite);
            writer.newLine();
            writer.close();
        }
    }

    /**
     * menu appearing when right clicked on the button
     * gives the option to save the selected button
     * @param name
     * @param url
     * @return created context menu
     */
    public ContextMenu saveButtonPopUp(String name, String url){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem saveButtonItem = new MenuItem("Save RSS link");
        saveButtonItem.setOnAction(event -> {
            try {
                addToSavedLinks(name, url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        contextMenu.getItems().add(saveButtonItem);
        return  contextMenu;
    }

}
