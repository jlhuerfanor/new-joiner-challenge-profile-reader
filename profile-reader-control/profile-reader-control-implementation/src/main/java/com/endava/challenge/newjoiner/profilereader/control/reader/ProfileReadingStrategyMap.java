package com.endava.challenge.newjoiner.profilereader.control.reader;

import com.endava.challenge.newjoiner.profilereader.model.domain.FileType;
import com.endava.challenge.newjoiner.profilereader.model.domain.Profile;
import com.endava.challenge.newjoiner.profilereader.model.domain.ProfileFile;

import java.util.HashMap;
import java.util.Map;

public class ProfileReadingStrategyMap implements ProfileReadingStrategy {
    private final Map<FileType, ProfileReader> readersPerFileType;

    public ProfileReadingStrategyMap(Map<FileType, ProfileReader> readersPerFileType) {
        this.readersPerFileType = readersPerFileType;
    }

    public ProfileReadingStrategyMap() {
        this(new HashMap<>());
    }

    @Override
    public Profile read(ProfileFile file) {
        if(!readersPerFileType.containsKey(file.getFileType())) {
            throw new ReaderNotFoundException(file.getFileType());
        }

        return readersPerFileType.get(file.getFileType())
                .read(file);
    }

    @Override
    public ProfileReadingStrategy registerReader(FileType fileType, ProfileReader reader) {
        readersPerFileType.put(fileType, reader);
        return this;
    }

}
