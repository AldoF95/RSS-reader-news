package sample;

import javafx.event.ActionEvent;

import java.io.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import com.sun.syndication.feed.synd.*;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;


public class Controller extends Utils implements Initializable{

    private final String ALL_LINKS="links.txt";
    private final String SAVE_LINKS="savedLinks.txt";

    @FXML
    public GridPane mainPane;
    @FXML
    public GridPane feedListPane;
    @FXML
    public ScrollPane feedScrollPane;
    @FXML
    public GridPane newsButtonsPane;
    @FXML
    public VBox tabAllLinksButtons;
    @FXML
    public VBox tabSavedLinkButtons;
    @FXML
    public TabPane tabPaneOptions;
    @FXML
    public Tab tabSave;
    @FXML
    public Tab tabAll;

    public Controller() throws MalformedURLException {
    }

    /**
     * overrided method for initialization
     * load all the dynamic buttons
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadButtons();
            loadSavedButtons();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * load buttons from links file
     * @throws FileNotFoundException
     */
    @FXML
    public void loadButtons() throws FileNotFoundException {
        File links = new File(ALL_LINKS);
        Scanner scn = new Scanner(links);
        tabAllLinksButtons.setSpacing(20);
        while (scn.hasNextLine()){
            String linkName = scn.nextLine();
            //split on comma
            String[] nameAndLink = linkName.split(",");
            //create button with parameters name and link from class Utils
            Button newsButton = creatNewsNameButton(nameAndLink[0], nameAndLink[1], "All");
            String path = nameAndLink[1];
            newsButton.setOnAction(event -> {
                try {
                    retriveFeeds(path);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (FeedException e) {
                    e.printStackTrace();
                }
            });
            tabAllLinksButtons.getChildren().add(newsButton);
        }
    }

    /**
     * load button from savedLinks file
     * @throws FileNotFoundException
     */
    public void loadSavedButtons() throws FileNotFoundException {
        File links = new File(SAVE_LINKS);
        Scanner scn = new Scanner(links);
        tabSavedLinkButtons.setSpacing(20);
        while (scn.hasNextLine()){
            String linkName = scn.nextLine();
            String[] nameAndLink = linkName.split(",");
            Button newsButton = creatNewsNameButton(nameAndLink[0], nameAndLink[1], "Save");
            //add names of saved button for later check of existence
            listOfNames.add(nameAndLink[0]);
            String path = nameAndLink[1];
            newsButton.setOnAction(event -> {
                try {
                    retriveFeeds(path);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (FeedException e) {
                    e.printStackTrace();
                }
            });
            tabSavedLinkButtons.getChildren().add(newsButton);
        }
    }

    /**
     * getting and reading rss feeds from clicked button
     * retrieves title, published date, description and url for redirecting on click
     * @param path
     * @throws IOException
     * @throws FeedException
     */
    public  void retriveFeeds(String path) throws IOException, FeedException {
        //url for reading
        URL pathUrl = new URL(path);
        SyndFeedInput inputFeeds = new SyndFeedInput();
        SyndFeed feeds = inputFeeds.build(new XmlReader(pathUrl));
        //located in a vertical box
        VBox feedsList = new VBox();
        feedsList.setStyle("-fx-background-color: #001245;"+"-fx-padding: 20;"+"-fx-alignment: center;"+"-fx-border-color: #001245");
        feedScrollPane.setContent(feedsList);
        feedScrollPane.setPannable(true);
        feedsList.setSpacing(20);
        int index = 0;
        //read first 10 entries
        for (Iterator i = feeds.getEntries().iterator(); i.hasNext();){
            SyndEntry entry = (SyndEntry) i.next();
            feedsList.getChildren().add(showFeeds(entry.getTitle(), entry.getDescription().getValue(), entry.getPublishedDate(), entry.getLink(), index));
            index++;
            if (index>=10){break;}
        }
    }

}
