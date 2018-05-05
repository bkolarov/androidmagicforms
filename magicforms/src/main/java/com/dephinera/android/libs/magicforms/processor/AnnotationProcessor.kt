package com.dephinera.android.libs.magicforms.processor

import com.dephinera.android.libs.magicforms.annotations.MagicForm
import com.dephinera.android.libs.magicforms.annotations.StringFormField
import com.dephinera.android.libs.magicforms.processor.log.Logger
import com.dephinera.android.libs.magicforms.processor.magicform.MagicFormProcessor
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

class AnnotationProcessor : AbstractProcessor() {

    private val magicFormProcessor: MagicFormProcessor = MagicFormProcessor()

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        Logger.init(processingEnv!!.messager)

        magicFormProcessor.init(processingEnv)
    }

    override fun process(set: MutableSet<out TypeElement>?, roundEnvironment: RoundEnvironment?): Boolean {
        val formClasses = roundEnvironment?.getElementsAnnotatedWith(MagicForm::class.java)

        formClasses?.forEach {
            if (it.kind != ElementKind.CLASS) {
                processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, MagicForm::class.qualifiedName + " must be used on classes only!")
            }

            magicFormProcessor.processClass(it as TypeElement)
        }

        return true
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(
                MagicForm::class.qualifiedName!!,
                StringFormField::class.qualifiedName!!)
    }
}