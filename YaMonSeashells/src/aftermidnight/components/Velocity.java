package aftermidnight.components;

import com.artemis.Component;

public class Velocity extends Component {
	private float x;
	private float y;
  private float multiplier;

	public Velocity() {
	}

	public Velocity(float x, float y) {
		this.x = x;
		this.y = y;
    this.multiplier = 1f;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

  public float getMultiplier() {
    return multiplier;
  }
  
	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

  public void setMultiplier(float multiplier) {
		this.multiplier = multiplier;
	}

	public void setVelocity(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void addX(float x) {
		this.x += x;
	}

	public void addY(float y) {
		this.y += y;
	}
}
