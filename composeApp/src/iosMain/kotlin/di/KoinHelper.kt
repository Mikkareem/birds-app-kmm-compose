package di

import org.koin.core.context.startKoin

fun doStartKoin() {
    startKoin {
        modules(appModule)
    }
}