package tests.sprite.oldBugsSolved;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import engine.sprites.Sprite;
import engine.sprites.SpriteAnimation;
import engine.sprites.SpriteImage;
import engine.sprites.SpriteManager;
import engine.sprites.SpriteMesh;
import engine.util.FileUtilities;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

//ALLOCATE_NEW_BUFFER : 520 fps for 5000 animated sprites. If Texture Size(128,128) : 360-660 fps (immune to this)
//KEEP_BUFFER : 500 fps for 5000 animated sprites.         If Texture Size(128,128) :  41 fps (because sprites >> 5000, there are 5000*21 sprites.)
public class TestKeepBufferStrategyIsSlow extends SimpleApplication
{
   private SpriteManager spriteManager;
   private SpriteImage[] npcList ;
   private int numAnimatedSprites = 5000;

   public static void main(String[] args)
   {
      Logger.getLogger("").setLevel(Level.SEVERE);
      TestKeepBufferStrategyIsSlow app = new TestKeepBufferStrategyIsSlow();
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

      File npcLocation = new File(FileUtilities.ASSET_DIRECTORY + "2d/npc/");
      String[] fileList = npcLocation.list(FileUtilities.SUPPORTED_IMAGES);
      npcList = new SpriteImage[fileList.length];
      for (int i = 0; i < fileList.length; i++) 
      {
         npcList[i] = spriteManager.createSpriteImage("2d/npc/" + fileList[i], false);
      }

      SpriteAnimation anim = new SpriteAnimation(npcList, 0.2f);
      for (int i = 0; i < numAnimatedSprites; i++)
      {
         Sprite sprite = new Sprite(anim);
         sprite.setAnimation(anim, 0);
         sprite.setPosition(i, 0, 0);
      }
   }
}