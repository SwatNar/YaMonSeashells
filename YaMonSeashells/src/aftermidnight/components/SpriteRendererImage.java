/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aftermidnight.components;

import com.artemis.Component;
import engine.sprites.SpriteImage;

public class SpriteRendererImage extends Component {
	public SpriteImage spriteImage;

  public SpriteRendererImage (SpriteImage spriteImage)
	{
		this.spriteImage = spriteImage;
    System.out.println("made SpriteRendererImage");
	}

	public SpriteImage getSpriteImage() {
		return spriteImage;
	}

	public void setSpatial(SpriteImage spriteImage) {
		this.spriteImage = spriteImage;
	}
  

}
