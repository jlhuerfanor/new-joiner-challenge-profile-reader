package com.endava.challenge.newjoiner.profilereader.business.reader;

import com.endava.challenge.newjoiner.profilereader.control.converter.Converter;
import com.endava.challenge.newjoiner.profilereader.control.validation.Validation;
import com.endava.challenge.newjoiner.profilereader.model.domain.Profile;
import com.endava.challenge.newjoiner.profilereader.model.domain.ProfileFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

public class ProfileReaderBusiness {

    private final Converter converter;
    private final List<Validation<Profile>> profileValidationList = profileValidations();
    private final List<Validation<ProfileFile>> fileValidationList = fileValidations();

    public ProfileReaderBusiness(Converter converter) {
        this.converter = converter;
    }

    public Mono<Profile> process(ProfileFile fileToProcess) {
        return Mono.just(fileToProcess)
                .flatMapMany(file -> Flux.fromIterable(fileValidationList)
                        .map(validation -> validation.validate(file)))
                .last()
                .map(converter.convertTo(ProfileFile.class, Profile.class))
                .flatMapMany(pf -> Flux.fromIterable(profileValidationList)
                        .map(validation -> validation.validate(pf)))
                .last();
    }

    private static List<Validation<Profile>> profileValidations() {
        return List.of(
                Validation.<Profile>create()
                        .when(profile -> Objects.isNull(profile.getIdNumber()))
                        .then(Validation.ValidationException.withMessage("ID Number is required.")),
                Validation.<Profile>create()
                        .when(profile -> Objects.isNull(profile.getFirstName()))
                        .or(profile -> profile.getFirstName().isBlank())
                        .then(Validation.ValidationException.withMessage("First Name is required.")),
                Validation.<Profile>create()
                        .when(profile -> Objects.isNull(profile.getLastName()))
                        .or(profile -> profile.getLastName().isBlank())
                        .then(Validation.ValidationException.withMessage("Last Name is required.")),
                Validation.<Profile>create()
                        .when(profile -> Objects.isNull(profile.getStack()))
                        .or(profile -> profile.getStack().isBlank())
                        .then(Validation.ValidationException.withMessage("Stack is required.")),
                Validation.<Profile>create()
                        .when(profile -> Objects.isNull(profile.getRole()))
                        .or(profile -> profile.getRole().isBlank())
                        .then(Validation.ValidationException.withMessage("Role is required.")),
                Validation.<Profile>create()
                        .when(profile -> Objects.isNull(profile.getEnglishLevel()))
                        .or(profile -> profile.getEnglishLevel().isBlank())
                        .then(Validation.ValidationException.withMessage("English Level is required.")),
                Validation.<Profile>create()
                        .when(profile -> Objects.isNull(profile.getDomainExperience()))
                        .or(profile -> profile.getDomainExperience().isBlank())
                        .then(Validation.ValidationException.withMessage("Domain Experience is required."))
        );
    }

    private static List<Validation<ProfileFile>> fileValidations() {
        return List.of(
                Validation.<ProfileFile>create()
                        .when(profileFile -> Objects.isNull(profileFile.getFilename()))
                        .or(profileFile -> profileFile.getFilename().isBlank())
                        .then(Validation.ValidationException.withMessage("Filename must not be empty")),
                Validation.<ProfileFile>create()
                        .when(profileFile -> Objects.isNull(profileFile.getFileType()))
                        .then(Validation.ValidationException.withMessage("File Type is not recognized.")),
                Validation.<ProfileFile>create()
                        .when(profileFile -> profileFile.getSize() <= 0)
                        .then(Validation.ValidationException.withMessage("File is empty.")),
                Validation.<ProfileFile>create()
                        .when(profileFile -> Objects.isNull(profileFile.getContentStream()))
                        .then(Validation.ValidationException.withMessage("Stream was not provided."))
                );
    }
}
