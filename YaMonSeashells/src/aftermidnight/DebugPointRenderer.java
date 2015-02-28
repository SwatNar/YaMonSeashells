/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aftermidnight;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import engine.sprites.SpriteImage;
import engine.sprites.SpriteManager;
import engine.sprites.SpriteMesh;

/**
 *
 * @author Daniel
 */
public class DebugPointRenderer extends EntitySystem {

  @Mapper
  ComponentMapper<Position> pm;
  private SpriteManager spriteManager;
  private SpriteImage mySprite;

  @SuppressWarnings("unchecked")
  public DebugPointRenderer() {
    super(Aspect.getAspectForAll(Position.class));
  }

  @Override
  protected void processEntities(ImmutableBag<Entity> entities) {
  }

  @Override
  protected boolean checkProcessing() {
    return true;
  }

  @Override
  protected void initialize() {
    spriteManager = new SpriteManager(1024, 1024, SpriteMesh.Strategy.KEEP_BUFFER, SharedVars.rootNode, SharedVars.assetManager);
    mySprite = spriteManager.createSpriteImage("2d/npc/npc0.png", true);
    SharedVars.appStateManager.attach(spriteManager);

    System.out.println("loaded sprite manager");
  }

  @Override
  protected void inserted(Entity e) {

  
  }
}
