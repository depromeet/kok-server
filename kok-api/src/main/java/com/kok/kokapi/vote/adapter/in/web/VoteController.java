package com.kok.kokapi.vote.adapter.in.web;

import com.kok.kokapi.common.response.ApiResponseDto;
import com.kok.kokapi.config.annotion.V1Controller;
import com.kok.kokapi.vote.adapter.in.dto.request.VoteRequest;
import com.kok.kokapi.vote.adapter.in.dto.response.CandidateResponse;
import com.kok.kokapi.vote.adapter.in.dto.response.MemberVoteStatusResponse;
import com.kok.kokapi.vote.application.service.VoteFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@V1Controller
@RequiredArgsConstructor
public class VoteController {

    private final VoteFacadeService voteFacadeService;

    @Operation(summary = "투표 후보지 목록 조회", description = "방 ID과 사용자 ID를 기반으로 투표 후보지 상세 정보를 조회합니다.")
    @GetMapping("/votes/{roomId}/{memberId}/candidates")
    public ResponseEntity<ApiResponseDto<List<CandidateResponse>>> getCandidates(
        @PathVariable String roomId, @PathVariable String memberId) {
        List<CandidateResponse> responses = voteFacadeService.getCandidates(roomId, memberId);
        return ResponseEntity.ok(ApiResponseDto.success(responses));
    }

    @Operation(summary = "투표하기", description = "방 ID와 사용자 ID에 대한 찬성 투표 목록을 받아서 투표 정보를 저장합니다.")
    @PostMapping("/votes/{roomId}/{memberId}")
    public ResponseEntity<ApiResponseDto<Void>> createVote(
        @PathVariable String roomId,
        @PathVariable String memberId,
        @RequestBody VoteRequest voteRequest
    ) {
        voteFacadeService.createVote(roomId, memberId, voteRequest);
        return ResponseEntity.ok(ApiResponseDto.success(null));
    }

    @Operation(summary = "사용자별 투표 상태 조회", description = "방 ID에 대해 사용자 정보와 투표 상태(투표 전/투표 완료)를 조회합니다.")
    @GetMapping("/vote/{roomId}/status")
    public ResponseEntity<ApiResponseDto<List<MemberVoteStatusResponse>>> getMemberVoteStatus(
        @PathVariable String roomId) {
        List<MemberVoteStatusResponse> responses = voteFacadeService.getVoteMembersStatus(roomId);
        return ResponseEntity.ok(ApiResponseDto.success(responses));
    }
}
