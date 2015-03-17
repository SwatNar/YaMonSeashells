/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aftermidnight.systems;

import aftermidnight.SharedVars;
import aftermidnight.components.MapComponent;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import engine.sprites.Sprite;
import engine.sprites.SpriteAnimation;
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
  ComponentMapper<MapComponent> mapMapper;
  ArrayList<SpriteImage[]> spriteImageList = new ArrayList<SpriteImage[]>();
  //ArrayList<AnimationFrame> animations = new ArrayList<AnimationFrame>();
  
  private SpriteManager spriteManager;
  private Sprite[][] sprites;
  private float z = -0f;

  @SuppressWarnings("unchecked")
  public MapRenderer() {
    super(Aspect.getAspectForAll(MapComponent.class));
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

  public void loadSpriteSheet(String spriteSheet, int numSpritesX, int numSpritesY, String transparentColor) {
    spriteSheet = spriteSheet.replaceAll("\\.\\.\\/", ""); // to fix how Tiled exports the path to the tiles
    System.out.println("loading spritesheet " + spriteSheet);

    //18x30
    // LOAD THESE FROM JSON
    Color COLOR_TO_MAKE_TRANSPARENT = Color.decode(transparentColor);


    //int numSpritesX = 16;
    //int numSpritesY = 13;

    BufferedImage image = ImageUtilities.loadImage(spriteSheet, SharedVars.assetManager);
    BufferedImage transparentImage = ImageUtilities.transformColorToTransparency(image, COLOR_TO_MAKE_TRANSPARENT);
    BufferedImage[][] split = ImageUtilities.split(transparentImage, numSpritesX, numSpritesY);

    for (int y = 0; y < numSpritesY; y++) {
      for (int x = 0; x < numSpritesX; x++) {
        //spriteImageList.add(spriteManager.createSpriteImage(split[x][y], false));
      }
    }
  }
  
  public void addSpriteImage(SpriteImage si)
  {
    addSpriteImage(new SpriteImage[] { si } );
    
  }
  
  public void addSpriteImage(SpriteImage[] si)
  {
    spriteImageList.add(si);
    
  }

  
  public void loadSpriteSheet(JsonObject jsOb) {

    //return jsonObject.getAsJsonArray("tilesets").get(0).getAsInt();
//    System.out.println("loadSpriteSheet:\n\n" + jsOb + "\n\n\n");


    JsonArray jsAr = jsOb.getAsJsonArray("tilesets");
    for (int k = 0; k < jsAr.size(); k++) {
      JsonObject jsOb2 = new Gson().fromJson(jsAr.get(k), JsonObject.class);

      String spriteSheet = jsOb2.get("image").getAsString();
      int numSpritesX = jsOb2.get("imagewidth").getAsInt() / jsOb2.get("tilewidth").getAsInt();
      int numSpritesY = jsOb2.get("imageheight").getAsInt() / jsOb2.get("tileheight").getAsInt();
      String transparentColor = jsOb2.get("transparentcolor").getAsString();

      spriteSheet = spriteSheet.replaceAll("\\.\\.\\/", ""); // to fix how Tiled exports the path to the tiles
      System.out.println("loading spritesheet " + spriteSheet);

      Color COLOR_TO_MAKE_TRANSPARENT = Color.decode(transparentColor);

      BufferedImage image = ImageUtilities.loadImage(spriteSheet, SharedVars.assetManager);
      BufferedImage transparentImage = ImageUtilities.transformColorToTransparency(image, COLOR_TO_MAKE_TRANSPARENT);
      BufferedImage[][] split = ImageUtilities.split(transparentImage, numSpritesX, numSpritesY);

      for (int y = 0; y < numSpritesY; y++) {
        for (int x = 0; x < numSpritesX; x++) {
          
          addSpriteImage(spriteManager.createSpriteImage(split[x][y], false));
        }
      }

      JsonObject jsOb3 = new Gson().fromJson(jsOb2.get("tiles"), JsonObject.class);
      for (int abc = 0; abc < numSpritesX * numSpritesY; abc++) {
        if (jsOb3.get(Integer.toString(abc)) != null) {
          JsonObject jsOb4 = new Gson().fromJson(jsOb3.get(Integer.toString(abc)), JsonObject.class);
          loadSpriteAnimation(abc, jsOb4);
        }
      }
    }
  }

  public void loadSpriteAnimation(int tile, JsonObject jsOb)
  {
    
    System.out.println("TILE " + tile + " HAS ANIMATIONS: ");
    JsonArray jsAr = jsOb.get("animation").getAsJsonArray();
    SpriteImage[] imageList = new SpriteImage[jsAr.size()];
    float duration = .1f;
    for (int x = 0; x < jsAr.size(); x++)
    {
      //AnimationFrame animframe = new Gson().fromJson(jsAr.get(x), AnimationFrame.class);
      //animframe.parenttile = tile;
      //animations.add(animframe);
      JsonObject jsOb_frame = new Gson().fromJson(jsAr.get(x), JsonObject.class);
      //System.out.println(jsOb_frame.get("duration") + " ms tile " + jsOb_frame.get("tileid"));
      duration = jsOb_frame.get("tileid").getAsFloat() / 1000f;
      //spriteImageList.get
      imageList[x] = spriteImageList.get(jsOb_frame.get("tileid").getAsInt())[0];
      
    }
    
    //Sprite spr = new Sprite(new SpriteAnimation(imageList, duration));
    spriteImageList.set(tile, imageList);
  }
  
  @Override
  protected void inserted(Entity e) {
    // Load these numbers from json

    float tilesize = 1f;
    float position_offset = .7f;

    /* for parallel projection off
     float tilesize = 1f;
     float position_offset = .8f;
     */

    MapComponent m = mapMapper.getSafe(e);
    if (m != null) {

      loadSpriteSheet(m.jsonObject);
      sprites = new Sprite[m.getWidth()][m.getHeight()];
//      int x = 0; int y = 1;
      for (int x = 0; x < m.getWidth(); x++) {
        for (int y = 0; y < m.getHeight(); y++) {


          int tile = m.getTile(x, y);

          if (tile > 0) {
            tile--;
            sprites[x][y] = new Sprite(new SpriteAnimation(spriteImageList.get(tile), .1f));
            sprites[x][y].setPosition(x * tilesize * position_offset, -y * tilesize * position_offset, z);
            sprites[x][y].setSize(tilesize * 1f);

          }
        }
      }
    }
  }

  @Override
  protected void removed(Entity e) {
//    srsMapper.get(e).sprite.delete();
  }
  
  public class AnimationFrame {
    public int parenttile;
    public int duration;
    public int tileid; 
  }
}
