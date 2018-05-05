package com.dephinera.android.libs.magicforms.processor.magicform

import com.dephinera.android.libs.magicforms.annotations.MagicForm
import com.dephinera.android.libs.magicforms.annotations.StringFormField
import com.dephinera.android.libs.magicforms.processor.VALIDATOR_CLASS_SUFFIX
import com.dephinera.android.libs.magicforms.processor.VALIDATOR_FIELD_FORM
import com.dephinera.android.libs.magicforms.processor.VALIDATOR_INIT_METHOD_NAME
import com.dephinera.android.libs.magicforms.processor.fields.StringFieldProcessor
import com.dephinera.android.libs.magicforms.processor.log.Logger
import com.dephinera.android.libs.magicforms.processor.util.getFieldsAnnotatedWith
import com.squareup.javapoet.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class MagicFormProcessor {

    private var processingEnv: ProcessingEnvironment? = null
    private val stringFieldProcessor: StringFieldProcessor = StringFieldProcessor()

    fun init(processingEnvironment: ProcessingEnvironment?) {
        this.processingEnv = processingEnvironment
        stringFieldProcessor.init(processingEnvironment)
    }

    fun processClasses(formClasses: Set<Element>?) {
        formClasses?.forEach {
            if (it.kind != ElementKind.CLASS) {
                Logger.e(MagicForm::class.qualifiedName + " must be used on classes only!")
            }

            processClass(it as TypeElement)
        }
    }

    private fun processClass(classElement: TypeElement) {
        val className = getValidatorClassName(classElement)
        val classBuilder = createValidatorClass(className)
        val classPackage = processingEnv?.elementUtils?.getPackageOf(classElement)?.qualifiedName.toString()

        addInitialization(classBuilder, classElement, className, classPackage)

        Logger.n("processing class: ${classElement.simpleName}")

        val fields = classElement.getFieldsAnnotatedWith(StringFormField::class.java)

        stringFieldProcessor.processFields(fields)


        val javaFile = JavaFile.builder(classPackage, classBuilder.build())
                .build()

        javaFile.writeTo(processingEnv?.filer)
    }

    private fun createValidatorClass(className: String): TypeSpec.Builder {
        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
    }

    private fun getValidatorClassName(element: TypeElement): String {
        val annotation = element.getAnnotation(MagicForm::class.java)

        val className = if (annotation.validatorName.isNotEmpty()) {
            annotation.validatorName
        } else {
            "${element.simpleName}$VALIDATOR_CLASS_SUFFIX"
        }
        return className
    }

    private fun addInitialization(classBuilder: TypeSpec.Builder,
                                  classElement: TypeElement,
                                  className: String,
                                  classPackage: String) {

        val formType = TypeName.get(classElement.asType())
        val parameter = ParameterSpec.builder(formType, VALIDATOR_FIELD_FORM)
                .build()

        val constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(parameter)
                .addStatement("this.\$N = \$N", VALIDATOR_FIELD_FORM, VALIDATOR_FIELD_FORM)
                .build()

        val returnType = ClassName.get(classPackage, className)

        Logger.n("$classPackage.$className")

        val initMethod = MethodSpec.methodBuilder(VALIDATOR_INIT_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(parameter)
                .returns(returnType)
                .addStatement("return new \$N(\$N)", className, VALIDATOR_FIELD_FORM)
                .build()

        classBuilder
                .addField(formType, VALIDATOR_FIELD_FORM, Modifier.PRIVATE)
                .addMethod(constructor)
                .addMethod(initMethod)
    }
}
