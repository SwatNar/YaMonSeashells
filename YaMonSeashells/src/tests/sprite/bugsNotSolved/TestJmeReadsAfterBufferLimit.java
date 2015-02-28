package tests.sprite.bugsNotSolved;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Format;
import com.jme3.scene.VertexBuffer.Usage;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

//not using this implementation because jme reads after buffer limit.
public class TestJmeReadsAfterBufferLimit extends SimpleApplication
{
   public static class SimpleSprite
   {
      public Vector3f position;
      public float size = 1f;
      public ColorRGBA color ;
      public float startX = 1f;
      public float startY = 1f;
      public float endX = 0f;
      public float endY = 0f;
   }
   
   Mesh mesh ;
   SimpleSprite[] sprites = new SimpleSprite[3000];
   private int spritesNum = 300;//3000;
   Texture tex ;
   Material mat;

   public static void main(String[] args)
   {
      TestJmeReadsAfterBufferLimit app = new TestJmeReadsAfterBufferLimit();
      app.start();
   }

   @Override
   public void simpleInitApp()
   {
      getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
      getFlyByCamera().setMoveSpeed(50);
      mesh = createSpriteMesh(sprites);

      float min = 0;
      float max = 3;
      for(int i=0; i< sprites.length; i++)
      {
         sprites[i] = new SimpleSprite();
         sprites[i].position = new Vector3f(min+(float)(Math.random()*max),min+(float)(Math.random()*max),min+(float)(Math.random()*max));
         sprites[i].color = ColorRGBA.White;
      }
      updateParticleData(mesh,sprites);

      Geometry geom = new Geometry("", mesh);
      mat = new Material(assetManager, "3d/shaders/Particle.j3md");
      tex = assetManager.loadTexture("2d/npc/npc0.png");
      tex.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
      tex.setMagFilter(Texture.MagFilter.Nearest);
      geom.setMaterial(mat);
      mat.setTexture("Texture", tex);

      rootNode.attachChild(geom);
   }

   int a = 0;
   @Override
   public void simpleUpdate(float tpf)
   {
      super.simpleUpdate(tpf);
      sprites[ (int)(spritesNum*Math.random())].position.x+=tpf;
      updateParticleData(mesh, sprites);
   }
   
   public void updateParticleData(Mesh mesh, SimpleSprite[] particles)
   {
      VertexBuffer pvb = mesh.getBuffer(VertexBuffer.Type.Position);
      FloatBuffer positions = (FloatBuffer) pvb.getData();

      VertexBuffer cvb = mesh.getBuffer(VertexBuffer.Type.Color);
      ByteBuffer colors = (ByteBuffer) cvb.getData();

      VertexBuffer svb = mesh.getBuffer(VertexBuffer.Type.Size);
      FloatBuffer sizes = (FloatBuffer) svb.getData();

      VertexBuffer tvb = mesh.getBuffer(VertexBuffer.Type.TexCoord);
      FloatBuffer texcoords = (FloatBuffer) tvb.getData();

      // update data in vertex buffers
      positions.rewind();
      colors.rewind();
      sizes.rewind();
      texcoords.rewind();
      for (int i = 0; i < spritesNum; i++)
      {
         SimpleSprite p = particles[i];
         positions.put(p.position.x).put(p.position.y).put(p.position.z);
         colors.putInt(p.color.asIntABGR());
         sizes.put(p.size);
         texcoords.put(p.startX).put(p.startY).put(p.endX).put(p.endY);
      }
      positions.flip();
      colors.flip();
      sizes.flip();
      texcoords.flip();

      // force renderer to re-send data to GPU
      pvb.updateData(positions);
      cvb.updateData(colors);
      svb.updateData(sizes);
      tvb.updateData(texcoords);
      mesh.updateBound();
      mesh.updateCounts();
   }

   public static void initBuffer(Mesh mesh, VertexBuffer.Type type, Buffer pb, Usage usage, int components, Format format, boolean normalise)
   {
      VertexBuffer pvb = new VertexBuffer(type);
      pvb.setupData(usage, components, format, pb);
      if (normalise) pvb.setNormalized(true);

      VertexBuffer buf = mesh.getBuffer(type);
      if (buf != null)
      {
         buf.updateData(pb);
      }
      else
      {
         mesh.setBuffer(pvb);
      }
   }

   public Mesh createSpriteMesh(SimpleSprite[] sprites)
   {
      Mesh spriteMesh = new Mesh();
      int numParticles = sprites.length;
      spriteMesh.setMode(Mesh.Mode.Points);
      initBuffer(spriteMesh, VertexBuffer.Type.Position, BufferUtils.createFloatBuffer(numParticles*3), Usage.Stream, 3, Format.Float       , false );
      initBuffer(spriteMesh, VertexBuffer.Type.Color   , BufferUtils.createByteBuffer(numParticles*4) , Usage.Stream, 4, Format.UnsignedByte, true  );
      initBuffer(spriteMesh, VertexBuffer.Type.Size    , BufferUtils.createFloatBuffer(numParticles)  , Usage.Stream, 1, Format.Float       , false );
      initBuffer(spriteMesh, VertexBuffer.Type.TexCoord, BufferUtils.createFloatBuffer(numParticles*4), Usage.Stream, 4, Format.Float       , false );
      return spriteMesh;
   }
}