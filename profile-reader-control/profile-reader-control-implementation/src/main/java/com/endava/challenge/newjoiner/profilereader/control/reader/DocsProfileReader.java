package com.endava.challenge.newjoiner.profilereader.control.reader;

import com.endava.challenge.newjoiner.profilereader.model.domain.Profile;
import com.endava.challenge.newjoiner.profilereader.model.domain.ProfileFile;
import org.apache.poi.xwpf.usermodel.XWPFAbstractSDT;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFSDT;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtBlock;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import java.util.ArrayList;
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
        } catch (Exception e) {
            throw new WordFileAccessException(e);
        }
    }

    public List<XWPFSDT> getFromBody(XWPFDocument document) {
        var cursor = document.getDocument().getBody().newCursor();
        var qnameSdt = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "sdt", "w");
        var allStds = new ArrayList<XWPFSDT>();

        while(cursor.hasNextToken()) {
            var tokenType = cursor.toNextToken();

            if (tokenType.isStart()) {
                var name = cursor.getName();
                if (qnameSdt.equals(name)) {
                    var obj = cursor.getObject();
                    if (obj instanceof CTSdtRun) {
                        allStds.add(new XWPFSDT((CTSdtRun)cursor.getObject(), document));
                    } else if (obj instanceof CTSdtBlock) {
                        allStds.add(new XWPFSDT((CTSdtBlock)cursor.getObject(), document));
                    }
                }
            }
        }

        return allStds;
    }

    public static class WordFileAccessException extends RuntimeException {
        public WordFileAccessException(Throwable e) {
            super(e);
        }
    }
}
