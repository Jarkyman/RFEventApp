//
//  SplashView.swift
//  iosApp
//
//  Created by Jackie Jensen on 22/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import FirebaseAuth
import shared

struct SplashView: View {
	@StateObject private var eventViewModel = EventViewModel()
	@StateObject private var userViewModel = UserViewModel()
	
	@State var isLogin: Bool = false
	@State var isHome: Bool = false
	
	var body: some View {
		NavigationView {
			ZStack {
				if self.isLogin {
					NavigationLink(destination: LoginView(), isActive: $isLogin, label: {})
					
				}else if self.isHome {
					NavigationLink(destination: BottomNavView(), isActive: $isHome, label: {})
				} else {
					BackgroundWithPicture()
					VStack {
						Image("Logo")
							.resizable()
							.scaledToFit()
							.frame(width: 300, height: 300)
						ProgressView()
							.scaleEffect(2)
							.progressViewStyle(CircularProgressViewStyle(tint: .backgroundColor))
					}
				}
			}
			.onAppear {
				AppState.shared.swipeEnabled = false
				DispatchQueue.main.asyncAfter(deadline: .now() + 2.5) {
					//TODO: Den skal vente på user er loaded og ikke 2.5 sek
					withAnimation {
						initLoad()
					}
				}
			}
			.onDisappear {
				AppState.shared.swipeEnabled = true
			}
		}
		.environmentObject(eventViewModel)
		.environmentObject(userViewModel)
		.navigationBarHidden(true)
		.navigationBarBackButtonHidden(true)
		.navigationViewStyle(.stack)
	}
	
	func initLoad() {
		if Auth.auth().currentUser != nil {
			userViewModel.getUser()
			isHome = true
		}else {
			isLogin = true
		}
	}
}

struct SplashView_Previews: PreviewProvider {
	static var previews: some View {
		SplashView()
	}
}
