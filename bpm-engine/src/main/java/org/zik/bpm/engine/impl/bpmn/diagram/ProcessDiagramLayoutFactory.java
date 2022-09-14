// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.diagram;

import org.zik.bpm.engine.impl.context.Context;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPathFactory;
import java.util.Iterator;
import org.w3c.dom.Node;
import java.util.TreeMap;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.repository.DiagramElement;
import org.zik.bpm.engine.repository.DiagramNode;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.zik.bpm.engine.repository.DiagramLayout;
import java.io.InputStream;
import java.util.Map;

public class ProcessDiagramLayoutFactory
{
    private static final int GREY_THRESHOLD = 175;
    private static final Map<String, Boolean> XXE_FEATURES;
    
    public DiagramLayout getProcessDiagramLayout(final InputStream bpmnXmlStream, final InputStream imageStream) {
        final Document bpmnModel = this.parseXml(bpmnXmlStream);
        return this.getBpmnProcessDiagramLayout(bpmnModel, imageStream);
    }
    
    public DiagramLayout getBpmnProcessDiagramLayout(final Document bpmnModel, final InputStream imageStream) {
        if (imageStream == null) {
            return null;
        }
        final DiagramNode diagramBoundsXml = this.getDiagramBoundsFromBpmnDi(bpmnModel);
        DiagramNode diagramBoundsImage;
        if (this.isExportedFromAdonis50(bpmnModel)) {
            final int offsetTop = 29;
            final int offsetBottom = 61;
            diagramBoundsImage = this.getDiagramBoundsFromImage(imageStream, offsetTop, offsetBottom);
        }
        else {
            diagramBoundsImage = this.getDiagramBoundsFromImage(imageStream);
        }
        final Map<String, DiagramNode> listOfBounds = new HashMap<String, DiagramNode>();
        listOfBounds.put(diagramBoundsXml.getId(), diagramBoundsXml);
        listOfBounds.putAll(this.fixFlowNodePositionsIfModelFromAdonis(bpmnModel, this.getElementBoundsFromBpmnDi(bpmnModel)));
        final Map<String, DiagramElement> listOfBoundsForImage = this.transformBoundsForImage(diagramBoundsImage, diagramBoundsXml, listOfBounds);
        return new DiagramLayout(listOfBoundsForImage);
    }
    
