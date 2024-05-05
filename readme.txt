Name: Robot simulation
Authors: Artem Vereninov (xveren00), Denys Dumych(xdumyc00)

Description

We implemented a simulation, as required by our task, with all necessary controls. These controls enable adding new
robots and obstacles, manually controlling robots, and adjusting the time in the simulation.

Setup details:

Initial setup can be changed using field.json in /data directory:
"robots":[x,y,speed]
"obstacles":[x,y]
"room":[height, width]
New images for background should be added to the /data/img directory.

Controls:

Single click (left mouse button) - adding new autonomous robot to the field
Single click (right mouse button) - adding new obstacle to the field
Double click (left mouse button) - adding new manually operated robot
Q - changing backgrounds
Arrows - moving robot around

Program execution (should be executed in the root directory)
* mvn compile
* mvn package
* java -jar target/ija_project-1.0.jar