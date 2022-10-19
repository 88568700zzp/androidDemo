package com.pdf.lib.model;

import java.util.ArrayList;
import java.util.Arrays;

public class PdfNode {
    private ArrayList<String> contents = new ArrayList<>();

    public PdfNode() {
    }


    public void addTxt(String txt) {
        contents.add(txt);
    }

    @Override
    public String toString() {
        return "PdfNode{" +
                "contents='" + Arrays.toString(contents.toArray()) + '\'' +
                '}';
    }
}
