import java.util.*;
import java.awt.Color;
import java.util.Random;
import java.io.*;
import java.awt.Font;

public class GameState {
  // Class representing the game state and implementing main game loop update step.

  private Collection<GameObject> objects = new HashSet<GameObject>();
  private PlayerObject player;
  private int N = 2000;
  private int i = 0; //number of screenShots
  private static double difficulty;
  private Font font;

  private boolean menu = true;
  private boolean pause = false;
  private boolean gameover = false;
  private boolean win = false;
  private boolean mode = false;
  private boolean init = true;
  private boolean restart = false;
  private int delay;

  private double disbullet = 0;
  GameObject bullet;
  private boolean bbullet = false;

  /*
   * Constructor
   * Set new Canvas
   * Call Update
   */
  public GameState(int delay)
  {
    StdDraw.setCanvasSize(1000,1000);

    this.delay = delay;
    try{
      font = Font.createFont(Font.TRUETYPE_FONT, new File ("Capture it.ttf")); //choose police for menu
      font = font.deriveFont(30F);
      StdDraw.setFont(font);
    }catch (Exception e){
    }
    update(delay);
  }


  void update(int delay) {

    // Main game loop update step

    while(true) // start the game
    {
      /*
       * First page menu
       */
      while(menu)
      {
        StdDraw.setXscale(0, +1);
        StdDraw.setYscale(0, +1);
        try{
          StdDraw.picture(0.5, 0.5, "chateau.jpg",1, 1.9);   //choose menu wallpaper
          StdDraw.setPenColor(StdDraw.BLACK);
          StdDraw.text(0.5,0.9,"STELLAR CRUSH");
          StdDraw.text(0.5,0.7,"Press SPACE to Start");
          StdDraw.text(0.5,0.6,"In game :");
          StdDraw.text(0.5,0.5,"R for restart");
          StdDraw.text(0.5,0.4,"s for screenshot");
          StdDraw.text(0.5,0.3,"m for quit");
          StdDraw.show(10);
        }catch (Exception e){
        }
        if(StdDraw.isKeyPressed(32)) //Go choose the mode
        {
          menu = false;
          mode = true;
          try{
            Thread.sleep(100);
          }catch (Exception e){
          }
          StdDraw.clear();
        }
      }

      /*
       * Choose the mode
       */
      while(mode)
      {
        StdDraw.picture(0.5, 0.5, "chateau.jpg",1, 1.9);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(0.5,0.8,"E for EASY");
        StdDraw.text(0.5,0.6,"M for MEDIUM");
        StdDraw.text(0.5,0.4,"H for HARD");
        StdDraw.show(10);

        if(StdDraw.isKeyPressed(77)) // Medium
        {
          difficulty = 1.5;
          mode = false;
          try{
            Thread.sleep(500);
          }catch (Exception e){
          }
          StdDraw.clear();
          StdDraw.show(10);
        }
        if(StdDraw.isKeyPressed(69)) // Easy
        {
          difficulty = 1;
          mode = false;
          try{
            Thread.sleep(100);
          }catch (Exception e){
          }
          StdDraw.clear();
          StdDraw.show(10);
        }
        if(StdDraw.isKeyPressed(72))// Hard
        {
          difficulty = 2;
          mode = false;
          try{
            Thread.sleep(100);
          }catch (Exception e){
          }
          StdDraw.clear();
          StdDraw.show(10);
        }
      }

      if(difficulty == 1) //EASY reduce the other objects
        N = 2;
      else if(difficulty == 1.5) //MEDIUM
        N = 20;
      else                     // HARD
        N = 2000;

      StdDraw.setXscale(-StellarCrush.scale, +StellarCrush.scale);
      StdDraw.setYscale(-StellarCrush.scale, +StellarCrush.scale);

      /*
       * First initialisation
       * Only made one time
       */
      if(init)
      {
        init = false;
        initialisation(N); // initialise objects and player
      }

      /*
       * Restart
       */
      if(restart)
      {
        restart = false;
        restart();
      }

      /*
       * Pause
       * Wait for play
       */
      if(StdDraw.isKeyPressed(80)|| player.getCam().getDraw().isKeyPressed(80)) // PLAY
      {
        StdDraw.clear();
        pause = false;
        try{
          Thread.sleep(100);
        }catch (Exception e){
        }
      }

      /*
       * Quit
       */
      if(StdDraw.isKeyPressed(77)|| player.getCam().getDraw().isKeyPressed(77))
      {
        //StdDraw.quit();
        System.exit(0);
      }

      /*
       * Gameover or Win
       * Wait for restart
       */
      if((gameover|| win) && StdDraw.isKeyPressed(82) || player.getCam().getDraw().isKeyPressed(82)) // RESTART
      {
        player.getCam().getDraw().clear();
        player.getCam().getDraw().show();
        try{
          Thread.sleep(100);
        }catch (Exception e){
        }
        menu = true;
        restart = true;
        gameover = false;
        win = false;
      }

      /*
       * While playing the game
       */
      while(!menu && !pause && !gameover && !win) //Playing
      {
        StdDraw.clear();

        /*
         * M for quit
         * Inside the game
         */
        if(StdDraw.isKeyPressed(77) || player.getCam().getDraw().isKeyPressed(77)) // QUIT
        {
          System.exit(0);
        }

        /*
         * S for screenshot
         */
        if(StdDraw.isKeyPressed(83) || player.getCam().getDraw().isKeyPressed(83)) // Screenshot
        {
          try{screenShot(player.getCam().getDraw(),i);} catch(IOException e)
          {System.out.print("An error occured");}
          i++;
        }

        /*
         * Calculate forces
         */
        Map<GameObject,Vector> m = calculateForces();
        for(GameObject i:objects)
        {
          i.move(m.get(i),delay);
        }


        collision(); // MERGE AND SPLIT
        draw(); // Draw on the third person canvas
       // player.draw();// Draw the player and a filled circle for his direction
        player.getCam().render(objects); //Draw on the first person canvas

        /*
         * Make a PAUSE
         */
        if(StdDraw.isKeyPressed(80) || player.getCam().getDraw().isKeyPressed(80)) // PAUSE
        {
          pause = true;
          StdDraw.setPenColor(StdDraw.BLACK);
          StdDraw.text(0.5,0.5,"PAUSE");
          player.getCam().getDraw().text(0,0,"PAUSE");
          player.getCam().getDraw().show();
          try{
            Thread.sleep(100);
          }catch (Exception e){
          }
        }

        /*
         * GameOver
         * No more oil to continue
         */
        if(player.getOil() <= 0) // GameOver
        {
          StdDraw.clear();
          player.getCam().getDraw().clear();

          gameover = true;
          StdDraw.setPenColor(StdDraw.BLACK);
          StdDraw.text(0.5,3e9,"GAME OVER");
          player.getCam().getDraw().text(0,0,"GAME OVER");
          StdDraw.text(0.5,-3e9,"Press R to restart");
          player.getCam().getDraw().text(0,-0.3,"Press R to restart");
          player.getCam().getDraw().show();
          try{
            Thread.sleep(100);
          }catch (Exception e){
          }
        }

        /*
         * Win
         * No more object to eat
         */
        if(objects.size() == 1) //Win
        {
          StdDraw.clear();
          player.getCam().getDraw().clear();
          win = true;
          StdDraw.setPenColor(StdDraw.BLACK);
          StdDraw.text(0.5,3e9,"WIN");
          player.getCam().getDraw().text(0,0,"WIN");

          StdDraw.text(0.5,-3e9,"Press R to restart");
          player.getCam().getDraw().text(0,-0.3,"Press R to restart");
          player.getCam().getDraw().show();
          try{
            Thread.sleep(100);
          }catch (Exception e){
          }
        }

        /*
         * Shot bullets
         * Cannot shot if the last bullet is too close to the player
         * player lose mass when he shot
         */
        if(StdDraw.isKeyPressed(66) || player.getCam().getDraw().isKeyPressed(66))
        {
          if(bbullet)
            disbullet = player.getr().distanceTo(bullet.getr());

          if(disbullet > 5*player.getRadius() || disbullet == 0)
          {
            bullet = fire();
            bbullet = true;
          }

        }
        /*
         * Restart
         * Go for choising a new mode
         */
        if(StdDraw.isKeyPressed(82) || player.getCam().getDraw().isKeyPressed(82)) // RESTART
        {
          player.getCam().getDraw().clear();
          player.getCam().getDraw().show();
          try{
            Thread.sleep(100);
          }catch (Exception e){
          }
          menu = true;
          restart = true;
        }
        StdDraw.show(10);
      }
    }
  }

