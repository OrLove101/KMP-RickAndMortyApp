import SwiftUI
import Shared

@main
struct iOSApp: App {

    init() {
        KoinKt.doInitKoinIos()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
