package com.kok.kokcore.room.domain.vo;

public enum RoomStatus {

    LOCATION_INPUT,
    VOTE,
    VOTE_RESULT;

    public boolean isLocationInput() {
        return this.equals(LOCATION_INPUT);
    }
}