  /*
   * Create the Player
   * Create Objects
   * Delete all co-located objects before drawing them
   */
  public void initialisation(int n)
  {

    /*
     * initialise player
     */
    Random rand =  new Random();
    Color c = new Color((int)(Math.random()*256), (int)(Math.random()*256),(int)(Math.random()*256));
    double mass = 4E25 / difficulty;
    double radius = Math.random()*(5e9 - 1e9) +1e9;
    player = new PlayerObject(new Vector(0,0),new Vector(0,0), new Color(139,0,139), radius, mass);
    objects.add(player);

    /*
     * initialise objects in an asteroide circle
     */
    for(int i = 0; i < n; i++)
    {
      double rad = (rand.nextDouble() * 0.2 + 0.5) * StellarCrush.scale;
      double teta = Math.random()*2*Math.PI;
      c = new Color((int)(Math.random()*256), (int)(Math.random()*256),(int)(Math.random()*256));
      rand = new Random();
      mass = 9E24 + Math.random()*(4E25 - 3E25);
      radius = Math.random()*(5e9 - 1e9) +1e9;

      objects.add(new GameObject(new Vector(rad * Math.cos(teta),rad *Math.sin(teta)),new Vector(0.0,0.0),c,radius,mass));
    }


    /*
     * delete co-located objects before drawing anything
     */
    Collection<GameObject> rem = new HashSet<GameObject>();
    for(GameObject i:objects)
    {
      for(GameObject j:objects)
      {
        if(i != j && i != player)
        {
          if(i.getr().distanceTo(j.getr()) < j.getRadius()) // if objects are too close
          {
            if(i.getMass() < j.getMass()) // delete the little one
            {
              rem.add(i);
              j.setMass(j.getMass() + i.getMass()/1000); // bigger one gets little bit bigger
            }
          }
        }
      }
    }
    for (GameObject i:rem)// remove co-located object in the real list
    {
      objects.remove(i);
    }
  }

