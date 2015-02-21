package engine.util;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Format;
import com.jme3.scene.VertexBuffer.Usage;
import com.jme3.util.BufferUtils;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.*;

public final class ConvertionUtilities
{
   public static final float MILLIS_TO_SECONDS = 1 / 1000f;

   public static FloatBuffer createFloatBuffer(int size)
   {
      int crashes = 0;
      FloatBuffer fb = null;
      while (fb == null)
      {
         try
         {
            //System.out.println(size);
            fb = BufferUtils.createFloatBuffer(size);
         }
         catch (OutOfMemoryError oom)
         {
            System.out.println("Warning: out of memory. Garbage collecting. Crash : "+(crashes+1));
            System.gc();
            if (crashes > 0) try
            {
               Thread.sleep(100);
            }
            catch (InterruptedException ex)
            {
            }
         }
      }

      return fb;
   }

   public static ByteBuffer createByteBuffer(int size)
   {
      int crashes = 0;
      ByteBuffer fb = null;
      while (fb == null)
      {
         try
         {
            fb = BufferUtils.createByteBuffer(size);
         }
         catch (OutOfMemoryError oom)
         {
            System.out.println("Warning: out of memory. Garbage collecting. Crash : "+(crashes+1));
            System.gc();
            if (crashes > 0) try
            {
               Thread.sleep(100);
            }
            catch (InterruptedException ex)
            {
            }
         }
      }

      return fb;
   }
   
   public static void initBuffer(Mesh mesh, VertexBuffer.Type type, Buffer pb, Usage usage, int components, Format format, boolean normalise)
   {
      VertexBuffer pvb = new VertexBuffer(type);
      pvb.setupData(usage, components, format, pb);
      if (normalise) pvb.setNormalized(true);

      VertexBuffer buf = mesh.getBuffer(type);
      if (buf != null)
      {
         buf.updateData(pb);
      }
      else
      {
         mesh.setBuffer(pvb);
      }
   }

   public Date getDate(long millis)
   {
      return new Date(millis);
   }

   public static boolean isBlank(String str)
   {
      if (str == null) return true;
      if (str.isEmpty()) return true;
      for (char c : str.toCharArray()) if (!Character.isWhitespace(c)) return false;
      return true;
   }

   public static void appendSpaces(StringBuilder sb, int numberOfSpaces)
   {
      for (int i = 0; i < numberOfSpaces; i++)
      {
         sb.append(' ');
      }
   }

   public static Vector3f parseVector3f(String s)
   {
      StringTokenizer st = new StringTokenizer(s, "[ ,()]");
      if (st.countTokens() != 3) throw new IllegalArgumentException("String " + s + " is not a vector3f");
      return new Vector3f(Float.parseFloat(st.nextToken()), Float.parseFloat(st.nextToken()), Float.parseFloat(st.nextToken()));
   }

   public static String IpToString(InetSocketAddress address)
   {
      return address.getAddress().getHostAddress() + ":" + address.getPort();
   }

   public static String getFile(String filepath)
   {
      return filepath.substring(filepath.lastIndexOf('/') + 1);
   }

   public static String getDirectory(File file)
   {
      return getDirectory(file.getPath().replaceAll("\\\\", "/"));
   }

   public static String getDirectory(String filename)
   {
      int seperatorPos = filename.lastIndexOf('/');
      return filename.substring(0, seperatorPos);
   }

   public static String getPrefix(String name)
   {
      return name.substring(0, name.indexOf('.'));
   }

   /* Get the extension of a file.
    *
    * Code taken from java tutorial : http://download.oracle.com/javase/tutorial/uiswing/components/filechooser.html#filters
    */
   public static String getExtension(File f)
   {
      String ext = null;
      String s = f.getName();
      int i = s.lastIndexOf('.');

      if (i > 0 && i < s.length() - 1)
      {
         ext = s.substring(i + 1).toLowerCase();
      }
      return ext;
   }

   public static float[] merge(float[] f1, float[] f2)
   {
      if (f1 == null) return f2;
      if (f2 == null) return f1;
      float[] f = new float[f1.length + f2.length];
      System.arraycopy(f1, 0, f, 0, f1.length);
      System.arraycopy(f2, 0, f, f1.length, f2.length);
      return f;
   }

