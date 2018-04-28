package com.dephinera.android.libs.magicforms.processor

import com.dephinera.android.libs.magicforms.annotations.MagicForm
import com.dephinera.android.libs.magicforms.annotations.StringFormField
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

@SupportedAnnotationTypes(
        "com.dephinera.android.libs.magicforms.annotations.MagicForm",
        "com.dephinera.android.libs.magicforms.annotations.StringFormField")
class MagicFormsProcessor : AbstractProcessor() {

    override fun process(set: MutableSet<out TypeElement>?, roundEnvironment: RoundEnvironment?): Boolean {
        val formClasses = roundEnvironment?.getElementsAnnotatedWith(MagicForm::class.java)

        formClasses?.forEach(this::processClass)

        return true
    }

    private fun processClass(classElement: Element) {
        val classBuilder = createValidatorClass(classElement)

        val classPackage = processingEnv.elementUtils.getPackageOf(classElement).qualifiedName.toString()

        val javaFile = JavaFile.builder(classPackage, classBuilder.build())
                .build()

        javaFile.writeTo(processingEnv.filer)
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

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(
                MagicForm::class.qualifiedName!!,
                StringFormField::class.qualifiedName!!)
    }
}