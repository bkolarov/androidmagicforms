package com.dephinera.android.libs.magicforms.processor.util

import javax.lang.model.element.VariableElement

class StringUtils {

    companion object {

        @JvmStatic
        fun getterNameFor(fieldElement: VariableElement): String {
            val fieldName = fieldElement.simpleName.toString()

            val capitalizedName = fieldName.replace(Regex("^[a-zA-Z]"), "${fieldName[0].toUpperCase()}")

            return "get$capitalizedName"
        }

    }

}