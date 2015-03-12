/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aftermidnight.systems;

import aftermidnight.SharedVars;
import aftermidnight.components.Map;
import aftermidnight.components.Position;
import aftermidnight.components.SpriteRendererSprite;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
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
public class MapRenderer extends EntityProcessingSystem {

  @Mapper
  ComponentMapper<Map> mapMapper;
  ArrayList<SpriteImage> spriteImageList = new ArrayList<SpriteImage>();
  private SpriteManager spriteManager;
  private Sprite[][] sprites;

  @SuppressWarnings("unchecked")
  public MapRenderer() {
    super(Aspect.getAspectForAll(Map.class));
  }

  @Override
  protected void process(Entity e) {
//      Entity e = entities.get(i);
//      Position position = positionMapper.get(e);
//      Sprite thisObject = srsMapper.get(e).getSprite();
//    }
  }

  @Override
  protected boolean checkProcessing() {
    return true;
  }

  @Override
  protected void initialize() {

    // TODO: Optimize this

    spriteManager = new SpriteManager(1024, 1024, SpriteMesh.Strategy.KEEP_BUFFER, SharedVars.rootNode, SharedVars.assetManager);

    SharedVars.appStateManager.attach(spriteManager);


  }

  private void loadSpriteSheet(String spriteSheet) {

    //18x30
    // LOAD THESE FROM JSON
    Color COLOR_TO_MAKE_TRANSPARENT = new Color(157, 142, 136);
    int numSpritesX = 12;
    int numSpritesY = 6;

    BufferedImage image = ImageUtilities.loadImage(spriteSheet, SharedVars.assetManager);
    BufferedImage transparentImage = ImageUtilities.transformColorToTransparency(image, COLOR_TO_MAKE_TRANSPARENT);
    BufferedImage[][] split = ImageUtilities.split(transparentImage, numSpritesX, numSpritesY);


      for (int y = 0; y < numSpritesY; y++) {
    for (int x = 0; x < numSpritesX; x++) {

        spriteImageList.add(spriteManager.createSpriteImage(split[x][y], false));
      }
    }


  }

  @Override
  protected void inserted(Entity e) {
    // Load these numbers from json
    //float tilesize = .075f;
    float tilesize = .75f;
    
    Map m = mapMapper.getSafe(e);
    if (m != null)
    {
      loadSpriteSheet(m.getTilesheet());
      sprites = new Sprite[m.getWidth()][m.getHeight()];
      for (int x = 0; x < m.getWidth(); x++) {
        for (int y = 0; y < m.getHeight(); y++) {
          int tile = m.getTile(x,y);
          if (tile > 0)
          {
             tile--;
             sprites[x][y] = new Sprite(spriteImageList.get(tile));
             sprites[x][y].setPosition(x * tilesize, -y * tilesize, 0f);
             
          }
        }
      }
    }
  }

  @Override
  protected void removed(Entity e) {
//    srsMapper.get(e).sprite.delete();


  }
}
