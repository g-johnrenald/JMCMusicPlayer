package test;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.ListViewMatchers.hasItems;

public class ControllerUnitTest extends ApplicationTest {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().
                getResource("/jmcmusicplayer/PlayerView.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Test
    public void testAddButton() {
        String files = "\"sample1.mp3\" \"sample2.mp3\" \"sample3.mp3\" \"sample4.mp3\"";
        StringSelection stringSelection = new StringSelection(files);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);

        clickOn("#addBtn").push(KeyCode.CONTROL, KeyCode.V).press(KeyCode.ENTER);
        verifyThat("#playlist", hasItems(4));
    }

    @Test
    public void testSaveButton() {
        String files = "\"sample1.mp3\" \"sample2.mp3\" \"sample3.mp3\" \"sample4.mp3\"";
        StringSelection stringSelection = new StringSelection(files);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);

        clickOn("#addBtn").push(KeyCode.CONTROL, KeyCode.V).press(KeyCode.ENTER);
        clickOn("#saveBtn").press(KeyCode.ENTER);
    }

    @Test
    public void testOpenButton() {
        String files = "Test_Playlist.csv";
        StringSelection stringSelection = new StringSelection(files);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);

        clickOn("#opnBtn").push(KeyCode.CONTROL, KeyCode.V).press(KeyCode.ENTER);
        verifyThat("#playlist", hasItems(4));
    }

    @Test
    public void testSortButton() {
        String files = "\"sample3.mp3\" \"sample1.mp3\" \"sample2.mp3\" \"sample4.mp3\"";
        StringSelection stringSelection = new StringSelection(files);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);

        clickOn("#sortBtn").push(KeyCode.CONTROL, KeyCode.V).press(KeyCode.ENTER);
    }

    @Test
    public void testSearchButton(){
        // add files
        String files = "\"sample1.mp3\" \"sample2.mp3\" \"sample3.mp3\" \"sample4.mp3\"";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(files), null);
        clickOn("#addBtn").push(KeyCode.CONTROL, KeyCode.V).press(KeyCode.ENTER);

        // set keyword
        String keyword = "sample1";
        clipboard.setContents(new StringSelection(keyword), null);
        clickOn("#searchField").push(KeyCode.CONTROL, KeyCode.V).press(KeyCode.ENTER);
    }
}