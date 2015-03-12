/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aftermidnight.components;

import aftermidnight.SharedVars;
import com.artemis.Component;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class PlatformRendererParticleEmitter extends Component {
	public ParticleEmitter particleEmitter;

  public PlatformRendererParticleEmitter (ParticleEmitter particleEmitter)
	{
		this.particleEmitter = particleEmitter;
	}
  
  public PlatformRendererParticleEmitter ()
	{
		this.particleEmitter = this.fire();
	}

	public ParticleEmitter getParticleEmitter() {
		return particleEmitter;
	}

	public void setParticleEmitter(ParticleEmitter particleEmitter) {
		this.particleEmitter = particleEmitter;
	}
  
  public void setPosition(float x, float y, float z)
  {
   this.particleEmitter.setLocalTranslation(x,y,z);
  }
  
  public static ParticleEmitter fire()
  {
        Material material = new Material(SharedVars.assetManager, "Common/MatDefs/Misc/Particle.j3md");
        material.setTexture("Texture", SharedVars.assetManager.loadTexture("Effects/Explosion/flame.png"));
        material.setFloat("Softness", 3f); // 
        ParticleEmitter particleEmitter = new ParticleEmitter("Fire", ParticleMesh.Type.Triangle, 30);
        particleEmitter.setMaterial(material);
        particleEmitter.setShape(new EmitterSphereShape(Vector3f.ZERO, 0.1f));
        particleEmitter.setImagesX(2);
        particleEmitter.setImagesY(2); // 2x2 texture animation
        particleEmitter.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f)); // red
        particleEmitter.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
        particleEmitter.setStartSize(0.6f);
        particleEmitter.setEndSize(0.01f);
        particleEmitter.setGravity(0, -0.3f, 0);
        particleEmitter.setLowLife(0.5f);
        particleEmitter.setHighLife(3f);
        
        return particleEmitter;
  }
  
  public static ParticleEmitter smoke()
  {
        Material material = new Material(SharedVars.assetManager, "Common/MatDefs/Misc/Particle.j3md");
        material.setTexture("Texture", SharedVars.assetManager.loadTexture("Effects/Explosion/flame.png"));
        material.setFloat("Softness", 3f); // 
        ParticleEmitter smoke = new ParticleEmitter("Smoke", ParticleMesh.Type.Triangle, 30);
        smoke.setMaterial(material);
        smoke.setShape(new EmitterSphereShape(Vector3f.ZERO, 5));
        smoke.setImagesX(1);
        smoke.setImagesY(1); // 2x2 texture animation
        smoke.setStartColor(new ColorRGBA(0.1f, 0.1f, 0.1f,1f)); // dark gray
        smoke.setEndColor(new ColorRGBA(0.5f, 0.5f, 0.5f, 0.3f)); // gray      
        smoke.setStartSize(3f);
        smoke.setEndSize(5f);
        smoke.setGravity(0, -0.001f, 0);
        smoke.setLowLife(100f);
        smoke.setHighLife(100f);
        smoke.setLocalTranslation(0, 0.1f, 0);        
        smoke.emitAllParticles();
        
        return smoke;
  }
}
