#include "Meeple.h"

Meeple::Meeple(int ownerId){
	this->ownerId = ownerId;
	this->status = 0;
	location.x = -1;
	location.y = -1;
	return;
}

void Meeple::updateStatus(int status){
	this->status = status;
	return;
}

void Meeple::updateLocation(int x, int y){
	location.x = x;
	location.y = y;
}

int Meeple::getOwnerId(){
	return ownerId;
}

int Meeple::getLocationX(){
	return location.x;
}

int Meeple::getLocationY(){
	return location.y;
}

int Meeple::getStatus(){
	return status;
}

Meeple::~Meeple() {

	return;
}
