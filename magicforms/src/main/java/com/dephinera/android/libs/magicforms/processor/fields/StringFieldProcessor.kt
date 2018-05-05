package com.dephinera.android.libs.magicforms.processor.fields

import com.dephinera.android.libs.magicforms.annotations.StringFormField
import com.dephinera.android.libs.magicforms.processor.log.Logger
import com.dephinera.android.libs.magicforms.processor.util.StringUtils
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.VariableElement

class StringFieldProcessor {
    private var processingEnv: ProcessingEnvironment? = null

    fun init(processingEnvironment: ProcessingEnvironment?) {
        this.processingEnv = processingEnvironment
    }

    fun processFields(fields: List<VariableElement>) {
        fields.forEach {
            if (it.asType().toString() != String::class.java.canonicalName) {
                Logger.e(StringFormField::class.qualifiedName + " must be used on String fields only!")
            }

            processField(it)
        }
    }

    private fun processField(fieldElement: VariableElement) {
        Logger.n("processing field: ${fieldElement.simpleName}")
        Logger.n("getter: ${StringUtils.getterNameFor(fieldElement)}")

    }
}