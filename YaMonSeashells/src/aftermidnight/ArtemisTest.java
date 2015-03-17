/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aftermidnight;

import aftermidnight.components.Position;
import aftermidnight.components.Root;
import aftermidnight.components.Velocity;
import aftermidnight.components.MapComponent;
import aftermidnight.components.UserControllable;
import aftermidnight.systems.CameraSystem;
import aftermidnight.systems.MapRenderer;
import aftermidnight.systems.SpriteRenderer;
import aftermidnight.systems.MovementSystem;
import aftermidnight.systems.OutOfBoundsSystem;
import aftermidnight.systems.SpriteCollisionSystem;
import aftermidnight.systems.UserInputSystem;
import com.artemis.Entity;
import com.artemis.World;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class ArtemisTest extends SimpleApplication {

  private World world;
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
    SharedVars.paused = true;
    SharedVars.assetManager = assetManager;
    SharedVars.rootNode = rootNode;
    SharedVars.guiNode = rootNode;
    SharedVars.inputManager = inputManager;
    SharedVars.random = new Random(System.currentTimeMillis());
    SharedVars.camera = getCamera();
    
    myApp = this;

    SharedVars.appStateManager = myApp.getStateManager();

    // Graphics
    //Vector3f defaultView = new Vector3f(0, 0, 3f);
    Vector3f defaultView = new Vector3f(40f, -60f, 35f);
    SharedVars.camera.setLocation(defaultView);
    float[] angles = cam.getRotation().toAngles(null);
            
    getViewPort().setBackgroundColor(new ColorRGBA(0.1f, 0.1f, .1f, 1f));
    getFlyByCamera().setMoveSpeed(25f);
    
    flyCam.setEnabled(false);
    
    float frustumSize = 1f;
    
    //cam.setParallelProjection(true);
    float aspect = (float) cam.getWidth() / cam.getHeight();
    //cam.setFrustum(-1000, 1000, -aspect * frustumSize, aspect * frustumSize, frustumSize, -frustumSize);
        
    //cam.setFrustumPerspective(45f, settings.getWidth() / settings.getHeight() + 0f, 1f, 900f);

    // World setup


    world = new World();
    world.setSystem(new UserInputSystem());
    world.setSystem(new MovementSystem());
    world.setSystem(new CameraSystem());
    world.setSystem(new SpriteCollisionSystem());
    world.setSystem(new OutOfBoundsSystem());
    world.setSystem(new SpriteRenderer());
    world.setSystem(new MapRenderer());
    
    //world.setSystem(new PlatformerRenderer());
    world.initialize();

    //randomFill(5000);

    Entity e = world.createEntity();

    e.addComponent(new Position(40f, -50f));
    e.addComponent(new Velocity(0f, 0f));
    e.addComponent(new UserControllable(true));
//    e.addComponent(new PlatformRendererParticleEmitter());
    world.addEntity(e);
  
    Entity map = world.createEntity();
    map.addComponent(new MapComponent());
    world.addEntity(map);
    // Input
    //initKeys();

    // Logic

    // Network

    SharedVars.paused = false;

  }

  private void drawBox(float size) {
    Quad quad = new Quad();
    quad.updateGeometry(size, size);
    

    SharedVars.dumbCollisionGlobal = new Geometry("Box", quad);
    //red.setLocalTranslation(new Vector3f(1, 3, 1));
    Material mat2 = new Material(SharedVars.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

    Random rand = new Random();

    float r = rand.nextFloat();// * .5f + 0.5f;
    float g = rand.nextFloat();// * .5f + 0.5f;
    float b = rand.nextFloat();// * .5f + 0.5f;

    //mat2.setColor("Color", new ColorRGBA(r, g, b, .1f));
    SharedVars.dumbCollisionGlobal.setMaterial(mat2);
    SharedVars.dumbCollisionGlobal.setLocalTranslation(0f - size / 2, 0f - size / 2, 0f);
    SharedVars.dumbCollisionGlobal.setQueueBucket(Bucket.Transparent);
    //SharedVars.dumbCollisionGlobal.rotate((float) Math.PI, 0f, 0f);
    SharedVars.rootNode.attachChild(SharedVars.dumbCollisionGlobal);

  }

  private void randomFill(int max) {

    int min = 0;

    for (int x = min; x < max; x++) {
      Entity e = world.createEntity();

      e.addComponent(new Position(SharedVars.random.nextFloat() * fieldOfView - fieldOfView / 2f, SharedVars.random.nextFloat() * fieldOfView - fieldOfView / 2f));
      e.addComponent(new Velocity(SharedVars.random.nextFloat() * 20f - 10f, SharedVars.random.nextFloat() * 20f - 10f));
      if (SharedVars.random.nextFloat() < .2f) {
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

//    inputManager.addListener(actionListener, "Pause");
//    inputManager.addListener(analogListener, "Left", "Right", "Rotate", "Up", "Down", "Alt 1", "Alt 2", "Alt 3", "Alt 4");

  }

  private void initController() {
  }

  @Override
  public void simpleUpdate(float tpf) {
    world.process();
    world.setDelta(tpf);
  }
}
