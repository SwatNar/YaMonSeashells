/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aftermidnight.components;

import com.artemis.Component;
import engine.sprites.Sprite;
import engine.sprites.SpriteImage;

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
}
