#include "catch.hpp"

#include "../Tile.h"

std::vector<int> plainsEdges(4, TerrainType::Plains);
std::vector<int> roadsEdges(4, TerrainType::Road);
std::vector<int> citiesEdges(4, TerrainType::City);
std::vector<int> halfCityWithPlainsEdges = {TerrainType::Plains, TerrainType::Plains, TerrainType::City, TerrainType::City };
std::vector<int> halfCityWithRoadsEdges = {TerrainType::Road, TerrainType::Road, TerrainType::City, TerrainType::City };

Tile allTrueTile(true, true, true, true, plainsEdges, 'A');
Tile allFalseTile(false, false, false, false, plainsEdges, 'B');
Tile allPlainsTile(false, false, false, false, plainsEdges, 'C');
Tile allCitiesTile(false, false, false, false, citiesEdges, 'D');
Tile allRoadsTile(false, true, false, false, roadsEdges, 'E');
Tile halfConnectedCityWithPlainsTile(false, false, false, false, halfCityWithPlainsEdges, 'F');
Tile halfUnconnectedCityWithPlainsTile(false, false, true, false, halfCityWithPlainsEdges, 'G');
Tile halfConnectedCityWithRoadsTile(false, false, false, false, halfCityWithRoadsEdges, 'H');
Tile halfUnconnectedCityWithRoadsTile(false, false, true, false, halfCityWithRoadsEdges, 'I');

TEST_CASE("Tile booleans are set properly", "[tile]"){
	REQUIRE(allTrueTile.GetHasMonastery() == true);
	REQUIRE(allFalseTile.GetHasMonastery() == false);
	REQUIRE(allTrueTile.GetRoadsEnd() == true);
	REQUIRE(allFalseTile.GetRoadsEnd() == false);
	REQUIRE(allTrueTile.GetCitiesAreIndependent() == true);
	REQUIRE(allFalseTile.GetCitiesAreIndependent() == false);
	REQUIRE(allTrueTile.GetHasShield() == true);
	REQUIRE(allFalseTile.GetHasShield() == false);
}

TEST_CASE("Tile edges are set properly", "[tile]") {
	REQUIRE(allPlainsTile.GetEdges() == plainsEdges);
	REQUIRE(allCitiesTile.GetEdges() == citiesEdges);
	REQUIRE(allRoadsTile.GetEdges() == roadsEdges);
	REQUIRE(halfConnectedCityWithPlainsTile.GetEdges() == halfUnconnectedCityWithPlainsTile.GetEdges());
	REQUIRE(halfConnectedCityWithPlainsTile.GetEdges() == halfCityWithPlainsEdges);
	REQUIRE(halfConnectedCityWithRoadsTile.GetEdges() == halfUnconnectedCityWithRoadsTile.GetEdges());
	REQUIRE(halfConnectedCityWithRoadsTile.GetEdges() == halfCityWithRoadsEdges);
}

TEST_CASE("Tile rotate function works properly", "[tile]") {
	//Originally plains, plains, city, city
	std::vector<int> StartingRotation = halfConnectedCityWithPlainsTile.GetEdges();
	std::vector<int> OneRotation = { TerrainType::City,	TerrainType::Plains, TerrainType::Plains, TerrainType::City };
	std::vector<int> TwoRotations = { TerrainType::City, TerrainType::City, TerrainType::Plains, TerrainType::Plains };
	std::vector<int> ThreeRotations = { TerrainType::Plains, TerrainType::City, TerrainType::City, TerrainType::Plains };
	halfConnectedCityWithPlainsTile.RotateClockwise(4);
	REQUIRE(halfConnectedCityWithPlainsTile.GetEdges() == StartingRotation);
	halfConnectedCityWithPlainsTile.RotateClockwise(0);
	REQUIRE(halfConnectedCityWithPlainsTile.GetEdges() == StartingRotation);

	halfConnectedCityWithPlainsTile.RotateClockwise(1);
	REQUIRE(halfConnectedCityWithPlainsTile.GetEdges() == OneRotation);
	halfConnectedCityWithPlainsTile.RotateClockwise(1);
	REQUIRE(halfConnectedCityWithPlainsTile.GetEdges() == TwoRotations);
	halfConnectedCityWithPlainsTile.RotateClockwise(1);
	REQUIRE(halfConnectedCityWithPlainsTile.GetEdges() == ThreeRotations);
	halfConnectedCityWithPlainsTile.RotateClockwise(1);
	REQUIRE(halfConnectedCityWithPlainsTile.GetEdges() == StartingRotation);

	halfConnectedCityWithPlainsTile.RotateClockwise(2);
	REQUIRE(halfConnectedCityWithPlainsTile.GetEdges() == TwoRotations);
	halfConnectedCityWithPlainsTile.RotateClockwise(2);
	REQUIRE(halfConnectedCityWithPlainsTile.GetEdges() == StartingRotation);

	halfConnectedCityWithPlainsTile.RotateClockwise(3);
	REQUIRE(halfConnectedCityWithPlainsTile.GetEdges() == ThreeRotations);
	halfConnectedCityWithPlainsTile.RotateClockwise(3);
	REQUIRE(halfConnectedCityWithPlainsTile.GetEdges() == TwoRotations);
	halfConnectedCityWithPlainsTile.RotateClockwise(3);
	REQUIRE(halfConnectedCityWithPlainsTile.GetEdges() == OneRotation);
	halfConnectedCityWithPlainsTile.RotateClockwise(3);
	REQUIRE(halfConnectedCityWithPlainsTile.GetEdges() == StartingRotation);

	halfConnectedCityWithPlainsTile.RotateClockwise(-1);
	REQUIRE(halfConnectedCityWithPlainsTile.GetEdges() == ThreeRotations);
	halfConnectedCityWithPlainsTile.RotateClockwise(-3);
	REQUIRE(halfConnectedCityWithPlainsTile.GetEdges() == StartingRotation);

	halfConnectedCityWithPlainsTile.RotateClockwise(5);
	REQUIRE(halfConnectedCityWithPlainsTile.GetEdges() == OneRotation);
	halfConnectedCityWithPlainsTile.RotateClockwise(-5);
	REQUIRE(halfConnectedCityWithPlainsTile.GetEdges() == StartingRotation);
}

TEST_CASE("Tile equality operator works properly", "[tile]") {
	Tile allTrueTileCopy = allTrueTile;
	REQUIRE(allTrueTile == allTrueTileCopy);
	allTrueTileCopy.RotateClockwise(2);
	REQUIRE(allTrueTile == allTrueTileCopy);
	
	//This test is not actually a requirement of our system, but could be in the future
	//if the tileset becomes not under our control
	//Tile allTrueTileWithDifferentChar(true, true, true, true, plainsEdges, 'Z');
	//REQUIRE(allTrueTile == allTrueTileWithDifferentChar);

	REQUIRE_FALSE(allTrueTile == allFalseTile);
	REQUIRE_FALSE(allTrueTile == allCitiesTile);
	REQUIRE_FALSE(allTrueTile == halfConnectedCityWithPlainsTile);
	
	REQUIRE_FALSE(halfConnectedCityWithPlainsTile == halfUnconnectedCityWithPlainsTile);
	REQUIRE_FALSE(halfConnectedCityWithRoadsTile == halfUnconnectedCityWithRoadsTile);

	REQUIRE_FALSE(allTrueTile == Tile());
}