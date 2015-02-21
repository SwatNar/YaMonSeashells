package tests.sprite;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import engine.sprites.Sprite;
import engine.sprites.SpriteImage;
import engine.sprites.SpriteMesh;
import engine.sprites.atlas.AtlasSprite;
import engine.sprites.atlas.SimpleTextureAtlas;
import engine.sprites.atlas.TextureAtlas;
import engine.util.FileUtilities;
import engine.util.ImageUtilities;
import java.io.File;

//for internal use. low level api.
public class TestSpriteMesh extends SimpleApplication
{
   private SpriteMesh spriteMesh ;

   private final int SPRITES_NUMBER = 65536;

   public static void main(String[] args)
   {
      TestSpriteMesh app = new TestSpriteMesh();
      app.start();
   }

   @Override
   public void simpleInitApp()
   {
      getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
      spriteMesh = new SpriteMesh(SpriteMesh.Strategy.ALLOCATE_NEW_BUFFER, true, rootNode, assetManager);
      getFlyByCamera().setMoveSpeed(50);
      getCamera().setLocation(new Vector3f(-15,0,55));
      getCamera().lookAtDirection(new Vector3f(12,7.5f,-15), Vector3f.UNIT_Y);

      TextureAtlas textureAtlas = new SimpleTextureAtlas(1024, 1024);
      File npcLocation = new File(FileUtilities.ASSET_DIRECTORY+"2d/npc/");
      String[] npcList = npcLocation.list(FileUtilities.SUPPORTED_IMAGES);
      
      SpriteImage[] images = new SpriteImage[npcList.length];
      for (int i = 0; i < npcList.length; i++)
      {
         AtlasSprite atlasSprite = new AtlasSprite(ImageUtilities.loadImage("2d/npc/"+npcList[i], assetManager));
         if (!textureAtlas.canAdd(atlasSprite)) break;
         textureAtlas.add(atlasSprite);
         images[i]= new SpriteImage( atlasSprite, spriteMesh );
      }
      spriteMesh.setAtlas(textureAtlas);

      for(int i=0; i< SPRITES_NUMBER; i++)
      {
         Sprite sprite = new Sprite(images[0]);
         sprite.setPosition(new Vector3f(i,0,0));
      }
   }

   @Override
   public void simpleUpdate(float tpf)
   {
      super.simpleUpdate(tpf);
      spriteMesh.updateMesh(tpf);
   }
}