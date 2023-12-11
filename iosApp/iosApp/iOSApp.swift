import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        KoinHelperKt.doStartKoin()
    }

	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}