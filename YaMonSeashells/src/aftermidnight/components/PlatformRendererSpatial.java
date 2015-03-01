/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aftermidnight.components;

import com.artemis.Component;
import com.jme3.scene.Spatial;

public class PlatformRendererSpatial extends Component {
	public Spatial spatial;

  public PlatformRendererSpatial (Spatial spatial)
	{
		this.spatial = spatial;
	}

	public Spatial getSpatial() {
		return spatial;
	}

	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}
}
