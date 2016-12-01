# Tiger Zone
Designed for Software Engineering (CEN3031), Tiger Zone is a Carcassonne-esque game. The implementation here is able to both host local games, either between players or our competitive AI, as well as compete over a TCP connection to a remote server hosting the game, returning feedback on each move.

## Installation
Installation requires the JDK and should be performed with the following command/s in the source directory after cloning. 

    javac TigerZone.java
    javac TigerZoneClient.java
    
The former will compile everything necessary for a local game, while the latter will do the same but for connecting to a remote server.

## Usage
The local game takes in no arguments, and can simply be run with:
  
    java TigerZone
    
The remote game requires arguments (as a result of information being necessary to connect to the server) and should be run as such:

    java TigerZoneClient <hostname> <port> <tournament password> <client identifier> <client password>

## The Team
Assigned the identifier 'Team G,' our members are as follows. 

Timothy Russell-Wagner - atonement100

Bobbie Isaly - bobbieisaly

Michael Florica - michaelflorica

Darshil Patel - darshil24

David Wetzel - DavidW09

Alec Hoffman - astrohoff

