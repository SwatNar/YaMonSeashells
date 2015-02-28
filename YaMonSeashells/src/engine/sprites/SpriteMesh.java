package engine.sprites;

import engine.sprites.atlas.TextureAtlas;
import engine.sprites.atlas.AtlasSprite;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Format;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.VertexBuffer.Usage;
import com.jme3.texture.Texture;

import engine.util.ConvertionUtilities;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *  computer hardware limitation: one spriteMesh can have only 1 textureAtlas,
 *  because 1 mesh has only 1 texture. And a texture atlas has limited size.
 *  So in order to bypass this we have multiple spriteMeshes.
 *  SpriteManager will choose the corresponding spriteMesh depending on sprite "image" used.
 */
public class SpriteMesh
{
   private AssetManager assetManager;
   private Node rootNode;
   private TextureAtlas atlas;
   private SpriteManager manager;
   private Mesh mesh;
   private Geometry spriteGeometry;
   private ArrayList<Sprite> sprites;
   private LinkedList<Sprite> deletedSprites = new LinkedList<Sprite>();
   private final Strategy strategy;
   private boolean isStatic;
   private boolean trim = false;
   private boolean texBufferChanged = true;
   private boolean sizeBufferChanged = true;
   private boolean positionBufferChanged = true;
   private boolean colorBufferChanged = true;
   private static float BUFFER_SIZE = 1.5f;                       // for KEEP_BUFFER strategy: New Buffer size = 1.5 x total sprites = 50% bigger than asked.
   private static final boolean DISABLE_OPTIMIZATIONS = false;    // for fps benchmark tests.

   public enum Strategy
   {
      /**
       * When number of sprites is changed it will allocate a new buffer. Its like a KEEP_BUFFER that spams trim every frame.
       *  pros: 100% recommented for static sprites, since they will have the exact correct capacity size, and you almost never add or delete them.
       *  cons: Extreme memory requirements when adding or deleting sprites. Example: TestAllocateNewBufferMemoryGoesCrazy
       */
      ALLOCATE_NEW_BUFFER,
      /**
       * When number of sprites is changed buffer will not increase, instead binds a big array 
       * and deleted sprites are rendered at infinity position(to hide them under the carpet).
       *  Pros : doesnt require too much memory.
       *  Cons : render speed depends on capacity of buffer.
       *         for example if you add 1.000.000 sprites and then delete them,
       *         the buffer will still have capacity 1.000.000, which will mean that even 1 sprite is rendered as slow as 1.000.000.
       *         trim() will resize him to have min size.
       *  Cons2: If spriteManager has small (maxWidth, maxHeight) and is forced to create more than 1 spriteMeshes
       *         it will have bad fps. This is because even if sprites are "deleted" or moved, the capacity remains the same.
       */
      KEEP_BUFFER
   }

   public SpriteMesh(Strategy strategy, boolean isStatic, Node rootNode, AssetManager assetManager)
   {
      this.strategy = strategy;
      this.rootNode = rootNode;
      this.assetManager = assetManager;
      this.isStatic = isStatic;
      sprites = new ArrayList<Sprite>();
      mesh = new Mesh();
      mesh.setMode(Mesh.Mode.Points);
   }

   public void add(Sprite sprite)
   {
      if (deletedSprites.isEmpty())
      {
         addSprite(sprite);
      }
      else
      {
         Sprite deleted = deletedSprites.pop();
         setSprite(deleted.getPositionInArray(), sprite);
      }
   }

   protected void addDeletedSprite(Sprite sprite)
   {
      deletedSprites.add(sprite);
   }

   private void addSprite(Sprite sprite)
   {
      assert (!sprite.isDeleted()) : "Cannot add deleted sprites";
      assert (atlas != null) : "Use spriteMesh.setAtlas() before using add.";
      assert (sprite.getImage().getWhereToPut() == this && sprite.getImage().getSprite().getAtlas() == atlas) : "Image/AtlasSprite requested doesnt belong in this TextureAtlas.";

      sprite.setPositionInArray(sprites.size());
      sprites.add(sprite);
      setChanged(true);
   }

   /** you can put unlimited sprites, but not atlasSprites because of limited texture size. */
   public boolean canAdd(AtlasSprite sprite)
   {
      return atlas.canAdd(sprite);
   }

