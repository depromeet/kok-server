package com.kok.kokapi.room.adapter.inbound.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomRequest {
    private String roomName;
    private int capacity;
    private String hostProfile;
    private String password;
}