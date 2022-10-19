package com.pdf.lib.util;

import com.pdf.lib.model.PdfNode;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.contentstream.operator.OperatorName;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfUtil {

    public static List<PdfNode> getPageText(PDPage page) throws IOException {
        List<PdfNode> nodeList = new ArrayList<>();

        PDFStreamParser pdfsp = new PDFStreamParser(page);
        List<Object> tokens = pdfsp.parse();
        Object beforeToken = null;
        PdfNode node = null;
        boolean startFoundText = false;
        for (int j = 0; j < tokens.size(); j++) {
            Object next = tokens.get( j );
            if(next instanceof Operator){//是操作符
                Operator operator = (Operator)next;
                String operatorName = operator.getName();
                if(!startFoundText && operatorName.equals(OperatorName.BEGIN_TEXT)){
                    startFoundText = true;
                    node = new PdfNode();
                }else if(startFoundText){
                    if(operatorName.equals(OperatorName.SHOW_TEXT)){
                        if(beforeToken instanceof COSString){
                            COSString base = (COSString)beforeToken;
                            node.addTxt(new String(base.getBytes()));
                        }else if(beforeToken instanceof COSArray){
                            COSArray cosArray = (COSArray)beforeToken;
                            StringBuffer stringBuffer = new StringBuffer();
                            for(int i = 0; i < cosArray.size();i++){
                                COSBase base = cosArray.get(i);
                                if(base instanceof COSString){
                                    stringBuffer.append(new String(((COSString)base).getBytes()));
                                }
                            }
                            node.addTxt(stringBuffer.toString());
                        }
                    }

                    if(operatorName.equals(OperatorName.END_TEXT)){
                        startFoundText = false;
                        nodeList.add(node);
                    }
                }
            }

            beforeToken = next;
        }
        return nodeList;
    }
}
