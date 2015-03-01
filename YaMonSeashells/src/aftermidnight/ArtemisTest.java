/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aftermidnight;

import aftermidnight.components.Position;
import aftermidnight.components.Root;
import aftermidnight.components.Velocity;
import aftermidnight.systems.MovementSystem;
import aftermidnight.systems.OutOfBoundsSystem;
import aftermidnight.systems.PlatformerRenderer;
import com.artemis.Entity;
import com.artemis.World;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class ArtemisTest extends SimpleApplication {

  private World world;
  private boolean isRunning = true;
  public static SimpleApplication myApp;
  private float fieldOfView = 150f;

  public static void main(String[] args) {

    Logger.getLogger("").setLevel(Level.SEVERE);

    ArtemisTest app = new ArtemisTest();
    //app.setDisplayFps(true);
    //app.setDisplayStatView(false);
    //app.setPauseOnLostFocus(false);
    app.start();

  }

  @Override
  public void simpleInitApp() {

    // Setup
    SharedVars.assetManager = assetManager;
    SharedVars.rootNode = rootNode;
    SharedVars.guiNode = rootNode;
    SharedVars.paused = false;
    myApp = this;
    SharedVars.appStateManager = myApp.getStateManager();

    // Graphics
    //Vector3f defaultView = new Vector3f(fieldOfView / 2f, fieldOfView / 2f, 750f);
    Vector3f defaultView = new Vector3f(0, 0, 750f);
    getCamera().setLocation(defaultView);
    getViewPort().setBackgroundColor(new ColorRGBA(0.1f, 0.1f, .1f, 1f));
    getFlyByCamera().setMoveSpeed(25);
    cam.setFrustumPerspective(45, settings.getWidth() / settings.getHeight(), 1, 1000);
    
    // World setup
    world = new World();
    //world.setSystem(new DebugPointRenderer()); // Sprite Render System
    world.setSystem(new PlatformerRenderer());
    world.setSystem(new MovementSystem());
    world.setSystem(new OutOfBoundsSystem());
    world.initialize();

    randomFill(25000);
    
    // Input
    initKeys();

    // Logic

    // Network

  }

  private void randomFill(int max) {

    Random rand = new Random();
    int min = 0;

    for (int x = min; x < max; x++) {
      Entity e = world.createEntity();

      e.addComponent(new Position(rand.nextFloat() * fieldOfView, rand.nextFloat() * fieldOfView));
      e.addComponent(new Velocity(rand.nextFloat() * 20f - 10f, rand.nextFloat() * 20f - 10f));
      if (rand.nextFloat() < .2f) {
        e.addComponent(new Root(true));

      }

      world.addEntity(e);

    }
  }

  private void fillScreen(int max) {
    int min = 0;

    for (int x = min; x < max; x++) {
      for (int y = min; y < max; y++) {
        Entity e = world.createEntity();
        e.addComponent(new Position(x, y));
        world.addEntity(e);

      }
    }

    Entity e = world.createEntity();
    e.addComponent(new Position(min + (int) (Math.random() * max), min + (int) (Math.random() * max)));
    world.addEntity(e);


  }

  private void initKeys() {
    inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
    inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping("Alt 1", new KeyTrigger(KeyInput.KEY_Q));
    inputManager.addMapping("Alt 2", new KeyTrigger(KeyInput.KEY_E));
    inputManager.addMapping("Alt 3", new KeyTrigger(KeyInput.KEY_F));
    inputManager.addMapping("Alt 4", new KeyTrigger(KeyInput.KEY_G));
    inputManager.addMapping("Rotate", new KeyTrigger(KeyInput.KEY_SPACE), new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

    inputManager.addListener(actionListener, "Pause");
    inputManager.addListener(analogListener, "Left", "Right", "Rotate", "Up", "Down", "Alt 1", "Alt 2", "Alt 3", "Alt 4");

  }

  private void initController() {
  }

  @Override
  public void simpleUpdate(float tpf) {
    world.process();
    world.setDelta(tpf);
  }
  private ActionListener actionListener = new ActionListener() {
    public void onAction(String name, boolean keyPressed, float tpf) {
      if (name.equals("Pause") && !keyPressed) {
        SharedVars.paused = !SharedVars.paused;
      }
    }
  };
  private AnalogListener analogListener = new AnalogListener() {
    float speed = .01f;
    float scale = 1f;

    public void onAnalog(String name, float value, float tpf) {
      if (name.equals("Rotate")) {
        randomFill(100);
        
      }
      if (name.equals("Right")) {
      }
      if (name.equals("Left")) {
      }
      if (name.equals("Up")) {
      }
      if (name.equals("Down")) {
      }
      if (name.equals("Alt 1")) {
        System.out.println("Alt 1 = REMOVING " + SharedVars.rootNode.getChildren().size() + " objects");
        for (int x = 0; x < SharedVars.rootNode.getChildren().size(); x++) {
        }
      }
      if (name.equals("Alt 2")) {
        System.out.println("Alt 2");
      }
      if (name.equals("Alt 3")) {
        System.out.println("Alt 3 = " + SharedVars.rootNode.getChildren().size() + " objects");

      }
      if (name.equals("Alt 4")) {
        System.out.println("Alt 4");
        System.out.println("total entities: " + SharedVars.rootNode.getChildren().size());
      }
    }
  };
}
