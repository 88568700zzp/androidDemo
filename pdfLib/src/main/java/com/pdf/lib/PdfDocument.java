package com.pdf.lib;

import com.pdf.lib.model.PdfNode;
import com.pdf.lib.util.PDFTextCheck;
import com.pdf.lib.util.PdfUtil;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDPageLabelRange;
import org.apache.pdfbox.pdmodel.common.PDPageLabels;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.blend.BlendMode;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.tools.PDFMerger;
import org.apache.pdfbox.tools.PDFText2HTML;
import org.apache.pdfbox.util.Matrix;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class PdfDocument {

    public static void main(String[] args) throws Exception {
        createPdf();
        //editExistPdf();
        //extractText();
        //pdfToImage();
        //addWater();
        //parsePdf();
        //merge();


    }

    private static void extractText() throws Exception{
        String url = "C://android//file//test.pdf";
        try  {
            PDDocument doc = Loader.loadPDF(new File(url));
            System.out.println("pageCount:" + doc.getNumberOfPages());


            AccessPermission ap = doc.getCurrentAccessPermission();
            if (!ap.canExtractContent())
            {
                throw new IOException("You do not have permission to extract text");
            }


            PDFTextCheck stripper = new PDFTextCheck();


            //System.out.println(stripper.getText(doc));

            stripper.setSortByPosition(true);

            for(int p = 0; p < doc.getNumberOfPages();p++){

                stripper.setStartPage(p);
                stripper.setEndPage(p+1);

                // let the magic happen
                String text = stripper.getText(doc);

                // do some nice output with a header
                /*String pageStr = String.format("page %d:", p);
                System.out.println(pageStr);
                for (int i = 0; i < pageStr.length(); ++i)
                {
                    System.out.print("-");
                }
                System.out.println();
                System.out.println(text.trim());
                System.out.println();*/

            }

            doc.close();
        }catch (Exception e){
            throw e;
        }
    }

    private static void createPdf() throws IOException {
        String fontfile = "C://android//file//LiberationSans-Regular.ttf";
        String output_url = "C://android//file//test1.pdf";
        String imagePath = "C://android//file//icon.png";

        try (PDDocument doc = new PDDocument())
        {
            for(int i = 0;i < 1;i++) {



                PDPage page = new PDPage(PDRectangle.A4);
                doc.addPage(page);

                // load the font as this needs to be embedded
                PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                //PDFont font = PDType0Font.load(doc,new File(fontfile));

                float pageHeight = page.getMediaBox().getHeight();
                float pageWidth = page.getMediaBox().getWidth();

                PDRectangle pdRectangle = page.getMediaBox();
                System.out.println("pdRectangle:" + pdRectangle.toString());
                System.out.println("pageWidth:" + pageWidth + " pageHeight:" + pageHeight);

                PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, doc);

                // create a page with the message
                try (PDPageContentStream contents = new PDPageContentStream(doc, page)) {

                    PDExtendedGraphicsState r = new PDExtendedGraphicsState();

                    // 设置透明度
                    r.setNonStrokingAlphaConstant(0.2f);
                    r.setAlphaSourceFlag(true);
                    contents.setGraphicsStateParameters(r);

                    float fontSize = 12;

                    double cRadians = Math.toRadians(10);

                    float cTx = (float) (Math.sin(cRadians) * pageHeight/2);
                    float cTy = (float) (Math.sin(cRadians) * pageWidth/2);

                    contents.transform(Matrix.getRotateInstance(cRadians,cTx,-cTy));

                    String pageText = "test new text create ";

                    float textWidth = font.getStringWidth(pageText) / 1000 * fontSize;
                    float textHeight = font.getFontDescriptor().getCapHeight()/1000 * fontSize;

                    contents.beginText();
                    contents.setFont(font, fontSize);
                    //contents.setLeading(12);
                    //contents.newLineAtOffset((pageWidth - textWidth)/2, pageHeight - 72/2);

                    float ty = pageHeight - textHeight;

                    float tx = 0;

                    while(ty > 0) {
                        tx = 0;
                        while (tx < pageWidth) {
                            Matrix matrix = new Matrix();
                            matrix.translate(tx, ty);
                            contents.setTextMatrix(matrix);
                            contents.newLineAtOffset(0, 0);
                            contents.showText(pageText);

                            tx = tx + textWidth + 10;
                        }
                        ty = ty - textHeight - 10;
                    }



                    /*contents.newLine();

                    contents.showText(pageText);

                    contents.newLine();

                    contents.showText(pageText);

                    contents.endText();

                    contents.beginText();
                    contents.setFont(font, 25);
                    contents.setLeading(25);
                    contents.newLineAtOffset(0, 100);
                    contents.showText("zzp123");

                    contents.newLine();

                    contents.showText("zzp124");*/

                    contents.endText();

                /*contents.setLineWidth(2);
                contents.setStrokingColor(Color.BLUE);
                contents.setNonStrokingColor(Color.RED);
                contents.moveTo(100,600);
                contents.lineTo(200,600);
                contents.lineTo(200,500);
                contents.lineTo(100,500);
                contents.lineTo(100,600);
                contents.closeAndFillAndStroke();*/

                    //contents.drawImage(pdImage,(pageWidth - pdImage.getWidth())/2,(pageHeight - pdImage.getHeight())/2);

                }
            }

            PDPageLabels pageLabels = new PDPageLabels(doc);
            PDPageLabelRange pageLabelRange1 = new PDPageLabelRange();
            pageLabelRange1.setPrefix("RO ");
            pageLabelRange1.setStart(3);
            pageLabelRange1.setStyle(PDPageLabelRange.STYLE_ROMAN_UPPER);
            pageLabels.setLabelItem(0, pageLabelRange1);
            PDPageLabelRange pageLabelRange2 = new PDPageLabelRange();
            pageLabelRange2.setStart(1);
            pageLabelRange2.setStyle(PDPageLabelRange.STYLE_DECIMAL);
            pageLabels.setLabelItem(2, pageLabelRange2);
            doc.getDocumentCatalog().setPageLabels(pageLabels);

            doc.save(output_url);

        }

    }

    private static void editExistPdf(){
        String url = "C://android//file//test1.pdf";
        try {
            PDDocument doc = Loader.loadPDF(new File(url));
            PDPage page = doc.getPage(0);

            COSName fontName = null;

            PDFStreamParser pdfsp = new PDFStreamParser(page);
            List<Object> tokens = pdfsp.parse();
            for (int j = 0; j < tokens.size(); j++) {
                Object next = tokens.get( j );
                if(next  instanceof COSName) {
                    fontName= (COSName)next;
                    System.out.println("--COSName----" + fontName.getName());
                }else if(next  instanceof COSString) {
                    COSString previous = (COSString)next;
                    previous.setValue("666z12zp".getBytes("utf-8"));
                    //System.out.println("--COSString----"+ new String(previous.getBytes()));
                }else if(next  instanceof COSArray) {
                    COSArray cosArray = (COSArray)next;
                    StringBuffer stringBuffer = new StringBuffer();
                    for(int i = 0; i < cosArray.size();i++){
                        COSBase base = cosArray.get(i);
                        if(base instanceof COSString){
                            stringBuffer.append(new String(((COSString)base).getBytes()));
                        }
                    }
                    System.out.println("--COSArray----"+ stringBuffer.toString());
                }
            }
            PDStream updatedStream = new PDStream(doc);
            OutputStream out = updatedStream.createOutputStream();
            ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
            tokenWriter.writeTokens(tokens);
            out.close();
            page.setContents(updatedStream);

            doc.save(url);
        }catch (Exception e){

        }
    }

    private static void parsePdf() throws Exception{
        String url = "C://android//file//merge1.pdf";
        try  {
            PDDocument doc = Loader.loadPDF(new File(url));

            COSName fontName = null;

            for(int p = 0; p < doc.getNumberOfPages();p++){
                PDPage page = doc.getPage(p);


                PDFStreamParser pdfsp = new PDFStreamParser(page);
                List<Object> tokens = pdfsp.parse();
                for (int j = 0; j < tokens.size(); j++) {
                    //创建一个object对象去接收标记
                    Object next = tokens.get( j );
                    //System.out.println("--token----" + next.toString());
                    //instanceof判断其左边对象是否为其右边类的实例
                    if(next  instanceof COSName) {
                        fontName= (COSName)next;
                        //System.out.println("--COSName----" + fontName.getName());
                    }else if(next  instanceof COSString) {
                        COSString previous = (COSString)next;
                        //System.out.println("--COSString----"+ new String(previous.getBytes()));
                    }else if(next  instanceof COSArray) {
                        COSArray cosArray = (COSArray)next;
                        StringBuffer stringBuffer = new StringBuffer();
                        for(int i = 0; i < cosArray.size();i++){
                            COSBase base = cosArray.get(i);
                            if(base instanceof COSString){
                                stringBuffer.append(new String(((COSString)base).getBytes()));
                            }
                        }
                        //System.out.println("--COSArray----"+ stringBuffer.toString());
                    }
                }
                PDStream updatedStream = new PDStream(doc);
                OutputStream out = updatedStream.createOutputStream();
                ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
                tokenWriter.writeTokens(tokens);
                out.close();
               /* oldfont.forEach((k,v)->{
                    page.getResources().put(k, targetfont);
                });*/
                page.setContents(updatedStream);

                /*
                 PDResources resources = page.getResources();
                Iterable<COSName> xObjectNames = resources.getXObjectNames();
                //遍历COS对象
                xObjectNames.forEach(item->{
                    try {
                        PDXObject xObject = resources.getXObject(item);
                        System.out.println(item.getName());
                        //如果为图片类型就调Java的图片处理接口，将其保存下来
                        if(xObject instanceof PDImageXObject) {
                            PDImageXObject imgobject = (PDImageXObject)xObject;
                            BufferedImage image = imgobject.getImage();
                            System.out.println("image:" + image.toString());
                            //ImageIO.write(image, "PNG", new File("C://android//file//image.png"));
                        }
                    } catch (IOException e) {
                        System.out.println("图片保存出错");
                        e.printStackTrace();
                    }
                });*/

                Iterator<PDStream> streams = page.getContentStreams();
                while (streams.hasNext()){
                    System.out.println("-----stream start--------");
                    System.out.println( new String(streams.next().toByteArray()));
                    System.out.println("-----stream end--------");
                }

                //List<PdfNode> nodeList = PdfUtil.getPageText(page);
                //System.out.println(nodeList);
            }
            doc.close();
        }catch (Exception e){
            throw e;
        }
    }

    private static void pdfToImage(){
        String url = "C://android//file//test1.pdf";
        try {
            PDDocument doc = Loader.loadPDF(new File(url));
            PDFRenderer renderer = new PDFRenderer(doc);
            BufferedImage image = renderer.renderImage(0);
            ImageIO.write(image, "PNG", new File("C://android//file//render.png"));
        }catch (Exception e){

        }
    }

    private static void addWater(){
        String url = "C://android//file//test1.pdf";
        try {
            PDDocument doc = Loader.loadPDF(new File(url));
            for (PDPage page : doc.getPages())
            {
                PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
                addWatermarkText(doc, page, font, "water");
            }
            doc.save(url);
        }catch (Exception e){

        }
    }

    private static void addWatermarkText(PDDocument doc, PDPage page, PDFont font, String text)
            throws IOException {
        try (PDPageContentStream cs
                     = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true))
        {
            float fontHeight = 100; // arbitrary for short text
            float width = page.getMediaBox().getWidth();
            float height = page.getMediaBox().getHeight();

            int rotation = page.getRotation();
            switch (rotation)
            {
                case 90:
                    width = page.getMediaBox().getHeight();
                    height = page.getMediaBox().getWidth();
                    cs.transform(Matrix.getRotateInstance(Math.toRadians(90), height, 0));
                    break;
                case 180:
                    cs.transform(Matrix.getRotateInstance(Math.toRadians(180), width, height));
                    break;
                case 270:
                    width = page.getMediaBox().getHeight();
                    height = page.getMediaBox().getWidth();
                    cs.transform(Matrix.getRotateInstance(Math.toRadians(270), 0, width));
                    break;
                default:
                    break;
            }

            float stringWidth = font.getStringWidth(text) / 1000 * fontHeight;
            float diagonalLength = (float) Math.sqrt(width * width + height * height);
            float angle = (float) Math.atan2(height, width);
            float x = (diagonalLength - stringWidth) / 2; // "horizontal" position in rotated world
            float y = -fontHeight / 4; // 4 is a trial-and-error thing, this lowers the text a bit
            cs.transform(Matrix.getRotateInstance(angle, 0, 0));
            cs.setFont(font, fontHeight);
            // cs.setRenderingMode(RenderingMode.STROKE) // for "hollow" effect

            PDExtendedGraphicsState gs = new PDExtendedGraphicsState();
            gs.setNonStrokingAlphaConstant(0.2f);
            gs.setStrokingAlphaConstant(0.2f);
            gs.setBlendMode(BlendMode.MULTIPLY);
            gs.setLineWidth(3f);
            cs.setGraphicsStateParameters(gs);

            cs.setNonStrokingColor(Color.red);
            cs.setStrokingColor(Color.red);

            cs.beginText();
            cs.newLineAtOffset(x, y);
            cs.showText(text);
            cs.endText();
        }
    }

    private static void merge(){
        PDFMergerUtility merger = new PDFMergerUtility();

        try {
            File file1 = new File("C://android//file//merge1.pdf");
            File file2 = new File("C://android//file//merge2.pdf");
            merger.addSource(file1);
            merger.addSource(file2);
            merger.setDestinationFileName("C://android//file//mergeResult.pdf");
            merger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
        } catch (IOException exception) {
        }
    }

    private static void sign(){

    }
}