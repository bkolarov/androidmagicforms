package com.dephinera.android.libs.magicforms.processor.log

import javax.annotation.processing.Messager
import javax.tools.Diagnostic

class Logger {

    companion object {

        @JvmStatic
        private var messager: Messager? = null

        @JvmStatic
        fun init(messager: Messager) {

            if (this.messager != null) {
                throw IllegalStateException("Logger already initialized.")
            }

            this.messager = messager
        }

        @JvmStatic
        fun n(message: String) {
            ensureMessager()
            messager?.printMessage(Diagnostic.Kind.NOTE, message)
        }

        @JvmStatic
        fun w(message: String) {
            ensureMessager()
            messager?.printMessage(Diagnostic.Kind.WARNING, message)
        }

        @JvmStatic
        fun mw(message: String) {
            ensureMessager()
            messager?.printMessage(Diagnostic.Kind.MANDATORY_WARNING, message)
        }

        @JvmStatic
        fun e(message: String) {
            ensureMessager()
            messager?.printMessage(Diagnostic.Kind.ERROR, message)
        }

        @JvmStatic
        fun o(message: String) {
            ensureMessager()
            messager?.printMessage(Diagnostic.Kind.OTHER, message)
        }

        @JvmStatic
        private fun ensureMessager() {
            if (messager == null) {
                throw IllegalStateException("Logger not initialized. Make sure you call Logger.init(Messager)")
            }
        }
    }

}