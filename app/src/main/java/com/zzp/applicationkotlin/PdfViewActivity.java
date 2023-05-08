package com.zzp.applicationkotlin;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.graphics.pdf.PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;
import com.tom_roush.pdfbox.pdmodel.common.PDRectangle;
import com.tom_roush.pdfbox.pdmodel.font.PDFont;
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font;
import com.tom_roush.pdfbox.pdmodel.graphics.color.PDColor;
import com.tom_roush.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import com.tom_roush.pdfbox.pdmodel.graphics.image.JPEGFactory;
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;
import com.tom_roush.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfViewActivity extends AppCompatActivity {

    private LinearLayout bitmapContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PDFBoxResourceLoader.init(getApplicationContext());

        setContentView(R.layout.activity_pdf_view);

        bitmapContainer = findViewById(R.id.bitmapContainer);
    }

    public void readPdf(View view) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "我的简历.pdf");
            //AssetFileDescriptor assetFileDescriptor = getAssets().openFd("mergeResult.pdf");
            PdfRenderer pdfRenderer = new PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));
            for (int i = 0; i < pdfRenderer.getPageCount(); i++) {
                PdfRenderer.Page page = pdfRenderer.openPage(i);
                Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), ARGB_8888);
                Rect rect = new Rect(0, 0, page.getWidth(), page.getHeight());
                Matrix matrix = new Matrix();
                page.render(bitmap, rect, matrix, RENDER_MODE_FOR_DISPLAY);
                Log.d("zzp1234", "page:" + i + " height:" + page.getHeight() + " width:" + page.getWidth());
                //pdfRenderer.close();
                page.close();

                ImageView imageView = new ImageView(PdfViewActivity.this);
                imageView.setImageBitmap(bitmap);

                bitmapContainer.addView(imageView);
            }
            pdfRenderer.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /*public void writePdf(View view) {
        PdfDocument document = new PdfDocument();
        try {
            Rect contentRect = new Rect(0, 0, 200, 200);
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo
                    .Builder(200, 200, 0)
                    .setContentRect(contentRect)
                    .create();
            PdfDocument.Page page = document.startPage(pageInfo);

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.RED);

            page.getCanvas().drawCircle(contentRect.centerX(), contentRect.centerY(), contentRect.width() / 2, paint);

            document.finishPage(page);


            File file = new File(Environment.getExternalStorageDirectory(), "output.pdf");

            file.deleteOnExit();

            document.writeTo(new FileOutputStream(file));
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            document.close();
        }
    }*/

    public void writePdf(View view) {
        String message = "This is a sample PDF";

        PDDocument doc = new PDDocument();
        try {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            int fontSize = 50;

            PDFont font = PDType1Font.HELVETICA;

            PDRectangle mediaBox = page.getMediaBox();

            Log.d("zzp1234",mediaBox.toString());

            com.tom_roush.pdfbox.util.Matrix matrix = new com.tom_roush.pdfbox.util.Matrix();
            matrix.rotate(90);

            float fontHeight = (font.getFontDescriptor().getCapHeight())/1000 * fontSize;
            float fontWidth = font.getStringWidth(message) * fontSize / 1000;

            PDPageContentStream stream = new PDPageContentStream(doc, page);

            stream.setNonStrokingColor(new PDColor(new float[] { 1, 1 / 255F, 1 }, PDDeviceRGB.INSTANCE));
            stream.addRect(0,mediaBox.getHeight() - fontSize,mediaBox.getWidth(),fontSize);
            stream.fill();

            stream.setNonStrokingColor(new PDColor(new float[] { 1 / 255F, 1 / 255F, 1 }, PDDeviceRGB.INSTANCE));

            stream.beginText();
            //stream.setTextMatrix(matrix);
            stream.setFont(font, fontSize);
            stream.newLineAtOffset((mediaBox.getWidth() - fontWidth)/2, mediaBox.getHeight() - fontSize);
            stream.showText(message);
            stream.endText();



            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_doll_gift_mid);

            int wantWidth = 200;
            int wantHeight = 200;

            PDImageXObject ximage = JPEGFactory.createFromImage(doc, bitmap,0.3f);
            stream.drawImage(ximage, (mediaBox.getWidth() - wantWidth)/2, (mediaBox.getHeight() - wantHeight)/2,wantWidth,wantHeight);

            /*//创建拓展显示状态
            PDExtendedGraphicsState r = new PDExtendedGraphicsState();

            // 设置透明度（阿尔法混合是一种让图形显示透明的技术，设置其透明度参数）
            r.setNonStrokingAlphaConstant(0.2f);
            r.setAlphaSourceFlag(true);
            stream.setGraphicsStateParameters(r);


            stream.beginText();
            stream.setFont(PDType1Font.SYMBOL, 10);
            stream.newLineAtOffset(0, -15);

            // 获取PDF页面大小
            float pageHeight = mediaBox.getHeight();
            float pageWidth = mediaBox.getWidth();

            //设置文本显示矩阵，并设置旋转的角度
            stream.setTextMatrix(com.tom_roush.pdfbox.util.Matrix.getRotateInstance(0.4, pageHeight/3, pageWidth/3));
            //写入水印文本
            stream.showText("这是你要的水印");

            // 结束渲染，关闭流
            stream.endText();
            stream.restoreGraphicsState();*/

            stream.close();

            File file = new File(Environment.getExternalStorageDirectory(), "output.pdf");

            file.deleteOnExit();

            doc.save(new FileOutputStream(file));
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                doc.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}