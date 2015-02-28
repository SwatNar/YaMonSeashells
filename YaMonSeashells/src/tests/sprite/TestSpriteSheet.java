package tests.sprite;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import engine.sprites.Sprite;
import engine.sprites.SpriteAnimation;
import engine.sprites.SpriteImage;
import engine.sprites.SpriteManager;
import engine.sprites.SpriteMesh;
import engine.util.FileUtilities;
import engine.util.ImageUtilities;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

// KEEP_BUFFER:         700 rows = 2450 fps.  1000 rows = 2430 fps   10000 rows = 280 fps. client: 444.
// ALLOCATE_NEW_BUFFER: 700 rows = 2450 fps.  1000 rows = 2220 fps   10000 rows = 280 fps. client: 444.
public class TestSpriteSheet extends SimpleApplication
{
   private SpriteManager spriteManager;
   private String[] npcList;
   private int SPRITES = 10000; //num of columns = *6.
   private static final String NPC_IMAGE_DIRECTORY = "2d/npc/refmap/";
   private static final Color COLOR_TO_MAKE_TRANSPARENT = new Color(120, 195, 128);
   private static final boolean RANDOM_FACING_DIRECTION = false;

   public static void main(String[] args)
   {
      Logger.getLogger("").setLevel(Level.SEVERE);
      TestSpriteSheet app = new TestSpriteSheet();
      app.start();
   }

   @Override
   public void simpleInitApp()
   {
      spriteManager = new SpriteManager(1024, 1024, SpriteMesh.Strategy.ALLOCATE_NEW_BUFFER, rootNode, assetManager);
      getStateManager().attach(spriteManager);
      getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
      getFlyByCamera().setMoveSpeed(50);

      File npcLocation = new File(FileUtilities.ASSET_DIRECTORY + NPC_IMAGE_DIRECTORY);
      npcList = npcLocation.list(FileUtilities.SUPPORTED_IMAGES);
      for (int i = 0; i < npcList.length; i++) npcList[i] = NPC_IMAGE_DIRECTORY + npcList[i];

      int numSpritesX = 12;
      int numSpritesY = 8;
      int numSubSpritesX = 3;
      int numSubSpritesY = 4;
      int numSpriteSheets = (numSpritesX / numSubSpritesX) * (numSpritesY / numSubSpritesY);

      BufferedImage image = ImageUtilities.loadImage(npcList[0], assetManager);
      BufferedImage transparentImage = ImageUtilities.transformColorToTransparency(image, COLOR_TO_MAKE_TRANSPARENT);
      BufferedImage[][] split = ImageUtilities.split(transparentImage, numSpritesX, numSpritesY);
      //ImageUtilities.viewImage(ImageUtilities.merge(split));

      for (int index = 0; index < numSpriteSheets; index++)
      {
         BufferedImage[][] sheet = ImageUtilities.getSubsheet(split, numSubSpritesX, numSubSpritesY, index);
         //ImageUtilities.viewImage(ImageUtilities.merge(sheet));

         BufferedImage[] images = ImageUtilities.asSingleArray(sheet, false);
         //ImageUtilities.viewImage(ImageUtilities.merge(images));

         SpriteImage[] sprites = new SpriteImage[images.length];
         for (int i = 0; i < images.length; i++)
         {
            sprites[i] = spriteManager.createSpriteImage(images[i], false);
         }

         SpriteAnimation anim = new SpriteAnimation(sprites, 0.3f);
         //spriteManager.putAnimation("rotateAroundSelf"+index, anim);

         for (int i = 0; i < SPRITES; i++)
         {
            int startFrame = (RANDOM_FACING_DIRECTION)?i:0;
            Sprite sprite = new Sprite(anim, startFrame);
            sprite.setPosition(index * 2, 0, i*1.5f);
         }
      }
   }

   /*
   @Override
   public void simpleUpdate(float tpf)
   {
      super.simpleUpdate(tpf);
      final int numdirs = 4;
      for(SpriteMesh mesh : spriteManager.getSpriteMeshes())
      {
         for(Sprite sprite : mesh.getSprites())
         {
            float direction = (float)MathUtilities.getAngleBetween(cam.getLocation().x, cam.getLocation().z, sprite.getPosition().x, sprite.getPosition().z)+FastMath.PI;
            int facing      = (int) Math.floor(((direction + FastMath.PI / numdirs) % FastMath.TWO_PI) / (FastMath.TWO_PI / numdirs));
            System.out.println(sprite+" "+direction+" "+facing);
         }
      }
   }*/
}