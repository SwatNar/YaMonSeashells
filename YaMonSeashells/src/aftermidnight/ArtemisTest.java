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
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import engine.sprites.Sprite;
import engine.sprites.SpriteAnimation;
import engine.sprites.SpriteImage;
import engine.sprites.SpriteManager;
import engine.sprites.SpriteMesh;
import engine.util.FileUtilities;
import engine.util.ImageUtilities;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class ArtemisTest extends SimpleApplication {
	
  private World world;
	private boolean isRunning = true;

  
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
      
    Vector3f defaultView = new Vector3f(0f, 0f, 15f);
    getCamera().setLocation(defaultView);
    
    world = new World();
		world.setSystem( new DebugPointRenderer() );
//		world.setSystem( new MovementSystem() );
		world.initialize();
		
		
		Entity e = world.createEntity();
		e.addComponent( new Position(100,100) );
		e.addToWorld();
		
		e = world.createEntity();
		e.addComponent( new Position(200,100) );
//		e.addComponent( new Velocity(100,20) );
		e.addToWorld();
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
