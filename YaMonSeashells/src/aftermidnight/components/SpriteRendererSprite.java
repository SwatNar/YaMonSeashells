/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aftermidnight.components;

import com.artemis.Component;
import engine.sprites.Sprite;

public class SpriteRendererSprite extends Component {
	public Sprite sprite;

  public SpriteRendererSprite (Sprite sprite)
	{
		this.sprite = sprite;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSpatial(Sprite sprite) {
		this.sprite = sprite;
	}
  
  public void rotate(float x, float y, float z)
  {
//    sprite.rotate(x, y, z);
//    System.out.println("rotating (" + x + ", " + y + ", " + z + ")!");
  }
}
