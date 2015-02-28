package tests.sprite.oldBugsSolved;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import engine.sprites.Sprite;
import engine.sprites.SpriteImage;
import engine.sprites.SpriteManager;
import engine.sprites.SpriteMesh;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestInvisibleMesh extends SimpleApplication
{
   private SpriteManager spriteManager ;
   private SpriteImage SPRITE_TO_USE ;
   private int totalSprites = 0;
   private int MAX_SPRITES = 10000;
   private float MIN_POS = 0;
   private float MAX_POS = 30;
   private boolean createOnInit = false;

   public static void main(String[] args)
   {
      Logger.getLogger("").setLevel(Level.SEVERE);
      TestInvisibleMesh app = new TestInvisibleMesh();
      app.start();
   }
   
   @Override
   public void simpleInitApp()
   {
      spriteManager = new SpriteManager(128, 128, SpriteMesh.Strategy.KEEP_BUFFER, rootNode, assetManager);
      getStateManager().attach(spriteManager);
      getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
      getFlyByCamera().setMoveSpeed(50);
      getCamera().setLocation(new Vector3f(-15,0,55));
      getCamera().lookAtDirection(new Vector3f(12,7.5f,-15), Vector3f.UNIT_Y);
      SPRITE_TO_USE = spriteManager.createSpriteImage("2d/npc/npc0.png", false);

      if (createOnInit) for(int i=0; i< MAX_SPRITES; i++)
      {
         Sprite sprite = new Sprite(SPRITE_TO_USE);
         sprite.setPosition(new Vector3f(MIN_POS+(float)(Math.random()*MAX_POS),MIN_POS+(float)(Math.random()*MAX_POS),MIN_POS+(float)(Math.random()*MAX_POS)));
         totalSprites++;
      }      
   }

   @Override
   public void simpleUpdate(float tpf)
   {
      super.simpleUpdate(tpf);

      if (!createOnInit && totalSprites < MAX_SPRITES)
      {
         Sprite sprite = new Sprite(SPRITE_TO_USE);
         sprite.setPosition(new Vector3f(MIN_POS+(float)(Math.random()*MAX_POS),MIN_POS+(float)(Math.random()*MAX_POS),MIN_POS+(float)(Math.random()*MAX_POS)));
         totalSprites++;
      }
   }
}