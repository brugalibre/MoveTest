<h1> Introduction </h1>
#'MoveTest' is a 'work in progress' project which allowes to place and move GridElements like moving circles, obstacles, walls or even projectiles on a Grid. 

<h1> Collision Detection</h1>
It comes with a full collision- and bouncing-system which detects and handles collision and bouncing GridElements. The collision detector takes the path-segments of all shapes within reach and verifies weather or not there is a collision with the shape of the moved GridElement.
With a 'CollisionDetectionHandler' you can define the behavour of a grid in case of a collision. The 'BouncingCollisionDetectionHandlerImpl' e.g. provides a bouncing mecanism whenever two or more GridElements collides with each other.

<h1> Evasion-Detection</h1>
Detectors and evasion-avoiding systems like the EvasionStateMachine allows any moving GridElement to detect a potential collision with 'avoidable' GridElements 
and initiat a five stage evasion maneuvre which, in the end, will lead the moveable back on track after successfully avoided a obstacle.
The detectors distinguish between detected objects and detected objects which are so close, that they are in potential collision course (evasion).
Therefore you can defines detecting distance & angle as well as an evasion distance and angle.

Besides the 'Moveables', which are simply moveable GridElements there is also the EndPointMoveable. It describes a moveable which can be lead from position to position
Here an example of such an EndPointMoveable which is lead through a bounch of randomly placed moveable obstacles:

![randomendpointmoveableexample](https://user-images.githubusercontent.com/29772244/85270834-34056500-b47a-11ea-9da2-bdacbcb86ed7.png)

Note that the visualization of the detector can be turned off.

<h1> Maze </h1>
With the detector cluster, a special kind of a detector, a moveable can even navigate through a maze. A light-barrier at the end indicates that the moving GridElement has successfully reached the end of the maze:

![mazeexample](https://user-images.githubusercontent.com/29772244/85270831-32d43800-b47a-11ea-87c5-36b7f4077822.png)

<h1> Battlefield </h1>
There are also elements like a tank or turret for less peacfull examples. 
<h3> Turret</h3>
The behaviour of a turret is defines by it's strategy. Like this it's possible to distinguish between  human- and automated turrets. Thanks to a scanner, a turret can detect, acquire and shoot on enemy targets like obstacles 
or other tanks and turrets automatically. Evaluating the target position includes also evaluating a lead for moving targets. A turret has gun mounted - either has a bullet or a missile gun.

<h4> Missile</h4>
The missile is a more intelligent type of a projectile which detect and follows the nearest detected target as soon as it's acquired. Since it's computing a lead for it's target, this usualy takes two cycles.

<h3> Tanks</h3>
A Tank comes with a 'TankEngine', a 'TankDetector' and a 'TankTurret'. The tank itselfs comes with a own tank-strategy which defines it's behaviour. Those mentioned tank elements can be replaced by different implementation using the builder pattern of the TankBuilder.

<h4> Missile-Countermeasure-System</h4>
In order to fight a missile, the tank-detector comes now with a 'MissileCounterMeasureSystem' which automatically detects missiles within a certain distance and automatically deploys a set of decoy flares. Those will lead the missile to change it's direction towards those decoy flares.

Tank/turret example:
![tankturretexample](https://user-images.githubusercontent.com/29772244/87281725-ccc85700-c4f3-11ea-9ba1-73b665b63802.png)

One of this strategy is a 'human' strategy which allows the user to control the turret by himself (the bunch of blue circles are deployed decoy flares):

![human_test](https://user-images.githubusercontent.com/29772244/87281721-cb972a00-c4f3-11ea-9f0f-4aff3d4f222b.png)

<h1> Whats missing</h1>

- The collision detection system is going to be improve, especially when a Tank moves forward
- And also in the near futur I'll add some visual effects for explosions and stuff.


