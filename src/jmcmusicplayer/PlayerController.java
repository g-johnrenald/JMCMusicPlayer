/*
Copyright 2020 John Renald Garcines
Diploma of Software Development
South Metropolitan TAFE Murdoch Campus
*/
/*
    Created on : 2020/04/25, 18:25:16
    Author     : John
*/
package jmcmusicplayer;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.*;

public class PlayerController implements Initializable {

    private Scene loginScene;

    //getting main scene from main method
    public void setLoginScene(Scene scene) {
        this.loginScene = scene;
    }

    //method to open main scene
    public void openLoginScene(MouseEvent event) {
        //getting current stage and set scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(loginScene);
    }

    LinkedList<Song> source; //list of Song object
    MediaPlayer player; //current media player
    int currentIndex; //index of current music

    //stores window's position
    Stage stage;
    double xOffset = 0;
    double yOffset = 0;

    @FXML
    private AnchorPane mainForm;
    @FXML
    private Label nowPlaying;
    @FXML
    private ListView<String> playlist;
    @FXML
    private TextField searchField;
    @FXML
    private ToggleButton playToggle;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        source = new LinkedList<>(); //initiate source list
        playlist.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); //enable multiple selection on playlist
    }

    @FXML
    private void mainFormMousePressedHandler(MouseEvent event) {
        this.stage = (Stage) mainForm.getScene().getWindow();
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    private void mainFormMouseDraggedHandler(MouseEvent event) {
        this.stage = (Stage) mainForm.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    @FXML
    private void saveBtnHandler() {
        FileChooser fc = new FileChooser();
        String initialPath = System.getProperty("user.dir"); //set initial path to project's root
        fc.setInitialDirectory(new File(initialPath + "/sampleplaylist"));
        fc.setInitialFileName("New Playlist");
        fc.setTitle("Save your playlist");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV File", "*.csv"));
        File csv = fc.showSaveDialog(null);

        // check if csv is opened
        if (csv != null) {
            try {
                WriteCSV(csv.getPath(), source);
            } catch (IOException ex) {
                nowPlaying.setText("Cannot save playlist");
            }
        }
    }

    /**
     * Writes CSV file
     * @param path
     * @param source
     * @throws IOException
     */
    private void WriteCSV(String path, LinkedList<Song> source) throws IOException {
        try (Writer writer = new FileWriter(path)) {
            var beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            try {
                beanToCsv.write(source);
            } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException ex) {
                nowPlaying.setText("Cannot save playlist");
            }
        }
    }

    @FXML
    private void opnBtnHandler(MouseEvent event) {
        FileChooser fc = new FileChooser();
        String initialPath = System.getProperty("user.dir");
        fc.setInitialDirectory(new File(initialPath + "/sampleplaylist"));
        fc.setTitle("Choose your playlist");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV File", "*.csv"));
        File csv = fc.showOpenDialog(null);

        // check if csv is opened
        if (csv != null) {
            try {
                source.clear(); //clear the source
                playlist.getItems().clear(); //clear the playlist
                source = new LinkedList<>(ReadCSV(csv.getPath())); //assign new source which obtained from csv
                displayPlaylist();
            } catch (FileNotFoundException ex) {
                nowPlaying.setText("Cannot open playlist");
            }
        }
    }

    /**
     * Reads CSV file and pass List of <Song>
     *
     * @param path
     * @return
     * @throws FileNotFoundException
     */
    private List<Song> ReadCSV(String path) throws FileNotFoundException {
        return (List<Song>) new CsvToBeanBuilder(new FileReader(path))
                .withType(Song.class).build().parse(); //read csv and return list of Song object
    }

    @FXML
    private void addBtnHandler(MouseEvent event) {
        FileChooser fc = new FileChooser();
        String initialPath = System.getProperty("user.dir");
        fc.setInitialDirectory(new File(initialPath + "/samplemusic"));
        fc.setTitle("Choose your song");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MP3 File", "*.mp3"));
        List<File> files = fc.showOpenMultipleDialog(null);

        // check if files are empty
        if (!files.isEmpty()) {
            //add names and URIs of files to source
            files.forEach(s -> source.add(new Song(s.getName().replaceFirst("[.][^.]+$", ""), s.toURI().toASCIIString())));
            displayPlaylist();
        }
    }

    /**
     * Displays the title of <Song>
     */
    private void displayPlaylist() {
        playlist.getItems().clear();
        this.source.forEach(s -> {
            playlist.getItems().add(s.getTitle());
        });
    }

    @FXML
    private void trashBtnHandler(MouseEvent event) {
        if (!playlist.getSelectionModel().getSelectedIndices().isEmpty()) {
            List<String> items = new ArrayList<>(playlist.getSelectionModel().getSelectedItems());
            items.forEach((i -> source.removeIf(s -> s.getTitle().equals(i))));
            this.displayPlaylist();
        }
    }

    @FXML
    private void sortBtnHandler(MouseEvent event) {
        if (!source.isEmpty()) {
            //sorts songs by title
            source.sort(Comparator.comparing(Song::getTitle));
            displayPlaylist();
        }
    }

    @FXML
    private void searchFieldHandler(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            search(searchField.getText());
        }
    }

    @FXML
    private void searchBtnHandler(MouseEvent event) {
        search(searchField.getText());
    }

    /**
     * Searches song by title and play
     *
     * @param key
     */
    private void search(String key) {
        currentIndex = Collections.binarySearch(source, new Song(key),
                Comparator.comparing(Song::getTitle)); //compare using name

        if (currentIndex >= 0) {
            CreateNewPlayer(source.get(currentIndex));
            playlist.getSelectionModel().select(currentIndex);
            nowPlaying.setText(source.get(currentIndex).getTitle());
            player.play();
        } else {
            nowPlaying.setText("Not found");
        }
    }

    @FXML
    private void playToggleHandler(MouseEvent event) {
        //pause song when its played
        if (!source.isEmpty()) {
            if (playToggle.isSelected()) {
                playSong();
            } else {
                player.pause(); //if toggle button is not selected then pause
            }
        } else {
            playToggle.setSelected(false); //if source is empty then keep the button status to unselected
            nowPlaying.setText("Nothing to play");
        }
    }

    /**
     * Plays song with new media player
     */
    private void playSong() {
        if (currentIndex >= 0) {
            if (playlist.getSelectionModel().getSelectedIndex() >= 0) {
                currentIndex = playlist.getSelectionModel().getSelectedIndex(); //pass selected index
                CreateNewPlayer(source.get(currentIndex)); //creates new player
                playlist.getSelectionModel().select(currentIndex); //highlight current song
                nowPlaying.setText(source.get(currentIndex).getTitle()); //show title of current song
                player.play();
            } else {
                CreateNewPlayer(source.get(0)); //play first song
                nowPlaying.setText(source.get(0).getTitle()); //show title of current song
                playlist.getSelectionModel().select(0); //highlight current song
            }
        }
    }

    /**
     * Creates media player object
     *
     * @param source
     */
    private void CreateNewPlayer(Song source) {
        //dispose player when it is not null
        if (player != null) {
            player.pause();
            player.dispose();
        }

        //create new player
        this.player = new MediaPlayer(new Media(source.getPath()));
    }

    @FXML
    private void forwardBtnHandler(MouseEvent event) {
        if (!source.isEmpty()) {
            //keep current index range
            if (currentIndex >= 0 && currentIndex < source.size() - 1) {
                CreateNewPlayer(source.get(++currentIndex)); // go to next song
                nowPlaying.setText(source.get(currentIndex).getTitle()); //show title of current song
                playlist.getSelectionModel().clearSelection(); //clear selection
                playlist.getSelectionModel().select(currentIndex); //highlight current song
                player.play();
            }
        } else {
            nowPlaying.setText("Nothing to play");
        }
    }

    @FXML
    private void rewindBtnHandler(MouseEvent event) {
        if (!source.isEmpty()) {
            //keep current index range
            if (currentIndex > 0 && currentIndex < source.size()) {
                CreateNewPlayer(source.get(--currentIndex)); //go to previous sng
                nowPlaying.setText(source.get(currentIndex).getTitle()); //show title of current song
                playlist.getSelectionModel().clearSelection(); //clear selection
                playlist.getSelectionModel().select(currentIndex); //highlight current song
                player.play();
            }
        } else {
            nowPlaying.setText("Nothing to play");
        }
    }

    @FXML
    private void closeBtnHandler(MouseEvent event) {
        this.stage = (Stage) mainForm.getScene().getWindow();
        this.stage.close();
    }

    @FXML
    private void miniBtnHandler(MouseEvent event) {
        this.stage = (Stage) mainForm.getScene().getWindow();
        this.stage.setIconified(true);
    }

    @FXML
    private void helpBtnHandler(MouseEvent event) {
        String url = "src/help/help.html";
        File htmlFile = new File(url);

        try {
            //open the help file in a default browser
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch (IOException ex) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "unable to open help file. " + ex);
            errorAlert.show();
        }
    }
}
