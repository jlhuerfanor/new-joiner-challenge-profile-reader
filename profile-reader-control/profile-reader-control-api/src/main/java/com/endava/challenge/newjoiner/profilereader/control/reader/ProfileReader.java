package com.endava.challenge.newjoiner.profilereader.control.reader;

import com.endava.challenge.newjoiner.profilereader.control.validation.Validation;
import com.endava.challenge.newjoiner.profilereader.model.domain.Profile;
import com.endava.challenge.newjoiner.profilereader.model.domain.ProfileFile;

import java.math.BigInteger;
import java.util.*;

public interface ProfileReader {
    Profile read(ProfileFile file);

    static Profile fromMap(String filename, Map<String, String> fieldMap) {
        return Profile.builder()
                .filename(filename)
                .idNumber(Optional.of("IDNumber")
                        .map(fieldMap::get)
                        .map(Validation.<String>create()
                                .when(s -> !s.matches("\\d+"))
                                .then(Validation.ValidationException.withMessage("ID Number is not valid."))
                                ::validate)
                        .filter(s -> s.matches("\\d+"))
                        .map(BigInteger::new)
                        .orElse(null))
                .firstName(fieldMap.get("FirstName"))
                .lastName(fieldMap.get("LastName"))
                .stack(fieldMap.get("Stack"))
                .role(fieldMap.get("Role"))
                .englishLevel(fieldMap.get("EnglishLevel"))
                .domainExperience(fieldMap.get("DomainExperience"))
                .build();
    }
}
