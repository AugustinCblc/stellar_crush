import java.util.*;
import java.awt.Color;
import java.awt.Font;
import java.io.*;
/*
 * comare distance between objects
 */
class Comp implements Comparator<GameObject> 
{
  public int compare(GameObject i, GameObject j)
  {
    if(i.getDist()<j.getDist())
      return 1;
    return -1;
  }
}
public class Camera {
  // Virtual camera - uses a plane one unit away from the focal point
  // For ease of use, this simply locates where the centre of the object is, and renders it if that is in the field of view.
  // Further, the correct rendering is approximated by a circle centred on the projected centre point.
  
  private final IViewPort holder; // Object from whose perspective the first-person view is drawn
  private final Draw dr; // Canvas on which to draw
  //private double FOV; // field of view of camera
  
  /*
   * Camera constructor
   * Set new canvas
   */
  Camera(IViewPort holder, double FOV) {
    // Constructs a camera with field of view FOV, held by holder, and rendered on canvas dr.
    this.holder = holder;
    //this.FOV = FOV;
    dr = new Draw();
    dr.setCanvasSize(600,600);
    dr.setXscale(FOV/2, -FOV/2);
    dr.setYscale(-1, 1);
    dr.setLocationOnScreen(1000,1);
    try{
      Font font = Font.createFont(Font.TRUETYPE_FONT, new File ("Capture it.ttf")); //choose police
      font = font.deriveFont(15F);
      dr.setFont(font);
    }catch (Exception e){
    }
  }
  
  /*
   * Classify objects in order to appear on the first person camera
   */
  private PriorityQueue<GameObject> inFront(Collection<GameObject> objDisToPlay)
  {
    
    PriorityQueue<GameObject> q = new PriorityQueue<GameObject>(objDisToPlay.size()+1, new Comp());
    for(GameObject o : objDisToPlay)
    {
      q.add(o);
    }
    return q;
  }
  
  /*
   *for make a loop with ang 
   */
  public double clamp(double ang)
  {
    if(ang < -Math.PI)
      ang +=2*Math.PI;
    
    if(ang > Math.PI)
      ang -=2*Math.PI;
    
    return ang;
  }
  
  /*
   * Select objects to draw
   */
  void render(Collection<GameObject> objects) {
    double x = 0;
    double y = 0;
    double ang;
    dr.clear();
    Collection<GameObject> o = new HashSet<GameObject>();
    for(GameObject i:objects)
    {
      if(i.getColor() != new Color(0,0,0))
      {
        i.setDist(holder.getLocation().distanceTo(i.getr()));
        x = i.getr().cartesian(0) - holder.getFacingVector().cartesian(0)- holder.getLocation().cartesian(0);
        y = i.getr().cartesian(1) - holder.getFacingVector().cartesian(1)- holder.getLocation().cartesian(1);
        ang = Math.atan2(y,x) - holder.getAngle();
        ang = clamp(ang);
        if(Math.abs(ang) < Math.PI/4.0)
        {
          o.add(i);
        }
      }
    }
    
    draw(o);
    dr.show(10);
  }
  
  /*
   * Draw objects
   * Make a black circle arround the closer object
   * Print oil reserve
   */
  public void draw(Collection<GameObject> objects)
  {
    double x = 0;
    double y = 0;
    double ang;
    double dist;
    PriorityQueue<GameObject> q = inFront(objects);
    while(q.size() != 0)
    {
      GameObject i = q.remove();
      dist = holder.getLocation().distanceTo(i.getr());
      x = i.getr().cartesian(0) - holder.getFacingVector().cartesian(0)- holder.getLocation().cartesian(0);
      y = i.getr().cartesian(1) - holder.getFacingVector().cartesian(1)- holder.getLocation().cartesian(1);
      ang = Math.atan2(y,x) - holder.getAngle();
      ang = clamp(ang);
      dr.setPenColor(i.getColor());
      dr.filledCircle(ang,0,i.getRadius()/dist);
      if(q.size()==0)
      {
        dr.setPenColor(StdDraw.BLACK);
        dr.circle(ang,0,i.getRadius()/dist);
      }
      
    }
    
    dr.setPenColor(new Color(0,0,0));
    dr.text(0.5,0.9, "Oil reserve : " + (int)holder.getOil() + "%");
    
  }
  
  public Draw getDraw()
  {
    return dr;
  }
}
