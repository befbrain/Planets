# Planets
A planetary orbit simulator thingy

## About
This is a relatively basic simulation of how objects orbit eachother in space. 
Though it is not perfect and SHOULD NOT BE USED FOR SCIENTIFIC PORPOSES ;-) , it does provide a good representation.

I programmed this in Java 11, using the processing framework. Because it is a computationally heavy program which is 
why I wanted to use a compiled language, and because processing is my favorite library to for visual stuff and that 
was written in Java.

## How It Works
The first step is to add objects to the "universe" which you do in the setup function. You do this using this line of code: 
```objs.add(new Object(81, width/2, height/2, 20, new Vector(0, 0) ));```. Bassically, you have an array (objs) which you add a new instance of the 
Object class to. The object will have a mass of 81 (Arbitrary units), and will be placed in the center of the screen with a size of 20, and an no 
initial force. 

In the draw function, you have the background function which is used to clear the screen every frame. You then draw the objects which is simply 
drawing a circle where the object is located.

Next, you update the velocity which is the hardest part by far. Here is a brief run down of how it works:
```
for every object a
  for every object b
    if object a and b are different objects
      get the distance in the x and y directions between object a and b
      get the acctual most direct distance between object a and b
      get the angle between object a and b using the inverse tangent function and the x and y distances
      use the gravitational net force equation to calculate the net force between the two objects. (G*M*m / r)
      get the net force in the x, and y axis
      multiply the x and y net force by 1, or -1 to make up for last negatives in when finding the distances
      add the x and y net forces to the objects force
```

The above psuedo code is a bit quick so look at the code for more info (There should be comments).

Finnaly, to need to update the object sposition by moving it in the direction of it force.

## Notes
All the above info is very brief so you should look at the comments in the code for more info.
