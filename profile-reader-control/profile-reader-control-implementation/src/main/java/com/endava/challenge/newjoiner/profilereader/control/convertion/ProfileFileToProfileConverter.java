package com.endava.challenge.newjoiner.profilereader.control.convertion;

import com.endava.challenge.newjoiner.profilereader.control.converter.ReactiveTypeConverter;
import com.endava.challenge.newjoiner.profilereader.control.reader.ProfileReadingStrategy;
import com.endava.challenge.newjoiner.profilereader.model.domain.Profile;
import com.endava.challenge.newjoiner.profilereader.model.domain.ProfileFile;
import org.springframework.core.convert.converter.Converter;
import reactor.core.publisher.Mono;

public class ProfileFileToProfileConverter implements ReactiveTypeConverter<ProfileFile, Profile> {
    private final ProfileReadingStrategy profileReadingStrategy;

    public ProfileFileToProfileConverter(ProfileReadingStrategy profileReadingStrategy) {
        this.profileReadingStrategy = profileReadingStrategy;
    }

    @Override
    public Mono<Profile> convert(ProfileFile profileFile) {
        return Mono.just(profileFile)
                .map(profileReadingStrategy::read);
    }
}
