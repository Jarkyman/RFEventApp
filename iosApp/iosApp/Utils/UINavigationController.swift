//
//  UINavigationController.swift
//  iosApp
//
//  Created by Jackie Jensen on 05/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

extension UINavigationController: UIGestureRecognizerDelegate {
	override open func viewDidLoad() {
		super.viewDidLoad()
		interactivePopGestureRecognizer?.delegate = self
	}

	public func gestureRecognizerShouldBegin(_ gestureRecognizer: UIGestureRecognizer) -> Bool {
		if AppState.shared.swipeEnabled {
			return viewControllers.count > 1
		}
		return false
	}
	
}
