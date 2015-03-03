/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aftermidnight.systems;

import aftermidnight.SharedVars;
import aftermidnight.components.Position;
import aftermidnight.components.SpriteRendererSprite;
import aftermidnight.components.Velocity;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.jme3.math.Vector3f;

/**
 *
 * @author Daniel
 */
public class SpriteCollisionSystem extends EntityProcessingSystem {
  
  @Mapper
  ComponentMapper<Position> pm;
  
  @Mapper
  ComponentMapper<Velocity> vm;
  
  @Mapper
  ComponentMapper<SpriteRendererSprite> srsM;

  @SuppressWarnings("unchecked")
  public SpriteCollisionSystem() {
    super(Aspect.getAspectForAll(Position.class));
  }

  protected void process(Entity e) {
    if (SharedVars.dumbCollisionGlobal == null) return;
    
    Vector3f myvec = SharedVars.dumbCollisionGlobal.getLocalTranslation();
   
    Position position = pm.get(e);    
    
    if (position.getX() > myvec.x && position.getY() > myvec.x &&
        position.getX() < myvec.x + 100f && position.getY() < myvec.x + 100f)
    {
      vm.getSafe(e).setMultiplier(3f);
    } else {
      vm.getSafe(e).setMultiplier(1f);
    }
    
    
    //float x = pm.get(e).getX();
    //float y = pm.get(e).getY();
    /*
    CollisionResults results = new CollisionResults();
    
    srsM.getSafe(e).getSprite().getSpriteMesh().getGeometry().collideWith(SharedVars.dumbCollisionGlobal, results);
    if (results.size() > 0)
    {
      vm.getSafe(e).setMultiplier(3f);
    } else {
      vm.getSafe(e).setMultiplier(1f);
    }
    * */
  }
}
