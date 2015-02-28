/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aftermidnight;

import com.artemis.Entity;
import com.artemis.World;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import java.util.logging.Level;
import java.util.logging.Logger;
import usandthem.Velocity;

/**
 *
 * @author Daniel
 */
public class ArtemisTest extends SimpleApplication {

  private World world;
  private boolean isRunning = true;
  public static SimpleApplication myApp;

  public static void main(String[] args) {

    Logger.getLogger("").setLevel(Level.SEVERE);
    ArtemisTest app = new ArtemisTest();
    app.start();


    // Graphics
    // Physics = box2d
    // Logic
    // Network

  }

  @Override
  public void simpleInitApp() {

    SharedVars.assetManager = assetManager;
    SharedVars.rootNode = rootNode;
    SharedVars.guiNode = rootNode;

    myApp = this;
    SharedVars.appStateManager = myApp.getStateManager();

    Vector3f defaultView = new Vector3f(0f, 0f, 25f);
    //getCamera().setLocation(defaultView);
    //getCamera().lookAtDirection(new Vector3f(0, 0f, 0f), Vector3f.UNIT_Y);

    getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
    getFlyByCamera().setMoveSpeed(50);
    getCamera().setLocation(new Vector3f(-15, 0, 55));
    getCamera().lookAtDirection(new Vector3f(12, 7.5f, -15), Vector3f.UNIT_Y);

    world = new World();
    world.setSystem(new DebugPointRenderer());
    world.setSystem(new MovementSystem());
    world.initialize();


    Entity e = world.createEntity();
    e.addComponent(new Position(100, 100));
    e.addToWorld();

    e = world.createEntity();
    e.addComponent(new Position(0, 0));
    e.addComponent(new Velocity(100, 20));
    e.addToWorld();

    Box b = new Box(1, 1, 1); // create cube shape
    Geometry geom = new Geometry("Box", b);  // create cube geometry from the shape
    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
    mat.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
    geom.setMaterial(mat);                   // set the cube's material
    // this audo predict sucks
    geom.move(1f, 1f,0);
    rootNode.attachChild(geom);              // make the cube appear in the scene

    initKeys();


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
    inputManager.addMapping("Rotate", new KeyTrigger(KeyInput.KEY_SPACE), new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    // Add the names to the action listener.
    inputManager.addListener(actionListener, "Pause");
    inputManager.addListener(analogListener, "Left", "Right", "Rotate", "Up", "Down", "Alt 1", "Alt 2", "Alt 3", "Alt 4");

  }

  @Override
  public void simpleUpdate(float tpf) {
    world.process();
    
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
          System.out.println("adding a noob");
          int min = -15;
          int max = 15;
          Entity e = world.createEntity();
          e.addComponent(new Position(min + (int) (Math.random() * max), min + (int) (Math.random() * max)));
          world.addEntity(e);

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
        }
        if (name.equals("Alt 2")) {
        }
        if (name.equals("Alt 3")) {
        }
        if (name.equals("Alt 4")) {
        }
      } else {
      }
    }
  };
}
