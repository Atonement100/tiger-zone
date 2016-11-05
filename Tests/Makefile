CC = g++
CFLAGS = -Wall -std=c++14
#Include -g if debugging through gdb, otherwise no need to add the overhead

TARGET = unitTests
SOURCE = CatchConfig.cpp CatchTile.cpp ../Tile.cpp

all: $(TARGET)

clean:
	$(RM) $(TARGET)

$(TARGET): CatchConfig.cpp 
	$(CC) $(CFLAGS) -o $(TARGET) $(SOURCE) 
