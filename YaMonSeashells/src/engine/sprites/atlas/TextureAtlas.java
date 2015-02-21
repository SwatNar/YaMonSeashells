package engine.sprites.atlas;

import com.jme3.texture.Texture2D;
import java.awt.image.BufferedImage;

public interface TextureAtlas
{
   /**
    * Prepares the sprite to be added on textureAtlas, it will be added on atlasTexture when build()
    * is called.
    */
   public void add(AtlasSprite sprite);

   /** rebuilds the atlasTexture by adding all sprites enqueued with add. */
   public void build();

   public boolean canAdd(AtlasSprite sprite);

   public Texture2D getAtlasTexture();

   public BufferedImage getBufferedImage();

   public int getHeight();

   public int getWidth();

   /** needs to be build or rebuild from start. */
   public boolean needsToBeBuild();
}