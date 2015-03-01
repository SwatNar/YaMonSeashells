/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aftermidnight.components;

import com.artemis.Component;

public class Root extends Component {
	private boolean rooted;

  public String toString()
  {
    return "rooted: " + rooted;
  
  }
	public Root ( boolean rooted)
	{
		this.rooted = rooted;
	}

	public boolean getRooted() {
		return rooted;
	}

 	public void flip() {
		rooted = !rooted;
	}

	public void setPaused(boolean rooted) {
		this.rooted = rooted;
	}

}
