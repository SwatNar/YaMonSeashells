package tests.sprite;

import com.jme3.app.SimpleApplication;
import engine.sprites.SpriteManager;
import engine.sprites.SpriteMesh;
import engine.util.FileUtilities;
import engine.util.ImageUtilities;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestSaveSprite extends SimpleApplication
{
   private SpriteManager spriteManager;
   private String[] npcList;
   private static final String LOAD_IMAGE_DIRECTORY = "2d/npc/refmap/";
   private static final String SAVE_IMAGE_DIRECTORY = "2d/npc/";
   private static final String FILE_NAME = "npc";
   private static final Color COLOR_TO_MAKE_TRANSPARENT = new Color(120, 195, 128);

   public static void main(String[] args)
   {
      Logger.getLogger("").setLevel(Level.SEVERE);
      TestSaveSprite app = new TestSaveSprite();
      app.start();
   }

   @Override
   public void simpleInitApp()
   {
      spriteManager = new SpriteManager(1024, 1024, SpriteMesh.Strategy.ALLOCATE_NEW_BUFFER, rootNode, assetManager);
      getStateManager().attach(spriteManager);

      File npcLocation = new File(FileUtilities.ASSET_DIRECTORY + LOAD_IMAGE_DIRECTORY);
      npcList = npcLocation.list(FileUtilities.SUPPORTED_IMAGES);
      for (int i = 0; i < npcList.length; i++) npcList[i] = LOAD_IMAGE_DIRECTORY + npcList[i];

      int numSpritesX = 12;
      int numSpritesY = 8;
      int numSubSpritesX = 3;
      int numSubSpritesY = 4;
      int numSpriteSheets = (numSpritesX / numSubSpritesX) * (numSpritesY / numSubSpritesY);

      for (int numberOfSpriteSheets = 0; numberOfSpriteSheets < npcList.length; numberOfSpriteSheets++)
      {
         BufferedImage image = ImageUtilities.loadImage(npcList[numberOfSpriteSheets], assetManager);
         BufferedImage transparentImage = ImageUtilities.transformColorToTransparency(image, COLOR_TO_MAKE_TRANSPARENT);
         BufferedImage[][] split = ImageUtilities.split(transparentImage, numSpritesX, numSpritesY);

         for (int index = 0; index < numSpriteSheets; index++)
         {
            BufferedImage[][] sheet = ImageUtilities.getSubsheet(split, numSubSpritesX, numSubSpritesY, index);
            BufferedImage[] images = ImageUtilities.asSingleArray(sheet, false);
            ImageUtilities.saveAsPng(images[1], new File(FileUtilities.ASSET_DIRECTORY + SAVE_IMAGE_DIRECTORY+FILE_NAME+(numberOfSpriteSheets*numSpriteSheets+index) + ".png"));
         }
      }
      System.exit(0);
   }
}