    protected Document parseXml(final InputStream bpmnXmlStream) {
        final DocumentBuilderFactory factory = this.getConfiguredDocumentBuilderFactory();
        Document bpmnModel;
        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            bpmnModel = builder.parse(bpmnXmlStream);
        }
        catch (Exception e) {
            throw new ProcessEngineException("Error while parsing BPMN model.", e);
        }
        return bpmnModel;
    }
    
    protected DiagramNode getDiagramBoundsFromBpmnDi(final Document bpmnModel) {
        Double minX = null;
        Double minY = null;
        Double maxX = null;
        Double maxY = null;
        final NodeList setOfBounds = bpmnModel.getElementsByTagNameNS("http://www.omg.org/spec/DD/20100524/DC", "Bounds");
        for (int i = 0; i < setOfBounds.getLength(); ++i) {
            final Element element = (Element)setOfBounds.item(i);
            final Double x = Double.valueOf(element.getAttribute("x"));
            final Double y = Double.valueOf(element.getAttribute("y"));
            final Double width = Double.valueOf(element.getAttribute("width"));
            final Double height = Double.valueOf(element.getAttribute("height"));
            if (x != 0.0 || y != 0.0 || width != 0.0 || height != 0.0) {
                if (minX == null || x < minX) {
                    minX = x;
                }
                if (minY == null || y < minY) {
                    minY = y;
                }
                if (maxX == null || maxX < x + width) {
                    maxX = x + width;
                }
                if (maxY == null || maxY < y + height) {
                    maxY = y + height;
                }
            }
        }
        final NodeList waypoints = bpmnModel.getElementsByTagNameNS("http://www.omg.org/spec/DD/20100524/DI", "waypoint");
        for (int j = 0; j < waypoints.getLength(); ++j) {
            final Element waypoint = (Element)waypoints.item(j);
            final Double x2 = Double.valueOf(waypoint.getAttribute("x"));
            final Double y2 = Double.valueOf(waypoint.getAttribute("y"));
            if (minX == null || x2 < minX) {
                minX = x2;
            }
            if (minY == null || y2 < minY) {
                minY = y2;
            }
            if (maxX == null || maxX < x2) {
                maxX = x2;
            }
            if (maxY == null || maxY < y2) {
                maxY = y2;
            }
        }
        final DiagramNode diagramBounds = new DiagramNode("BPMNDiagram");
        diagramBounds.setX(minX);
        diagramBounds.setY(minY);
        diagramBounds.setWidth(maxX - minX);
        diagramBounds.setHeight(maxY - minY);
        return diagramBounds;
    }
    
    protected DiagramNode getDiagramBoundsFromImage(final InputStream imageStream) {
        return this.getDiagramBoundsFromImage(imageStream, 0, 0);
    }
    
    protected DiagramNode getDiagramBoundsFromImage(final InputStream imageStream, final int offsetTop, final int offsetBottom) {
        BufferedImage image;
        try {
            image = ImageIO.read(imageStream);
        }
        catch (IOException e) {
            throw new ProcessEngineException("Error while reading process diagram image.", e);
        }
        final DiagramNode diagramBoundsImage = this.getDiagramBoundsFromImage(image, offsetTop, offsetBottom);
        return diagramBoundsImage;
    }
    
    protected DiagramNode getDiagramBoundsFromImage(final BufferedImage image, final int offsetTop, final int offsetBottom) {
        final int width = image.getWidth();
        final int height = image.getHeight();
        final Map<Integer, Boolean> rowIsWhite = new TreeMap<Integer, Boolean>();
        final Map<Integer, Boolean> columnIsWhite = new TreeMap<Integer, Boolean>();
        for (int row = 0; row < height; ++row) {
            if (!rowIsWhite.containsKey(row)) {
                rowIsWhite.put(row, true);
            }
            if (row <= offsetTop || row > image.getHeight() - offsetBottom) {
                rowIsWhite.put(row, true);
            }
            else {
                for (int column = 0; column < width; ++column) {
                    if (!columnIsWhite.containsKey(column)) {
                        columnIsWhite.put(column, true);
                    }
                    final int pixel = image.getRGB(column, row);
                    final int alpha = pixel >> 24 & 0xFF;
                    final int red = pixel >> 16 & 0xFF;
                    final int green = pixel >> 8 & 0xFF;
                    final int blue = pixel >> 0 & 0xFF;
                    if (alpha != 0 && (red < 175 || green < 175 || blue < 175)) {
                        rowIsWhite.put(row, false);
                        columnIsWhite.put(column, false);
                    }
                }
            }
        }
        int marginTop = 0;
        for (int row2 = 0; row2 < height && rowIsWhite.get(row2); ++row2) {
            ++marginTop;
        }
        int marginLeft = 0;
        for (int column2 = 0; column2 < width && columnIsWhite.get(column2); ++column2) {
            ++marginLeft;
        }
        int marginRight = 0;
        for (int column3 = width - 1; column3 >= 0 && columnIsWhite.get(column3); --column3) {
            ++marginRight;
        }
        int marginBottom = 0;
        for (int row3 = height - 1; row3 >= 0 && rowIsWhite.get(row3); --row3) {
            ++marginBottom;
        }
        final DiagramNode diagramBoundsImage = new DiagramNode();
        diagramBoundsImage.setX((double)marginLeft);
        diagramBoundsImage.setY((double)marginTop);
        diagramBoundsImage.setWidth((double)(width - marginRight - marginLeft));
        diagramBoundsImage.setHeight((double)(height - marginBottom - marginTop));
        return diagramBoundsImage;
    }
    
    protected Map<String, DiagramNode> getElementBoundsFromBpmnDi(final Document bpmnModel) {
        final Map<String, DiagramNode> listOfBounds = new HashMap<String, DiagramNode>();
        final NodeList shapes = bpmnModel.getElementsByTagNameNS("http://www.omg.org/spec/BPMN/20100524/DI", "BPMNShape");
        for (int i = 0; i < shapes.getLength(); ++i) {
            final Element shape = (Element)shapes.item(i);
            final String bpmnElementId = shape.getAttribute("bpmnElement");
            final NodeList childNodes = shape.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); ++j) {
                final Node childNode = childNodes.item(j);
                if (childNode instanceof Element && "http://www.omg.org/spec/DD/20100524/DC".equals(childNode.getNamespaceURI()) && "Bounds".equals(childNode.getLocalName())) {
                    final DiagramNode bounds = this.parseBounds((Element)childNode);
                    bounds.setId(bpmnElementId);
                    listOfBounds.put(bpmnElementId, bounds);
                    break;
                }
            }
        }
        return listOfBounds;
    }
    
    protected DiagramNode parseBounds(final Element boundsElement) {
        final DiagramNode bounds = new DiagramNode();
        bounds.setX(Double.valueOf(boundsElement.getAttribute("x")));
        bounds.setY(Double.valueOf(boundsElement.getAttribute("y")));
        bounds.setWidth(Double.valueOf(boundsElement.getAttribute("width")));
        bounds.setHeight(Double.valueOf(boundsElement.getAttribute("height")));
        return bounds;
    }
    
    protected Map<String, DiagramElement> transformBoundsForImage(final DiagramNode diagramBoundsImage, final DiagramNode diagramBoundsXml, final Map<String, DiagramNode> listOfBounds) {
        final Map<String, DiagramElement> listOfBoundsForImage = new HashMap<String, DiagramElement>();
        for (final Map.Entry<String, DiagramNode> bounds : listOfBounds.entrySet()) {
            listOfBoundsForImage.put(bounds.getKey(), this.transformBoundsForImage(diagramBoundsImage, diagramBoundsXml, bounds.getValue()));
        }
        return listOfBoundsForImage;
    }
    
    protected DiagramNode transformBoundsForImage(final DiagramNode diagramBoundsImage, final DiagramNode diagramBoundsXml, final DiagramNode elementBounds) {
        final double scalingFactorX = diagramBoundsImage.getWidth() / diagramBoundsXml.getWidth();
        final double scalingFactorY = diagramBoundsImage.getWidth() / diagramBoundsXml.getWidth();
        final DiagramNode elementBoundsForImage = new DiagramNode(elementBounds.getId());
        elementBoundsForImage.setX(Double.valueOf(Math.round((elementBounds.getX() - diagramBoundsXml.getX()) * scalingFactorX + diagramBoundsImage.getX())));
        elementBoundsForImage.setY(Double.valueOf(Math.round((elementBounds.getY() - diagramBoundsXml.getY()) * scalingFactorY + diagramBoundsImage.getY())));
        elementBoundsForImage.setWidth(Double.valueOf(Math.round(elementBounds.getWidth() * scalingFactorX)));
        elementBoundsForImage.setHeight(Double.valueOf(Math.round(elementBounds.getHeight() * scalingFactorY)));
        return elementBoundsForImage;
    }
    
    protected Map<String, DiagramNode> fixFlowNodePositionsIfModelFromAdonis(final Document bpmnModel, final Map<String, DiagramNode> elementBoundsFromBpmnDi) {
        if (this.isExportedFromAdonis50(bpmnModel)) {
            final Map<String, DiagramNode> mapOfFixedBounds = new HashMap<String, DiagramNode>();
            final XPathFactory xPathFactory = XPathFactory.newInstance();
            final XPath xPath = xPathFactory.newXPath();
            xPath.setNamespaceContext(new Bpmn20NamespaceContext());
            for (final Map.Entry<String, DiagramNode> entry : elementBoundsFromBpmnDi.entrySet()) {
                final String elementId = entry.getKey();
                final DiagramNode elementBounds = entry.getValue();
                final String expression = "local-name(//bpmn:*[@id = '" + elementId + "'])";
                try {
                    final XPathExpression xPathExpression = xPath.compile(expression);
                    final String elementLocalName = xPathExpression.evaluate(bpmnModel);
                    if (!"participant".equals(elementLocalName) && !"lane".equals(elementLocalName) && !"textAnnotation".equals(elementLocalName) && !"group".equals(elementLocalName)) {
                        elementBounds.setX(elementBounds.getX() - elementBounds.getWidth() / 2.0);
                        elementBounds.setY(elementBounds.getY() - elementBounds.getHeight() / 2.0);
                    }
                }
                catch (XPathExpressionException e) {
                    throw new ProcessEngineException("Error while evaluating the following XPath expression on a BPMN XML document: '" + expression + "'.", e);
                }
                mapOfFixedBounds.put(elementId, elementBounds);
            }
            return mapOfFixedBounds;
        }
        return elementBoundsFromBpmnDi;
    }
    
    protected boolean isExportedFromAdonis50(final Document bpmnModel) {
        return "ADONIS".equals(bpmnModel.getDocumentElement().getAttribute("exporter")) && "5.0".equals(bpmnModel.getDocumentElement().getAttribute("exporterVersion"));
    }
    
    protected DocumentBuilderFactory getConfiguredDocumentBuilderFactory() {
        final boolean isXxeParsingEnabled = Context.getProcessEngineConfiguration().isEnableXxeProcessing();
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            for (final Map.Entry<String, Boolean> feature : ProcessDiagramLayoutFactory.XXE_FEATURES.entrySet()) {
                factory.setFeature(feature.getKey(), isXxeParsingEnabled ^ feature.getValue());
            }
        }
        catch (Exception e) {
            throw new ProcessEngineException("Error while configuring BPMN parser.", e);
        }
        factory.setXIncludeAware(isXxeParsingEnabled);
        factory.setExpandEntityReferences(isXxeParsingEnabled);
        factory.setNamespaceAware(true);
        return factory;
    }
    
    static {
        XXE_FEATURES = new HashMap<String, Boolean>() {
            {
                this.put("http://apache.org/xml/features/disallow-doctype-decl", true);
                this.put("http://xml.org/sax/features/external-general-entities", false);
                this.put("http://xml.org/sax/features/external-parameter-entities", false);
                this.put("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            }
        };
    }
}
