package com.dephinera.android.libs.magicforms.processor.magicform

import com.dephinera.android.libs.magicforms.annotations.MagicForm
import com.dephinera.android.libs.magicforms.annotations.StringFormField
import com.dephinera.android.libs.magicforms.processor.VALIDATOR_CLASS_SUFFIX
import com.dephinera.android.libs.magicforms.processor.fields.StringFieldProcessor
import com.dephinera.android.libs.magicforms.processor.log.Logger
import com.dephinera.android.libs.magicforms.processor.util.getFieldsAnnotatedWith
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class MagicFormProcessor {

    private var processingEnv: ProcessingEnvironment? = null
    private val stringFieldProcessor: StringFieldProcessor = StringFieldProcessor()

    fun init(processingEnvironment: ProcessingEnvironment?) {
        this.processingEnv = processingEnvironment
        stringFieldProcessor.init(processingEnvironment)
    }

    fun processClass(classElement: TypeElement) {
        val classBuilder = createValidatorClass(classElement)

        Logger.n("processing class: ${classElement.simpleName}")

        val fields = classElement.getFieldsAnnotatedWith(StringFormField::class.java)

        stringFieldProcessor.processFields(fields)

        val classPackage = processingEnv?.elementUtils?.getPackageOf(classElement)?.qualifiedName.toString()

        val javaFile = JavaFile.builder(classPackage, classBuilder.build())
                .build()

        javaFile.writeTo(processingEnv?.filer)
    }

    private fun createValidatorClass(element: Element): TypeSpec.Builder {
        val annotation = element.getAnnotation(MagicForm::class.java)

        val className = if (annotation.validatorName.isNotEmpty()) {
            annotation.validatorName
        } else {
            "${element.simpleName}$VALIDATOR_CLASS_SUFFIX"
        }

        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
    }

}
