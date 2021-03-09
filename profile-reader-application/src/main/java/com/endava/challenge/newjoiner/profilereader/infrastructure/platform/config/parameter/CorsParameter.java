package com.endava.challenge.newjoiner.profilereader.infrastructure.platform.config.parameter;

import com.endava.challenge.newjoiner.profilereader.infrastructure.platform.web.security.CorsMapping;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class CorsParameter {
    private List<CorsMapping> mappings;
}
