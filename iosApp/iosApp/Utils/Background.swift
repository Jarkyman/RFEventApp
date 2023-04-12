//
//  Background.swift
//  iosApp
//
//  Created by Jackie Jensen on 22/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct Background: View {
    var body: some View {
		ZStack {
			LinearGradient(
				gradient: Gradient(
					colors: [
						.darkOrange,
						.backgroundColor,
					]
				),
				startPoint: .top,
				endPoint: .bottom
			)
			.edgesIgnoringSafeArea(.all)
		}
    }
}

struct Background_Previews: PreviewProvider {
    static var previews: some View {
        Background()
    }
}
