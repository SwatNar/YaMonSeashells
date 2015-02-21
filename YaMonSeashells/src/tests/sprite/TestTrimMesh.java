package tests.sprite;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import engine.sprites.Sprite;
import engine.sprites.SpriteImage;
import engine.sprites.SpriteManager;
import engine.sprites.SpriteMesh;
import java.util.logging.Level;
import java.util.logging.Logger;

//only for Strategy.KEEP_BUFFER.
public class TestTrimMesh extends SimpleApplication
{
   private static final int MAX_SPRITES = 1000;//=1500 sprites because KEEP_BUFFER uses extra buffer capacity +50% more sprites.
   private SpriteManager spriteManager;
   private boolean trim = true;
   private static final boolean DELETE = true;

   public static void main(String[] args)
   {
      Logger.getLogger("").setLevel(Level.SEVERE);
      TestTrimMesh app = new TestTrimMesh();
      app.start();
   }

   @Override
   public void simpleInitApp()
   {
      spriteManager = new SpriteManager(128, 128, SpriteMesh.Strategy.KEEP_BUFFER, rootNode, assetManager);
      getStateManager().attach(spriteManager);
      getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
      getFlyByCamera().setMoveSpeed(50);
      getCamera().setLocation(new Vector3f(-15, 0, 55));
      getCamera().lookAtDirection(new Vector3f(12, 7.5f, -15), Vector3f.UNIT_Y);

      SpriteImage image = spriteManager.createSpriteImage("2d/npc/npc2.png",true);
      float min = 0;
      float max = 30;
      for (int i = 0; i < MAX_SPRITES; i++)
      {
         Sprite sprite = new Sprite(image);
         sprite.getPosition().x = min + (float) (Math.random() * max);
         sprite.getPosition().y = min + (float) (Math.random() * max);
         sprite.getPosition().z = min + (float) (Math.random() * max);
      }
      System.out.println("Old capacity: " + spriteManager.getSpriteMeshes().get(0).getCapacity());

      if (DELETE)
      {
         for (Sprite sprite : spriteManager.getSpriteMeshes().get(0).getSprites())
         {
            sprite.delete();
         }
         new Sprite(image); // note sprite constructor automatically adds itself to corresponding spriteMesh.
      }
   }

   @Override
   public void simpleUpdate(float tpf)
   {
      //deleted sprites are not really deleted in KEEP_BUFFER, its capacity remains the same 1500.
      System.out.println("New capacity: " + spriteManager.getSpriteMeshes().get(0).getCapacity());
      if (trim)
      {
         spriteManager.trim();
         trim = false;
      }
   }
}