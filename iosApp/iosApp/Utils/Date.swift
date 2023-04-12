//
//  Date.swift
//  iosApp
//
//  Created by Jackie Jensen on 23/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

extension Date {

	/// Create a date from specified parameters
	///
	/// - Parameters:
	///   - year: The desired year
	///   - month: The desired month
	///   - day: The desired day
	/// - Returns: A `Date` object
	static func from(year: Int, month: Int, day: Int) -> Date? {
		let calendar = Calendar(identifier: .gregorian)
		var dateComponents = DateComponents()
		dateComponents.year = year
		dateComponents.month = month
		dateComponents.day = day
		return calendar.date(from: dateComponents) ?? nil
	}
}
