package ru.akimslava.bookshelf

import android.app.Application
import ru.akimslava.bookshelf.data.AppContainer
import ru.akimslava.bookshelf.data.DefaultAppContainer

class BookshelfApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}