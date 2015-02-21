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
import engine.sprites.Sprite;
import engine.sprites.SpriteImage;
import engine.sprites.SpriteManager;

/**
 *
 * @author Daniel
 */
public class DebugPointRenderer extends EntitySystem {

	@Mapper ComponentMapper<Position> pm;
	
	@SuppressWarnings("unchecked")
	public DebugPointRenderer() {
		super( Aspect.getAspectForAll(Position.class) );
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
    // DRAW THE GFX
    //SpriteManager spriteManager = new SpriteManager(1024, 1024, strategy, rootNode, assetManager);
    //getStateManager().attach(spriteManager);

    //SpriteImage image = spriteManager.createSpriteImage("2d/npc/npc0.png", true);
    //Sprite sprite = new Sprite(image);
    //sprite.getPosition().x = 0f;
    //sprite.getPosition().y = 0f;
    //sprite.getPosition().z = 0f;
      
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

}
