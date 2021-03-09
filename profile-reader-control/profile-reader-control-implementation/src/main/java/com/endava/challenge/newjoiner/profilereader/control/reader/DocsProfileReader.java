package com.endava.challenge.newjoiner.profilereader.control.reader;

import com.endava.challenge.newjoiner.profilereader.model.domain.Profile;
import com.endava.challenge.newjoiner.profilereader.model.domain.ProfileFile;
import org.apache.poi.EmptyFileException;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.xwpf.usermodel.XWPFAbstractSDT;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFSDT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DocsProfileReader implements ProfileReader {
    private static final Logger log = LoggerFactory.getLogger(DocsProfileReader.class);

    @Override
    public Profile read(ProfileFile file) {
        try(var document = new XWPFDocument(file.getContentStream())) {
            var bodyElements = document.getBodyElements();

            var elementsInBody = bodyElements.stream()
                    .filter(XWPFSDT.class::isInstance)
                    .map(XWPFSDT.class::cast);
            var elementsInParagraphs = bodyElements.stream()
                    .filter(XWPFParagraph.class::isInstance)
                    .map(XWPFParagraph.class::cast)
                    .map(XWPFParagraph::getIRuns)
                    .flatMap(List::stream)
                    .filter(XWPFSDT.class::isInstance)
                    .map(XWPFSDT.class::cast);

            var fieldMap = Stream.concat(elementsInBody, elementsInParagraphs)
                    .collect(Collectors.toMap(XWPFAbstractSDT::getTitle,
                            std -> std.getContent().getText()));

            return ProfileReader.fromMap(file.getFilename(), fieldMap);
        } catch (IOException | EmptyFileException | NotOfficeXmlFileException e) {
            throw new WordFileAccessException(e);
        }
    }

    public static class WordFileAccessException extends RuntimeException {
        public WordFileAccessException(Throwable e) {
            super(e);
        }
    }
}
