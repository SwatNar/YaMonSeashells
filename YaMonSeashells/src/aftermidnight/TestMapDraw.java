package aftermidnight;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestMapDraw extends SimpleApplication {

  private SpriteManager spriteManager;
  private SpriteImage[] npcList;
  //protected Geometry player;
  Boolean isRunning = true;
  Sprite mySprite;
  Vector3f defaultView = new Vector3f(19f, 15f, 15f);
  private static final String NPC_IMAGE_DIRECTORY = "2d/npc/refmap/";
  private static final Color COLOR_TO_MAKE_TRANSPARENT = new Color(120, 195, 128);
  private static final boolean RANDOM_FACING_DIRECTION = false;

  public static void main(String[] args) {
    Logger.getLogger("").setLevel(Level.SEVERE);
    TestMapDraw app = new TestMapDraw();
    app.start();

    // Graphics
    // Physics = box2d
    // Logic
    // Network

  }

  @Override
  public void simpleInitApp() {
    spriteManager = new SpriteManager(1024, 1024, SpriteMesh.Strategy.KEEP_BUFFER, rootNode, assetManager);
    getStateManager().attach(spriteManager);
    getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
    //getFlyByCamera().setMoveSpeed(50);
    flyCam.setEnabled(false);

    //getCamera().lookAtDirection(new Vector3f(12, 7.5f, -15), Vector3f.UNIT_Y);

    File npcLocation = new File(FileUtilities.ASSET_DIRECTORY + "kenny nl/Base pack/Enemies/");
    String[] fileList = npcLocation.list(FileUtilities.SUPPORTED_IMAGES);
    npcList = new SpriteImage[2];
    npcList[0] = spriteManager.createSpriteImage("kenny nl/Base pack/Enemies/" + fileList[1], false);
    npcList[1] = spriteManager.createSpriteImage("kenny nl/Base pack/Enemies/" + fileList[2], false);

    mySprite = new Sprite(new SpriteAnimation(npcList, 0.2f));
    mySprite.setPosition(0, 0, 0);
    initKeys(); // load my custom keybinding

    int numSpritesX = 12;
    int numSpritesY = 13;
    int numSubSpritesX = 1;
    int numSubSpritesY = 1;
    int numSpriteSheets = (numSpritesX / numSubSpritesX) * (numSpritesY / numSubSpritesY);

    BufferedImage image = ImageUtilities.loadImage("kenny nl/Base pack/Tiles/tiles_spritesheet_edit.png", assetManager);
    BufferedImage transparentImage = ImageUtilities.transformColorToTransparency(image, COLOR_TO_MAKE_TRANSPARENT);
    BufferedImage[][] split = ImageUtilities.split(transparentImage, numSpritesX, numSpritesY);
    //ImageUtilities.viewImage(ImageUtilities.merge(split));

    for (int index = 0; index < numSpriteSheets; index++) {
      BufferedImage[][] sheet = ImageUtilities.getSubsheet(split, numSubSpritesX, numSubSpritesY, index);
      BufferedImage[] images = ImageUtilities.asSingleArray(sheet, false);

      SpriteImage[] sprites = new SpriteImage[images.length];
      for (int i = 0; i < images.length; i++) {
        sprites[i] = spriteManager.createSpriteImage(images[i], false);
      }

      SpriteAnimation anim = new SpriteAnimation(sprites, 0.3f);
      //spriteManager.putAnimation("rotateAroundSelf"+index, anim);

      for (int i = 0; i < 100; i++) {
        Sprite sprite = new Sprite(anim, 0);
        sprite.setPosition(index * .5f, i * .5f, -15f);
      }
    }

    for (int x = 0; x < 100; x++) {
      for (int y = 0; y < 100; y++) {
      }
    }

    try {
      Scanner scanner = new Scanner(new File("daniel/maps/test.csv"));
      scanner.useDelimiter(",");
      while (scanner.hasNext()) {
        System.out.print(scanner.next() + "|");

      }
    } catch (Exception ex) {
      System.out.println(ex.toString());
    }

  }

  private void initKeys() {
    // You can map one or several inputs to one named action
    inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
    inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping("Alt 1", new KeyTrigger(KeyInput.KEY_Q));
    inputManager.addMapping("Alt 2", new KeyTrigger(KeyInput.KEY_E));
    inputManager.addMapping("Alt 3", new KeyTrigger(KeyInput.KEY_F));
    inputManager.addMapping("Alt 4", new KeyTrigger(KeyInput.KEY_G));
    inputManager.addMapping("Rotate", new KeyTrigger(KeyInput.KEY_SPACE),
            new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    // Add the names to the action listener.
    inputManager.addListener(actionListener, "Pause");
    inputManager.addListener(analogListener, "Left", "Right", "Rotate", "Up", "Down", "Alt 1", "Alt 2", "Alt 3", "Alt 4");

  }

  @Override
  public void simpleUpdate(float tpf) {
    // make the player rotate:
    Vector3f mySpriteLocation = mySprite.getPosition();
    //mySprite.setPosition(mySpriteLocation.x + .01f, mySpriteLocation.y, mySpriteLocation.z);

  }
  private ActionListener actionListener = new ActionListener() {
    public void onAction(String name, boolean keyPressed, float tpf) {
      if (name.equals("Pause") && !keyPressed) {
        isRunning = !isRunning;
      }
    }
  };
  private AnalogListener analogListener = new AnalogListener() {
    float speed = .01f;
    float scale = 1f;

    public void onAnalog(String name, float value, float tpf) {
      if (isRunning) {
        if (name.equals("Rotate")) {
          System.out.println(mySprite.getPosition());
          mySprite.setPosition(0, 0, 0);
        }
        if (name.equals("Right")) {
          Vector3f mySpriteLocation = mySprite.getPosition();
          mySprite.setPosition(mySpriteLocation.x + speed, mySpriteLocation.y, mySpriteLocation.z);
        }
        if (name.equals("Left")) {
          Vector3f mySpriteLocation = mySprite.getPosition();
          mySprite.setPosition(mySpriteLocation.x - speed, mySpriteLocation.y, mySpriteLocation.z);
        }
        if (name.equals("Up")) {
          Vector3f mySpriteLocation = mySprite.getPosition();
          mySprite.setPosition(mySpriteLocation.x, mySpriteLocation.y + speed, mySpriteLocation.z);
        }
        if (name.equals("Down")) {
          Vector3f mySpriteLocation = mySprite.getPosition();
          mySprite.setPosition(mySpriteLocation.x, mySpriteLocation.y - speed, mySpriteLocation.z);
        }
        if (name.equals("Alt 1")) {
          Vector3f mySpriteLocation = mySprite.getPosition();
          mySprite.setPosition(mySpriteLocation.x, mySpriteLocation.y, mySpriteLocation.z + speed);
        }
        if (name.equals("Alt 2")) {
          Vector3f mySpriteLocation = mySprite.getPosition();
          mySprite.setPosition(mySpriteLocation.x, mySpriteLocation.y, mySpriteLocation.z - speed);
        }
        if (name.equals("Alt 3")) {
          Vector3f cam = getCamera().getLocation();
          getCamera().setLocation(new Vector3f(cam.x, cam.y, cam.z - speed));
        }
        if (name.equals("Alt 4")) {
          Vector3f cam = getCamera().getLocation();
          getCamera().setLocation(new Vector3f(cam.x, cam.y, cam.z + speed));

        }
      } else {
      }
    }
  };
}