  /*
   * Shot bullets
   */
  public GameObject fire()
  {
    if(player.getMass()/1.2 > 1e25)
      player.setMass(player.getMass()/1.2);
    GameObject b = new GameObject(new Vector(player.getr().cartesian(0)+ player.getFacingVector().cartesian(0)*player.getRadius()*2,  player.getr().cartesian(1)+player.getFacingVector().cartesian(1)*player.getRadius()*2),player.getFacingVector().times(5000),new Color(245,222,179),player.getRadius()/2,player.getMass()/4);
    objects.add(b);
    return b;
  }
  /*
   * same as initialisation
   * not creating another player
   */
  public void restart()
  {
    objects = new HashSet<GameObject>();
    Random rand =  new Random();
    Color c;
    double mass = 4E25 / difficulty;
    double radius;
    player.setr(new Vector(0,0));
    player.setv(new Vector(0,0));
    player.setOil(100);
    player.setMass(4E25 / difficulty);
    objects.add(player);
    /*
     * initialise objects
     */
    for(int i = 0; i < N; i++)
    {
      double rad = (rand.nextDouble() * 0.2 + 0.5) * StellarCrush.scale;
      double teta = Math.random()*2*Math.PI;
      c = new Color((int)(Math.random()*256), (int)(Math.random()*256),(int)(Math.random()*256));
      rand = new Random();
      mass = 9E24 + Math.random()*(4E25 - 3E25);
      radius = Math.random()*(5e9 - 1e9) +1e9;

      objects.add(new GameObject(new Vector(rad * Math.cos(teta),rad *Math.sin(teta)),new Vector(0.0,0.0),c,radius,mass));
    }
    // delete co-located objects before drawing anything
    Collection<GameObject> rem = new HashSet<GameObject>();
    for(GameObject i:objects)
    {
      for(GameObject j:objects)
      {
        if(i != j && i != player)
        {
          if(i.getr().distanceTo(j.getr()) < j.getRadius()) // if objects are too close
          {
            if(i.getMass() < j.getMass()) // delete the little one
            {
              rem.add(i);
              j.setMass(j.getMass() + i.getMass()/1000); // bigger one gets little bit bigger
            }
          }
        }
      }
    }
    for (GameObject i:rem)// remove co-located object from the real list
    {
      objects.remove(i);
    }
  }

