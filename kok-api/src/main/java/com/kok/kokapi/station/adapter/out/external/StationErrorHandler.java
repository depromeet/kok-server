package com.kok.kokapi.station.adapter.out.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kok.kokapi.station.adapter.out.external.dto.StationResponses;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

@Component
@RequiredArgsConstructor
public class StationErrorHandler implements ResponseErrorHandler {

    private static final List<String> SERVER_ERROR = List.of("ERROR-500", "ERROR-600", "ERROR-601");
    private final ObjectMapper objectMapper;

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        String code = objectMapper.readValue(response.getBody(), StationResponses.class)
            .subwayStationMaster()
            .result()
            .code();
        return SERVER_ERROR.contains(code);
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        throw new RuntimeException("서버 오류입니다. 지속적으로 발생시 열린 데이터 광장으로 문의(Q&A) 바랍니다.");
    }
}
