package com.endava.challenge.newjoiner.profilereader.control.convertion;

import com.endava.challenge.newjoiner.profilereader.control.reader.ProfileReadingStrategy;
import com.endava.challenge.newjoiner.profilereader.model.domain.Profile;
import com.endava.challenge.newjoiner.profilereader.model.domain.ProfileFile;
import org.springframework.core.convert.converter.Converter;

public class ProfileFileToProfileConverter implements Converter<ProfileFile, Profile> {
    private final ProfileReadingStrategy profileReadingStrategy;

    public ProfileFileToProfileConverter(ProfileReadingStrategy profileReadingStrategy) {
        this.profileReadingStrategy = profileReadingStrategy;
    }

    @Override
    public Profile convert(ProfileFile profileFile) {
        return profileReadingStrategy.read(profileFile);
    }
}
