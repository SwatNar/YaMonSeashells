/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aftermidnight.systems;

import aftermidnight.SharedVars;
import aftermidnight.components.PlatformRendererParticleEmitter;
import aftermidnight.components.Position;
import aftermidnight.components.SpriteRendererSprite;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import engine.sprites.Sprite;
import engine.sprites.SpriteImage;
import engine.sprites.SpriteManager;
import engine.sprites.SpriteMesh;
import engine.util.ImageUtilities;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Daniel
 */
public class SpriteRenderer extends EntitySystem {

  @Mapper
  ComponentMapper<Position> positionMapper;
  @Mapper
  ComponentMapper<SpriteRendererSprite> srsMapper;
  @Mapper
  ComponentMapper<PlatformRendererParticleEmitter> prpeMapper;
  private SpriteManager spriteManager;
  //private SpriteImage mySprite;
  ArrayList<SpriteImage> spriteImageList = new ArrayList<SpriteImage>();

  @SuppressWarnings("unchecked")
  public SpriteRenderer() {
    super(Aspect.getAspectForAll(Position.class));
  }

  @Override
  protected void processEntities(ImmutableBag<Entity> entities) {
    int s = entities.size();
    for (int i = 0; i < s; i++) {
      Entity e = entities.get(i);
      Position position = positionMapper.get(e);
      Sprite thisObject = srsMapper.get(e).getSprite();
      thisObject.setPosition(position.getX(), position.getY(), 0f);
    }
  }

  @Override
  protected boolean checkProcessing() {
    return true;
  }

  @Override
  protected void initialize() {

    // TODO: Optimize this

    spriteManager = new SpriteManager(1024, 1024, SpriteMesh.Strategy.KEEP_BUFFER, SharedVars.rootNode, SharedVars.assetManager);

    // Load these numbers from json
    loadSpriteSheet("2d/npc/refmap/vx_chara01_a.png");

    SharedVars.appStateManager.attach(spriteManager);


  }

  private void loadSpriteSheet(String spriteSheet) {

    
    // LOAD THESE FROM JSON
    Color COLOR_TO_MAKE_TRANSPARENT = new Color(120, 195, 128);
    //Color COLOR_TO_MAKE_TRANSPARENT = new Color(157, 142, 136);
    int numSpritesX = 12;
    int numSpritesY = 8;

    BufferedImage image = ImageUtilities.loadImage(spriteSheet, SharedVars.assetManager);
    BufferedImage transparentImage = ImageUtilities.transformColorToTransparency(image, COLOR_TO_MAKE_TRANSPARENT);
    BufferedImage[][] split = ImageUtilities.split(transparentImage, numSpritesX, numSpritesY);
    

    for (int x = 0; x < numSpritesY; x++) {
      for (int y = 0; y < numSpritesX; y++) {

        spriteImageList.add(spriteManager.createSpriteImage(split[y][x], false));
      }
    }


  }

  @Override
  protected void inserted(Entity e) {
    Position position = positionMapper.get(e);

    Sprite sp = new Sprite(spriteImageList.get(SharedVars.random.nextInt(spriteImageList.size())));
    sp.setPosition(position.getX(), position.getY(), 0f);
    e.addComponent(new SpriteRendererSprite(sp));

  }

  @Override
  protected void removed(Entity e) {
    srsMapper.get(e).sprite.delete();
            

  }
}
