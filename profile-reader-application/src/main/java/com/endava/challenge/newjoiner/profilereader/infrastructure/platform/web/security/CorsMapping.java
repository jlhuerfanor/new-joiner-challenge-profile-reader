package com.endava.challenge.newjoiner.profilereader.infrastructure.platform.web.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class CorsMapping {
    private String pattern;
    private List<String> origins;
    private List<String> methods;
    private List<String> headers;
}
