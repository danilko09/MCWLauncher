package danilko09.mcwl.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Класс для работы с HTTP
 * @author danilko09
 */
public class HTTP {

    /**
     * Сохраняет содержимое страницы\файла из from в файл to
     * @param from URL файла\страницы
     * @param to путь куда сохранить файл
     * @throws IOException 
     */
    public static void download(URL from, String to) throws IOException {
        ReadableByteChannel rbc = Channels.newChannel(from.openStream());
        FileOutputStream fos = new FileOutputStream(to);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }

}
