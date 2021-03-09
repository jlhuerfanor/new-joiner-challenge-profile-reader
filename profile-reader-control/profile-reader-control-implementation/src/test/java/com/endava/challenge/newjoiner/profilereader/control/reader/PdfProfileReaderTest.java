package com.endava.challenge.newjoiner.profilereader.control.reader;

import com.endava.challenge.newjoiner.profilereader.model.domain.FileType;
import com.endava.challenge.newjoiner.profilereader.model.domain.Profile;
import com.endava.challenge.newjoiner.profilereader.model.domain.ProfileFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class PdfProfileReaderTest {

    private PdfProfileReader pdfProfileReader;
    private ProfileFile profileFile;
    private Profile profileObtained;

    @BeforeEach
    void setupTest() {
        pdfProfileReader = new PdfProfileReader();
    }

    @Test
    void readSuccessWithACompleteProfile() {
        givenAProfileFileWithACompleteProfile();
        whenReadTheProfile();
        thenGetACompleteProfile();
    }

    @Test
    void readSuccessWithAnEmptyDocument() {
        givenAnEmptyFile();
        whenReadTheProfile();
        thenGetAnEmptyProfile();
    }

    @Test
    void readFailDueToBadFormat() {
        givenANonPdfFile();
        assertThrows(PdfProfileReader.PdfFileAccessException.class, this::whenReadTheProfile);
    }

    private void givenANonPdfFile() {
        var contentStream = PdfProfileReaderTest.class.getResourceAsStream("/non-pdf-document.txt");
        this.profileFile = ProfileFile.builder()
                .filename("non-pdf-document.txt")
                .fileType(FileType.PDF)
                .size(2000)
                .contentStream(contentStream)
                .build();
    }

    private void givenAnEmptyFile() {
        var contentStream = PdfProfileReaderTest.class.getResourceAsStream("/empty-document.pdf");
        this.profileFile = ProfileFile.builder()
                .filename("empty-document.pdf")
                .fileType(FileType.PDF)
                .size(2000)
                .contentStream(contentStream)
                .build();
    }

    private void thenGetAnEmptyProfile() {
        assertNull(profileObtained.getIdNumber());
        assertNull(profileObtained.getFirstName());
        assertNull(profileObtained.getLastName());
        assertNull(profileObtained.getStack());
        assertNull(profileObtained.getRole());
        assertNull(profileObtained.getEnglishLevel());
        assertNull(profileObtained.getDomainExperience());
    }

    private void givenAProfileFileWithACompleteProfile() {
        var contentStream = PdfProfileReaderTest.class.getResourceAsStream("/profile-pdf-template-test.pdf");
        this.profileFile = ProfileFile.builder()
                .filename("profile-pdf-template-test.pdf")
                .fileType(FileType.PDF)
                .size(2000)
                .contentStream(contentStream)
                .build();
    }

    private void whenReadTheProfile() {
        this.profileObtained = this.pdfProfileReader.read(this.profileFile);
    }

    private void thenGetACompleteProfile() {
        assertEquals(new BigInteger("1023456789"), profileObtained.getIdNumber());
        assertEquals("John", profileObtained.getFirstName());
        assertEquals("Smith", profileObtained.getLastName());
        assertEquals("Duno", profileObtained.getStack());
        assertEquals("Developer", profileObtained.getRole());
        assertEquals("B1", profileObtained.getEnglishLevel());
        assertEquals("2bdfined", profileObtained.getDomainExperience());
    }
}