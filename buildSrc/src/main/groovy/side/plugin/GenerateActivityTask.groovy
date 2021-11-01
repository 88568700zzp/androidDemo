package side.plugin;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction

import java.util.regex.Matcher;


/**
 * Created by samzhang on 2021/10/28.
 */
class GenerateActivityTask extends DefaultTask {

    private File outputFile

    String mPackageName

    private List<String> mActivityNames = new ArrayList<>()

    GenerateActivityTask(){
        setGroup("test")
    }

    void setOutputFile(File outputFile){
        this.outputFile = outputFile
    }

    @TaskAction
    void task(){
        mainCreate()
    }

    //生成activity文件
    private void createActivityFile(String activityName,String parentActivityName,String packageName,String targetFilePath){
        File targetFile = new File(targetFilePath)
        if(!targetFile.exists()){
            targetFile.getParentFile().mkdirs()
        }
        FileWriter writer = new FileWriter(targetFile)
        writer.write("package ${packageName};\n")
       // writer.write("import com.tool.`in`.${parentActivityName}\n")
        writer.write("import android.app.Activity;\n")
        writer.write("public class ${activityName} extends Activity{}\n")
        writer.flush()
        writer.close()
    }

//生成activity列表类
    private void createSideActivityList(String targetFilePath,String packageName,List<String> activityNames){
        File targetFile = new File(targetFilePath)
        if(!targetFile.exists()){
            targetFile.getParentFile().mkdirs()
        }
        StringBuffer sb = new StringBuffer()
        for(String activity:activityNames){
            sb.append(activity).append(".class,")
        }
        FileWriter writer = new FileWriter(targetFile)
        writer.write("package com.sideapp;\n")
        writer.write("import ${packageName}.*;\n")
        writer.write("public class SideActivityList {\n")
        writer.write(" public static Class[] SideActivityList = new Class[]{${sb.substring(0,sb.length() - 1).toString()}};\n")
        writer.write("}")
        writer.flush()
        writer.close()
    }

