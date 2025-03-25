package com.kok.kokcore.vote.application.port.out;

public interface LoadVotePort {

    boolean isExistsByRoomIdAndMemberId(String roomId, String memberId);
}
