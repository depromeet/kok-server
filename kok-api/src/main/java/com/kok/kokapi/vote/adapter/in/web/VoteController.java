package com.kok.kokapi.vote.adapter.in.web;

import com.kok.kokapi.common.response.ApiResponseDto;
import com.kok.kokapi.config.annotion.V1Controller;
import com.kok.kokapi.vote.adapter.in.dto.response.CandidateResponse;
import com.kok.kokapi.vote.application.service.VoteFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
}