   public static short[] merge(short[] f1, short[] f2)
   {
      if (f1 == null) return f2;
      if (f2 == null) return f1;
      short[] f = new short[f1.length + f2.length];
      System.arraycopy(f1, 0, f, 0, f1.length);
      System.arraycopy(f2, 0, f, f1.length, f2.length);
      return f;
   }

   public static byte[] merge(byte[] f1, byte[] f2)
   {
      if (f1 == null) return f2;
      if (f2 == null) return f1;
      byte[] f = new byte[f1.length + f2.length];
      System.arraycopy(f1, 0, f, 0, f1.length);
      System.arraycopy(f2, 0, f, f1.length, f2.length);
      return f;
   }

   public static FloatBuffer merge(FloatBuffer f1, FloatBuffer f2)
   {
      if (f1 == null) return f2;
      if (f2 == null) return f1;
      return BufferUtils.createFloatBuffer(merge(BufferUtils.getFloatArray(f1), BufferUtils.getFloatArray(f2)));
   }

   public static ShortBuffer merge(ShortBuffer f1, ShortBuffer f2)
   {
      if (f1 == null) return f2;
      if (f2 == null) return f1;
      return BufferUtils.createShortBuffer(merge(getShortArray(f1), getShortArray(f2)));
   }

   public static VertexBuffer.Type getTexCoordType(int i)
   {
      switch (i)
      {
         case 0:
            return VertexBuffer.Type.TexCoord;
         case 1:
            return VertexBuffer.Type.TexCoord2;
         case 2:
            return VertexBuffer.Type.TexCoord3;
         case 3:
            return VertexBuffer.Type.TexCoord4;
         case 4:
            return VertexBuffer.Type.TexCoord5;
         case 5:
            return VertexBuffer.Type.TexCoord6;
         case 6:
            return VertexBuffer.Type.TexCoord7;
         case 7:
            return VertexBuffer.Type.TexCoord8;
         default:
            throw new IllegalArgumentException("The specified tex coord type not found");
      }
   }

   public static Vector3f getVector3FromArray(float[] array, int i)
   {
      return new Vector3f(array[i * 3], array[i * 3 + 1], array[i * 3 + 2]);
   }

   public static Vector3f getVector3FromArray(List<Float> array, int i)
   {
      return new Vector3f(array.get(i * 3), array.get(i * 3 + 1), array.get(i * 3 + 2));
   }

   public static Vector2f getVector2FromArray(float[] array, int i)
   {
      return new Vector2f(array[i * 2], array[i * 2 + 1]);
   }

   public static Vector2f getVector2FromArray(List<Float> array, int i)
   {
      return new Vector2f(array.get(i * 2), array.get(i * 2 + 1));
   }

   public static Vector2f[] getVector2FromArray(List<Float>[] array, int i)
   {
      Vector2f[] returned = new Vector2f[array.length];
      for (int j = 0; j < array.length; j++) returned[j] = getVector2FromArray(array[j], i);
      return returned;
   }

   public static Quaternion getQuaternionFromArray(List<Float> array, int i)
   {
      return new Quaternion(array.get(i * 4), array.get(i * 4 + 1), array.get(i * 4 + 2), array.get(i * 4 + 3));
   }

   public static ArrayList<Byte> getAsList(byte[] array)
   {
      ArrayList<Byte> resultArray = new ArrayList<Byte>();
      for (byte s : array)
      {
         resultArray.add(s);
      }
      return resultArray;
   }

   public static ArrayList<Short> getAsList(short[] array)
   {
      ArrayList<Short> resultArray = new ArrayList<Short>();
      for (short s : array)
      {
         resultArray.add(s);
      }
      return resultArray;
   }

   public static ArrayList<Float> getAsList(float[] array)
   {
      ArrayList<Float> resultArray = new ArrayList<Float>();
      for (float f : array)
      {
         resultArray.add(f);
      }
      return resultArray;
   }

   @SuppressWarnings("unchecked")
   public static ArrayList<Float>[] getAsList(float[][] array)
   {
      ArrayList<Float>[] resultArray = new ArrayList[array.length];
      for (int i = 0; i < array.length; i++)
      {
         resultArray[i] = new ArrayList<Float>();
         for (float f : array[i])
         {
            resultArray[i].add(f);
         }
      }
      return resultArray;
   }

