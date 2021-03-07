package com.endava.challenge.newjoiner.profilereader.model.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Profile {
    private BigInteger idNumber;
    private String firstName;
    private String lastName;
    private String stack;
    private String role;
    private String englishLevel;
    private String domainExperience;
    private String filename;
}
