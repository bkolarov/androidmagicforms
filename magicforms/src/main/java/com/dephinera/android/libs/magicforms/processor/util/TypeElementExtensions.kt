package com.dephinera.android.libs.magicforms.processor.util

import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

fun <T : Annotation?> TypeElement.getFieldsAnnotatedWith(annotationClass: Class<T>): List<VariableElement> {
    return this.enclosedElements.filter {
        it.kind == ElementKind.FIELD
    }.map {
        it as VariableElement
    }.filter {
        it.getAnnotation(annotationClass) != null
    }
}