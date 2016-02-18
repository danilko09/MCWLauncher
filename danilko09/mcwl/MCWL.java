package danilko09.mcwl;

import danilko09.mcwl.api.JSBridge;
import danilko09.mcwl.cookies.MCWLCookiePolicy;
import danilko09.mcwl.cookies.MCWLCookieStore;
import danilko09.mcwl.minecraft.launcher;
import java.net.CookieHandler;
import java.net.CookieManager;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import netscape.javascript.JSObject;

/**
 * Главный класс MCWL. Создает окошко-браузер и подключает к нему двухсторонний
 * мост Java-JS
 *
 * @author danilko09
 */
public class MCWL extends Application {

    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();

    @Override
    public void start(Stage stage) {
        
        stage.setWidth(700);
        stage.setHeight(500);

        Scene scene = new Scene(browser);

        setupCookies();
        
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
            @Override
            public void changed(ObservableValue ov, State oldState, State newState) {

                if (newState == Worker.State.SUCCEEDED) {
                    stage.setTitle(webEngine.getTitle());
                    JSObject jsobj = (JSObject) webEngine.executeScript("window");
                    jsobj.setMember("launcher", new JSBridge());
                }

            }
        });
        webEngine.load("http://piccolo.tk/mcwl/frame.html");

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Заменяет стандартный CookieStore, для того чтобы куки сохранялись
     */
    private void setupCookies(){
        MCWLCookieStore cookieStore = new  MCWLCookieStore();
        CookieManager cookieManager = new CookieManager(cookieStore, new MCWLCookiePolicy());
        CookieHandler.setDefault(cookieManager);
    }
    
}
