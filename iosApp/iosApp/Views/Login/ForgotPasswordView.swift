//
//  ForgotPasswordView.swift
//  iosApp
//
//  Created by Jackie Jensen on 22/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import FirebaseAuth

struct ForgotPasswordView: View {
	@State var emailState = ""
	@Environment(\.dismiss) private var dismiss
	
	var body: some View {
			ZStack(alignment: .top) {
				BackgroundWithPicture()
				VStack {
					Spacer()
					Text("Glemt din kodeord?")
						.font(.system(size: 26, weight: .heavy))
						.foregroundColor(.textColor)
						.padding(.horizontal, 20)
						
					Spacer().frame(height: 20)
						
					Text("Bare rolig, vi ville sende dig en email hvor du kan lave en ny kode.")
						.font(.system(size: 14, weight: .regular))
						.foregroundColor(.defaultGray)
						.multilineTextAlignment(.center)
						.padding(.horizontal, 30)
						.lineLimit(3)

					
					TextField("", text: $emailState) //TODO: Fix farven på label
						.disableAutocorrection(true)
						.keyboardType(.emailAddress)
						.placeholder(when: emailState.isEmpty) {
							Text("Skriv din email her").foregroundColor(.darkGray)
						}
						.padding(20)
						.background(Color(hex: 0xFF5E5E5E, opacity: 0.75))
						.foregroundColor(.textColor)
						.cornerRadius(30.0)
						.padding(.bottom, 15)
					
					
					Button(action: {
						forgotPassword()
					}) {
						Text("Få ny kode")
							.font(.system(size: 24, weight: .semibold))
							.foregroundColor(.textColor)
							.padding()
							.frame(minWidth: 0,
								   maxWidth: .infinity, minHeight: 60,
								   maxHeight: 60)
							.background(Color(
								hex: 0xFFEE7203, opacity: 1
							)
							)
							.cornerRadius(30.0)
					}
					Spacer()
				}
				.padding(.horizontal, 30)
				HStack(alignment: .top) {
					Button(action: {
						print("Back")
						self.dismiss()
					}) {
						Image(systemName: "chevron.backward")
							.frame(width: 50, height: 50)
							.font(.system(size: 26))
							.foregroundColor(Color.backgroundColor)
							.background(Color.defaultGray).opacity(0.75)
							.clipShape(Circle())
					}
					Spacer()
				}
				.padding(.horizontal, 20)
			}
		.navigationBarHidden(true)
		.navigationBarBackButtonHidden(true)
	}
	
	func forgotPassword() {
		Auth.auth().sendPasswordReset(withEmail: emailState) { (error) in
			showSpinner {
				if error != nil {
					hideSpinner {
						print(error?.localizedDescription ?? "Error")
						showMessagePrompt(error?.localizedDescription ?? "Error")
					}
				} else {
					hideSpinner {
						print("success")
						showMessagePrompt("En email er sendt hvor du kan lave et nyt kodeord.")
						emailState = ""
					}
				}
			}
		}
	}
}

struct ForgotPasswordView_Previews: PreviewProvider {
	static var previews: some View {
		ForgotPasswordView()
	}
}
