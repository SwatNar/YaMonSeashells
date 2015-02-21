package engine.sprites.atlas;

import com.jme3.math.Vector3f;

import engine.util.ImageUtilities;

import java.awt.image.BufferedImage;

public class AtlasSprite implements Comparable<AtlasSprite>, Cloneable
{
   private TextureAtlas  atlas;
   private int           atlasStartX;
   private int           atlasStartY;
   private BufferedImage image;
   private String        id;              //to prevent creating the same atlasSprite from image, if user is nab.
   
   private float atlasStartU;
   private float atlasStartV;
   private float atlasEndU;
   private float atlasEndV;

   public AtlasSprite(String id, BufferedImage image)
   {
      this.image      = image;
      this.id = id;
   }

   public AtlasSprite(BufferedImage image)
   {
      this(Integer.toHexString(image.hashCode()), image);
   }

   public int compareTo(AtlasSprite s)
   {
      return Integer.compare(getHeight(), s.getHeight());
   }

   public TextureAtlas getAtlas()
   {
      return atlas;
   }

   public int getAtlasEndX()
   {
      return getAtlasStartX() + getWidth();
   }

   public int getAtlasEndY()
   {
      return getAtlasStartY() + getHeight();
   }

   public int getAtlasStartX()
   {
      return atlasStartX;
   }

   public int getAtlasStartY()
   {
      return atlasStartY;
   }

   public float getEndU()
   {
      return atlasEndU;
   }

   public float getEndV()
   {
      return atlasEndV;
   }

   public int getHeight()
   {
      return image.getHeight();
   }

   public BufferedImage getImage()
   {
      return image;
   }

   public float getStartU()
   {
      return atlasStartU;
   }

   public float getStartV()
   {
      return atlasStartV;
   }

   public String getId()
   {
      return id;
   }

   public int getWidth()
   {
      return image.getWidth();
   }

   protected void setAtlas(TextureAtlas atlas)
   {
      this.atlas = atlas;
   }

   protected void setAtlasStartX(int textureAtlasStartX)
   {
      this.atlasStartX = textureAtlasStartX;
      atlasStartU = ((float) getAtlasStartX()) / atlas.getWidth();
      atlasEndU   = ((float) getAtlasEndX()  ) / atlas.getWidth();
   }

   protected void setAtlasStartY(int textureAtlasStartY)
   {
      this.atlasStartY = textureAtlasStartY;
      atlasEndV   = 1 - ((float) getAtlasEndY()  ) / atlas.getHeight();
      atlasStartV = 1 - ((float) getAtlasStartY()) / atlas.getHeight();
   }
   
   public AtlasSprite getMirrorX()
   {
      try
      {
         AtlasSprite mirrorX = (AtlasSprite) this.clone();
         mirrorX.atlasStartU = atlasEndU;
         mirrorX.atlasEndU   = atlasStartU;
         return mirrorX;
      }
      catch (CloneNotSupportedException ex)
      {
         ex.printStackTrace();
         return null;
      }
   }
   
   public AtlasSprite getMirrorY()
   {
      try
      {
         AtlasSprite mirrorY = (AtlasSprite) this.clone();
         mirrorY.atlasStartV = atlasEndV;
         mirrorY.atlasEndV   = atlasStartV;
         return mirrorY;
      }
      catch (CloneNotSupportedException ex)
      {
         ex.printStackTrace();
         return null;
      }
   }
}