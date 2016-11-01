CC = g++
CFLAGS = -Wall -std=c++11
#Include -g if debugging through gdb, otherwise no need to add the overhead

TARGET = tz
SOURCE = main.cpp GameController.cpp Meeple.cpp PlayerState.cpp Tile.cpp TileSetRetriever.cpp

all: $(TARGET)

clean:
	$(RM) $(TARGET)

$(TARGET): main.cpp
	$(CC) $(CFLAGS) -o $(TARGET) $(SOURCE) 

