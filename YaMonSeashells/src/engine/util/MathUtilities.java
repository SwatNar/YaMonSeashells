package engine.util;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public final class MathUtilities
{
   /** from : http://stackoverflow.com/questions/2422712/c-rounding-integer-division-up-instead-of-truncating
    */
   public static int divideRoundUp(float x, float y)
   {
      return (int)( (x / y) + ((x % y > 0)?1:0));
   }

   /** Returns the rotation between 2 positions based on X axis. Range [-PI, PI].
    *  Left   Of : 0  =  -0
    *  Top    Of :  HALF_PI
    *  Right  Of : PI = -PI
    *  Bottom Of : -HALF_PI
    */
   public static double getAngleBetween(double p1x, double p1y, double p2x, double p2y)
   {
      double frac = 1 / Math.hypot(p1x - p2x, p1y - p2y);
      double xMove = (p2x - p1x) * frac;
      double yMove = (p1y - p2y) * frac;
      
      return Math.atan2(yMove, xMove) % FastMath.PI;
   }

   /** code from http://graphics.stanford.edu/~seander/bithacks.html#RoundUpPowerOf2 */
   public static int nextPowerOfTwo(int v)
   {
      v--;
      v |= v >> 1;
      v |= v >> 2;
      v |= v >> 4;
      v |= v >> 8;
      v |= v >> 16;
      v++;
      return v;
   }
   
   public static boolean rectangleIntersects(int aLeft, int aTop, int aRight, int aBottom, int bLeft, int bTop, int bRight, int bBottom)
   {
      return !(aLeft > bRight || aRight < bLeft || aTop > bBottom || aBottom < bTop);
   }

   /** Spherical to cartesian coordinates. */
   public static Vector3f sphericalToCartesian(float phi, float theta, float r)
   {
      float x = r * FastMath.sin(theta) * FastMath.cos(phi);
      float y = r * FastMath.sin(theta) * FastMath.sin(phi);
      float z = r * FastMath.cos(theta);
      return new Vector3f(x, y, z);
   }
   
   public static float whiseSide(Vector3f p1, Vector3f planeOrigin, Vector3f planeNormal)
   {
      return (planeNormal.dot(p1) - planeNormal.dot(planeOrigin));
   }
   
   public static Quaternion interpolate(Quaternion q1, Quaternion q2, float changeAmnt)
   {
      return new Quaternion((1 - changeAmnt) * q1.getX() + changeAmnt * q2.getX(), (1 - changeAmnt) * q1.getY() + changeAmnt * q2.getY(), (1 - changeAmnt) * q1.getZ() + changeAmnt * q2.getZ(), (1 - changeAmnt) * q1.getW() + changeAmnt * q2.getW());
   }
   
   public static Vector3f findIntersectionPoint(Vector3f p0, Vector3f p1, Vector3f planeOrigin, Vector3f planeNormal)
   {
      Vector3f directionP0P1 = p1.subtract(p0);
      
      float t = findIntersectionPointFraction(p0, p1, planeOrigin, planeNormal);
      
      Vector3f r0 = p0.interpolate(p1, t);
      //Vector3f r1 = p0.add(directionP0P1.mult(t));
      //Vector3f r2 = Utilities.createPlane(planeOrigin, planeNormal).getClosestPoint(directionP0P1);

      //if (!r0.equalsIgnoreCase(r1)) System.err.println("r0 vs r1 : "+r0+" vs "+r1);
      //if (!r1.equalsIgnoreCase(r2)) System.err.println("r1 vs r2 : "+r1+" vs "+r2);

      return r0;
   }
   
   public static float findIntersectionPointFraction(Vector3f p0, Vector3f p1, Vector3f planeOrigin, Vector3f planeNormal)
   {
      Vector3f directionP0P1 = p1.subtract(p0);
      float t = planeNormal.dot(planeOrigin.subtract(p0)) / planeNormal.dot(directionP0P1);
      return t;
   }

   /**
    * Distance formula from Koen Samyn :
    * http://knol.google.com/k/plane-equation-in-3d#
    * <p/>
    * @param pointToBeMeasured the Point you want to find its distance from
    * plane.
    * @param anyPointOfPlane any plane's point e.g plane's origin
    * @param planeNormal the plane's Normal at the "anyPointOfPlane"
    */
   public static float getDistanceOfPointFromPlane(Vector3f pointToBeMeasured, Vector3f anyPointOfPlane, Vector3f planeNormal)
   {
      return pointToBeMeasured.subtract(anyPointOfPlane).dot(planeNormal);
   }

   /**
    * Symmetry formula from Koen Samyn :  http://knol.google.com/k/mirroring-a-point-on-a-3d-plane#
    * Same info on wikipedia : http://en.wikipedia.org/wiki/Reflection_%28mathematics%29
    *
    * @param pointToBeMirrored the Point you want to find its symmetric
    * @param planeOrigin  the planeOrigin e.g (0,0,0)
    * @param planeNormal  the planeNormal is a vector that points upward from the plane e.g Vector3f.UNIT_Z
    */
   public static Vector3f getSummetricPosition(Vector3f pointToBeMirrored, Vector3f planeOrigin, Vector3f planeNormal)
   {
      assert planeNormal.isUnitVector(): "planeNormal " + planeNormal.toString() + " is not a unit vector";
      Vector3f symmetricPoint = pointToBeMirrored.subtract(planeNormal.mult(2 * getDistanceOfPointFromPlane(pointToBeMirrored, planeOrigin, planeNormal)));
      return symmetricPoint;
   }
   
   public static Vector3f getPositionOnPlane(Vector3f pointToBeCentered, Vector3f planeOrigin, Vector3f planeNormal)
   {
      assert planeNormal.isUnitVector(): "planeNormal " + planeNormal.toString() + " is not a unit vector";
      Vector3f centerPoint = pointToBeCentered.subtract(planeNormal.mult(getDistanceOfPointFromPlane(pointToBeCentered, planeOrigin, planeNormal)));
      return centerPoint;
   }
   
   public Vector3f getPerpendicular(Vector3f p1)
   {
      float axisDistanceX = Math.abs(p1.x);
      float axisDistanceY = Math.abs(p1.y);
      float axisDistanceZ = Math.abs(p1.z);
      
      Vector3f p2 = Vector3f.UNIT_Z; //axisDistanceZ < axisDistanceY && axisDistanceZ < axisDistanceX
      if (axisDistanceX <= axisDistanceY && axisDistanceX <= axisDistanceZ) p2 = Vector3f.UNIT_X;
      else if (axisDistanceY <= axisDistanceX && axisDistanceY <= axisDistanceZ) p2 = Vector3f.UNIT_Y;
      
      return p1.cross(p2);
   }
   
   public static int clamp(int number, int minValue, int maxValue)
   {
      return Math.min(Math.max(number, minValue), maxValue);
   }

   /** returns a random int from [min, max]*/
   public static final int random(int min, int max)
   {  //return     (range+1) * [0,1) + min = [0 , range] + min = [min,max+min-min] = [min,max]
      return (int) ((max - min + 1) * Math.random() + min);
   }

   /**returns a random float from [min, max). Max is exclusive i.e it never takes that value.*/
   public static final float random(float min, float max)
   {
      max = (float) (max * Math.random());
      return min + max;
   }
}
