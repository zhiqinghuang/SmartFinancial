package com.manydesigns.elements.pdf;

import com.manydesigns.elements.fields.Field;
import com.manydesigns.elements.forms.FieldSet;
import com.manydesigns.elements.forms.Form;
import com.manydesigns.elements.xml.XmlBuffer;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

public class FormPdfExporter {
    public static final String copyright =
            "Copyright (c) 2005-2015, ManyDesigns srl";

    private final Form form;
    private final Source xsltSource;
    private String title;

    public FormPdfExporter(Form form, Source xsltSource) {
        this.form = form;
        this.xsltSource = xsltSource;
    }

    public void export(OutputStream out) throws FOPException, IOException, TransformerException {
        FopFactory fopFactory = FopFactory.newInstance();

        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

        // Setup XSLT
        TransformerFactory Factory = TransformerFactory.newInstance();
        Transformer transformer = Factory.newTransformer(xsltSource);

        // Set the value of a <param> in the stylesheet
        transformer.setParameter("versionParam", "2.0");

        // Setup input for XSLT transformation
        Reader reader = composeXml();
        Source src = new StreamSource(reader);

        // Resulting SAX events (the generated FO) must be piped through to
        // FOP
        Result res = new SAXResult(fop.getDefaultHandler());

        // Start XSLT transformation and FOP processing
        transformer.transform(src, res);

        reader.close();
        out.flush();
    }

    /**
     * Composes an XML document representing the current object.
     *
     * @return
     * @throws java.io.IOException
     */
    protected Reader composeXml() throws IOException {
        XmlBuffer xb = new XmlBuffer();
        xb.writeXmlHeader("UTF-8");
        xb.openElement("class");
        xb.openElement("table");
        if (title != null) {
            xb.write(title);
        }
        xb.closeElement("table");

        for (FieldSet fieldset : form) {
            xb.openElement("tableData");
            xb.openElement("rows");

            for (Field field : fieldset.fields()) {
                xb.openElement("row");
                xb.openElement("nameColumn");
                xb.write(field.getLabel());
                xb.closeElement("nameColumn");

                xb.openElement("value");
                xb.write(field.getStringValue());
                xb.closeElement("value");
                xb.closeElement("row");

            }
            xb.closeElement("rows");
            xb.closeElement("tableData");
        }

        xb.closeElement("class");

        return new StringReader(xb.toString());
    }

    public Form getForm() {
        return form;
    }

    public Source getXsltSource() {
        return xsltSource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}