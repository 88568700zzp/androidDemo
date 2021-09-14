package com.zzp.applicationkotlin;

import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by samzhang on 2021/9/14.
 */
public class XmlActivity extends AppCompatActivity {

    private final String TAG = "XmlActivity_";

    private TextView xml_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml);

        xml_text = findViewById(R.id.xml_text);

        XmlPullParser parser = Xml.newPullParser();
        StringBuffer sb = new StringBuffer();
        try {
            parser.setInput(getResources().openRawResource(R.raw.people), "utf-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.d(TAG,"START_DOCUMENT");
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        Log.d(TAG,"END_DOCUMENT");
                        break;
                    case XmlPullParser.START_TAG:
                        Log.d(TAG,"START_TAG:" + parser.getName());
                        for(int i = 0;i < parser.getAttributeCount();i++){
                            Log.d(TAG,"attributeName:" + parser.getAttributeName(i) + " attributeType:" + parser.getAttributeType(i) + " attributeValue:" + parser.getAttributeValue(i));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        Log.d(TAG,"END_TAG:" + parser.getName());
                        break;
                    case XmlPullParser.TEXT:
                        Log.d(TAG,"TEXT:" + parser.getText());
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

}
