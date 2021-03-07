package com.endava.challenge.newjoiner.profilereader.control.reader;

import com.endava.challenge.newjoiner.profilereader.model.domain.Profile;
import com.endava.challenge.newjoiner.profilereader.model.domain.ProfileFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PdfProfileReader implements ProfileReader {
    private static Logger log = LoggerFactory.getLogger(PdfProfileReader.class);

    @Override
    public Profile read(ProfileFile file) {
        try (var document = PDDocument.load(file.getContentStream())){
            var fieldMap = Optional.of(document)
                    .map(PDDocument::getDocumentCatalog)
                    .map(PDDocumentCatalog::getAcroForm)
                    .map(PDAcroForm::getFields)
                    .map(fields -> fields.stream()
                            .collect(Collectors.toMap(PDField::getFullyQualifiedName, PDField::getValueAsString)))
                    .orElseGet(Map::of);

            return Profile.builder()
                    .filename(file.getFilename())
                    .idNumber(Optional.of("IDNumber")
                            .map(fieldMap::get)
                            .map(BigInteger::new)
                            .orElse(null))
                    .firstName(fieldMap.get("FirstName"))
                    .lastName(fieldMap.get("LastName"))
                    .stack(fieldMap.get("Stack"))
                    .role(fieldMap.get("Role"))
                    .englishLevel(fieldMap.get("EnglishLevel"))
                    .domainExperience(fieldMap.get("DomainExperience"))

                    .build();
        } catch (IOException e) {
            throw new PdfFileAccessException(e);
        }
    }

    public static class PdfFileAccessException extends RuntimeException {
        public PdfFileAccessException(Throwable e) {
            super(e);
        }
    }
}
