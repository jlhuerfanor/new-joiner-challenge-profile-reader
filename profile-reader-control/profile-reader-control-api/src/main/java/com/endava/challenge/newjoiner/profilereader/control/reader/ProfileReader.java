package com.endava.challenge.newjoiner.profilereader.control.reader;

import com.endava.challenge.newjoiner.profilereader.model.domain.Profile;
import com.endava.challenge.newjoiner.profilereader.model.domain.ProfileFile;

public interface ProfileReader {
    Profile read(ProfileFile file);
}
