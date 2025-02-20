package com.kok.kokapi.room.adapter.in.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomRequest {

    @NotBlank(message = "방 이름은 필수 입력값입니다.")
    @Size(max = 30, message = "방 이름은 최대 30자까지 가능합니다.")
    private String roomName;

    @Min(value = 2, message = "참여 인원 수는 최소 2명 이상이어야 합니다.")
    private Integer capacity;

    private String hostProfile;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}
