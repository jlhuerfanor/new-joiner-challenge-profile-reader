package com.endava.challenge.newjoiner.profilereader.model.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum FileType {
    PDF("application/pdf"),
    WORD("application/msword")
    ;
    private final String mediaType;

    private static final Map<String, FileType> mapByMediaType = Arrays.stream(FileType.values())
            .collect(Collectors.toMap(FileType::getMediaType, Function.identity()));

    public static FileType getByMediaType(String mediaType) {
        return mapByMediaType.get(mediaType);
    }
}
