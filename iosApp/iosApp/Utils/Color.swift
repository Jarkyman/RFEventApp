//
//  Color.swift
//  iosApp
//
//  Created by Jackie Jensen on 20/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

extension Color {
	init(hex: UInt64, opacity: Double) {
		let red = Double((hex & 0xFF0000) >> 16) / 255.0
		let green = Double((hex & 0x00FF00) >> 8) / 255.0
		let blue = Double(hex & 0x0000FF) / 255.0
		
		self.init(red: red, green: green, blue: blue, opacity: opacity)
	}
	
	static let lightGray = Color(
		hex: 0xFF737274, opacity: 1
	)
	static let darkGray = Color(
		hex: 0xFF282729, opacity: 1
	)
	static let backgroundColor = Color( //
		hex: 0xFF0E0E0E, opacity: 1
	)
	static let mainOrange = Color(
		hex: 0xFFEE7203, opacity: 1
	)
	static let darkOrange = Color(
		hex: 0xFF753700, opacity: 1
	)
	static let defaultGray = Color(
		hex: 0xFF5E5E5E, opacity: 1
	)
	static let textColor = Color(
		hex: 0xFFFFFFFF, opacity: 1
	)
}

