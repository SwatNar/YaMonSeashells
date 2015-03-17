/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aftermidnight.components;

import com.artemis.Component;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

public class MapComponent extends Component {

  public Map map;
  public JsonObject jsonObject;

  public String toString() {
    return "tldr";

  }

  public MapComponent() {
    this("assets/daniel/maps/z2e mountain.json");

  }

  public MapComponent(String map) {
    System.out.println("MapComponent loading " + map);
    this.load(map);

  }

  public Tileset getTileset(int which) {
    Gson gs = new Gson();
    Tileset ts = gs.fromJson(jsonObject.getAsJsonArray("tilesets").get(which), Tileset.class);
    return ts;

  }

  public int getWidth() {
    return map.width;
  }

  public int getHeight() {
    return map.height;
  }

  public int getTile(int x, int y) {
    return map.layers.get(0).data.get(y * map.width + x);

  }

  public void load(String filename) {

    Gson gson = new Gson();

    try {
      BufferedReader br = new BufferedReader(new FileReader(filename));

      StringBuilder builder = new StringBuilder();
      String aux = "";

      while ((aux = br.readLine()) != null) {
        builder.append(aux);
      }

      String json = builder.toString();
      jsonObject = new JsonParser().parse(json).getAsJsonObject();

      map = gson.fromJson(json, Map.class);
    } catch (Exception ex) {
      System.out.println("MAP COMPONENT EXCEPTION: " + ex.toString());
    }
  }

  public class Map {

    public int height;
    public int width;
    public List<Layer> layers;
    public List<Tileset> tilesets;

    public String toString() {
      return "height: " + height + ", width: " + width + " and " + layers.size() + "layers";
    }
  }

  public class Layer {

    public List<Integer> data;
  }

  public class Tileset {

    public String image;
    public int tilewidth;
    public int tileheight;
    public int imagewidth;
    public int imageheight;
    public String transparentcolor;
    //public ArrayList<Tiles> tiles;

    public class Tiles {

      public class Animation {
      }
    }
  }
}
