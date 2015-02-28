/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aftermidnight;

import com.artemis.Component;

public class Position extends Component {
	private float x,y;
  public boolean rendered = false;
	
	public Position ( float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void addX(float x) {
		this.x += x;
	}
	
	public void addY(float y) {
		this.y += y;
	}
  
  public void setRendered()
  {
    rendered = true;
  }
  
}