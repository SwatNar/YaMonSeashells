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

//It was 2 bugs that got fixed :
//1) with "SpriteMesh.sprites" list, i modified the list directly instead of using SpriteMesh.setSprite = wrong positionInArray variable.
//2) with "SpriteMesh.findNewBufferResize", if (capacity == 0 && minCapacity == 0) it tried to update() causing strange artifacts.
public class TestStrategyNewBufferAllocationBug extends SimpleApplication
{
   private SpriteManager spriteManager;
   private SpriteImage[] npcList ;
   private int numAnimatedSprites = 10;

   public static void main(String[] args)
   {
      Logger.getLogger("").setLevel(Level.SEVERE);
      TestStrategyNewBufferAllocationBug app = new TestStrategyNewBufferAllocationBug();
      app.start();
   }

   @Override
   public void simpleInitApp()
   {
      spriteManager = new SpriteManager(128, 128, SpriteMesh.Strategy.ALLOCATE_NEW_BUFFER, rootNode, assetManager);
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
         sprite.setPosition(i, 0, 0);
         //sprite.setAnimation(anim, 0);
      }
   }
}