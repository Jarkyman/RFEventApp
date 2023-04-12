import SwiftUI

@main
struct iOSApp: App {
	@UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
	//@StateObject private var eventViewModel = EventViewModel()
	//@StateObject private var userViewModel = UserViewModel()
	
	var body: some Scene {
		WindowGroup {
			//NavigationView {
				SplashView()
			/*}
			.environmentObject(eventViewModel)
			.environmentObject(userViewModel)
			.navigationViewStyle(.stack)*/
		}
	}
}


class AppState {
	static let shared = AppState()

	var swipeEnabled = false
}
