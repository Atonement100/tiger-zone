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
Tile halfConnectedCityWithPlainsTiles(false, false, false, false, halfCityWithPlainsEdges, 'F');
Tile halfUnconnectedCityWithPlainsTiles(false, false, true, false, halfCityWithPlainsEdges, 'G');
Tile halfConnectedCityWithRoadsTiles(false, false, false, false, halfCityWithRoadsEdges, 'H');
Tile halfUnconnectedCityWithRoadsTiles(false, false, true, false, halfCityWithRoadsEdges, 'I');

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
	REQUIRE(halfConnectedCityWithPlainsTiles.GetEdges() == halfUnconnectedCityWithPlainsTiles.GetEdges());
	REQUIRE(halfConnectedCityWithPlainsTiles.GetEdges() == halfCityWithPlainsEdges);
	REQUIRE(halfConnectedCityWithRoadsTiles.GetEdges() == halfUnconnectedCityWithRoadsTiles.GetEdges());
	REQUIRE(halfConnectedCityWithRoadsTiles.GetEdges() == halfCityWithRoadsEdges);
}

TEST_CASE("Tile rotate function works properly", "[tile]") {
	//Originally plains, plains, city, city
	std::vector<int> StartingRotation = halfConnectedCityWithPlainsTiles.GetEdges();
	std::vector<int> OneRotation = { TerrainType::City,	TerrainType::Plains, TerrainType::Plains, TerrainType::City };
	std::vector<int> TwoRotations = { TerrainType::City, TerrainType::City, TerrainType::Plains, TerrainType::Plains };
	halfConnectedCityWithPlainsTiles.RotateClockwise(4);
	REQUIRE(halfConnectedCityWithPlainsTiles.GetEdges() == StartingRotation);
	halfConnectedCityWithPlainsTiles.RotateClockwise(3);
	REQUIRE(halfConnectedCityWithPlainsTiles.GetEdges() == StartingRotation);
}