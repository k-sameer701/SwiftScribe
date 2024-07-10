package com.example.swiftscribe

import android.app.Application
import com.example.swiftscribe.data.NoteContainer

class NoteApplication: Application() {
    lateinit var container: NoteContainer

    override fun onCreate() {
        super.onCreate()
        container = NoteContainer(this)
    }
}