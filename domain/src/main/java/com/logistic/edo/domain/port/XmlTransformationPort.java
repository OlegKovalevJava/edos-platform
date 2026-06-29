package com.logistic.edo.domain.port;

import com.logistic.edo.domain.model.Document;

public interface XmlTransformationPort {

    String transformToXml(Document document);
    Document transformFromXml(String xml);
}
