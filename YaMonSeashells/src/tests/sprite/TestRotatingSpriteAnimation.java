package tests.sprite;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import engine.sprites.Sprite;
import engine.sprites.SpriteAnimation;
import engine.sprites.SpriteImage;
import engine.sprites.SpriteManager;
import engine.sprites.SpriteMesh;
import engine.util.FileUtilities;
import engine.util.ImageUtilities;
import engine.util.MathUtilities;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/** 310 fps for 3000 rotating sprites.
 *  code to find sprite facing from : http://gmc.yoyogames.com/index.php?showtopic=345666 
 */
public class TestRotatingSpriteAnimation extends SimpleApplication
{
   private SpriteManager spriteManager;
   private String[] npcList;
   private SpriteAnimation[] directedAnimation;
   private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
   private int SPRITES = 3000; //num of columns = *6.
   private static final String NPC_IMAGE_DIRECTORY = "2d/npc/refmap/";
   private static final Color COLOR_TO_MAKE_TRANSPARENT = new Color(120, 195, 128);
   private boolean displayTextureAtlas = true;

   public static void main(String[] args)
   {
      Logger.getLogger("").setLevel(Level.SEVERE);
      TestRotatingSpriteAnimation app = new TestRotatingSpriteAnimation();
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

      BufferedImage image = ImageUtilities.loadImage(npcList[0], assetManager);
      BufferedImage transparentImage = ImageUtilities.transformColorToTransparency(image, COLOR_TO_MAKE_TRANSPARENT);
      BufferedImage[][] split = ImageUtilities.split(transparentImage, numSpritesX, numSpritesY);
      BufferedImage[][] sheet = ImageUtilities.getSubsheet(split, numSubSpritesX, numSubSpritesY, 1);
      BufferedImage[] images = ImageUtilities.asSingleArray(sheet, false);

      SpriteImage[] spritesImages = new SpriteImage[images.length];
      for(int i=0; i< 6; i++) spritesImages[i] = spriteManager.createSpriteImage(images[i], false);

      spritesImages[6] = spriteManager.createSpriteImage(images[9], false); //bot image is last row.
      spritesImages[7] = spriteManager.createSpriteImage(images[10], false);
      spritesImages[8] = spriteManager.createSpriteImage(images[11], false);

      spritesImages[9] = spritesImages[3].getMirrorX(); // right image.
      spritesImages[10] = spritesImages[4].getMirrorX(); // avoid wasting texture space by using symmetryX tex cords.
      spritesImages[11] = spritesImages[5].getMirrorX();

      directedAnimation = new SpriteAnimation[4]; //4 because top, left, right, bot
      for (int i = 0; i < 4; i++)
      {
         int currentFrame = i * 3;//3 frames per animation
         SpriteImage[] directionImages =
         {
            spritesImages[currentFrame], spritesImages[currentFrame + 1], spritesImages[currentFrame + 2], spritesImages[currentFrame + 1]
         };
         directedAnimation[i] = new SpriteAnimation(directionImages, 0.3f);
      }

      for (int i = 0; i < SPRITES; i++)
      {
         Sprite sprite = new Sprite(directedAnimation[0]);
         sprite.setPosition(i, 0, 0);
         sprites.add(sprite);
      }
   }

   @Override
   public void simpleUpdate(float tpf)
   {
      super.simpleUpdate(tpf);

      if (displayTextureAtlas)
      {
         ImageUtilities.viewImage(spriteManager.getSpriteMeshes().get(0).getAtlas().getBufferedImage()); //atlas is build after update.
         displayTextureAtlas = false;
      }

      Vector3f targetLook = cam.getLocation();
      int numdirs = 4;

      for (Sprite sprite : sprites)
      {
         float direction = (float) MathUtilities.getAngleBetween(sprite.getPosition().x, sprite.getPosition().z, targetLook.x, targetLook.z) + FastMath.PI;
         int facing = (int) Math.floor(((direction + FastMath.PI / numdirs) % FastMath.TWO_PI) / (FastMath.TWO_PI / numdirs));

         if (sprite.getCurrentAnimation() != directedAnimation[facing]) sprite.setAnimation(directedAnimation[facing], sprite.getCurrentFrame());
      }
   }
}