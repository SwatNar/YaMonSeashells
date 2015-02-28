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
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import engine.sprites.Sprite;
import engine.sprites.SpriteImage;
import engine.sprites.SpriteManager;
import engine.sprites.SpriteMesh;

/**
 *
 * @author Daniel
 */
public class DebugPointRenderer extends EntitySystem {

	@Mapper ComponentMapper<Position> pm;
  private SpriteManager spriteManager;
	private SpriteImage mySprite; 
	@SuppressWarnings("unchecked")
	public DebugPointRenderer() {
		super( Aspect.getAspectForAll(Position.class) );
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		int s = entities.size();
    //System.out.println("printing " + s + " entities.");
		  
		for (int i = 0 ; s > i; i++) {

      // i think i might be creating the entities 500000 times here, once every time process is run
    }
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}
  
  @Override
  protected void initialize() {
    spriteManager = new SpriteManager(1024, 1024, SpriteMesh.Strategy.KEEP_BUFFER, SharedVars.rootNode, SharedVars.assetManager);
    mySprite = spriteManager.createSpriteImage("2d/npc/npc0.png", true);
<<<<<<< HEAD
    SharedVars.appStateManager.attach(spriteManager);
    // where is the fucking run button while in fullscreen?
=======
    //getStateManager().attach(spriteManager);
>>>>>>> 1fe31e5d5fcc5d45b1ffbd1b4aef846a4bc18ddc
    
    System.out.println("loaded sprite manager");
  };
  
  @Override
  protected void inserted(Entity e) {
/*
    Box b = new Box(.1f, .1f, .1f); // create cube shape
    Geometry geom = new Geometry("Box", b);  // create cube geometry from the shape
    Material mat = new Material(SharedVars.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
    mat.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
    geom.setMaterial(mat);                   // set the cube's material
    Position position = pm.get(e);
    geom.move(position.getX(), position.getY(), 0f);
    SharedVars.rootNode.attachChild(geom);
<<<<<<< HEAD
*/
    //			Entity e = entities.get(i);
      Position position = pm.get(e);
    Box b = new Box(.1f, .1f, .1f); // create cube shape
    Geometry geom = new Geometry("Box", b);  // create cube geometry from the shape
    Material mat = new Material(SharedVars.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
    mat.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
    geom.setMaterial(mat);                   // set the cube's material
    geom.move(position.getX(), position.getY(), 0f);
    SharedVars.rootNode.attachChild(geom);              // make the cube appear in the scene


 Sprite sprite = new Sprite(mySprite);
      sprite.setPosition(position.getX(), position.getY(), 0f);
      

//    System.out.println("isnerted that hoe " + sprite.toString());
=======
  */
//			Entity e = entities.get(i);
      Position position = pm.get(e);

      Sprite sprite = new Sprite(mySprite);
      sprite.setPosition(position.getX(), position.getY(), 0f);

    System.out.println("isnerted that hoe");
>>>>>>> 1fe31e5d5fcc5d45b1ffbd1b4aef846a4bc18ddc
  }
}
