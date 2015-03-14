package aftermidnight.systems;

import aftermidnight.SharedVars;
import aftermidnight.components.Position;
import aftermidnight.components.Root;
import aftermidnight.components.SpriteRendererSprite;
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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class UserInputSystem extends EntitySystem {

  @Mapper
  ComponentMapper<Position> pm;
  @Mapper
  ComponentMapper<Velocity> vm;
  @Mapper
  ComponentMapper<SpriteRendererSprite> srim;

  //Queue<String> actionList = new Queue<String>();
  @SuppressWarnings("unchecked")
  public UserInputSystem() {
    super(Aspect.getAspectForAll(Position.class, Velocity.class));
  }

  /*
   protected void process(Entity e) {
   while(SharedVars.inputQueue.size() > 0)
   {
   String command = SharedVars.inputQueue.remove(0);
   if (command == "Rotate")
   {
   e.
   }
   }
   }
   */
  @Override
  protected void processEntities(ImmutableBag<Entity> entities) {
    while (!SharedVars.input.isEmpty()) {
      Set set = SharedVars.input.entrySet();
      // Get an iterator
      Iterator iterator = set.iterator();
      // Display elements
      while (iterator.hasNext()) {
        Map.Entry me = (Map.Entry) iterator.next();
        if (me.getKey() == "Rotate") {
          int s = entities.size();
          //System.out.println("RECEIVED ROTATE CMD: " + s + " entitties");
          for (int i = 0; i < s; i++) {
            Entity e = entities.get(i);
            SpriteRendererSprite sri = srim.getSafe(e);
            if (sri != null) {
              float myF;
//              myF = (float) me.getValue();
//              sri.rotate(0f, 0f, myF);
              //System.out.println("ROTATING " + i);
            }
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
    SharedVars.inputManager.addListener(analogListener, "Space", "F", "G", "H", "J", "K", "L");
//    SharedVars.inputManager.addListener(analogListener, "My Action");

  }
  private ActionListener actionListener = new ActionListener() {
    public void onAction(String name, boolean pressed, float tpf) {
      if (!pressed) {
        if (name.equals("Space")) {
//          if (world.getEntityManager().getTotalAdded() > 0) {
//            for (int temp = 0; temp < world.getEntityManager().getActiveEntityCount(); temp++) {
//              Entity en = world.getEntity(temp);
//              if (en.isActive()) {
//                Velocity theVel = vm.get(en);
//                theVel.setVelocity(SharedVars.random.nextFloat() * 20f - 10f, SharedVars.random.nextFloat() * 20f - 10f);

                //(new Velocity(rand.nextFloat() * 20f - 10f, rand.nextFloat() * 20f - 10f));
//              }
//            }
  //        }
        } else if (name.equals("Q")) {
          //setLocation(defaultView);
        } else if (name.equals("F")) {
          try {
            if (world.getEntityManager().getTotalAdded() > 0) {
              for (int temp = 0; temp < world.getEntityManager().getActiveEntityCount(); temp++) {
                Entity en = world.getEntity(temp);
                if (en.isActive()) {
                  Velocity theVel = vm.get(en);
                  theVel.setVelocity(0f - theVel.getX(), 0f - theVel.getY());

                  //(new Velocity(rand.nextFloat() * 20f - 10f, rand.nextFloat() * 20f - 10f));
                }
              }
            }
          } catch (Exception ex) {
            System.out.println(ex.toString());
          }

        } else if (name.equals("G")) {

          int min = 0;
          int max = 1000;
          float fieldOfView = 100f;

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
      }
    }
  };
  public AnalogListener analogListener = new AnalogListener() {
    public void onAnalog(String name, float value, float tpf) {
      if (name.equals("H")) {
        //SharedVars.input.put("Rotate", value);
        //System.out.println("put value " + value + " in rotate");
      }
    }
  };
}
