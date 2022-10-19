package com.zzp.lib.algorithm;

public class Code7 {

    private int skipIndex = 0;

    public void doJob(){
        String input = "2[6[c]dd5[a]]";
        System.out.println(handleTxt(input,0,1));
    }

    private String handleTxt(String txt,int index,int size){
        StringBuffer sb = new StringBuffer();
        for(int i = index;i < txt.length();i++){
           if(txt.charAt(i) == '1'){
                sb.append(handleTxt(txt,i + 1,1));
                i = skipIndex;
            }else if(txt.charAt(i) == '2'){
                sb.append(handleTxt(txt,i + 1,2));
                i = skipIndex;
                i = skipIndex;
            }else if(txt.charAt(i) == '3'){
                sb.append(handleTxt(txt,i + 1,3));
                i = skipIndex;
            }else if(txt.charAt(i) == '4'){
                sb.append(handleTxt(txt,i + 1,4));
                i = skipIndex;
            }else if(txt.charAt(i) == '5'){
                sb.append(handleTxt(txt,i + 1,5));
                i = skipIndex;
            }else if(txt.charAt(i) == '6'){
                sb.append(handleTxt(txt,i + 1,6));
                i = skipIndex;
            }else if(txt.charAt(i) == '7'){
                sb.append(handleTxt(txt,i + 1,7));
                i = skipIndex;
            }else if(txt.charAt(i) == '8'){
                sb.append(handleTxt(txt,i + 1,8));
                i = skipIndex;
            }else if(txt.charAt(i) == '9'){
                sb.append(handleTxt(txt,i + 1,9));
                i = skipIndex;
            }else if(txt.charAt(i) == '['){

            }else if(txt.charAt(i) == ']'){
                skipIndex = i;
                break;
            }else{
                sb.append(txt.charAt(i));
            }
        }
        StringBuffer result = new StringBuffer();
        for(int i = 0;i <size;i ++){
            result.append(sb);
        }
        return result.toString();
    }
}
