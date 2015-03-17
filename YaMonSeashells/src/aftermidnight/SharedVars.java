/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aftermidnight;

import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import java.util.HashMap;
import java.util.Random;

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
  public static InputManager inputManager;
  public static Random random;
  public static Geometry dumbCollisionGlobal;
  public static float frustumSize = 1;
  
  public static Camera camera;
}

