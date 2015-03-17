package aftermidnight.systems;

import aftermidnight.SharedVars;
import aftermidnight.components.Position;
import aftermidnight.components.SpriteRendererSprite;
import aftermidnight.components.UserControllable;
import aftermidnight.components.Velocity;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import java.util.ArrayList;

public class UserInputSystem extends EntitySystem {

  @Mapper
  ComponentMapper<Position> pm;
  @Mapper
  ComponentMapper<Velocity> vm;
  @Mapper
  ComponentMapper<SpriteRendererSprite> srim;
  ArrayList<String> commands = new ArrayList<String>();
  private float speed = 10f;

  //Queue<String> actionList = new Queue<String>();
  @SuppressWarnings("unchecked")
  public UserInputSystem() {
    super(Aspect.getAspectForAll(Position.class, Velocity.class, UserControllable.class));
  }

  @Override
  protected void processEntities(ImmutableBag<Entity> entities) {
    while (!commands.isEmpty()) {
      String cmd = commands.remove(0);
      if (cmd == "MOVE LEFT") {
        for (int x = 0; x < entities.size(); x++) {
          Velocity v = vm.getSafe(entities.get(x));
          if (v.getX() > 0f - speed) {
            v.setX(0f - speed);
          }
        }
      } else if (cmd == "MOVE RIGHT") {
        for (int x = 0; x < entities.size(); x++) {
          Velocity v = vm.getSafe(entities.get(x));
          if (v.getX() < speed) {
            v.setX(speed);
          }
        }
      } else if (cmd == "MOVE UP") {
        for (int x = 0; x < entities.size(); x++) {
          Velocity v = vm.getSafe(entities.get(x));
          if (v.getY() < speed) {
            v.setY(speed);
          }
        }
      } else if (cmd == "MOVE DOWN") {
        for (int x = 0; x < entities.size(); x++) {
          Velocity v = vm.getSafe(entities.get(x));
          if (v.getY() > 0f - speed) {
            v.setY(0f - speed);
          }
        }
      }
    }
  }

  @Override
  protected boolean checkProcessing() {
    return true;
  }

  protected void initialize() {
    // Test multiple inputs per mapping
    SharedVars.inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
    SharedVars.inputManager.addMapping("Q", new KeyTrigger(KeyInput.KEY_Q));
    SharedVars.inputManager.addMapping("W", new KeyTrigger(KeyInput.KEY_W));
    SharedVars.inputManager.addMapping("E", new KeyTrigger(KeyInput.KEY_E));
    SharedVars.inputManager.addMapping("A", new KeyTrigger(KeyInput.KEY_A));
    SharedVars.inputManager.addMapping("S", new KeyTrigger(KeyInput.KEY_S));
    SharedVars.inputManager.addMapping("D", new KeyTrigger(KeyInput.KEY_D));
    SharedVars.inputManager.addMapping("F", new KeyTrigger(KeyInput.KEY_F));
    SharedVars.inputManager.addMapping("F", new KeyTrigger(KeyInput.KEY_F));
    SharedVars.inputManager.addMapping("G", new KeyTrigger(KeyInput.KEY_G));
    SharedVars.inputManager.addMapping("H", new KeyTrigger(KeyInput.KEY_H));
    SharedVars.inputManager.addMapping("J", new KeyTrigger(KeyInput.KEY_J));
    SharedVars.inputManager.addMapping("K", new KeyTrigger(KeyInput.KEY_K));
    SharedVars.inputManager.addMapping("L", new KeyTrigger(KeyInput.KEY_L));

    // Test multiple listeners per mapping
    SharedVars.inputManager.addListener(actionListener, "Space", "F", "G", "H", "J", "K", "L");
    SharedVars.inputManager.addListener(analogListener, "W", "A", "S", "D", "Q", "E");
//    SharedVars.inputManager.addListener(analogListener, "My Action");

  }
  private ActionListener actionListener = new ActionListener() {
    public void onAction(String name, boolean pressed, float tpf) {
      if (!pressed) {
        if (name.equals("Space")) {

        } else if (name.equals("Q")) {
          //setLocation(defaultView);
        } else if (name.equals("F")) {
        }
      }
    }
  };
  public AnalogListener analogListener = new AnalogListener() {
    public void onAnalog(String name, float value, float tpf) {
      if (name.equals("A")) {
        commands.add("MOVE LEFT");
      } else if (name.equals("D")) {
        commands.add("MOVE RIGHT");
      } else if (name.equals("W")) {
        commands.add("MOVE UP");
      } else if (name.equals("S")) {
        commands.add("MOVE DOWN");
      } else if (name.equals("Q")) {

      } else if (name.equals("E")) {

      }


    }
  };
}
