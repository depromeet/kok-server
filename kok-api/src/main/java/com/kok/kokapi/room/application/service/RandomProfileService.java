package com.kok.kokapi.room.application.service;

import com.kok.kokcore.room.domain.Profile;
import com.kok.kokcore.room.usecase.CreateRandomProfileUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RandomProfileService implements CreateRandomProfileUseCase {

    @Value("${ncp.object-storage-url}")
    private String objectStorageUrl;

    private static final List<String> ADJECTIVES = List.of(
            "배고픈", "멋있는", "쿨한 ", "빠른", "친절한", "영리한", "행복한", "조용한", "강력한", "자유로운"
    );

    private static final List<String> NOUNS = List.of(
            "토미", "지미", "라이언", "루카스", "데이빗", "엠마", "올리버", "소피", "봉봉이", "피치"
    );

    
    @Override
    public Profile crateProfile() {
        String imageUrl = getRandomImageUrl();
        String nickname = generateRandomNickname();
        return new Profile(imageUrl, nickname);
    }

    private String getRandomImageUrl() {
        int randomImageIndex = new Random().nextInt(9) + 1;
        return objectStorageUrl + "/profile_focus/" + randomImageIndex + ".svg";
    }

    private String generateRandomNickname() {
        String adjective = ADJECTIVES.get(new Random().nextInt(ADJECTIVES.size()));
        String noun = NOUNS.get(new Random().nextInt(NOUNS.size()));
        return adjective + " " + noun;
    }
}