   public static byte[] toByteArray(Collection<Byte> array)
   {
      byte[] resultArray = new byte[array.size()];
      int i = 0;
      for (byte s : array)
      {
         resultArray[i++] = s;
      }
      return resultArray;
   }

   public static short[] toShortArray(Collection<Short> array)
   {
      short[] resultArray = new short[array.size()];
      int i = 0;
      for (short s : array)
      {
         resultArray[i++] = s;
      }
      return resultArray;
   }

   public static float[] toFloatArray(Collection<Float> array)
   {
      float[] resultArray = new float[array.size()];
      int i = 0;
      for (float s : array)
      {
         resultArray[i++] = s;
      }
      return resultArray;
   }

   public static float[][] toFloatArray(Collection<Float>[] array)
   {
      float[][] resultArray = new float[array.length][];
      int i = 0;
      for (int j = 0; j < array.length; j++)
      {
         resultArray[j] = new float[array[j].size()];
         for (float s : array[j])
         {
            resultArray[j][i++] = s;
         }
      }
      return resultArray;
   }

   public static void addInList(Quaternion p1, List<Float> array)
   {
      array.add(p1.getX());
      array.add(p1.getY());
      array.add(p1.getZ());
      array.add(p1.getW());
   }

   public static void addInList(Vector3f p1, List<Float> array)
   {
      array.add(p1.x);
      array.add(p1.y);
      array.add(p1.z);
   }

   public static void addInList(Vector2f p1, List<Float> array)
   {
      array.add(p1.x);
      array.add(p1.y);
   }

   public static void setInArray(Vector3f p1, float[] array, int i)
   {
      array[i * 3] = p1.x;
      array[i * 3 + 1] = p1.y;
      array[i * 3 + 2] = p1.z;
   }

   public static void setInArray(Vector2f p1, float[] array, int i)
   {
      array[i * 2] = p1.x;
      array[i * 2 + 1] = p1.y;
   }

   /**
    * Create a new short[] array and populate it with the given ShortBuffer's
    * contents.
    *
    * @param buff
    *            the ShortBuffer to read from
    * @return a new short array populated from the ShortBuffer
    */
   public static short[] getShortArray(ShortBuffer buff)
   {
      if (buff == null)
         return null;
      buff.clear();
      short[] inds = new short[buff.limit()];
      for (int x = 0; x < inds.length; x++)
      {
         inds[x] = buff.get();
      }
      return inds;
   }

   /**
    * Create a new byte[] array and populate it with the given ByteBuffer's
    * contents.
    *
    * @param buff
    *            the ByteBuffer to read from
    * @return a new byte array populated from the ByteBuffer
    */
   public static byte[] getByteArray(ByteBuffer buff)
   {
      if (buff == null)
         return null;
      buff.clear();
      byte[] inds = new byte[buff.limit()];
      for (int x = 0; x < inds.length; x++)
      {
         inds[x] = buff.get();
      }
      return inds;
   }

   public static String print(Buffer buffer)
   {
      if (buffer instanceof FloatBuffer)
      {
         return Arrays.toString(BufferUtils.getFloatArray((FloatBuffer) buffer));
      }
      else if (buffer instanceof ByteBuffer)
      {
         return Arrays.toString(ConvertionUtilities.getByteArray((ByteBuffer) buffer));
      }
      return null;
   }

   public static void print(Buffer buffer, int components)
   {
      buffer.rewind();
      int i = 0;
      while (buffer.hasRemaining())
      {
         System.out.print(i + ": (");
         for (int j = 0; j < components; j++)
         {
            if (buffer instanceof FloatBuffer) System.out.print(((FloatBuffer) buffer).get());
            if (buffer instanceof ByteBuffer) System.out.print(((ByteBuffer) buffer).get());

            if (j < components - 1) System.out.print(", ");
         }
         System.out.println(")");
         i++;
      }
      buffer.flip();
   }

   public static InetAddress getMyIpAddress()
   {
      try
      {
         return InetAddress.getLocalHost();
      }
      catch (UnknownHostException ex)
      {
         ex.printStackTrace();
         return null;
      }
   }
}
