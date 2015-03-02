/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aftermidnight.systems;

import aftermidnight.SharedVars;
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
import engine.util.FileUtilities;
import engine.util.ImageUtilities;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
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

      if (thisObject.getPosition().x != position.getX() || thisObject.getPosition().y != position.getY()) {
        thisObject.setPosition(position.getX(), position.getY(), 0f);
      }

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

    Color COLOR_TO_MAKE_TRANSPARENT = new Color(120, 195, 128);

    int numSpritesX = 14;
    int numSpritesY = 8;
    int numSubSpritesX = 1;
    int numSubSpritesY = 1;
    //int numSpriteSheets = (numSpritesX / numSubSpritesX) * (numSpritesY / numSubSpritesY);

    BufferedImage image = ImageUtilities.loadImage("kenny nl/sheets/sheet.png", SharedVars.assetManager);
    BufferedImage transparentImage = ImageUtilities.transformColorToTransparency(image, COLOR_TO_MAKE_TRANSPARENT);
    BufferedImage[][] split = ImageUtilities.split(transparentImage, numSpritesX, numSpritesY);
    //ImageUtilities.viewImage(ImageUtilities.merge(split));

    for (int x = 0; x < numSpritesX; x++) {
      for (int y = 0; y < numSpritesY; y++) {
         spriteImageList.add(spriteManager.createSpriteImage(split[x][y], false));
      }
    }

    SharedVars.appStateManager.attach(spriteManager);

  }

  @Override
  protected void inserted(Entity e) {
    Position position = positionMapper.get(e);
    
    Sprite sp = new Sprite(spriteImageList.get(SharedVars.random.nextInt(spriteImageList.size())));
    sp.setPosition(position.getX(), position.getY(), 0f);
    e.addComponent(new SpriteRendererSprite(sp));

  }
}