    private String getAndroidManifestActivity(){
        return new String("       <activity\n" +
                "                android:name=\"${mPackageName}.${mActivityNames[0]}\"\n" +
                "                android:configChanges=\"keyboardHidden|orientation|screenSize\"\n" +
                "                android:launchMode=\"singleTask\"\n" +
                "                android:excludeFromRecents=\"true\"\n" +
                "                android:hardwareAccelerated=\"true\"\n" +
                "                android:icon=\"@drawable/sdk_ic_launcher\"\n" +
                "                android:label=\"锁屏\"\n" +
                "                android:roundIcon=\"@drawable/sdk_ic_launcher\"\n" +
                "                android:taskAffinity=\"@string/adTaskAffinity_lock\"\n" +
                "                android:theme=\"@style/sdk_lock_transparent\"\n" +
                "                android:showOnLockScreen=\"true\"\n" +
                "                android:showWhenLocked=\"true\"\n" +
                "                android:screenOrientation=\"portrait\"\n" +
                "                tools:targetApi=\"o_mr1\"\n" +
                "                />\n" +
                "          <activity\n" +
                "                android:name=\"${mPackageName}.${mActivityNames[1]}\"\n" +
                "                android:configChanges=\"keyboardHidden|orientation|screenSize\"\n" +
                "                android:launchMode=\"singleTask\"\n" +
                "                android:excludeFromRecents=\"true\"\n" +
                "                android:hardwareAccelerated=\"true\"\n" +
                "                android:icon=\"@drawable/sdk_ic_logo_ad\"\n" +
                "                android:label=\"广告\"\n" +
                "                android:roundIcon=\"@drawable/sdk_ic_logo_ad\"\n" +
                "                android:taskAffinity=\"@string/requestTaskAffinity\"\n" +
                "                android:theme=\"@android:style/Theme.NoDisplay\"\n" +
                "                android:showOnLockScreen=\"true\"\n" +
                "                android:showWhenLocked=\"true\"\n" +
                "                tools:targetApi=\"o_mr1\"\n" +
                "                />\n" +
                "          <activity\n" +
                "                android:name=\"${mPackageName}.${mActivityNames[2]}\"\n" +
                "                android:configChanges=\"keyboardHidden|orientation|screenSize\"\n" +
                "                android:launchMode=\"singleTask\"\n" +
                "                android:excludeFromRecents=\"true\"\n" +
                "                android:hardwareAccelerated=\"true\"\n" +
                "                android:icon=\"@drawable/sdk_ic_logo_ad\"\n" +
                "                android:label=\"广告\"\n" +
                "                android:roundIcon=\"@drawable/sdk_ic_logo_ad\"\n" +
                "                android:taskAffinity=\"@string/adTaskAffinity\"\n" +
                "                android:theme=\"@style/sdk_transparent\"\n" +
                "                android:showOnLockScreen=\"true\"\n" +
                "                android:showWhenLocked=\"true\"\n" +
                "                />\n" +
                "          <activity\n" +
                "                android:name=\"${mPackageName}.${mActivityNames[3]}\"\n" +
                "                android:configChanges=\"keyboardHidden|orientation|screenSize\"\n" +
                "                android:launchMode=\"singleTask\"\n" +
                "                android:excludeFromRecents=\"true\"\n" +
                "                android:hardwareAccelerated=\"true\"\n" +
                "                android:icon=\"@drawable/sdk_ic_launcher\"\n" +
                "                android:label=\"工具\"\n" +
                "                android:roundIcon=\"@drawable/sdk_ic_launcher\"\n" +
                "                android:taskAffinity=\"@string/adTaskAffinity\"\n" +
                "                android:theme=\"@style/sdk_translucence\"\n" +
                "                android:showOnLockScreen=\"true\"\n" +
                "                android:showWhenLocked=\"true\"\n" +
                "                />\n" +
                "          <activity\n" +
                "                android:name=\"${mPackageName}.${mActivityNames[4]}\"\n" +
                "                android:configChanges=\"keyboardHidden|orientation|screenSize\"\n" +
                "                android:launchMode=\"singleTask\"\n" +
                "                android:excludeFromRecents=\"true\"\n" +
                "                android:hardwareAccelerated=\"true\"\n" +
                "                android:taskAffinity=\"@string/adTaskAffinityIn\"\n" +
                "                android:noHistory=\"true\"\n" +
                "                />\n" +
                "          <activity\n" +
                "                android:name=\"${mPackageName}.${mActivityNames[5]}\"\n" +
                "                android:configChanges=\"keyboardHidden|orientation|screenSize\"\n" +
                "                android:launchMode=\"singleTask\"\n" +
                "                android:excludeFromRecents=\"true\"\n" +
                "                android:hardwareAccelerated=\"true\"\n" +
                "                android:icon=\"@drawable/sdk_ic_logo_wifi\"\n" +
                "                android:label=\"WiFi管家\"\n" +
                "                android:roundIcon=\"@drawable/sdk_ic_logo_wifi\"\n" +
                "                android:taskAffinity=\"@string/adTaskAffinity\"\n" +
                "                android:theme=\"@style/sdk_translucence\"\n" +
                "                />\n" +
                "          <activity\n" +
                "                android:name=\"${mPackageName}.${mActivityNames[6]}\"\n" +
                "                android:configChanges=\"keyboardHidden|orientation|screenSize\"\n" +
                "                android:launchMode=\"singleTask\"\n" +
                "                android:excludeFromRecents=\"true\"\n" +
                "                android:hardwareAccelerated=\"true\"\n" +
                "                android:icon=\"@drawable/sdk_ic_logo_power\"\n" +
                "                android:label=\"电池管家\"\n" +
                "                android:roundIcon=\"@drawable/sdk_ic_logo_power\"\n" +
                "                android:taskAffinity=\"@string/adTaskAffinity\"\n" +
                "                android:theme=\"@style/sdk_translucence\"\n" +
                "                />\n")
    }

//替换money_sdk的清单文件
    void replaceAndroidManifest(String targetFilePath){
        println("replaceAndroidManifest")

        def file = new File(targetFilePath)
        try {
            BufferedReader br_File = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file),"utf-8"));
            CharArrayWriter caw = new CharArrayWriter();
            String string ;

            def foundStartTag = false

            while ((string = br_File.readLine()) != null){
                //判断是否包含目标字符，包含则替换
                if(string.contains("side app start")){
                    foundStartTag = true
                }
                if(foundStartTag){
                    if(string.contains("side app end")){
                        foundStartTag = false
                        StringBuffer sb = new StringBuffer()
                        sb.append(getAndroidManifestActivity())
                        string = sb.toString()
                    }
                }
                if(!foundStartTag) {
                    //写入内容并添加换行
                    caw.write(string);
                    caw.write("\r\n");
                }
            }
            br_File.close()
            BufferedWriter bw_File = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(file),"utf-8"));
            caw.writeTo(bw_File);
            caw.close();
            bw_File.close();
        } catch (Exception e) {
            System.out.println("hasError:" + e.getMessage());
        }
    }


    private void mainCreate(){
        println "-----createActivityFile start----"

        def codes = ["acc", "bec", "cde", "edq", "edd", "efd", "obv", "pvq", "cce", "tve", "occ", "koo","pvc","ovv"]
        Random random = new Random()
        String javaSourcePath = outputFile.absolutePath
        String packageName = codes[random.nextInt(codes.size())] + "." + codes[random.nextInt(codes.size())] + ".activity"
        String activityPreFile = javaSourcePath + File.separator + packageName.replaceAll("\\.", Matcher.quoteReplacement(File.separator)) + File.separator
        println "packegeName: ${packageName} activityPreFile: ${activityPreFile}"

        mPackageName = packageName
        mActivityNames.clear()

        String activityName0 = "A0" + random.nextInt(8000) + "c0"
        mActivityNames.add(activityName0)
        createActivityFile(activityName0, "BaiduActivity", packageName, activityPreFile + activityName0 + ".java")

        String activityName1 = "B1" + random.nextInt(8000) + "c1"
        mActivityNames.add(activityName1)
        createActivityFile(activityName1, "CodeActivity", packageName, activityPreFile + activityName1 + ".java")

        String activityName2 = "C2" + random.nextInt(8000) + "c2"
        mActivityNames.add(activityName2)
        createActivityFile(activityName2, "DialogActivity", packageName, activityPreFile + activityName2 + ".java")

        String activityName3 = "D3" + random.nextInt(8000) + "c3"
        mActivityNames.add(activityName3)
        createActivityFile(activityName3, "ToolsActivity", packageName, activityPreFile + activityName3 + ".java")

        String activityName4 = "E4" + random.nextInt(8000) + "c4"
        mActivityNames.add(activityName4)
        createActivityFile(activityName4, "CustomActivity", packageName, activityPreFile + activityName4 + ".java")

        String activityName5 = "F5" + random.nextInt(8000) + "c5"
        mActivityNames.add(activityName5)
        createActivityFile(activityName5, "WifiActivity", packageName, activityPreFile + activityName5 + ".java")

        String activityName6 = "G6" + random.nextInt(8000) + "c6"
        mActivityNames.add(activityName6)
        createActivityFile(activityName6, "PowerActivity", packageName, activityPreFile + activityName6 + ".java")

        String sideActivityListPath = javaSourcePath + "${File.separator}com${File.separator}sideapp${File.separator}SideActivityList.java"
        createSideActivityList(sideActivityListPath, packageName, mActivityNames)


        println "-----createActivityFile end----"
    }

}
