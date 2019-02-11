import java.awt.Color;
//import java.util.Random;

public class GameObject {
  // Default implementation of a game object
  
  private double radius;
  private double mass;
  private Color c;
  private Vector r; 
  private Vector v; 
  private double d;
  //private Vector vmax = new Vector(5e5, 5e5);
  
  /*
   * Constructor
   */
  public GameObject(Vector rO, Vector vO,Color c,double radius, double mass)
  {
    r = rO; 
    v = vO;
    this.c = c;
    this.radius =  mass * 1e-16;;
    this.mass = mass;
  }
  /*
   * Object go on the left : appear on the right
   * Go up : appear on the bottom
   */
  public void borderLine()
  {
    if((r.cartesian(0) /*+ v.cartesian(0)*/ > 5e10) || (r.cartesian(0)/* + v.cartesian(0)*/ < -5e10) )
    {
      r = new Vector(-r.cartesian(0),r.cartesian(1));
    }
    if((r.cartesian(1) /*+ v.cartesian(1)*/ > 5e10 )|| (r.cartesian(1) /*+ v.cartesian(1)*/ < -5e10))
    {
      r = new Vector(r.cartesian(0),-r.cartesian(1));
    }
  }
  
  /*
   * Update position and velocity. 
   */
  public void move(Vector f, double dt) { 

    Vector a = f.times(1/mass);
    v = v.plus(a.times(dt)); 
    r = r.plus(v.times(dt));
  }
  
  /*
   * Calculate foces between objects 
   */
  public Vector forceFrom(GameObject that) 
  {      
    double G = 6.67e-11;      
    Vector delta = that.r.minus(this.r);      
    double dist = delta.magnitude();
    double F = (G * this.mass * that.mass) / ((dist  * dist) + 0.001); 
    return delta.direction().times(F);   
  }
  
  /*
   * Draw object
   */
  public void draw()
  {
    StdDraw.setPenColor(c);
    radius = mass * 1e-16;
    StdDraw.filledCircle(r.cartesian(0), r.cartesian(1),radius);
  }
  
  
  
  public double getDist()
  {
    return d;
  }
  public void setDist(double d)
  {
    this.d = d;
  }
  public Vector getr()
  {
    return r;
  }
  public void setr(Vector r)
  {
    this.r = r;
  }
  public Vector getv()
  {
    return v;
  }
  public void setv(Vector v)
  {
    this.v = v;
  }
  public double getMass()
  {
    return mass;
  }
  public void setMass(double mass)
  {
    this.mass = mass;
  }
  public double getRadius()
  {
    return radius;
  }
  public void setRadius(double radius)
  {
    this.radius = radius;
  }
  public Color getColor()
  {
    return c;
  }
  public void setColor(Color c)
  {
    this.c = c;
  }
}

