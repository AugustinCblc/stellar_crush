import java.awt.*;
import java.awt.event.KeyEvent;


public class PlayerObject extends GameObject implements IViewPort {
  
//  private static final Color DEFAULT_COLOR = StdDraw.WHITE;
//  private static final Color DEFAULT_FACING_COLOR = StdDraw.BLACK;
//  private static final double DEFAULT_FOV = Math.PI/2; // field of view of player's viewport
  private static final double FOV_INCREMENT = Math.PI/36; // rotation speed
  
  private Camera cam;
  private Draw dr;
  private Vector dir = new Vector(1,0);
  private double angle = 0;
  private double oil = 100;
  
  
  public PlayerObject(Vector r, Vector v, Color c, double radius, double mass)
  {
    super(r,v,c,radius,mass);
    cam = new Camera (this, Math.PI/2);
  }
  void processCommand(int delay) {
    // Process keys applying to the player
    // Retrieve 
    
    if (cam != null) {
      // No commands if no draw canvas to retrieve them from!
      dr = cam.getDraw();
      
      if (dr != null) {
        // Example code
        oil -= getv().magnitude()/200000;
        if(this.oil + oil < 0)
          this.oil = 0;
        if (dr.isKeyPressed(KeyEvent.VK_UP) || StdDraw.isKeyPressed(KeyEvent.VK_UP))
        {
          setv(new Vector(getv().cartesian(0) +5*Math.cos(angle),getv().cartesian(1) +5*Math.sin(angle)));
        }
        
        if (dr.isKeyPressed(KeyEvent.VK_DOWN) || StdDraw.isKeyPressed(KeyEvent.VK_DOWN))
        {
          setv(new Vector(getv().cartesian(0) -2*Math.cos(angle),getv().cartesian(1) -2*Math.sin(angle)));
        }
        
        if (dr.isKeyPressed(KeyEvent.VK_RIGHT) || StdDraw.isKeyPressed(KeyEvent.VK_RIGHT))
        {
          //double ang = (Math.atan2((dir.cartesian(1) - unit.cartesian(1)),(dir.cartesian(0)-unit.cartesian(0))) + Math.PI) * (180 / Math.PI);
          //System.out.println(ang);
          if(angle <= -Math.PI)
            angle = Math.PI;
          angle -= FOV_INCREMENT*0.1;
          //dir = VectorUtil.rotate(dir,angle);
          //angle = (Math.atan2((dir.cartesian(1) - unit.cartesian(1)),(dir.cartesian(0)-unit.cartesian(0))) + Math.PI) * (180 / Math.PI);
          dir = new Vector(Math.cos(angle), Math.sin(angle));
          
          //System.out.println(ang);
        }
        
        if (dr.isKeyPressed(KeyEvent.VK_LEFT) || StdDraw.isKeyPressed(KeyEvent.VK_LEFT))
        {
          if(angle >= Math.PI)
            angle = -Math.PI;
          angle += FOV_INCREMENT*0.1;
          dir = new Vector(Math.cos(angle), Math.sin(angle));
        }
      }
    }
  }
  
  @Override
  public void draw()
  {
    setRadius(getMass() * 1e-16);
    StdDraw.setPenColor(getColor());
    StdDraw.filledCircle(getr().cartesian(0), getr().cartesian(1),getRadius());
    StdDraw.setPenColor(new Color(245,222,179));
    StdDraw.filledCircle(getr().cartesian(0)+ dir.cartesian(0)*getRadius(),  getr().cartesian(1)+dir.cartesian(1)*getRadius(),getRadius()/2);
   // StdDraw.filledRectangle(getr().cartesian(0)+ dir.cartesian(0)*getRadius(), getr().cartesian(1)+dir.cartesian(1)*getRadius(), getRadius()/2, getRadius()/2);
    //StdDraw.line(getr().cartesian(0),getr().cartesian(1),dir.cartesian(0)*5e10 +getr().cartesian(0), dir.cartesian(1)*5e10 + getr().cartesian(1));
    
  }
  
  public Draw getDr()
  {
    return dr;
  }
  public Camera getCam()
  {
    return cam;
  }
  public Vector getLocation()
  {
    return getr();
  }
  public Vector getFacingVector()
  {
    return dir;
  }
  public double highlightLevel()
  {
    return 0.0;
  }
  public double getOil()
  {
    return oil;
  }
  public void setOil(double oil)
  {
    if(this.oil + oil > 100)
      this.oil = 100;
    else if(this.oil + oil < 0)
      this.oil = 0;
    else
      this.oil += oil;
  }
  public double getAngle()
  {
    return angle; 
  }
}
