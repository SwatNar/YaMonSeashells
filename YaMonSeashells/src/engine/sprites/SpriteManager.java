package engine.sprites;

import engine.sprites.atlas.SimpleTextureAtlas;
import engine.sprites.atlas.AtlasSprite;
import com.jme3.app.state.AbstractAppState;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

import engine.sprites.SpriteMesh.Strategy;
import engine.util.ImageUtilities;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SpriteManager extends AbstractAppState
{
   private AssetManager assetManager;
   private int maxAtlasHeight;
   private int maxAtlasWidth;
   private Node rootNode;
   private ArrayList<SpriteMesh> spriteMeshes = new ArrayList<SpriteMesh>();
   private HashMap<String, SpriteImage> images = new HashMap<String, SpriteImage>();
   private Strategy strategy;

   public SpriteManager(int maxAtlasWidth, int maxAtlasHeight, Strategy strategy, Node rootNode, AssetManager assetManager)
   {
      this.strategy = strategy;
      this.maxAtlasWidth = maxAtlasWidth;
      this.maxAtlasHeight = maxAtlasHeight;
      this.rootNode = rootNode;
      this.assetManager = assetManager;
   }

   private SpriteMesh createSpriteMesh(boolean isStatic)
   {
      if (spriteMeshes.size() > 1) System.out.println("Warning: SpriteManager is forced to create an extra spriteMesh a) maxAtlasWidth=" + maxAtlasWidth + " maxAtlasHeight=" + maxAtlasHeight + " is small \nb) too many DIFFERENT atlasSprites. Total:" + images.size() + " c) total spriteMeshes=" + spriteMeshes.size() + ((strategy == Strategy.KEEP_BUFFER) ? " d) KEEP_BUFFER strategy is slow at this condition." : ""));
      SpriteMesh mesh = new SpriteMesh(strategy, isStatic, rootNode, assetManager);
      mesh.setAtlas(new SimpleTextureAtlas(maxAtlasWidth, maxAtlasHeight));
      mesh.setManager(this);
      spriteMeshes.add(mesh);
      return mesh;
   }

   public SpriteImage createSpriteImage(AtlasSprite image, boolean createAsStatic)
   {
      return createSpriteImage(image.getId(), image, createAsStatic);
   }

   public SpriteImage createSpriteImage(BufferedImage bufferedImage, boolean createAsStatic)
   {
      return createSpriteImage(new AtlasSprite(bufferedImage), createAsStatic);
   }

   public SpriteImage createSpriteImage(String imagePath, boolean createAsStatic)
   {
      return createSpriteImage(imagePath, null, createAsStatic);
   }

   private SpriteImage createSpriteImage(String imageId, AtlasSprite image, boolean createAsStatic)
   {
      SpriteImage atlasRecord = images.get(imageId);
      boolean newAtlasRecord = (atlasRecord == null);
      if (newAtlasRecord)
      {
         assert(assetManager != null) : "AssetManager is null. Please do new SpriteManager(...); at simpleInitApp() method.";
         if (image == null)
         {
            BufferedImage bufferedImage = ImageUtilities.loadImage(imageId, assetManager);
            image = new AtlasSprite(bufferedImage);
         }
         atlasRecord = new SpriteImage(image, findFreePosition(image, createAsStatic));
         atlasRecord.getWhereToPut().getAtlas().add(image);
         images.put(imageId, atlasRecord);
      }

      return atlasRecord;
   }

   private SpriteMesh findFreePosition(AtlasSprite sprite, boolean createAsStatic)
   {
      assert(sprite.getWidth() <= maxAtlasWidth && sprite.getHeight() <= maxAtlasHeight):"AtlasSprite is too big to be put in any texture. Sprite (" + sprite.getWidth() + ", " + sprite.getHeight() + ") cannot be added to atlas (" + maxAtlasWidth + ", " + maxAtlasHeight + ")";

      // find where to place.
      for (SpriteMesh mesh : spriteMeshes)
      {
         if ((mesh.isStatic() == createAsStatic) && mesh.canAdd(sprite))
         {
            return mesh;
         }
      }

      // if not enough size in any spriteMesh create new spriteMesh.
      return createSpriteMesh(createAsStatic);
   }

   public SpriteImage getAtlasSpriteRecord(String name)
   {
      return images.get(name);
   }

   public List<SpriteMesh> getSpriteMeshes()
   {
      return Collections.unmodifiableList(spriteMeshes);
   }

   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      sb.append("SpriteManager( strategy=").append(strategy.toString()).append(" maxWidth=").append(maxAtlasWidth).append(" maxHeight=").append(maxAtlasHeight).append(" spriteMeshes=").append(spriteMeshes);
      return sb.toString();
   }

   public void trim()
   {
      for (SpriteMesh mesh : spriteMeshes) mesh.trim();
   }

   @Override
   public void update(float tpf)
   {
      super.update(tpf);
      for (SpriteMesh mesh : spriteMeshes) mesh.updateMesh(tpf);
   }
}