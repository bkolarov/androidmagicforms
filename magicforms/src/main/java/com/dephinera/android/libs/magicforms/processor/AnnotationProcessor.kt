package com.dephinera.android.libs.magicforms.processor

import com.dephinera.android.libs.magicforms.annotations.MagicForm
import com.dephinera.android.libs.magicforms.annotations.StringFormField
import com.dephinera.android.libs.magicforms.processor.log.Logger
import com.dephinera.android.libs.magicforms.processor.magicform.MagicFormProcessor
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

class AnnotationProcessor : AbstractProcessor() {

    private val magicFormProcessor: MagicFormProcessor = MagicFormProcessor()

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        Logger.init(processingEnv!!.messager)

        magicFormProcessor.init(processingEnv)
    }

    override fun process(set: MutableSet<out TypeElement>?, roundEnvironment: RoundEnvironment?): Boolean {
        val formClasses = roundEnvironment?.getElementsAnnotatedWith(MagicForm::class.java)

        magicFormProcessor.processClasses(formClasses)

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