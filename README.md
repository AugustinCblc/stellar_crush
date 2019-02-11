# Stellar Crush

## Contextualization

This game was developed during the second semester of my second year school level that I spent in South Africa (Faculty of Engineering, Stellenbosch University).  

## Game description

The game is based on a planets' gravity application. You are going to control a planet, in the middle of others, with a first person view and a view from the top of the game.

During a collision, if the sizes of the planets are approximately equal, the smaller ones divide in two. Otherwise, the biggest is getting fatter while eating the other.

## Additional work

* I made different different modes: Easy, mediam and hard.
* Player can shoot GameObject's bullets in his direction. The target and the bullet are destroyed if a collision appear.
* Restart mode and go back to the menu, pause, quit, win and game over.
* The player have an oil-reserve, he gets oil when he merges with an other planet, and he loses oil according to his velocity or when he splits another object.
* Draw an object on the canvas before an other one if it is closer than it. Resize the objects on the first-person camera according to their distances to the player, and draw a circle around the closer one.
* I can control the game focusing on each of the two canvas (controlling player, start, pause, ect..)
* Objects appear with random positions in an asteroid belt around the player.


## Requirements

Java

## Run

cd src
java StellarCrush

## Authors

* **Augustin Chabrillac** - *augustin.chabrillac@epita.com*
