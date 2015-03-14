/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aftermidnight.components;

import com.artemis.Component;

public class UserControllable extends Component {
	public boolean userControllable;

  public String toString()
  {
    return "usercontrollable: " + userControllable;
  
  }
	public UserControllable ( boolean userControllable)
	{
		this.userControllable = userControllable;
	}

 	public void flip() {
		userControllable = !userControllable;
	}
}
