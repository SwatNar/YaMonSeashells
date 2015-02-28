package tests.sprite.oldBugsSolved;

import com.jme3.app.SimpleApplication;
import engine.util.ConvertionUtilities;

public class TestJmeBufferBug extends SimpleApplication
{
   public static void main(String[] args)
   {
      new TestJmeBufferBug().start();
   }

   @Override
   public void simpleInitApp()
   {
   }

   @Override
   public void simpleUpdate(float tpf)
   {
      super.simpleUpdate(tpf);
      ConvertionUtilities.createFloatBuffer(300000);
   }
}
