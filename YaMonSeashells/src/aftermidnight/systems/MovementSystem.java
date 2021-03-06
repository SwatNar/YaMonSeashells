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

public class MovementSystem extends EntityProcessingSystem {

  @Mapper ComponentMapper<Position> pm;
  @Mapper ComponentMapper<Velocity> vm;
  @Mapper ComponentMapper<Root> rootMapper;

  @SuppressWarnings("unchecked")
  public MovementSystem() {
    super(Aspect.getAspectForAll(Position.class, Velocity.class));
  }

  protected void process(Entity e) {

    Position position = pm.get(e);
    Velocity velocity = vm.get(e);
    if (SharedVars.paused) {
      return;
    }

    Root rooted = rootMapper.getSafe(e);
    if (rooted == null || !rooted.getRooted()) {
      
      position.addX(velocity.getX() * velocity.getMultiplier() * world.getDelta());
      position.addY(velocity.getY() * velocity.getMultiplier() * world.getDelta());
    }

    //
    // Shitty speed decrease
    //
    
    if (velocity.getX() != 0f)
    {
      velocity.setX(velocity.getX()/6f*5f);
    }
    if (velocity.getY() != 0f)
    {
      velocity.setY(velocity.getY()/6f*5f);
    }    
  }
}
