package danilko09.mcwl.minecraft;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import danilko09.mcwl.util.Files;
import danilko09.mcwl.util.JSON;
import danilko09.mcwl.util.Platform;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Класс для запуска minecraft
 * @author danilko09
 */
public class launcher {

    private String version,minecraft_dir,assets_dir;
    private final List<URL> jars = new ArrayList<>();
    
    public launcher(String vers){
        version = vers;
        minecraft_dir = Files.getMCDir();
        assets_dir = Files.getMCDir()+File.separator+"assets";
    }
    
    public launcher(String vers, String mc_dir){
        version = vers;
        minecraft_dir = mc_dir;
        assets_dir = Files.getMCDir()+File.separator+"assets";
    }
    
    public launcher(String vers, String mc_dir, String assets){
        version = vers;
        minecraft_dir = mc_dir;
        assets_dir = assets;
    }
    
   
    public void start(String player_name, String token) {
        
        File v_json = new File(minecraft_dir + "/versions" + "/" + version + "/" + version + ".json");

        String a_name = JSON.getStringValue(v_json, "assets");
        File a_json = new File(assets_dir + File.separator + "indexes" + File.separator + a_name + ".json");

        File mine = new File(minecraft_dir + "/versions" + "/" + version + "/" + version + ".jar");

        try {
            jars.add(mine.toURI().toURL());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        loadLibs(v_json);
        
        URLClassLoader cl = new URLClassLoader(jars.toArray(new URL[jars.size()]));

        List<String> Arguments = new ArrayList<>();
        for (String arg : JSON.getStringValue(v_json, "minecraftArguments").split(" ")) {
            Arguments.add(arg.replace("${auth_player_name}", player_name).replace("${version_name}", version).replace("${game_directory}", minecraft_dir + File.separator).replace("${assets_root}", assets_dir).replace("${assets_index_name}", a_name).replace("${auth_uuid}", "123").replace("${auth_access_token}", token).replace("${user_properties}", "{}").replace("${user_type}", "mojang"));
        }
        System.setProperty("fml.ignoreInvalidMinecraftCertificates", "true");
        System.setProperty("fml.ignorePatchDiscrepancies", "true");
        System.setProperty("org.lwjgl.librarypath", minecraft_dir + "/versions" + "/" + version + "/natives");
        System.setProperty("net.java.games.input.librarypath", minecraft_dir + "/versions" + "/" + version + "/natives");
        System.setProperty("java.library.path", minecraft_dir + "/versions" + version + "/natives");

        try {
            Class<?> start = cl.loadClass(JSON.getStringValue(v_json, "mainClass"));
            System.out.println("Запуск игры");
            Method main = start.getMethod("main", new Class[]{String[].class});
            main.invoke(null, new Object[]{Arguments.toArray(new String[0])});
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException x) {
            JOptionPane.showMessageDialog(null, x, "Ошибка запуска!", javax.swing.JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

    }

    private void loadLibs(File v_json) {
        
        
        ArrayList<String> libs = inheritedLibs(getLibs(v_json));
        
        for (String lib : libs) {
            String lib_package = lib.split(":")[0].replace(".", "/");
            String lib_name = lib.split(":")[1];
            String lib_version = lib.split(":")[2];
            File F = new File(minecraft_dir + "/libraries/" + lib_package + "/" + lib_name + "/" + lib_version + "/");
            if (lib.contains("org.lwjgl.lwjgl:lwjgl-platform:") || lib.contains("net.java.jinput:jinput-platform:")) {
                File l = new File(F + "/" + lib_name + "-" + lib_version + "-natives-" + Platform.getOSN() + ".jar");
                Files.unzip(minecraft_dir + "/versions/" + version + "/natives/", l.toString());
            } else {
                File l = new File(F + "/" + lib_name + "-" + lib_version + ".jar");
                try {
                    jars.add(l.toURI().toURL());
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }

    }

    private ArrayList<String> getLibs(File v_json){
        ArrayList<String> libs = new ArrayList<>();
        JsonArray libraries = JSON.getValue(v_json, "libraries");
        for (JsonElement el : libraries) {
            libs.add(el.getAsJsonObject().get("name").getAsString());
        }
        return libs;
    }
    
    private ArrayList<String> inheritedLibs(ArrayList<String> libs){
        
        File v_json = new File(minecraft_dir + "/versions" + "/" + version + "/" + version + ".json");
        String jar = JSON.getStringValue(v_json, "jar");
        String inheritsFrom = JSON.getStringValue(v_json, "inheritsFrom");
        if (!inheritsFrom.equals("")) {
            if (!jar.equals("")) {
                version = jar;
            }
            File i_json = new File(minecraft_dir + "/versions" + "/" + inheritsFrom + "/" + inheritsFrom + ".json");
            libs.addAll(getLibs(i_json));
            return inheritedLibs(libs);
        }
        return libs;
    }
    
}
