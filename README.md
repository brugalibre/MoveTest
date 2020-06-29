#'MoveTest' is a 'work in progress' project which allowes to place and move GridElements like moving circles, obstacles, walls or even projectiles on a Grid. 
It comes with a full collision- and bouncing-system which detects and handles collision and bouncing GridElements.

Detectors and evasion-avoiding systems like the EvasionStateMachine allows any moving GridElement to detect a potential collision with 'avoidable' GridElements 
and initiat a five stage evasion maneuvre which, in the end, will lead the moveable back on track after successfully avoided a obstacle.
The detectors distinguish between detected objects and detected objects which are so close, that they are in potential collision course (evasion).
Therefore you can defines detecting distance & angle as well as an evasion distance and angle.

Besides the 'Moveables', which are simply moveable GridElements there is also the EndPointMoveable. It describes a moveable which can be lead from position to position
Here an example of such an EndPointMoveable which is lead through a bounch of randomly placed moveable obstacles:

![randomendpointmoveableexample](https://user-images.githubusercontent.com/29772244/85270834-34056500-b47a-11ea-9da2-bdacbcb86ed7.png)

Note that the visualization of the detector can be turned off.

With the detector cluster, a special kind of a detector, a moveable can even navigate through a maze. A light-barrier at the end indicates that the moving GridElement has successfully reached the end of the maze:

![mazeexample](https://user-images.githubusercontent.com/29772244/85270831-32d43800-b47a-11ea-87c5-36b7f4077822.png)

There are also elements like a tank or turret for a less peacfull examples. Thanks to a scanner, a turret can detect, acquire and shoot on enemy targets like obstacles 
or other tanks and turrets. Evaluating the target position includes also evaluating a lead for moving targets. A turret can either has a bullet or a missile gun. 
Whereas the tank can have different types of turrets or turret- strategies.
In order to fight a missile, the tank-detector comes now with a MissileCounterMeasureSystem which automatically detects missiles within a certain distance and deploys automatically a set of decoy flares. Those will lead the missile to change it's direction towards those decoy flares.

Tank/turret example:
![tankturretexample](https://user-images.githubusercontent.com/29772244/86607008-099ec600-bfa9-11ea-9e1d-05aa0c8dbb09.png)

One of this strategy is a 'human' strategy which allows the user to control the turret by himself (the bunch of blue circles are deployed decoy flares):
![human_test](https://user-images.githubusercontent.com/29772244/86607010-0a375c80-bfa9-11ea-9e9a-f5a3cde2fbe4.png)

Whats missing:
- The collision detection system is going to be improve, especially when a Tank moves forward
- And also in the near futur I'll add some sound and visual effects for explosions and stuff.


