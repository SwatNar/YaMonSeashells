package tests.sprite;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import engine.sprites.Sprite;
import engine.sprites.SpriteManager;
import engine.sprites.SpriteMesh;
import engine.util.FileUtilities;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestSpriteManager extends SimpleApplication
{
   private SpriteManager spriteManager ;

   public static void main(String[] args)
   {
      Logger.getLogger("").setLevel(Level.SEVERE);
      TestSpriteManager app = new TestSpriteManager();
      app.start();
   }
   
   @Override
   public void simpleInitApp()
   {
      spriteManager = new SpriteManager(1024, 1024, SpriteMesh.Strategy.KEEP_BUFFER, rootNode, assetManager );
      getStateManager().attach(spriteManager);
      getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
      getFlyByCamera().setMoveSpeed(50);
      getCamera().setLocation(new Vector3f(-15,0,55));
      getCamera().lookAtDirection(new Vector3f(12,7.5f,-15), Vector3f.UNIT_Y);

      File npcLocation = new File(FileUtilities.ASSET_DIRECTORY+"2d/npc/");
      String[] npcList = npcLocation.list(FileUtilities.SUPPORTED_IMAGES);
      for (int i = 0; i < npcList.length; i++)
      {
         Sprite sprite = new Sprite(spriteManager.createSpriteImage("2d/npc/"+npcList[i], true));
         sprite.setPosition(i,0,0);
      }
   }
}