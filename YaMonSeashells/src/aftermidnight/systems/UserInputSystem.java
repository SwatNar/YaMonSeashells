package aftermidnight.systems;

import aftermidnight.SharedVars;
import aftermidnight.components.Position;
import aftermidnight.components.Root;
import aftermidnight.components.Velocity;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;

public class UserInputSystem extends EntityProcessingSystem {

  @Mapper
  ComponentMapper<Position> pm;
  @Mapper
  ComponentMapper<Velocity> vm;

  //Queue<String> actionList = new Queue<String>();
  @SuppressWarnings("unchecked")
  public UserInputSystem() {
    super(Aspect.getAspectForAll(Position.class, Velocity.class));
  }

  protected void process(Entity e) {
  }

  protected void initialize() {
    // Test multiple inputs per mapping
    SharedVars.inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
    SharedVars.inputManager.addMapping("F", new KeyTrigger(KeyInput.KEY_F));
    SharedVars.inputManager.addMapping("G", new KeyTrigger(KeyInput.KEY_G));

    // Test multiple listeners per mapping
    SharedVars.inputManager.addListener(actionListener, "Space", "F", "G");
//    SharedVars.inputManager.addListener(analogListener, "My Action");

  }
  private ActionListener actionListener = new ActionListener() {
    public void onAction(String name, boolean pressed, float tpf) {
      if (!pressed) {
        if (name.equals("Space")) {
          if (world.getEntityManager().getTotalAdded() > 0) {
            for (int temp = 0; temp < world.getEntityManager().getActiveEntityCount(); temp++) {
              Entity en = world.getEntity(temp);
              if (en.isActive()) {
                Velocity theVel = vm.get(en);
                theVel.setVelocity(SharedVars.random.nextFloat() * 20f - 10f, SharedVars.random.nextFloat() * 20f - 10f);

                //(new Velocity(rand.nextFloat() * 20f - 10f, rand.nextFloat() * 20f - 10f));
              }
            }
          }
        } else if (name.equals("F")) {
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
        } else if (name.equals("G")) {

          int min = 0; int max = 10000;
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
        //System.out.println(name + " = " + value);
      }
    };
  }