   /**
    * Avoid java.nio.BufferOverflowException by resizing arrays.
    * @return -1: if it should stay same, else the newCapacity.
    * @return -2: if it shouldnt be initialised / updated at all.
    */
   private int findNewBufferResize()
   {
      int capacity = getCapacity();
      int minCapacity = sprites.size();
      if ((capacity == 0) && (minCapacity == 0)) return -2;

      switch (strategy)
      {
         case ALLOCATE_NEW_BUFFER:
            assert (deletedSprites.isEmpty());
            if (capacity == minCapacity) return -1;         // if it doesnt need to change.
            else return minCapacity;
         case KEEP_BUFFER:
            if (trim && capacity != minCapacity) return minCapacity;
            else if (capacity >= minCapacity) return -1;    // if it doesnt need to change.
            else return (int) (minCapacity * BUFFER_SIZE);
         default:
            assert (false) : "unknown strategy";
            return -2;
      }
   }

   public TextureAtlas getAtlas()
   {
      return atlas;
   }

   public int getCapacity()
   {
      VertexBuffer vb = mesh.getBuffer(VertexBuffer.Type.Position);
      if (vb == null) return 0;
      return vb.getNumElements();
   }

   public List<Sprite> getDeletedSprites()
   {
      return Collections.unmodifiableList(deletedSprites);
   }

   public Geometry getGeometry()
   {
      return spriteGeometry;
   }

   public SpriteManager getManager()
   {
      return manager;
   }

   public Mesh getMesh()
   {
      return mesh;
   }

   public List<Sprite> getSprites()
   {
      return Collections.unmodifiableList(sprites);
   }

   private void initGeometry(Texture texture)
   {
      spriteGeometry = new Geometry("SpriteMesh", mesh);
      Material mat = new Material(assetManager, "3d/shaders/Particle.j3md");
      mat.setTexture("Texture", texture);
      spriteGeometry.setMaterial(mat);
      rootNode.attachChild(spriteGeometry);
   }

   public boolean isStatic()
   {
      return isStatic;
   }

   public void printBuffers()
   {
      System.out.println("Position: " + mesh.getBuffer(Type.Position).getData());
      ConvertionUtilities.print(mesh.getBuffer(Type.Position).getData(), 3);
      System.out.println("Color: " + mesh.getBuffer(Type.Color).getData());
      ConvertionUtilities.print(mesh.getBuffer(Type.Color).getData(), 4);
      System.out.println("Size: " + mesh.getBuffer(Type.Size).getData());
      ConvertionUtilities.print(mesh.getBuffer(Type.Size).getData(), 1);
      System.out.println("TexCoord: " + mesh.getBuffer(Type.TexCoord).getData());
      ConvertionUtilities.print(mesh.getBuffer(Type.TexCoord).getData(), 4);
   }

   public void rebuildAtlas()
   {
      setAtlas(atlas);
   }

   protected void removeDeletedSprites()
   {
      if (deletedSprites.isEmpty()) return;
      sprites.removeAll(deletedSprites);
      deletedSprites.clear();
      updateSpritePositionsInArray();
   }

   public void setAtlas(TextureAtlas atlas)
   {
      if (atlas.needsToBeBuild())
      {
         atlas.build();
         this.atlas = atlas;
         setTexture(atlas.getAtlasTexture());
      }
      else if (this.atlas != atlas)
      {
         this.atlas = atlas;
         setTexture(atlas.getAtlasTexture());
      }
   }

   protected void setChanged(boolean changed)
   {
      positionBufferChanged = changed;
      colorBufferChanged = changed;
      sizeBufferChanged = changed;
      texBufferChanged = changed;
   }

   protected void setColorBufferChanged(boolean colorBufferChanged)
   {
      this.colorBufferChanged = colorBufferChanged;
   }

   protected void setManager(SpriteManager manager)
   {
      this.manager = manager;
   }

   protected void setPositionBufferChanged(boolean positionBufferChanged)
   {
      this.positionBufferChanged = positionBufferChanged;
   }

   protected void setSizeBufferChanged(boolean sizeBufferChanged)
   {
      this.sizeBufferChanged = sizeBufferChanged;
   }

   protected void setSprite(int pos, Sprite sprite)
   {
      assert (!sprite.isDeleted()) : "Cannot add deleted sprites";
      assert (atlas != null) : "Use spriteMesh.setAtlas() before using add.";
      assert (sprite.getImage().getWhereToPut() == this && sprite.getImage().getSprite().getAtlas() == atlas) : "Image/AtlasSprite requested doesnt belong in this TextureAtlas.";

      sprite.setPositionInArray(pos);
      sprites.set(pos, sprite);
      setChanged(true);
   }

   protected void setTexBufferChanged(boolean texBufferChanged)
   {
      this.texBufferChanged = texBufferChanged;
   }