  /*
   * Take Screenshot
   */
  public void screenShot(Draw dr, int i) throws java.io.IOException
  {
    File dir = new File("screenshot" + i);
    dir.mkdir();
    StdDraw.save("screenshot" + i +"/ScreenshotMap" + i +".jpg");
    dr.save("screenshot" + i +"/ScreenshotFP" + i +".jpg");
  }

  /*
   * Merge and split
   */
  private void collision ()
  {
    Collection<GameObject> objs = new HashSet<GameObject>(); //adding list
    Collection<GameObject> rem = new HashSet<GameObject>(); //removing list
    for(GameObject i:objects)
    {
      for(GameObject j:objects)
      {
        if(i != j)
        {
          if(i.getr().distanceTo(j.getr()) < j.getRadius() + i.getRadius()) //one object touch another
          {
            if(i == bullet || j == bullet)
            {
              rem.add(i);
              rem.add(j);
            }
            else if(i != player && i.getRadius()< j.getRadius() && i.getRadius()*2 > j.getRadius())// i is smaller than j but not twice smaller -> SPLIT(except player)
            {
              if(i.getMass()/2 < 3e24) // i is to small so it have to be merge
              {
                rem.add(i);
                if(j.getMass() + i.getMass()/2 < 3e25) // j get bigger but not bigger than the maximum mass
                  j.setMass(j.getMass() + i.getMass()/2);
                if(j == player)
                {
                  player.setOil(10);//get oil if player merge an object
                }
              }
              else // create two objects from the split one
              {
                Vector v = new Vector(5e9, 5e9);
                objs.add(new GameObject(i.getr().plus(v), i.getv(),i.getColor(), 0, i.getMass()/2));
                objs.add(new GameObject(i.getr(), i.getv().times(-1),i.getColor(), 0, i.getMass()/2));
                rem.add(i);
                if(j == player)
                  player.setOil(-10);//loose oil if objects split on the player
              }
            }
            else if(i == player && i.getRadius()< j.getRadius()) //the player spit bigger objects
            {
              Vector v = new Vector(5e9, 5e9);
              objs.add(new GameObject(j.getr().plus(v), j.getv(),j.getColor(), 0, j.getMass()/2));
              objs.add(new GameObject(j.getr(), j.getv().times(-1),j.getColor(), 0, j.getMass()/2));
              rem.add(j);
              player.setOil(-10); //loose oil if objects split on the player
            }
            else if(i != player) //Merge
            {
              if(i.getMass() < j.getMass())//Choose the little one
              {
                rem.add(i);
                if(j.getMass() + i.getMass()/2 < 4e25)//j get bigger but not bigger than the maximum mass
                {
                  j.setMass(j.getMass() + i.getMass()/2);
                }
                if(j == player)
                {
                  player.setOil(10);//get oil if player merge an object
                }
              }
            }
          }
        }
      }
    }
    for (GameObject i:rem)//Remove
    {
      objects.remove(i);
    }
    for (GameObject i:objs)//Create split objects
    {
      boolean b1 = true; //Create split boject
      for(GameObject i2:objects)
      {
        if (i2.getr().distanceTo(i.getr()) <i2.getRadius() + i.getRadius() && b1)//do not create if co-located object
        {
          b1 = false;
        }
      }
      if(b1)
        objects.add(i);
    }
  }

  /*
   * Draw on the third person canvas
   */
  public void draw()
  {
    for(GameObject i:objects)
    {
      i.draw();
      i.borderLine();
      player.processCommand(delay);
    }
  }

  /*
   * Calculate the force on each object for the next update step.
   */
  private Map<GameObject,Vector> calculateForces() {

    Map<GameObject,Vector> m = new HashMap<GameObject,Vector>();

    for (GameObject i:objects)
    {
      m.put(i,new Vector(new double[2]));
    }
    for(GameObject i:objects)
    {
      for(GameObject j:objects)
      {
        if(!i.equals(j))
        {
          m.put(i,m.get(i).plus(i.forceFrom(j)));
        }
      }
    }
    return m;
  }

  public Collection<GameObject> getObjects()
  {
    return objects;
  }
  public double getN()
  {
    return N;
  }
}
