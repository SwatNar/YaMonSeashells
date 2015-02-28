package tests.sprite;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import engine.sprites.Sprite;
import engine.sprites.SpriteImage;
import engine.sprites.SpriteManager;
import engine.sprites.SpriteMesh;
import engine.sprites.SpriteMesh.Strategy;
import engine.util.FileUtilities;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

//all tests enabled:
//45-55 fps: KEEP_BUFFER, 30000 sprites. Memory: 200m at start then decreasing to 100m.
//50 fps: ALLOCATE_NEW_BUFFER, 30000 sprites. Memory: 200m then increasing to 1giga.
public class TestDynamicSprites extends SimpleApplication
{
   private SpriteManager spriteManager;
   private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
   private SpriteImage[] npcList ;
   
   private float totalTime = 0;
   //performance
   private int MAX_SPRITES = 30000;
   private Strategy strategy = SpriteMesh.Strategy.ALLOCATE_NEW_BUFFER;
   private int MAX_TEXTURE_WIDTH = 1024;
   private int MAX_TEXTURE_HEIGHT = 1024;
   private boolean cacheImages = true;
   //test
   private int SPRITE_TO_USE = 0;
   private float TIMES_TO_ADD = 100;
   private float TIMES_TO_DELETE = 100;
   private boolean RANDOM_MOVE = true;
   private boolean RANDOM_MOVE2 = true;
   private boolean CHANGE_SIZE = true;
   private boolean CHANGE_COLOR = true;
   private boolean CHANGE_COLOR2 = true;
   private boolean DELETE = true; //fps drop continually, because java's garbage collector sucks. Should use set instead of new.
   private boolean CHANGE_IMAGE = true;
   private float MIN_POS = 0;
   private float MAX_POS = 30;

   public static void main(String[] args)
   {
      Logger.getLogger("").setLevel(Level.SEVERE);
      TestDynamicSprites app = new TestDynamicSprites();
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
      initKeys();

      File npcLocation = new File(FileUtilities.ASSET_DIRECTORY + "2d/npc/");
      String[] fileList = npcLocation.list(FileUtilities.SUPPORTED_IMAGES);
      npcList = new SpriteImage[fileList.length];
      for (int i = 0; i < fileList.length; i++) 
      {
         npcList[i] = spriteManager.createSpriteImage("2d/npc/" + fileList[i], false);
      }

      for (int i = 0; i < MAX_SPRITES; i++)
      {
         Sprite sprite = new Sprite(npcList[SPRITE_TO_USE]);
         sprite.getPosition().x = MIN_POS + (float) (Math.random() * MAX_POS);
         sprite.getPosition().y = MIN_POS + (float) (Math.random() * MAX_POS);
         sprite.getPosition().z = MIN_POS + (float) (Math.random() * MAX_POS);
         sprites.add(sprite);
      }
   }

   //space bar to delete
   private void initKeys()
   {
      inputManager.addMapping("Delete", new KeyTrigger(KeyInput.KEY_1));
      inputManager.addListener(new ActionListener()
      {
         public void onAction(String name, boolean keyPressed, float tpf)
         {
            if (keyPressed) return;
            for (int i = sprites.size() - 1; i >= 0; i--)
            {
               sprites.remove(i).delete();
            }
         }
      }, "Delete");

      inputManager.addMapping("Print", new KeyTrigger(KeyInput.KEY_P));
      inputManager.addListener(new ActionListener()
      {
         public void onAction(String name, boolean keyPressed, float tpf)
         {
            if (keyPressed) return;
            spriteManager.getSpriteMeshes().get(0).printBuffers();
         }
      }, "Print");
   }

   public Sprite getAnySprite()
   {
      int random = (int) (Math.random() * sprites.size());
      if (random >= sprites.size()) return null;
      else return sprites.get(random);
   }

   public SpriteImage getAnyImage()
   {
      int random = (int) (Math.random() * npcList.length);
      return npcList[random];
   }
   Vector3f a = new Vector3f();

   @Override
   public void simpleUpdate(float tpf)
   {
      super.simpleUpdate(tpf);
      totalTime += tpf;

      //1. create sprites dynamically.
      for (int i = 0; i < TIMES_TO_ADD * Math.random(); i++)
      {
         Sprite sprite = new Sprite(npcList[SPRITE_TO_USE]);
         sprite.getPosition().x = MIN_POS + (float) (Math.random() * MAX_POS);
         sprite.getPosition().y = MIN_POS + (float) (Math.random() * MAX_POS);
         sprite.getPosition().z = MIN_POS + (float) (Math.random() * MAX_POS);
         //sprite.setPosition(MIN_POS + (float) (Math.random() * MAX_POS), MIN_POS + (float) (Math.random() * MAX_POS), MIN_POS + (float) (Math.random() * MAX_POS));
         sprites.add(sprite);
      }

      //2. move random sprites.
      if (totalTime > 3f && RANDOM_MOVE)
      {
         Sprite justAnySprite = getAnySprite();
         if (justAnySprite != null) justAnySprite.getPosition().x += tpf * 100;
      }
      if (RANDOM_MOVE2)
      {
         for (Sprite s : sprites) s.getPosition().x += ((Math.random() >= 0.5f) ? 1 : -1) * tpf * 10;
      }

      //3. change size dynamically
      if (CHANGE_SIZE)
      {
         Sprite justAnySprite = getAnySprite();
         if (justAnySprite != null) justAnySprite.setSize((float) (Math.random()*2));
      }

      //4. chance color dynamically
      if (CHANGE_COLOR2)
      {
         for (Sprite s : sprites) s.setColor(ColorRGBA.randomColor());
      }
      if (CHANGE_COLOR)
      {
         Sprite justAnySprite = getAnySprite();
         if (justAnySprite != null) justAnySprite.setColor(ColorRGBA.randomColor());
      }

      //5. test delete.
      for (int i = 0; i < TIMES_TO_DELETE * Math.random(); i++)
         if (DELETE)
         {
            Sprite justAnySprite = getAnySprite();
            if (justAnySprite != null)
            {
               justAnySprite.delete();
               sprites.remove(justAnySprite);
            }
         }

      //6. test image change.
      if (CHANGE_IMAGE && totalTime > 4f)
      {
         Sprite justAnySprite = getAnySprite();
         if (justAnySprite != null)
         {
            justAnySprite.setImage(getAnyImage());
         }
      }
   }
}