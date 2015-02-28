package tests.sprite;

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

public class TestAnimation extends SimpleApplication
{
   private SpriteManager spriteManager;
   private SpriteImage[] npcList ;

   public static void main(String[] args)
   {
      Logger.getLogger("").setLevel(Level.SEVERE);
      TestAnimation app = new TestAnimation();
      app.start();
   }

   @Override
   public void simpleInitApp()
   {
      spriteManager = new SpriteManager(1024, 1024, SpriteMesh.Strategy.KEEP_BUFFER, rootNode, assetManager);
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

      new Sprite( new SpriteAnimation(npcList, 0.3f));
   }
}