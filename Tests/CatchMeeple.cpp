#include "catch.hpp"

#include "../Meeple.h"

TEST_CASE("Meeple construction works properly", "[meeple]"){
	Meeple testMeeple(12);
	REQUIRE(testMeeple.getOwnerId() == 12);
	REQUIRE(testMeeple.getLocationX() == -1);
	REQUIRE(testMeeple.getLocationY() == -1);
	REQUIRE(testMeeple.getStatus() == 0);
}

TEST_CASE("Meeple location updater works properly", "[meeple]"){
	Meeple testMeeple(12);
	testMeeple.updateLocation(4,8);
	REQUIRE(testMeeple.getLocationX() == 4);
	REQUIRE(testMeeple.getLocationY() == 8);
	testMeeple.updateLocation(-23, -5);
	REQUIRE(testMeeple.getLocationX() == -23);
	REQUIRE(testMeeple.getLocationY() == -5);
}

TEST_CASE("Meeple status updater works properly", "[meeple]"){
	Meeple testMeeple(12);
	testMeeple.updateStatus(2);
	REQUIRE(testMeeple.getStatus() == 2);
	testMeeple.updateStatus(0);
	REQUIRE(testMeeple.getStatus() == 0);
}
