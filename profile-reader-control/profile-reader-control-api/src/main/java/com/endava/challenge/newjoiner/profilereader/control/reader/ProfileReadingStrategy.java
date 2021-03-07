package com.endava.challenge.newjoiner.profilereader.control.reader;

import com.endava.challenge.newjoiner.profilereader.model.domain.FileType;

public interface ProfileReadingStrategy extends ProfileReader {
    ProfileReadingStrategy registerReader(FileType fileType, ProfileReader reader);

    class ReaderNotFoundException extends IllegalArgumentException {
        public ReaderNotFoundException(FileType fileType) {
            super(String.format("There is not a reader for the media type %s", fileType.getMediaType()));
        }
    }
}
