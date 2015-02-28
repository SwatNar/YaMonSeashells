package tests.sprite.bugsNotSolved;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import engine.sprites.Sprite;
import engine.sprites.SpriteImage;
import engine.sprites.SpriteManager;
import engine.sprites.SpriteMesh;
import engine.sprites.SpriteMesh.Strategy;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

//This test makes ALLOCATE_NEW_BUFFER memory requirements go crazy, it wants 1 gigabyte memory, and even throws outOfMemoryExceptions
//KEEP_BUFFER is immune to this.
//I had windows task manager open to see memory it consumes.
public class TestAllocateNewBufferMemoryGoesCrazy extends SimpleApplication
{
   private SpriteManager spriteManager;
   private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
   //performance
   private int MAX_SPRITES = 30000;
   private Strategy strategy = SpriteMesh.Strategy.ALLOCATE_NEW_BUFFER;
   private int MAX_TEXTURE_WIDTH = 1024;
   private int MAX_TEXTURE_HEIGHT = 1024;
   //test
   private SpriteImage SPRITE_TO_USE ;
   private float ADD_SPEED = 100;
   private float DELETE_SPEED = 100;
   private float MIN_POS = 0;
   private float MAX_POS = 30;

   public static void main(String[] args)
   {
      Logger.getLogger("").setLevel(Level.SEVERE);
      TestAllocateNewBufferMemoryGoesCrazy app = new TestAllocateNewBufferMemoryGoesCrazy();
      app.start();
   }

   @Override
   public void simpleInitApp()
   {
      spriteManager = new SpriteManager(MAX_TEXTURE_WIDTH, MAX_TEXTURE_HEIGHT, strategy, rootNode, assetManager);
      getStateManager().attach(spriteManager);
      getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
      getFlyByCamera().setMoveSpeed(50);
      getCamera().setLocation(new Vector3f(-15, 0, 55));
      getCamera().lookAtDirection(new Vector3f(12, 7.5f, -15), Vector3f.UNIT_Y);
      SPRITE_TO_USE = spriteManager.createSpriteImage("2d/npc/npc0.png",true);

      for (int i = 0; i < MAX_SPRITES; i++)
      {
         Sprite sprite = new Sprite(SPRITE_TO_USE);
         sprite.getPosition().x = MIN_POS + (float) (Math.random() * MAX_POS);
         sprite.getPosition().y = MIN_POS + (float) (Math.random() * MAX_POS);
         sprite.getPosition().z = MIN_POS + (float) (Math.random() * MAX_POS);
         sprites.add(sprite);
      }
   }

   public Sprite getAnySprite()
   {
      int random = (int) (Math.random() * sprites.size());
      if (random >= sprites.size()) return null;
      else return sprites.get(random);
   }

   @Override
   public void simpleUpdate(float tpf)
   {
      super.simpleUpdate(tpf);

      //1. create sprites dynamically.
      for (int i = 0; i < ADD_SPEED * Math.random(); i++)
      {
         Sprite sprite = new Sprite(SPRITE_TO_USE);
         sprite.getPosition().x = MIN_POS + (float) (Math.random() * MAX_POS);
         sprite.getPosition().y = MIN_POS + (float) (Math.random() * MAX_POS);
         sprite.getPosition().z = MIN_POS + (float) (Math.random() * MAX_POS);
         //sprite.setPosition(MIN_POS + (float) (Math.random() * MAX_POS), MIN_POS + (float) (Math.random() * MAX_POS), MIN_POS + (float) (Math.random() * MAX_POS));
         sprites.add(sprite);
      }
      for (Sprite s : sprites) s.getPosition().x += ((Math.random() >= 0.5f) ? 1 : -1) * tpf * 10;

      //5. test delete.
      for (int i = 0; i < DELETE_SPEED * Math.random(); i++)
      {
         Sprite justAnySprite = getAnySprite();
         if (justAnySprite != null)
         {
            justAnySprite.delete();
            sprites.remove(justAnySprite);
         }
      }
   }
}