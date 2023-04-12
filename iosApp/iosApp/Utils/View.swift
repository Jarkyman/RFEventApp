//
//  View.swift
//  iosApp
//
//  Created by Jackie Jensen on 22/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

extension View {
	func showMessagePrompt(_ message: String) {
		let alert = UIAlertController(title: nil, message: message, preferredStyle: .alert)
		let okAction = UIAlertAction(title: "OK", style: .default, handler: nil)
		alert.addAction(okAction)
		UIApplication.shared.windows.first?.rootViewController?.present(alert, animated: true, completion: nil)
	}
	
	func showMessagePromptBottom(_ message: String) {
		let alert = UIAlertController(title: nil, message: message, preferredStyle: .actionSheet)
		let okAction = UIAlertAction(title: "OK", style: .default, handler: nil)
		alert.addAction(okAction)
		UIApplication.shared.windows.first?.rootViewController?.present(alert, animated: true, completion: nil)
	}
	
	
	func showTextInputPrompt(withMessage message: String, completionBlock: @escaping ((Bool, String?) -> Void)) {
		let alert = UIAlertController(title: nil, message: message, preferredStyle: .alert)
		alert.addTextField()
		let okAction = UIAlertAction(title: "OK", style: .default) { _ in
			guard let text = alert.textFields?.first?.text else { return }
			completionBlock(true, text)
		}
		let cancelAction = UIAlertAction(title: "Cancel", style: .cancel) { _ in
			completionBlock(false, nil)
		}
		alert.addAction(okAction)
		alert.addAction(cancelAction)
		UIApplication.shared.windows.first?.rootViewController?.present(alert, animated: true, completion: nil)
	}
	
	func showSpinner(_ completion: (() -> Void)?) {
		let alert = UIAlertController(title: nil, message: "Please Wait...\n\n\n\n", preferredStyle: .alert)
		let spinner = UIActivityIndicatorView(style: .large)
		spinner.color = UIColor(ciColor: .black)
		spinner.center = CGPoint(x: alert.view.frame.midX, y: alert.view.frame.midY)
		spinner.autoresizingMask = [.flexibleBottomMargin, .flexibleTopMargin, .flexibleLeftMargin, .flexibleRightMargin]
		spinner.startAnimating()
		alert.view.addSubview(spinner)
		UIApplication.shared.windows.first?.rootViewController?.present(alert, animated: true, completion: completion)
	}
	
	func hideSpinner(_ completion: (() -> Void)?) {
		guard let controller = UIApplication.shared.windows.first?.rootViewController else {
			return
		}
		controller.dismiss(animated: true, completion: completion)
	}
	
	func placeholder<Content: View>(
		when shouldShow: Bool,
		alignment: Alignment = .leading,
		@ViewBuilder placeholder: () -> Content) -> some View {
			
			ZStack(alignment: alignment) {
				placeholder().opacity(shouldShow ? 1 : 0)
				self
			}
		}
	
	func cornerRadius(_ radius: CGFloat, corners: UIRectCorner) -> some View {
			clipShape( RoundedCorner(radius: radius, corners: corners) )
		}
	
	func customBackground(_ color: Color) -> some View {
			self.background(color)
		}
}

struct RoundedCorner: Shape {

	var radius: CGFloat = .infinity
	var corners: UIRectCorner = .allCorners

	func path(in rect: CGRect) -> Path {
		let path = UIBezierPath(roundedRect: rect, byRoundingCorners: corners, cornerRadii: CGSize(width: radius, height: radius))
		return Path(path.cgPath)
	}
}
