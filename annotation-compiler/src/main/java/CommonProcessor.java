import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.zzp.annotation.Hello;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by samzhang on 2021/10/13.
 */
@AutoService(Processor.class)
public class CommonProcessor extends AbstractProcessor {
    //保存生成的java文件
    private Filer filer;
    //工具类，获取java类文件
    private Elements elementUtils;

    private Messager mMessager;

    //初始化赋值
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnv.getFiler();
        elementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
    }

    //返回支持处理的注解类型
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(Hello.class.getCanonicalName());
        return types;
    }

    //返回支持的java版本号
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if(set.isEmpty()){
            return false;
        }
        for(TypeElement element:set) {
            mMessager.printMessage(Diagnostic.Kind.WARNING, "do process:" + element.getSimpleName());
        }

        //获取所有源码文件
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addStatement("$T.out.println($S)",System.class, "Hello, JavaPoet!")
                .build();
        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();
        JavaFile javaFile = JavaFile.builder("com.songwenju.aptproject", helloWorld)
                .build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();}
        return false;
    }
}
