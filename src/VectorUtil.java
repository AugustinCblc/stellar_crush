
public class VectorUtil {
  // Class containing additional utility functions for working with vectors.
  
 // public static final Vector TWO_D_ZERO = new Vector(new double[]{0, 0});
  /*
  static Vector rotate(Vector v, double ang) {
    // Rotate v by ang radians - two dimensions only.
    double x = (v.cartesian(0) * Math.cos(ang)) - (v.cartesian(1) * Math.sin(ang));
    double y = (v.cartesian(0) * Math.cos(ang)) - (v.cartesian(1) * Math.sin(ang));
    Vector r = new Vector(x,y);
    return r;
  }
  
  static Vector direction(Vector v) {
    // Returns direction of v, but sets angle to Math.PI/2 when v is the zero vector
    // Used to avoid exception in Vector.java
    if(v==null)
      return (rotate(v,Math.PI/2).direction());
    else
      return v.direction();
  }
  */
  public static void main(String[] args)
  {
    /*Vector v = new Vector(2.0,5.0,6.0,10.0);
    v=rotate(v,Math.PI/2);
    System.out.println(v.cartesian(2));
    System.out.println(v.cartesian(3));*/
  }
}

