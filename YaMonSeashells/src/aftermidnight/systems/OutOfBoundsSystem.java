/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aftermidnight.systems;

import aftermidnight.components.Position;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;

/**
 *
 * @author Daniel
 */
public class OutOfBoundsSystem extends EntityProcessingSystem {
  
  @Mapper
  ComponentMapper<Position> pm;

  @SuppressWarnings("unchecked")
  public OutOfBoundsSystem() {
    super(Aspect.getAspectForAll(Position.class));
  }

  protected void process(Entity e) {
    float x = pm.get(e).getX();
    float y = pm.get(e).getY();
    
    float oob = 250f;
    
    if (x > oob || x < 0f-oob || y > oob || y < 0f-oob)
    {
      e.deleteFromWorld();
    }
  }
}
