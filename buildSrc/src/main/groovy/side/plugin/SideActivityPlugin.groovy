package side.plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.DefaultTask
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty

/**
 * Created by samzhang on 2021/10/27.
 */
class SideActivityPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def variants = null
        boolean isLibrary = false
        if (project.plugins.hasPlugin(LibraryPlugin.class)){
            //这里使用libraryVariants对象，只能用于library插件中
            variants = project.android.libraryVariants
            isLibrary = true
        }else if (project.plugins.hasPlugin(AppPlugin.class)){
            //这里使用applicationVariants对象，只能用于application插件中
            variants = project.android.applicationVariants
            isLibrary = false
        }

        applyExtension(project)

        project.afterEvaluate {
            println("applyExtension:${project.side.name} version:${project.side.version}")
        }

        if(isLibrary){
            configureR2Generation(project,variants,isLibrary)
        }
    }

    private void configureR2Generation(Project project, DomainObjectSet<BaseVariant> variants,boolean isLibrary){
        variants.all{variant ->
            File outputDir = new File(project.buildDir,"generateActivity/${variant.name}")
            if(!outputDir.exists()){
                outputDir.mkdir()
            }

            GenerateActivityTask generate = project.tasks.create(getTaskNameByVariant(variant),GenerateActivityTask.class)
            generate.setOutputFile(outputDir)


            variant.registerJavaGeneratingTask(generate, outputDir)

            variant.getOutputs().all { output ->
                DefaultTask task = output.processManifest
                task.dependsOn(generate)
            }
        }

        project.afterEvaluate {
            variants.all { variant ->
                variant.getOutputs().all { output ->
                    def task = output.processManifest
                    if (task == null) {
                        return
                    }
                    println("task:${task.name} isLibrary:${isLibrary}")
                    task.doLast {
                        File androidManifest = null
                        try {
                            androidManifest = getManifestOutputFile().getAsFile().get()
                        } catch (Exception e) {
                            androidManifest = null
                        }

                        GenerateActivityTask generateActivityTask = project.tasks.getByName(getTaskNameByVariant(variant))

                        if (androidManifest != null && androidManifest.exists()) {
                            println("file:${androidManifest.absolutePath} packName:${generateActivityTask.mPackageName} taskName:${generateActivityTask.name}")
                            generateActivityTask.replaceAndroidManifest(androidManifest.absolutePath)
                        }


                    }
                }
            }
        }
    }

    //通过变体生成任务名
    private String getTaskNameByVariant(BaseVariant variant){
        return "generate${variant.name.capitalize()}SideActivity"
    }

    void applyExtension(Project project) {
        project.extensions.create("side", SideExtension)
    }


}


