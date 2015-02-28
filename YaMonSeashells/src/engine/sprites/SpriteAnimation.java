package engine.sprites;

import engine.util.MathUtilities;

public class SpriteAnimation
{
   /** The list of frames to render in this animation */
   private SpriteImage[] frames;
   private float duration ;

   public int size()
   {
      return frames.length;
   }

   public SpriteImage getFrame(int i)
   {
      return frames[i];
   }

   public SpriteImage getCurrentFrame(Sprite sprite)
   {
      return frames[sprite.getCurrentFrame()];
   }

   public float getDuration()
   {
      return duration;
   }

   public void update(Sprite sprite, float tpf)
   {
      sprite.remainingTime -= tpf;
      if(sprite.getRemainingTime() <= 0)
      {
         sprite.currentFrame = (sprite.getCurrentFrame()+MathUtilities.divideRoundUp(tpf, duration)) % frames.length;
         sprite.remainingTime = duration;
         sprite.setImage(frames[sprite.getCurrentFrame()]);
      }
   }

   public SpriteAnimation(SpriteImage[] images, float duration)
   {
      this.duration = duration;
      frames = images;
   }
   
   protected void setAnimationData(Sprite sprite, int frameToStart)
   {
      sprite.currentAnimation = this;
      sprite.currentFrame = frameToStart % frames.length;
      sprite.remainingTime = duration;
   }
}