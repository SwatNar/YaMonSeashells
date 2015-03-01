/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aftermidnight.systems;

import aftermidnight.SharedVars;
import aftermidnight.components.PlatformRendererSpatial;
import aftermidnight.components.Position;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import engine.sprites.SpriteImage;
import engine.sprites.SpriteManager;
import java.util.Random;

/**
 *
 * @author Daniel
 */
public class PlatformerRenderer extends EntitySystem {

  @Mapper
  ComponentMapper<Position> pm;
  
  @Mapper
  ComponentMapper<PlatformRendererSpatial> prsMapper;
    
  private SpriteManager spriteManager;
  private SpriteImage mySprite;

  @SuppressWarnings("unchecked")
  public PlatformerRenderer() {
    super(Aspect.getAspectForAll(Position.class));
  }

  @Override
  protected void processEntities(ImmutableBag<Entity> entities) {
    int s = entities.size();
    for (int i = 0; i < s; i++) {
      Entity e = entities.get(i);
      Position position = pm.get(e);
//      Spatial thisObject = SharedVars.rootNode.getChild("" + e.getUuid());
//      if (thisObject.getLocalTranslation().x != position.getX() || thisObject.getLocalTranslation().y != position.getY())
//        thisObject.setLocalTranslation(position.getX(), position.getY(), 0f);
      Spatial thisObject = prsMapper.get(e).getSpatial();
      if (thisObject.getLocalTranslation().x != position.getX() || thisObject.getLocalTranslation().y != position.getY())
        thisObject.setLocalTranslation(position.getX(), position.getY(), 0f);
      
    }
  }

  @Override
  protected void removed(Entity e) {
    prsMapper.get(e).getSpatial().removeFromParent();

  }

  @Override
  protected boolean checkProcessing() {
    return true;
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void inserted(Entity e) {

    Position position = pm.get(e);
    Box box = new Box(1, 1, 1);
    Geometry red = new Geometry("Box", box);
    //red.setLocalTranslation(new Vector3f(1, 3, 1));
    Material mat2 = new Material(SharedVars.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

    Random rand = new Random();

    float r = rand.nextFloat() * .5f + 0.5f;
    float g = rand.nextFloat() * .5f + 0.5f;
    float b = rand.nextFloat() * .5f + 0.5f;

    mat2.setColor("Color", new ColorRGBA(r, g, b, 1));
    red.setMaterial(mat2);
    
    e.addComponent(new PlatformRendererSpatial(red));
    
    //System.out.println("made this noob: " + e.getUuid());

    red.move(position.getX(), position.getY(), 0);

    SharedVars.rootNode.attachChild(red);

  }
}
