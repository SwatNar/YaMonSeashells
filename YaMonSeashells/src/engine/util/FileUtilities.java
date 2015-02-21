package engine.util;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class FileUtilities
{
   public static final String ASSET_DIRECTORY = "./assets/";
   public static final String NEW_LINE_CHARACTER = System.getProperty("line.separator");
   public static final FileExtensionFilter SUPPORTED_IMAGES = new FileExtensionFilter(new String[]{".png",".jpg",".jpeg",".gif",".bmp",".tiff"});

   public static String[] listFiles(String path, String... extensions)
   {
      File file = new File(path);
      return file.list( new FileExtensionFilter(extensions));
   }

   public static File[] getFiles(String path, File currentFile)
   {
      String currentDir = ConvertionUtilities.getDirectory(currentFile);
      String normalizedPath = path.replaceAll("\\\\", "/");
      String destDir = ConvertionUtilities.getDirectory(normalizedPath);
      String filename = ConvertionUtilities.getFile(normalizedPath);

      if (!filename.contains("*"))
      {
         File filepath = new File(new StringBuilder(currentDir).append('/').append(destDir).append('/').append(filename).toString());
         File[] files =
         {
            filepath
         };
         return files;
      }
      else
      {
         File filepath = new File(new StringBuilder(currentDir).append('/').append(destDir).toString());
         return filepath.listFiles();
      }
   }

   public static InputStream loadFile(String file)
   {
      try
      {
         return new BufferedInputStream(new FileInputStream(new File(file)));
      }
      catch (Exception e)
      {
         e.printStackTrace();
         throw new IllegalArgumentException("cannot find file" + file);
      }
   }

   public static InputStream loadFile(String filepath, AssetManager assetManager)
   {
      AssetInfo file = assetManager.locateAsset(new AssetKey(filepath));
      if (file==null) throw new IllegalArgumentException("Cannot find file "+filepath);
      return file.openStream();
   }

   public static List<File> getSubfiles(File file)
   {
      return getSubfiles(file, new ArrayList<File>(80));
   }

   public static List<File> getSubfiles(File file, List<File> fileList)
   {
      File[] files = file.listFiles();
      for (File f : files)
      {
         if (f.isFile()) fileList.add(f);
         else getSubfiles(f, fileList);
      }
      return fileList;
   }

   public static InputStream StringToInputStream(String s)
   {
      try
      {
         return new ByteArrayInputStream(s.getBytes("UTF-8"));
      }
      catch (UnsupportedEncodingException e)
      {
         e.printStackTrace();
         return null;
      }
   }

   ///-----------------------------------------------------------------------------------
   //Converts a file into an array of string
   public static ArrayList<String> readFile(File file)
   {
      return readFile(file, "UTF8");
   }

   ///-----------------------------------------------------------------------------------
   //Converts a file into an array of string
   public static ArrayList<String> readFile(File file, String encoding)
   {
      ArrayList<String> lines = new ArrayList<String>(50);
      if (!file.exists())
      {
         throw new IllegalStateException("Unable to find File " + file + " bye");
      }//if
      String line;
      boolean end = false;
      try
      {
         BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
         while (!end)
         {
            line = br.readLine();
            if (line == null)
            {
               end = true;
            }
            else
            {
               lines.add(line);
            }
         }//while
         br.close();
      }//try
      catch (IOException ioe)
      {
         System.err.println("Unable to open file");
         System.exit(-1);
      }
      return lines;
   }//readFile

   public static String readFileAsString(String file)
   {
      return readFileAsString(new File(file), "UTF8");
   }

   public static String readFileAsString(File file)
   {
      return readFileAsString(file, "UTF8");
   }

   public static String readFileAsString(File file, String encoding)
   {
      StringBuilder sb = new StringBuilder();
      ArrayList<String> lines = readFile(file, encoding);
      for (String line : lines)
      {
         sb.append(line).append(NEW_LINE_CHARACTER);
      }
      return new String(sb);
   }

   public static void saveFile(File file, String whatToSave)
   {
      saveFile(file, whatToSave, "UTF8");
   }

   public static void saveFile(File file, String whatToSave, String encoding)
   {
      if (file.isDirectory())
      {
         throw new IllegalArgumentException("File " + file + " should not be a directory");
      }

      try
      {
         BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding));
         output.write(whatToSave);
         output.close();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
}