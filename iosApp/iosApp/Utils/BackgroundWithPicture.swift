//
//  BackgroundWithPicture.swift
//  iosApp
//
//  Created by Jackie Jensen on 20/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct BackgroundWithPicture: View {
	var body: some View {
		ZStack {
			Image("Background")
				.resizable()
				.scaledToFill()
				.edgesIgnoringSafeArea(.all)
			LinearGradient(
				gradient: Gradient(
					colors: [
						.darkOrange.opacity(0.3),
						.backgroundColor.opacity(0.3),
					]
				),
				startPoint: .top,
				endPoint: .bottom
			)
			.edgesIgnoringSafeArea(.all)
		}
	}
}

struct BackgroundWithPicture_Previews: PreviewProvider {
    static var previews: some View {
		BackgroundWithPicture()
    }
}
