package danilko09.mcwl.api;

import danilko09.mcwl.minecraft.launcher;
import danilko09.mcwl.util.Files;
import danilko09.mcwl.util.HTTP;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 * Мост для свзи с JavaScript в браузере
 * @author danilko09
 */
public class JSBridge {
    
    /**
     * Скачивает файл игры
     * @param url url файла
     * @param filename где должен располагаться в папке с игрой
     */
    public void download(String url, String filename){
        File f = Files.getFile("game"+File.separator+filename);
        if(!f.exists()){
            f.getParentFile().mkdirs();
            try {
                HTTP.download(new URL(url), f.getAbsolutePath());
            } catch (IOException ex) {
                Logger.getLogger(JSBridge.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Запускает игру из папки MCWL
     * @param player_name имя игрока
     * @param version версия игры
     * @param uuid uuid игрока
     */
    public void startMC(String player_name, String version, String uuid){
        new launcher(version, Files.getBaseDir()+File.separator+"game").start(player_name, uuid);
    }
    
    /**
     * Запускает игру из .minecraft
     * @param player_name имя игрока
     * @param version версия игры
     * @param uuid uuid игрока
     */
    public void startRMC(String player_name, String version, String uuid){
        new launcher(version).start(player_name, uuid);
    }
    
    /**
     * Удаляет все загруженные файлы игры
     */
    public void removeGame(){
        Files.delete(Files.getFile("game"));
    }
    
    /**
     * Закрывает приложение
     */
    public void exit() {
        Platform.exit();
    }
    
}
