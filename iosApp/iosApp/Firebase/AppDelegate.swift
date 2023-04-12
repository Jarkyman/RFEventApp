//
//  AppDelegate.swift
//  iosApp
//
//  Created by Jackie Jensen on 22/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import FirebaseCore
import FirebaseDatabase
import FirebaseAuth


class AppDelegate: NSObject, UIApplicationDelegate {
  func application(_ application: UIApplication,
				   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
	FirebaseApp.configure()

	return true
  }
}