   protected void setTexture(Texture texture)
   {
      if (texture != null)
      {
         if (spriteGeometry == null)
         {
            initGeometry(texture);
         }
         else spriteGeometry.getMaterial().setTexture("Texture", texture);
      }
   }

   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      sb.append("SpriteMesh( id=").append(this.hashCode());
      if (!sprites.isEmpty()) sb.append(" sprites=").append(sprites);
      if (!deletedSprites.isEmpty()) sb.append(" deleted=").append(deletedSprites);
      sb.append(" )");
      return sb.toString();
   }

   /** resizes buffer to min possible size on next update(). */
   public void trim()
   {
      if (strategy != Strategy.KEEP_BUFFER) return;
      removeDeletedSprites();
      trim = true;
   }

   private void updateBuffer(Buffer assignNewBuffer, Type type, int components, Format format, boolean normalise)
   {
      if (DISABLE_OPTIMIZATIONS || trim || (positionBufferChanged && (type == Type.Position)) || (colorBufferChanged && (type == Type.Color)) || (sizeBufferChanged && (type == Type.Size)) || (texBufferChanged && (type == Type.TexCoord)))
      {
         boolean changeCapacity = (assignNewBuffer != null);
         VertexBuffer vertex = mesh.getBuffer(type);
         Buffer buf;

         if (changeCapacity) buf = assignNewBuffer;
         else buf = vertex.getData();
         buf.rewind();
         for (Sprite sprite : sprites)
         {
            switch (type)
            {
               case Position:
                  Vector3f position = sprite.getPosition();
                  ((FloatBuffer) buf).put(position.x).put(position.y).put(position.z);
                  break;
               case Color:
                  ((ByteBuffer) buf).putInt(sprite.getColor().asIntABGR());
                  break;
               case Size:
                  ((FloatBuffer) buf).put(sprite.getSize());
                  break;
               case TexCoord:
                  ((FloatBuffer) buf).put(sprite.getStartU()).put(sprite.getStartV()).put(sprite.getEndU()).put(sprite.getEndV());
                  break;
            }
         }
         if (vertex == null)    // initialise
         {
            vertex = new VertexBuffer(type);
            Usage usage = Usage.Stream;
            if (isStatic) usage = Usage.Static;
            vertex.setupData(usage, components, format, buf);
            if (normalise) vertex.setNormalized(true);
            mesh.setBuffer(vertex);
         }
         else if (changeCapacity)
         {
            vertex.updateData(buf);
            mesh.clearBuffer(type);
            mesh.setBuffer(vertex);
         }
         else
         {
            vertex.updateData(buf);
         }
      }
   }

   public void updateMesh(float tpf)
   {
      rebuildAtlas();
      updateSpriteAnimation(tpf);
      if (strategy == Strategy.ALLOCATE_NEW_BUFFER) removeDeletedSprites();

      int changeCapacity = findNewBufferResize();
      if (changeCapacity == -2) return;    // if it shouldn't be updated.
      else if (changeCapacity == -1)       // if should stay same size.
      {
         updateBuffer(null, Type.Position, 3, Format.Float, false);
         updateBuffer(null, Type.Color, 4, Format.UnsignedByte, true);
         updateBuffer(null, Type.Size, 1, Format.Float, false);
         updateBuffer(null, Type.TexCoord, 4, Format.Float, false);
      }
      else
      {
         updateBuffer(ConvertionUtilities.createFloatBuffer(changeCapacity * 3), Type.Position, 3, Format.Float, false);
         updateBuffer(ConvertionUtilities.createByteBuffer(changeCapacity * 4), Type.Color, 4, Format.UnsignedByte, true);
         updateBuffer(ConvertionUtilities.createFloatBuffer(changeCapacity), Type.Size, 1, Format.Float, false);
         updateBuffer(ConvertionUtilities.createFloatBuffer(changeCapacity * 4), Type.TexCoord, 4, Format.Float, false);
      }
      if (DISABLE_OPTIMIZATIONS || positionBufferChanged || sizeBufferChanged) spriteGeometry.updateModelBound();
      setChanged(false);
      trim = false;
   }

   public void updateSpriteAnimation(float tpf)
   {
      if (isStatic) return;
      for (Sprite sprite : sprites) sprite.updateAnimation(tpf);
   }

   protected void updateSpritePositionsInArray()
   {
      for (int i = 0; i < sprites.size(); i++) sprites.get(i).setPositionInArray(i);
   }

   public Strategy getStrategy()
   {
      return strategy;
   }
   
  //un tested
     public void rotate(float a, float b, float c)
   {
      spriteGeometry.rotate(a,b,c);

   }
   
   public void scale(float myf)
   {
      spriteGeometry.scale(myf);

   }

      
}
