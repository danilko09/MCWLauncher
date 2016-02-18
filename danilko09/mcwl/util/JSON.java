package danilko09.mcwl.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Работает с JSON с помощью библиотеки GSON
 *
 * @author danilko09
 */
public class JSON {

    /**
     * Возвращает файл расположенный по адресу json_path в виде JsonObject
     *
     * @param json_path Расположение файла
     * @return Объект типа JsonOBject или null, если не удалось распарсить файл
     */
    public static JsonElement loadJSON(String json_path) {
        return loadJSON(new File(json_path));
    }

    /**
     * Сохраняет data в файл json_file
     *
     * @param json_path Файл для сохранения
     * @param data данные для сохранения
     */
    public static void saveJSON(String json_path, JsonElement data) {
        saveJSON(new File(json_path), data);
    }

    /**
     * Возвращает файл в виде JsonObject
     *
     * @param json_file Файл для обработки
     * @return Объект типа JsonOBject или null, если не удалось распарсить файл
     */
    public static JsonElement loadJSON(File json_file) {
        try {
            JsonElement json = (new JsonParser()).parse(new FileReader(json_file));
            if (!json.isJsonNull()) {
                return json;
            }
        } catch (FileNotFoundException | JsonIOException | JsonSyntaxException e) {
            System.err.println(e.getMessage());
            return null;
        }
        return null;
    }

    /**
     * Сохраняет data в файл json_file
     *
     * @param json_file Файл для сохранения
     * @param data данные для сохранения
     */
    public static void saveJSON(File json_file, JsonElement data) {

        try {
            Writer writer = new FileWriter(json_file.getAbsolutePath());

            Gson gson = new GsonBuilder().create();
            gson.toJson(data, writer);

            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(JSON.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Возвращает значение елемента element в JSON-объекте json_array (со
     * вложенностью) Например, при element = {'a','b'} вернет json_array[a][b]
     *
     * @param json_array JSON-объект
     * @param element "путь" до элемента
     * @return Значеине элемента element в json_array
     */
    public static JsonElement getValue(JsonObject json_array, String[] element) {
        for (String el : element) {
            json_array = getValue(json_array, el).getAsJsonObject();
        }
        return json_array;
    }

    /**
     * Возвращает значение елемента element в JSON-объекте json_array
     *
     * @param json_array JSON-объект
     * @param element название элемента для возврата
     * @return Значеине элемента element в json_array
     */
    public static JsonElement getValue(JsonObject json_array, String element) {
        return json_array.get(element);
    }

    /**
     * Возвращает строковое значение элемента в JSON-файле
     *
     * @param json_file JSON-файл
     * @param element индекс элемента
     * @return Возвращает строковое значение элемента, в случае ошибки
     * возвращает пустую строку
     */
    public static String getStringValue(File json_file, String element) {
        try {
            return loadJSON(json_file).getAsJsonObject().get(element).getAsString();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "";
        }
    }
    
    /**
     * Возвращает строковое значение элемента в JSON-файле
     *
     * @param json_file JSON-файл
     * @param element индекс элемента
     * @return Возвращает строковое значение элемента, в случае ошибки
     * возвращает пустую строку
     */
    public static ArrayList<String> getStringValues(File json_file, String element) {
        try {
            ArrayList<String> ret = new ArrayList<>();
            JsonArray arr = getValue(json_file,element);
            for(JsonElement el:arr){
                ret.add(el.getAsString());
            }
            return ret;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public static JsonArray getValue(File json_file, String element) {
        return loadJSON(json_file).getAsJsonObject().get(element).getAsJsonArray();        
    }

}
