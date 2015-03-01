/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aftermidnight;

import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

/**
 *
 * @author bryant
 */
public class SharedVars
{
	public static AssetManager assetManager;
	public static Node rootNode;
	public static Node guiNode;
  public static AppStateManager appStateManager;
  public static boolean paused = false;
}
