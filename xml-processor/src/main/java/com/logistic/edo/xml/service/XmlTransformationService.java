package com.logistic.edo.xml.service;

import com.edo.logistic.xml.document.Document;
import com.edo.logistic.xml.document.ObjectFactory;
import com.logistic.edo.domain.model.DocumentId;
import com.logistic.edo.domain.model.DocumentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.UUID;

@Service
public class XmlTransformationService {

    private static final Logger log = LoggerFactory.getLogger(XmlTransformationService.class);

    private final JAXBContext jaxbContext;
    private final ObjectFactory objectFactory;

    public XmlTransformationService() {
        try {
            log.info("Initializing JAXBContext for package: com.edo.logistic.xml.document");
            this.jaxbContext = JAXBContext.newInstance("com.edo.logistic.xml.document");
            this.objectFactory = new ObjectFactory();
            log.info("JAXBContext initialized successfully!");
        } catch (JAXBException e) {
            log.error("Failed to initialize JAXBContext", e);
            throw new RuntimeException("Failed to initialize JAXBContext", e);
        }
    }

    public String transformToXml(com.logistic.edo.domain.model.Document domainDocument) {
        log.info("Starting XML transformation for document ID: {}", domainDocument.getId());
        try {
            Document jaxbDocument = objectFactory.createDocument();
            jaxbDocument.setId(domainDocument.getId().value().toString());
            jaxbDocument.setStatus(domainDocument.getStatus().name());
            jaxbDocument.setContent(domainDocument.getXmlContent());
            jaxbDocument.setCompanyId(domainDocument.getCompanyId());
            jaxbDocument.setCreatedAt(convertToXMLGregorianCalendar(domainDocument.getCreatedAt()));

            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter writer = new StringWriter();
            marshaller.marshal(jaxbDocument, writer);
            String xml = writer.toString();

            log.info("Successfully transformed document {} to XML. XML length: {}", domainDocument.getId(), xml.length());
            log.debug("XML content: {}", xml);
            return xml;

        } catch (JAXBException e) {
            log.error("Failed to marshal document: {}", domainDocument.getId(), e);
            throw new RuntimeException("Failed to marshal document", e);
        }
    }

    public com.logistic.edo.domain.model.Document transformFromXml(String xml) {
        log.info("Starting XML transformation from XML string");
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(xml);
            Document jaxbDocument = (Document) unmarshaller.unmarshal(reader);

            log.info("Successfully unmarshalled XML to JAXB object");
            return com.logistic.edo.domain.model.Document.reconstitute(
                    new DocumentId(UUID.fromString(jaxbDocument.getId())),
                    DocumentStatus.valueOf(jaxbDocument.getStatus()),
                    jaxbDocument.getContent(),
                    jaxbDocument.getCompanyId(),
                    convertToInstant(jaxbDocument.getCreatedAt()),
                    convertToInstant(jaxbDocument.getCreatedAt())
            );

        } catch (JAXBException e) {
            log.error("Failed to unmarshal XML", e);
            throw new RuntimeException("Failed to unmarshal XML", e);
        }
    }

    private XMLGregorianCalendar convertToXMLGregorianCalendar(Instant instant) {
        try {
            GregorianCalendar calendar = GregorianCalendar.from(instant.atZone(ZoneId.systemDefault()));
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        } catch (DatatypeConfigurationException e) {
            log.error("Failed to convert Instant to XMLGregorianCalendar", e);
            throw new RuntimeException("Failed to convert Instant to XMLGregorianCalendar", e);
        }
    }

    private Instant convertToInstant(XMLGregorianCalendar xmlGregorianCalendar) {
        return xmlGregorianCalendar.toGregorianCalendar().toInstant();
    }
}
