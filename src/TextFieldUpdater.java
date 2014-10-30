
import javafx.application.Platform;
import javafx.scene.control.TextField;


/**
 *
 * @author HEIDY2016
 */
public class TextFieldUpdater implements Updater{
    public TextField text;
    
    public void update(final String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                text.setText(message);
            }
        });

    }
}