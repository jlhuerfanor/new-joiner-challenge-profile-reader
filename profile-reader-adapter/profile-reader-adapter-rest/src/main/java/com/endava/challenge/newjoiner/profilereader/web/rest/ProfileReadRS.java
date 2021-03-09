package com.endava.challenge.newjoiner.profilereader.web.rest;


import com.endava.challenge.newjoiner.profilereader.business.converter.ReactiveConversionBusiness;
import com.endava.challenge.newjoiner.profilereader.business.reader.ProfileReaderBusiness;
import com.endava.challenge.newjoiner.profilereader.model.domain.Profile;
import com.endava.challenge.newjoiner.profilereader.model.domain.ProfileFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/profile")
public class ProfileReadRS {
    private static Logger log = LoggerFactory.getLogger(ProfileReadRS.class);

    private final ReactiveConversionBusiness reactiveConversionBusiness;
    private final ProfileReaderBusiness profileReaderBusiness;

    @Autowired
    public ProfileReadRS(ReactiveConversionBusiness conversionService, ProfileReaderBusiness profileReaderBusiness) {
        this.reactiveConversionBusiness = conversionService;
        this.profileReaderBusiness = profileReaderBusiness;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<Profile> readProfileFile(@RequestPart("file") FilePart file) {
        return Mono.just(file)
                .flatMap(this.reactiveConversionBusiness.convertMono(FilePart.class, ProfileFile.class))
                .flatMap(this.profileReaderBusiness::process);
    }
}
