package aftermidnight.systems;

import aftermidnight.SharedVars;
import aftermidnight.components.SpriteRendererSprite;
import aftermidnight.components.UserControllable;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.IntervalEntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.jme3.math.Vector3f;
import java.util.ArrayList;

public class CameraSystem extends IntervalEntityProcessingSystem {

  @Mapper ComponentMapper<SpriteRendererSprite> srsm;
  //@Mapper ComponentMapper<Velocity> vm;
  
  @SuppressWarnings("unchecked")
  public CameraSystem() {
    super(Aspect.getAspectForAll(UserControllable.class), .5f);
  }

  @Override
  protected void process(Entity e) 
  {
    
    
  }
  @Override
  protected void processEntities(ImmutableBag<Entity> entities) {

    Vector3f camLoc = SharedVars.camera.getLocation();
    
    ArrayList<Vector3f> locs = new ArrayList<Vector3f>();

    for (int x = 0; x < entities.size(); x++) {
      SpriteRendererSprite srs = srsm.getSafe(entities.get(x));
      if (srs != null)
        locs.add(srs.getSprite().getPosition());
    }
    
    // TODO: Generate average of vectors after multiple players are added
    if (locs.size() > 0)
      SharedVars.camera.setLocation(new Vector3f(locs.get(0).x, locs.get(0).y, camLoc.z));
            
  }
  
  
  @Override
  protected boolean checkProcessing() {
    return true;
  }

  protected void initialize() {

  }
}
