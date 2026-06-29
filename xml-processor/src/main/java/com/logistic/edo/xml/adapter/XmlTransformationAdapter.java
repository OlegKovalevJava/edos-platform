package com.logistic.edo.xml.adapter;

import com.logistic.edo.domain.model.Document;
import com.logistic.edo.domain.port.XmlTransformationPort;
import com.logistic.edo.xml.service.XmlTransformationService;
import org.springframework.stereotype.Component;

@Component
public class XmlTransformationAdapter implements XmlTransformationPort {

    private final XmlTransformationService xmlTransformationService;

    public XmlTransformationAdapter(XmlTransformationService xmlTransformationService) {
        this.xmlTransformationService = xmlTransformationService;
    }

    @Override
    public String transformToXml(Document document) {
        return xmlTransformationService.transformToXml(document);
    }

    @Override
    public Document transformFromXml(String xml) {
        return xmlTransformationService.transformFromXml(xml);
    }
}
