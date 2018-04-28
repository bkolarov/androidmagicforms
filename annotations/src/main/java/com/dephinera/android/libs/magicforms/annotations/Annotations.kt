package com.dephinera.android.libs.magicforms.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class MagicForm(val validatorName: String = "")

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class StringFormField(val canBeEmpty: Boolean = false,
                                 val minLength: Int = 0,
                                 val maxLength: Int = MAX_LENGTH_INDEVINITE,
                                 val regex: String = STRING_EMPTY)