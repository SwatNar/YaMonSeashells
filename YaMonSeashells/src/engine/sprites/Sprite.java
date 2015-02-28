package engine.sprites;

import com.jme3.math.ColorRGBA;
<<<<<<< HEAD
=======
import com.jme3.math.FastMath;
>>>>>>> 1fe31e5d5fcc5d45b1ffbd1b4aef846a4bc18ddc
import com.jme3.math.Vector3f;
import engine.sprites.SpriteMesh.Strategy;

public class Sprite {

  private static final Vector3f DO_NOT_SEE = new Vector3f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
  private int positionInArray = -1;
  private Vector3f position = new Vector3f();
  private float size = 1f;
  private boolean deleted = false;
  private ColorRGBA color = new ColorRGBA(ColorRGBA.White);
  private SpriteImage image;
  protected int currentFrame;
  /**
   * The frame currently being displayed
   */
  protected float remainingTime;
  protected SpriteAnimation currentAnimation;

  public boolean isAnimated() {
    return getCurrentAnimation() != null;
  }

  public void setAnimation(SpriteAnimation animation, int frameToStart) {
    animation.setAnimationData(this, frameToStart);
    setImage(animation.getCurrentFrame(this));
  }

  public void updateAnimation(float tpf) {
    if (getCurrentAnimation() == null || deleted) {
      return;
    }
    getCurrentAnimation().update(this, tpf);
  }

  private void initialiseSprite(SpriteImage image) {
    this.image = image;
    image.getWhereToPut().add(this);
  }

  private Sprite() {
  }

  public Sprite(SpriteImage image) {
    initialiseSprite(image);
  }

  public Sprite(SpriteAnimation animation, int frameToStart) {
    animation.setAnimationData(this, frameToStart);
    initialiseSprite(animation.getCurrentFrame(this));
  }

  public Sprite(SpriteAnimation animation) {
    this(animation, 0);
  }

  public SpriteMesh getSpriteMesh() {
    return image.getWhereToPut();
  }

  /**
   * Destroys this sprite.
   */
  public void delete() {
    assert !deleted : "Can't delete an already deleted sprite";
    assert (positionInArray != -1) : "Wtf sprite " + this + " was never in spriteMesh it claimed it was. It says it is on " + getSpriteMesh();
    deleted = true;
    if (getSpriteMesh().getStrategy() == Strategy.KEEP_BUFFER) {
      setPosition(DO_NOT_SEE);
    }
    getSpriteMesh().addDeletedSprite(this);
  }

  public ColorRGBA getColor() {
    return color;
  }

  protected float getEndU() {
    return image.getSprite().getEndU();
  }

  protected float getEndV() {
    return image.getSprite().getEndV();
  }

  public SpriteImage getImage() {
    return image;
  }

  public Vector3f getPosition() {
    return position;
  }

  public int getPositionInArray() {
    return positionInArray;
  }

  public float getSize() {
    return size;
  }

  protected float getStartU() {
    return image.getSprite().getStartU();
  }

  protected float getStartV() {
    return image.getSprite().getStartV();
  }

  public boolean isDeleted() {
    return deleted;
  }

  /**
   * @param color: passed by value.
   */
  public void setColor(ColorRGBA color) {
    this.color.set(color);
    getSpriteMesh().setColorBufferChanged(true);
  }

  public void setImage(SpriteImage image) {
    if (image.getWhereToPut() != getSpriteMesh()) {
      // leave an empty sprite in its place(sprite array).
      Sprite deletedSprite = new Sprite();
      deletedSprite.image = this.image;
      getSpriteMesh().setSprite(positionInArray, deletedSprite);
      deletedSprite.delete();
      this.image = image;
      image.getWhereToPut().add(this);
    } else {
      this.image = image;
      getSpriteMesh().setTexBufferChanged(true);
    }
  }

  /**
   * @param position: passed by value.
   */
  public void setPosition(Vector3f position) {
    setPosition(position.x, position.y, position.z);
  }

  public void setPosition(float x, float y, float z) {
    this.position.x = x;
    this.position.y = y;
    this.position.z = z;
    getSpriteMesh().setPositionBufferChanged(true);
  }

  protected void setPositionInArray(int positionInArray) {
    this.positionInArray = positionInArray;
  }

  public void setSize(float size) {
    this.size = size;
    getSpriteMesh().setSizeBufferChanged(true);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Sprite( mesh=").append(getSpriteMesh().hashCode()).append(" position=").append(positionInArray).append(" id=").append((image.getSprite() != null) ? image.getSprite().getId() : "null").append(" )");
    return sb.toString();
  }

  public int getCurrentFrame() {
    return currentFrame;
  }

  public float getRemainingTime() {
    return remainingTime;
  }

  public SpriteAnimation getCurrentAnimation() {
    return currentAnimation;
  }

  //un tested
  public void rotate(float a, float b, float c) {
    getSpriteMesh().rotate(a,b,c);
  }
  
  public void scale(float a) {
    getSpriteMesh().scale(a);
  }
}