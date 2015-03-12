/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aftermidnight.components;

import aftermidnight.SharedVars;
import com.artemis.Component;
import com.google.gson.Gson;
import engine.sprites.SpriteImage;
import engine.sprites.SpriteManager;
import engine.util.ImageUtilities;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Map extends Component {

  public Layer map;

  public String toString() {
    return "tldr";

  }

  public Map() {
    System.out.println("loading this map");
    this.load("assets/daniel/maps/test.json");
//    this.loadSpriteSheet("2d/seasonal-tiles.png");
    
  }

  public String getTilesheet() {
    return map.tilesheet;
  }

  public int getWidth() {
    return map.width;
  }

  public int getHeight() {
    return map.height;
  }
  
  public int getTile(int x, int y) {
    return map.layer1.get(y * map.width + x);
    
  }

  public void load(String filename) {

    Gson gson = new Gson();
//    Response response = gson.fromJson(responseString, Response.class);
//    Type collectionType = new TypeToken<Collection<Integer>>(){}.getType();
//    Collection<Integer> ints2 = gson.fromJson(json, collectionType);

    try {
      BufferedReader br = new BufferedReader(new FileReader("assets/daniel/maps/test.json"));
      map = gson.fromJson(br, Layer.class);

    } catch (Exception ex) {
      System.out.println("MAP COMPONENT EXCEPTION: " + ex.toString());
    }
  }

  private class Layer {

    public int height;
    public int width;
    public List<Integer> layer1;
    public String tilesheet;
    

    public String toString() {
      return "height: " + height + ", width: " + width + ", layer: " + layer1.toString();
    }
  }
}
