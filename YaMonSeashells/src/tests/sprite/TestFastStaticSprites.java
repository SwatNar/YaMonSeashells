package tests.sprite;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import engine.sprites.Sprite;
import engine.sprites.SpriteImage;
import engine.sprites.SpriteManager;
import engine.sprites.SpriteMesh.Strategy;
import java.util.logging.Level;
import java.util.logging.Logger;

//ALLOCATE_NEW_BUFFER: 149 fps for 1500000.
//KEEP_BUFFER:         149 fps for 1500000.   108 fps if you dont trim, because it puts automatically +50% sprites than you ask in order not to do many resizes.

//if SpriteMesh.disable optimizations=true. 37 fps for 150000 sprites. 100 times slower.
public class TestFastStaticSprites extends SimpleApplication
{
   private static final int MAX_SPRITES = 1500000;
   private static final Strategy strategy = Strategy.KEEP_BUFFER;
   private SpriteManager spriteManager;

   public static void main(String[] args)
   {
      Logger.getLogger("").setLevel(Level.SEVERE);
      TestFastStaticSprites app = new TestFastStaticSprites();
      app.start();
   }

   @Override
   public void simpleInitApp()
   {
      spriteManager = new SpriteManager(1024, 1024, strategy, rootNode, assetManager);
      getStateManager().attach(spriteManager);
      getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
      getFlyByCamera().setMoveSpeed(50);
      getCamera().setLocation(new Vector3f(-15, 0, 55));
      getCamera().lookAtDirection(new Vector3f(12, 7.5f, -15), Vector3f.UNIT_Y);

      SpriteImage image = spriteManager.createSpriteImage("2d/npc/npc0.png", true);
      float min = 0;
      float max = 30;
      for (int i = 0; i < MAX_SPRITES; i++)
      {
         Sprite sprite = new Sprite(image);
         sprite.getPosition().x = min + (float) (Math.random() * max);
         sprite.getPosition().y = min + (float) (Math.random() * max);
         sprite.getPosition().z = min + (float) (Math.random() * max);
      }
      if (strategy == Strategy.KEEP_BUFFER) spriteManager.trim(); //since they are static, we reduce its capacity to MAX_SPRITES, instead of 1.5 * MAX_SPRITES.
   }
}