package engine.sprites.atlas;

import com.jme3.texture.Texture2D;

import engine.util.ImageUtilities;
import engine.util.MathUtilities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.util.ArrayList;

/**
 * Dumpest texture atlas ever, sprites are added to next position (potentially leaving empty spaces).
 * fast insert but sprites are not compressed or sorted = waste of texture space = bad.
 */
public class SimpleTextureAtlas implements TextureAtlas
{
   protected static final int     FREE_SPRITE = -1;
   private final int              atlasHeight;
   private final int              atlasWidth;
   private Texture2D              atlasTexture;
   private int                    currentX       = 0;
   private int                    currentY       = 0;
   private ArrayList<AtlasSprite> sprites        = new ArrayList<AtlasSprite>();
   private int                    rowMaxY        = Integer.MIN_VALUE;
   private boolean                needsToBeBuild = false;
   private BufferedImage          testImage;

   public SimpleTextureAtlas(int textureWidth, int textureHeight)
   {
      this.atlasWidth  = textureWidth;
      this.atlasHeight = textureHeight;
   }

   /**
    * Prepares the sprite to be added on textureAtlas, it will be added on atlasTexture when build()
    * is called.
    */
   public void add(AtlasSprite sprite)
   {
      sprites.add(sprite);
      increaseBounds(sprite);
      needsToBeBuild = true;
   }

   /** rebuilds the atlasTexture by adding all sprites enqueued. */
   public void build()
   {
      if (!needsToBeBuild) return;
      createTexture();
      needsToBeBuild = false;
   }

   public boolean canAdd(AtlasSprite sprite)
   {
      int currentXCopy = this.currentX;
      int currentYCopy = this.currentY;

      if (currentXCopy + sprite.getWidth() > atlasWidth)    // change row.
      {
         currentXCopy = 0;
         currentYCopy = rowMaxY + 1;
      }

      return (currentXCopy + sprite.getWidth() <= atlasWidth) && (currentYCopy + sprite.getHeight() <= atlasHeight);
   }

   private void createTexture()
   {
      BufferedImage image = new BufferedImage(atlasWidth, atlasHeight, BufferedImage.TYPE_INT_ARGB);
      Graphics2D    g2d   = image.createGraphics();

      for (AtlasSprite sprite : sprites)
      {
         g2d.drawImage(sprite.getImage(), sprite.getAtlasStartX(), sprite.getAtlasStartY(), null);
      }

      atlasTexture = ImageUtilities.createTexture(image, g2d);
      testImage    = image;
   }

   public Texture2D getAtlasTexture()
   {
      return atlasTexture;
   }

   public BufferedImage getBufferedImage()
   {
      return testImage;
   }

   public int getHeight()
   {
      return atlasHeight;
   }

   public int getSpriteIndex(int startX, int startY, int endX, int endY)
   {
      for (int i = 0; i < sprites.size(); i++)
      {
         AtlasSprite sprite = sprites.get(i);

         if (MathUtilities.rectangleIntersects(startX, startY, endX, endY, sprite.getAtlasStartX(), sprite.getAtlasStartY(), sprite.getAtlasStartX() + sprite.getWidth(), sprite.getAtlasStartY() + sprite.getHeight()))
         {
            return i;
         }
      }

      return FREE_SPRITE;
   }

   public ArrayList<AtlasSprite> getSprites()
   {
      return sprites;
   }

   public int getWidth()
   {
      return atlasWidth;
   }

   /** Increases bounds as if the sprite was added at next available position. */
   private void increaseBounds(AtlasSprite spriteToAdd)
   {
      if (currentX + spriteToAdd.getWidth() > atlasWidth)    // change row.
      {
         currentX = 0;
         currentY = rowMaxY + 1;
         rowMaxY  = Integer.MIN_VALUE;
      }

      setSprite(spriteToAdd, currentX, currentY);
      if (rowMaxY < spriteToAdd.getAtlasEndY()) rowMaxY = spriteToAdd.getAtlasEndY();
      currentX = spriteToAdd.getAtlasEndX() + 1;
   }

   public boolean needsToBeBuild()
   {
      return needsToBeBuild;
   }

   public void print()
   {
      int i = 0;

      for (AtlasSprite sprite : sprites)
      {
         System.out.println(i + ". image" + sprite.getImage().hashCode() + ": (" + sprite.getAtlasStartX() + ", " + sprite.getAtlasStartY() + ", " + sprite.getAtlasEndX() + ", " + sprite.getAtlasEndY() + ")");
      }
   }

   private void setSprite(AtlasSprite sprite, int x, int y)
   {
      sprite.setAtlas(this);
      sprite.setAtlasStartX(x);
      sprite.setAtlasStartY(y);
   }
}