# Tiger Zone
Tiger Zone is an implemenatation of a Carcassonne-esque game, designed for CEN3031, Intro. to Software Engineering. Our implemenation of the game is able to both host local games, as well as compete over a TCP connection to a remote server hosting the game as well.

Our local implementation hosts for up to two players, and each player may either be a human or an instance of our AI. The remotely connected version, per project specifications, is only playable by the AI, but could be modified to allow a human player if desired. 

The remote game required connected players to maintain two active games in each match it played and returned feedback about all moves to players after moves in each game were confirmed. Links to complete project specifications are provided below.

## Debug GUI
This is a snapshot of the graphics interface used to either play the game or observe it played by our AI. It was used as a driver for acceptance testing.

![Graphics Interface used to play TigerZone for acceptance testing](https://raw.githubusercontent.com/Atonement100/tiger-zone/master/TigerZoneDemo.png)

## Project Specification

Project specifications were changed throughout the development cycle to imitate evolving requirements of a real-world project.

+ Day 0 - Develop as if the game were Carcassonne. Proper development practices will allow work to be usable once TigerZone rules are released.
+ Day 17 - [TigerZone Rules v1.2](http://www.cise.ufl.edu/~dts/cen3031/TigerZone/TigerZone%20v1.2.pdf)
+ Day 22 - [TigerZone Tournament Rules v1.0](http://www.cise.ufl.edu/~dts/cen3031/TigerZone/TigerZone%20-%20Tournament%20Rules%20v1.0.pdf) - Conceptual dicta about how the tournament would be run.
+ Day 24 - [TigerZone Rules v2.2](http://www.cise.ufl.edu/~dts/cen3031/TigerZone/TigerZone%20v2.2.pdf)
+       - [TigerZone Tournament Rules v2.2](http://www.cise.ufl.edu/~dts/cen3031/TigerZone/TigerZone%20-%20Tournament%20Rules%20v2.2.pdf)
+       - [TigerZone Networking Protocol v1.1](http://www.cise.ufl.edu/~dts/cen3031/TigerZone/TigerZone%20-%20Networking%20Protocol%20v1.1.pdf) - Actual networking formats
+ Day 26 - [TigerZone Networking Protocol v1.2](http://www.cise.ufl.edu/~dts/cen3031/TigerZone/TigerZone%20-%20Networking%20Protocol%20v1.2.pdf)
+ Day 30 - [TigerZone Networking Protocol v1.3](http://www.cise.ufl.edu/~dts/cen3031/TigerZone/TigerZone%20-%20Networking%20Protocol%20v1.3.pdf)
+ Day 31 - [TigerZone Networking Protocol v1.4](http://www.cise.ufl.edu/~dts/cen3031/TigerZone/TigerZone%20-%20Networking%20Protocol%20v1.4.pdf)
+ Day 32 - Tournaments Begin.
       
## Installation
Installation requires the JDK and should be performed with the following command/s in the source directory after cloning. 

    javac TigerZone.java
    javac TigerZoneClient.java
    
The former will compile everything necessary for a local game, while the latter will do the same but for connecting to a remote server.

Unit tests will require a JUnit installation and all tests can be built with the following command.

    javac *Test.java
    
Acceptance testing was carried out through the use of a graphical interface that launches with each instance of the game.

## Usage
The local game takes in no arguments, and can simply be run with:
  
    java TigerZone
    
The remote game requires arguments (as a result of information being necessary to connect to the server) and should be run as such:

    java TigerZoneClient <hostname> <port> <tournament password> <client identifier> <client password>
    
Tests can be run by individual file names on the command line (each ending with \*Test), or run much more quickly inside a configured IDE such as Intellij or Eclipse.

## The Team
Assigned the identifier 'Team G,' our members are as follows. 

Timothy Russell-Wagner - atonement100

Bobbie Isaly - bobbieisaly

Michael Florica - michaelflorica

Darshil Patel - darshil24

David Wetzel - DavidW09

Alec Hoffman - astrohoff

