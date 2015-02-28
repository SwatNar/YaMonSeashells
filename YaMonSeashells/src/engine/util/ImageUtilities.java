package engine.util;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public final class ImageUtilities
{
   /**@return array containing all individual sprites in column based format. E.g [2,3] = go to column 2 at line 3.*/
   public static BufferedImage[][] split(BufferedImage image, int numSpritesX, int numSpritesY)
   {
      BufferedImage[][] tiles = new BufferedImage[numSpritesX][numSpritesY];
      int tileWidth  = image.getWidth()/numSpritesX;
      int tileHeight = image.getHeight()/numSpritesY;

      for(int i=0; i< numSpritesX; i++)
      {
         for(int j=0; j< numSpritesY; j++)
         {
            tiles[i][j]= ImageUtilities.crop(image, i*tileWidth, j*tileHeight, (i+1)*tileWidth, (j+1)*tileHeight);
         }
      }
      return tiles;
   }

   /**
    * @param sheet array in column based format. E.g [2,3] = go to column 2 at line 3.
    */
   public static BufferedImage[][] getSubsheet(BufferedImage[][] sheet, int numSubSpritesX, int numSubSpritesY, int subSheetIndex)
   {
      BufferedImage[][] tiles = new BufferedImage[numSubSpritesX][numSubSpritesY];
      int totalSpritesX = sheet.length;
      int numSheetsX = totalSpritesX / numSubSpritesX;

      for(int i=0; i< numSubSpritesX; i++)
      {
         for(int j=0; j< numSubSpritesY; j++)
         {
            tiles[i][j]= sheet[numSubSpritesX*(subSheetIndex%numSheetsX)+i][numSubSpritesY *(subSheetIndex/numSheetsX)+j];
         }
      }
      return tiles;
   }
   
   public static void saveAsPng(BufferedImage image, File file)
   {
      saveImage(image, "PNG", file);
   }

   public static void saveImage(BufferedImage image, String FormatName, File file)
   {
      try
      {
         ImageIO.write(image, FormatName, file);
      }
      catch (IOException ex)
      {
         ex.printStackTrace();
      }
   }

   public static void viewImage(BufferedImage image)
   {
      JFrame frame = new JFrame();
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.setTitle("Viewing image");
      frame.add(new JScrollPane(new JLabel(new ImageIcon(image))));
      frame.pack();
      frame.setVisible(true);
   }

   public static BufferedImage transformColorToTransparency(BufferedImage image, Color c1)
   {
      return transformColorToTransparency(image,c1,c1);
   }
   
   /** code from http://stackoverflow.com/questions/665406/how-to-make-a-color-transparent-in-a-bufferedimage-and-save-as-png
    */
   public static BufferedImage transformColorToTransparency(BufferedImage image, Color c1, Color c2)
   {
      final int r1 = c1.getRed();
      final int g1 = c1.getGreen();
      final int b1 = c1.getBlue();
      final int r2 = c2.getRed();
      final int g2 = c2.getGreen();
      final int b2 = c2.getBlue();
      ImageFilter filter = new RGBImageFilter()
      {
         public final int filterRGB(int x, int y, int rgb)
         {
            int r = (rgb & 0xFF0000) >> 16;
            int g = (rgb & 0xFF00) >> 8;
            int b = rgb & 0xFF;
            if (r >= r1 && r <= r2
                    && g >= g1 && g <= g2
                    && b >= b1 && b <= b2)
            {
               // Set fully transparent but keep color
               return rgb & 0xFFFFFF;
            }
            return rgb;
         }
      };

      ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
      Image img = Toolkit.getDefaultToolkit().createImage(ip);
      return imageToBufferedImage(img,image.getWidth(), image.getHeight());
   }

   /** code from http://stackoverflow.com/questions/665406/how-to-make-a-color-transparent-in-a-bufferedimage-and-save-as-png
    */
   public static BufferedImage imageToBufferedImage(Image image, int width, int height)
   {
      BufferedImage dest = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
      Graphics2D g2 = dest.createGraphics();
      g2.drawImage(image, 0, 0, null);
      g2.dispose();
      return dest;
   }

   public static Vector3f findNormalisedSize(int w, int h)
   {
      float width = w / 48f;
      float height = h / 50f;

      return new Vector3f(width, height, width);
   }

   public static BufferedImage crop(BufferedImage image, int startX, int startY, int endX, int endY)
   {
      BufferedImage newImage = new BufferedImage(endX - startX, endY - startY, BufferedImage.TYPE_INT_ARGB);
      for (int i = 0; i < newImage.getWidth(); i++)
      {
         for (int j = 0; j < newImage.getHeight(); j++)
         {
            newImage.setRGB(i, j, image.getRGB(startX + i, startY + j));
         }
      }
      return newImage;
   }

   public static BufferedImage merge(BufferedImage[][] images)
   {
      int sw = images[0][0].getWidth();
      int sh = images[0][0].getHeight();
      int w = sw * images.length;
      int h = sh * images[0].length;

      BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = newImage.createGraphics();
      for (int i = 0; i < images.length; i++)
      {
         for (int j = 0; j < images[i].length; j++)
         {
            g.drawImage(images[i][j], i * sw, j * sh, null);
         }
      }
      g.dispose();
      return newImage;
   }
   
   public static BufferedImage merge(BufferedImage[] images)
   {
      int maxHeight = 0;
      for(BufferedImage image : images) if ( image.getHeight() > maxHeight) maxHeight = image.getHeight();
      int totalWidth = 0;
      for(BufferedImage image : images) totalWidth+= image.getWidth();

      BufferedImage newImage = new BufferedImage(totalWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = newImage.createGraphics();
      for (int i = 0; i < images.length; i++)
      {
         g.drawImage(images[i], i * images[i].getWidth(), 0, null);
      }
      g.dispose();
      return newImage;
   }

   public static BufferedImage symmetrifyX(BufferedImage image, boolean useFirstHalfImage, boolean flipHorizontial)
   {
      int halfWidth = image.getWidth() / 2;
      int startReadPosition = 0;
      int startWritePosition = 0;
      int endWritePosition = image.getWidth() - 1;

      if (!useFirstHalfImage) startReadPosition = halfWidth;
      if (!useFirstHalfImage ^ flipHorizontial)//xor 
      {
         startWritePosition = halfWidth;
         endWritePosition = halfWidth;
      }

      BufferedImage returned = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
      for (int i = 0; i < image.getWidth() / 2; i++)
      {
         for (int j = 0; j < image.getHeight(); j++)
         {
            int color = image.getRGB(startReadPosition + i, j);
            returned.setRGB(startWritePosition + i, j, color);
            returned.setRGB(endWritePosition - i, j, color);
         }
      }
      return returned;
   }

   public static BufferedImage symmetrifyY(BufferedImage image, boolean useFirstHalfImage, boolean flipVertical)
   {
      int halfWidth = image.getHeight() / 2;
      int startReadPosition = 0;
      int startWritePosition = 0;
      int endWritePosition = image.getHeight() - 1;

      if (!useFirstHalfImage) startReadPosition = halfWidth;
      if (!useFirstHalfImage ^ flipVertical)//xor 
      {
         startWritePosition = halfWidth;
         endWritePosition = halfWidth;
      }

      BufferedImage returned = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
      for (int i = 0; i < image.getWidth(); i++)
      {
         for (int j = 0; j < image.getHeight() / 2; j++)
         {
            int color = image.getRGB(i, startReadPosition + j);
            returned.setRGB(i, startWritePosition + j, color);
            returned.setRGB(i, endWritePosition - j, color);
         }
      }
      return returned;
   }

   /** the graphic2d changes, must use the new one for future operations. use image.createGraphics(); */
   public static BufferedImage flip(BufferedImage image, boolean flipHorizontal, boolean flipVertical)
   {
      Point scale = new Point(1, 1);
      Point translate = new Point(0, 0);
      if (flipHorizontal)
      {
         scale.x = -1;
         translate.x = -image.getWidth();
      }
      if (flipVertical)
      {
         scale.y = -1;
         translate.y = -image.getHeight();
      }
      AffineTransform tx = AffineTransform.getScaleInstance(scale.x, scale.y);
      tx.translate(translate.x, translate.y);
      AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
      return op.filter(image, null);
   }

   public static Color[] getVerticalColors(BufferedImage image, int x, boolean hasAlpha)
   {
      Color[] colors = new Color[image.getHeight()];
      for (int i = 0; i < image.getHeight(); i++)
      {
         colors[i] = getColor(image, x, i, hasAlpha);
      }
      return colors;
   }

   public static Color getColor(BufferedImage image, int x, int y, boolean hasAlpha)
   {
      return new Color(image.getRGB(x, y), hasAlpha);
   }

   public static int getRed(int color)
   {
      return (color & 0x00ff0000) >> 16;
   }

   public static int getGreen(int color)
   {
      return (color & 0x0000ff00) >> 8;
   }

   public static int getBlue(int color)
   {
      return color & 0x000000ff;
   }

   public static int getAlpha(int color)
   {
      return (color >> 24) & 0xff;
   }

   /** the graphic2d changes, must use the new one for future operations. use image.createGraphics(); */
   public static BufferedImage rotateImage(BufferedImage image, float angle)
   {
      AffineTransform tx = new AffineTransform();
      tx.rotate(angle, image.getWidth() / 2, image.getHeight() / 2);

      AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
      return op.filter(image, null);
   }

   public static void verticalGradient(BufferedImage image, Graphics2D g2d, Color... colors)
   {
      int miniHeight = image.getHeight() / (colors.length - 1);
      for (int i = 1; i < colors.length; i++)
      {
         Point start = new Point(0, (i - 1) * miniHeight);
         Point end = new Point(0, i * miniHeight);
         gradientPaint(g2d, start, colors[i - 1], end, colors[i], start.x, start.y, image.getWidth(), end.y);
      }
   }

   public static void gradientPaint(Graphics2D g2d, Point2D startPosition, Color startColor, Point2D endPosition, Color endColor, int sx, int sy, int ex, int ey)
   {
      GradientPaint gradient = new GradientPaint(startPosition, startColor, endPosition, endColor, true);
      g2d.setPaint(gradient);
      g2d.fillRect(sx, sy, ex, ey);
   }

   /** the graphic2d changes, must use the new one for future operations. */
   public static Pair<BufferedImage, Graphics2D> fillBackground(BufferedImage originalImage, Color color)
   {
      BufferedImage modifiedImage = new BufferedImage(originalImage.getWidth(null), originalImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
      Graphics2D g = modifiedImage.createGraphics();
      g.drawImage(originalImage, 0, 0, modifiedImage.getWidth(), modifiedImage.getHeight(), color, null);

      return new Pair<BufferedImage, Graphics2D>(modifiedImage, g);
   }

   public static BufferedImage readImage(InputStream input) throws IOException
   {
      BufferedImage img = ImageIO.read(input);
      return img;
   }

   public static BufferedImage loadImage(String url, AssetManager assetManager)
   {
      try
      {
         return readImage(FileUtilities.loadFile(url, assetManager));
      }
      catch (IOException ex)
      {
         ex.printStackTrace();
         throw new IllegalArgumentException("Cant find file " + url);
      }
   }

   public static final BufferedImage merge(BufferedImage src, BufferedImage added, int x, int y)
   {
      BufferedImage returned = clone(src);
      Graphics2D g = returned.createGraphics();
      g.drawImage(added, x, y, null);
      g.dispose(); //We are done drawing destroy graphics
      return returned;
   }

   public static final BufferedImage clone(BufferedImage source)
   {
      WritableRaster raster = source.copyData(null);
      BufferedImage copy = new BufferedImage(source.getColorModel(), raster, source.isAlphaPremultiplied(), null);
      return copy;
   }

   public static BufferedImage loadImage(String url)
   {
      try
      {
         return readImage(FileUtilities.loadFile(url));
      }
      catch (IOException ex)
      {
         ex.printStackTrace();
         throw new IllegalArgumentException("Cant find file " + url);
      }
   }

   public static BufferedImage[] asSingleArray( BufferedImage[][] images, boolean transpose)
   {
      if (!transpose) images = transpose(images);
      BufferedImage[] newImages = new BufferedImage[images.length * images[0].length];
      int k = 0;
      for(int i=0; i<images.length; i++)
      {
         for(int j=0; j<images[i].length; j++)
         {
            newImages[k++]=images[i][j];
         }
      }
      return newImages;
   }
   
   public static BufferedImage[][] transpose( BufferedImage[][] images)
   {
      BufferedImage[][] newImages = new BufferedImage[images[0].length][images.length];
      for(int i=0; i< images.length; i++)
      {
         for(int j=0; j< images[0].length; j++)
         {
            newImages[j][i] = images[i][j];
         }
      }
      return newImages;
   }

   /** this method calls dispose on Graphics2D g */
   public static Texture2D createTexture(BufferedImage img, Graphics2D g, Texture.MinFilter min, Texture.MagFilter mag)
   {
      if (g != null) g.dispose();
      AWTLoader loader = new AWTLoader();
      Texture2D tex = new Texture2D(loader.load(img, true));

      tex.setMinFilter(min);
      tex.setMagFilter(mag);
      return tex;
   }

   /** create a texture from this image without min maps.
    *  this method calls dispose on Graphics2D g 
    */
   public static Texture2D createTexture(BufferedImage img, Graphics2D g)
   {
      return createTexture(img, g, Texture.MinFilter.NearestNoMipMaps, Texture.MagFilter.Nearest);
   }
}