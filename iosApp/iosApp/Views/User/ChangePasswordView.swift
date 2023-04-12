//
//  ChangePasswordView.swift
//  iosApp
//
//  Created by Jackie Jensen on 23/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import FirebaseAuth

struct ChangePasswordView: View {
	@Environment(\.dismiss) private var dismiss
	
	@State private var passwordOld = ""
	@State private var passwordNew = ""
	@State private var passwordNewConfirm = ""
	
	var body: some View {
		NavigationView {
			ZStack(alignment: .top) {
				Background()
				VStack {
					SecureField("", text: $passwordOld) //TODO: Fix farven på label
						.disableAutocorrection(true)
						.foregroundColor(.textColor)
						.placeholder(when: passwordOld.isEmpty) {
							Text("Kodeord").foregroundColor(.darkGray)
						}
						.padding(20)
						.background(Color(hex: 0xFF5E5E5E, opacity: 0.75))
						.foregroundColor(.textColor)
						.frame(height: 60)
						.cornerRadius(30.0)
						.padding(.bottom, 15)
					
					SecureField("", text: $passwordNew) //TODO: Fix farven på label
						.disableAutocorrection(true)
						.foregroundColor(.textColor)
						.placeholder(when: passwordNew.isEmpty) {
							Text("Ny kode").foregroundColor(.darkGray)
						}
						.padding(20)
						.background(Color(hex: 0xFF5E5E5E, opacity: 0.75))
						.foregroundColor(.textColor)
						.frame(height: 60)
						.cornerRadius(30.0)
						.padding(.bottom, 15)
					
					SecureField("", text: $passwordNewConfirm) //TODO: Fix farven på label
						.disableAutocorrection(true)
						.foregroundColor(.textColor)
						.placeholder(when: passwordNewConfirm.isEmpty) {
							Text("Bekræft kode").foregroundColor(.darkGray)
						}
						.padding(20)
						.background(Color(hex: 0xFF5E5E5E, opacity: 0.75))
						.foregroundColor(.textColor)
						.frame(height: 60)
						.cornerRadius(30.0)
						.padding(.bottom, 15)
					
					Button(action: {
						changePassword()
						//TODO: GO Back
					}) {
						Text("Skift kode")
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
				}
				.padding(.horizontal, 30)
				.padding(.top, 60)
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
				.padding(.horizontal, 10)
			}
		}
		.navigationBarHidden(true)
		.navigationBarBackButtonHidden(true)
	}
	
	func changePassword() {
		if (!passwordNew.isEmpty && !passwordOld.isEmpty && !passwordNewConfirm.isEmpty)  {
			if (passwordNew == passwordNewConfirm) {
				showSpinner {
					let user = Auth.auth().currentUser
					let credential = EmailAuthProvider.credential(withEmail: user!.email!, password: passwordOld)
					
					print("withEmail " + user!.email! + " password " + passwordOld)
					
					user?.reauthenticate(with: credential, completion: { (authResult, error) in
						if let error = error {
							hideSpinner {
								showMessagePrompt("Fejl: " + error.localizedDescription)
							}
							return
						}
						user?.updatePassword(to: passwordNew, completion: { (error) in
							if let error = error {
								hideSpinner {
									showMessagePrompt("Fejl: " + error.localizedDescription)
								}
								return
							}
							hideSpinner {
								//Go Back
								self.dismiss()
							}
						})
					})
					
				}
			}else {
				hideSpinner {
					showMessagePrompt("Kodeord matcher ikke")
				}
			}
		}else {
			hideSpinner {
				showMessagePrompt("Udfyld alle felter")
			}
		}
	}
	
}

struct ChangePasswordView_Previews: PreviewProvider {
	static var previews: some View {
		ChangePasswordView()
	}
}
