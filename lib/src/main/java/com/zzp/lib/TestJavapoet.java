package com.zzp.lib;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.lang.model.element.Modifier;

/**
 * Created by samzhang on 2021/9/23.
 */
public class TestJavapoet {

    public static void testMain(){
        ClassName string = ClassName.get("java.lang", "String");

        ParameterSpec parameterSpec = ParameterSpec.builder(string,"isResult").build();

        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .addParameter(parameterSpec)
                .addStatement("this.$N = $N", "handsign", "handsign")
                .addStatement("int $N = 5", "zzp")
                .returns(void.class)
                .build();

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .superclass(string)
                .build();



        JavaFile javaFile = JavaFile.builder("com.zzp.lib", helloWorld)
                .build();

        try {
            javaFile.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
