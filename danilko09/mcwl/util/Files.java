package danilko09.mcwl.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Класс для базовой работы с файлами
 *
 * @author danilko09
 */
public class Files {

    /**
     * @return Путь до папки с файлами MCWL
     */
    public static String getBaseDir() {
        return System.getProperty("user.home") + File.separator + "MCWL";
    }

    /**
     * @return Возвращает путь до папки .minecraft
     */
    public static String getMCDir() {
        String home = System.getProperty("user.home", "");
        String path = "minecraft";
        switch (Platform.getOS()) {
            case Platform.OS_WINDOWS:
                String appData = System.getenv("APPDATA");
                return appData == null ? home +File.separator+ path : appData + File.separator + "." + path;
            case Platform.OS_MAC:
                return home+"/Library/Application Support/" + path;
            case Platform.OS_SUNOS:
            default:
                return home + File.separator + "." + path;
        }
    }

    /**
     * Удаляет файл или папку. От File.delete() отличается тем, что при удалении папки не требует её очистки.
     * @param f файл\папка для удаления
     */
    public static void delete(File f){
        if(f.exists()){
            if(f.isFile()){f.delete();return;}
            String[] files = f.list();
            if(files.length == 0){f.delete();return;}
            for(String file:files){
                delete(new File(f,file));
            }
            f.delete();
        }
    }
    
    /**
     * Создает объект класса File из папки MCWL по его названию
     *
     * @param filename Путь до файла относительно папки MCWL
     * @return Файл filename в папке MCWL
     */
    public static File getFile(String filename) {
        return new File(getBaseDir() + File.separator + filename);
    }

    public static void unzip(String path, String file)
	{
		try {
		Vector<ZipEntry> zipentry = new Vector<ZipEntry>();
		ZipFile zipfile = new ZipFile(file);
		Enumeration<?> en = zipfile.entries();
	
		while(en.hasMoreElements()) zipentry.addElement((ZipEntry)en.nextElement());
	
		for (int i = 0; i < zipentry.size(); i++)
		{
			ZipEntry ze = (ZipEntry)zipentry.elementAt(i);
			extractFromZip(file, path, ze.getName(), zipfile, ze);
		}
	
		zipfile.close();
		}
		catch(Exception e){}
	}

	static void extractFromZip(String szZipFilePath, String szExtractPath, String szName, ZipFile zf, ZipEntry ze) throws Exception
	{
		if(ze.isDirectory()) return;
	
		String szDstName = slash2sep(szName);
		String szEntryDir;
	
		if(szDstName.lastIndexOf(File.separator) != -1) szEntryDir = szDstName.substring(0, szDstName.lastIndexOf(File.separator));
		else szEntryDir = "";
		File newDir = new File(szExtractPath + File.separator + szEntryDir);
	
		newDir.mkdirs();	 
	
		FileOutputStream fos = 
		new FileOutputStream(szExtractPath +
		File.separator + szDstName);
	
		InputStream is = zf.getInputStream(ze);
		byte[] buf = new byte[1024];
	
		int nLength;
	
		while(true)
		{
			nLength = is.read(buf);
			if(nLength < 0) 
			break;
			fos.write(buf, 0, nLength);
		}

		is.close();
		fos.close();
	}
	
	static String slash2sep(String src)
	{
		int i;
		char[] chDst = new char[src.length()];
		String dst;
	
		for(i = 0; i < src.length(); i++)
		{
			if(src.charAt(i) == '/') chDst[i] = File.separatorChar;
			else chDst[i] = src.charAt(i);
		}
		dst = new String(chDst);
		return dst;
	}
    
}
