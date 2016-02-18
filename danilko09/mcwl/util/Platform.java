package danilko09.mcwl.util;


/**
 * Класс для работы с платформой
 *
 * @author danilko09
 */
public class Platform {

    public static final int OS_LINUX = 0;
    public static final int OS_WINDOWS = 1;
    public static final int OS_MAC = 2;
    public static final int OS_SUNOS = 3;
    public static final int OS_UNDEFINED = 4;

    /**
     * Определяет ОС
     *
     * @return Возвращает значение одной из констант с префиксом OS_ (см код
     * класса)
     */
    public static int getOS() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win")) {
            return OS_WINDOWS;
        }
        if (osName.contains("mac")) {
            return OS_MAC;
        }
        if (osName.contains("solaris") || osName.contains("sunos")) {
            return OS_SUNOS;
        }
        if (osName.contains("linux") || osName.contains("unix")) {
            return OS_LINUX;
        }

        return OS_UNDEFINED;
    }

    public static String getOSN() {
        switch (getOS()) {
            case OS_WINDOWS:
                return "windows";
            case OS_MAC:
                return "osx";
            default:
                return "linux";
        }
    }

}
