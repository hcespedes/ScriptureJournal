
import javafx.application.Platform;
import javafx.scene.control.TextField;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author HEIDY2016
 */
public class TextFieldUpdater implements Updater{
    public TextField text;
    
    public void update(final int count) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                text.setText(Integer.toString(count));
            }
        });

    }
}