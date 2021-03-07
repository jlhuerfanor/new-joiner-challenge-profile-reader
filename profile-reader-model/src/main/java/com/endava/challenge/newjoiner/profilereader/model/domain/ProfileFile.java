package com.endava.challenge.newjoiner.profilereader.model.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.InputStream;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ProfileFile {
    private String filename;
    private FileType fileType;
    private long size;
    private InputStream contentStream;
}
