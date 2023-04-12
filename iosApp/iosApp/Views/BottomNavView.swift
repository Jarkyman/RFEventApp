//
//  BottomNavView.swift
//  iosApp
//
//  Created by Jackie Jensen on 22/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct BottomNavView: View {
	@State private var selection = 1
	@EnvironmentObject private var userViewModel: UserViewModel
	let tabBarAppearance: UITabBarAppearance = UITabBarAppearance()
	
	init() {
		tabBarAppearance.backgroundColor = UIColor(Color.darkGray)
		tabBarAppearance.stackedLayoutAppearance.selected.titleTextAttributes = [.foregroundColor: UIColor(Color.mainOrange)]
		tabBarAppearance.stackedLayoutAppearance.normal.titleTextAttributes = [.foregroundColor: UIColor(Color.lightGray)]
		tabBarAppearance.stackedLayoutAppearance.selected.iconColor = UIColor(Color.mainOrange)
		tabBarAppearance.stackedLayoutAppearance.normal.iconColor = UIColor(Color.lightGray)
		UITabBar.appearance().standardAppearance = tabBarAppearance
		UITabBar.appearance().scrollEdgeAppearance = tabBarAppearance
	}
	var body: some View {
		TabView(selection: $selection) {
			MapView()
				.tabItem {
					Image(systemName: "map.fill")
					Text("Kort")
				}
				.tag(0)
			HomeView()
				.tabItem {
					Image(systemName: "person.3.fill")
					Text("Hjem")
				}
				.tag(1)
				.onAppear {
					AppState.shared.swipeEnabled = false
				}
			CalendarView()
				.tabItem {
					Image(systemName: "calendar")
					Text("Kalender")
				}
				.tag(2)
			ProfileView()
				.tabItem {
					Image(systemName: "person.fill")
					Text("Profil")
				}
				.tag(3)
		}
		.onAppear {
			AppState.shared.swipeEnabled = false
		}
		.navigationBarHidden(true)
		.navigationBarBackButtonHidden(true)
	}
}

struct BottomNavView_Previews: PreviewProvider {
	static var previews: some View {
		BottomNavView()
	}
}
