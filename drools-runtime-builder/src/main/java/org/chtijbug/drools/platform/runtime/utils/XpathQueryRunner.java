package org.chtijbug.drools.platform.runtime.utils;

import com.google.common.base.Throwables;
import org.apache.commons.io.IOUtils;
import org.springframework.util.xml.SimpleNamespaceContext;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static javax.xml.xpath.XPathConstants.NODESET;
import static javax.xml.xpath.XPathConstants.STRING;

public class XpathQueryRunner {
    private byte[] originalSource;
    private XPath xPath;

    public XpathQueryRunner(InputStream sourceXml) throws IOException {
        this(IOUtils.toByteArray(sourceXml));
    }

    public XpathQueryRunner(byte[] originalSource) {
        this.originalSource = originalSource;
        this.xPath = XPathFactory.newInstance().newXPath();
        SimpleNamespaceContext nc = new SimpleNamespaceContext();
        nc.bindNamespaceUri("wsdl", "http://schemas.xmlsoap.org/wsdl/");
        nc.bindNamespaceUri("xsd", "http://www.w3.org/2001/XMLSchema");
        this.xPath.setNamespaceContext(nc);
    }

    public String executeXpath(String xPathQuery) {
        try {
            XPathExpression xPathExpression = xPath.compile(xPathQuery);
            return (String) xPathExpression.evaluate(getSource(), STRING);
        } catch (XPathExpressionException e) {
            throw Throwables.propagate(e);
        }
    }

    private InputSource getSource() {
        return new InputSource(new ByteArrayInputStream(this.originalSource));
    }

    public List<String> executeXpathAsList(String xPathQuery) throws XPathExpressionException {
        List<String> items = new ArrayList<String>();
        XPathExpression xPathExpression = xPath.compile(xPathQuery);
        NodeList nodeSet = (NodeList) xPathExpression.evaluate(getSource(), NODESET);
        for (int i = 0; i < nodeSet.getLength(); i++) {
            items.add(nodeSet.item(i).getNodeValue());
        }
        return items;
    }
}
