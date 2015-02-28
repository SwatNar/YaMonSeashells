package engine.sprites;

import engine.sprites.atlas.AtlasSprite;

public final class SpriteImage
{
   private final AtlasSprite sprite;
   private final SpriteMesh whereToPut;

   public SpriteImage(AtlasSprite sprite, SpriteMesh whereToPut)
   {
      this.sprite = sprite;
      this.whereToPut = whereToPut;
   }

   public AtlasSprite getSprite()
   {
      return sprite;
   }

   public SpriteMesh getWhereToPut()
   {
      return whereToPut;
   }

   public boolean isStatic()
   {
      return whereToPut.isStatic();
   }
   
   public SpriteImage getMirrorX()
   {
      return new SpriteImage( sprite.getMirrorX(), whereToPut );
   }
   
   public SpriteImage getMirrorY()
   {
      return new SpriteImage( sprite.getMirrorY(), whereToPut );
   }
}