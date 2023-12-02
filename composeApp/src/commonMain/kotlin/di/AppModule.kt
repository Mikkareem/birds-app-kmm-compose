package di

import network.NuthatchApi
import network.NuthatchApiImpl
import network.provideHttpClient
import org.koin.dsl.module

val appModule = module {
    single { provideHttpClient() }

    single<NuthatchApi> { NuthatchApiImpl(get()) }
}