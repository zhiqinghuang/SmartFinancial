package com.manydesigns.portofino.chart;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

import org.jfree.chart.plot.DrawingSupplier;

public class DesaturatedDrawingSupplier implements DrawingSupplier {
    private final DrawingSupplier inner;

    // desaturated
    private final Paint[] paintArray = {
        new Color(0xcc5252), // saturazione 60, luminosita' 80
        new Color(0x52cc52),
        new Color(0x5252cc),

        new Color(0xd9d957), // saturazione 60, luminosita' 85
        new Color(0x57d9d9),
        new Color(0xd957d9),

        new Color(0xd49455), // saturazione 60, luminosita' 83
        new Color(0x94d455),
        new Color(0x55d494),

        new Color(0x5594d4), // saturazione 60, luminosita' 83
        new Color(0x9455d4),
        new Color(0xd45594)
    };

    private int index;

    public DesaturatedDrawingSupplier(DrawingSupplier inner) {
        this.inner = inner;
        index = 0;
    }

    public Paint getNextPaint() {
        Paint result = paintArray[index++];
        if (index == paintArray.length) {
            index = 0;
        }
        return result;
    }

    public Paint getNextOutlinePaint() {
        return inner.getNextOutlinePaint();
    }

    public Paint getNextFillPaint() {
        return inner.getNextFillPaint();
    }

    public Stroke getNextStroke() {
        return inner.getNextStroke();
    }

    public Stroke getNextOutlineStroke() {
        return inner.getNextOutlineStroke();
    }

    public Shape getNextShape() {
        return inner.getNextShape();
    }
}