//
//  LoginView.swift
//  iosApp
//
//  Created by Jackie Jensen on 20/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared
import FirebaseAuth
import FirebaseDatabase

struct LoginView: View {
	let dbref = Database.database().reference()
	
	@EnvironmentObject var userViewModel: UserViewModel
	
	@State private var emailState: String = ""
	@State private var passwordState: String = ""
	
	@State var loginShow = false
	@State var shouldShowSignUp = false
	@State var shouldShowForgot = false
	
	@FocusState var focusedInput: Int?
	
	var body: some View {
		NavigationView {
			ZStack {
				BackgroundWithPicture()
				VStack {
					Image("Logo")
						.resizable()
						.frame(width: 180, height: 180)
						.padding(48)
					
					TextField("", text: $emailState) //TODO: Fix farven på label
						.disableAutocorrection(true)
						.keyboardType(.emailAddress)
						.placeholder(when: emailState.isEmpty) {
							Text("Email").foregroundColor(.darkGray)
						}
						.padding(20)
						.background(Color(hex: 0xFF5E5E5E, opacity: 0.75))
						.foregroundColor(.textColor)
						.cornerRadius(30.0)
						.padding(.bottom, 15)
						.focused($focusedInput, equals: 0)
						.submitLabel(.next)
						.id(0)
						.onSubmit {
							focusedInput = 1
						}
					
					
					SecureField("", text: $passwordState) //TODO: Fix farven på label
						.disableAutocorrection(true)
						.foregroundColor(.textColor)
						.placeholder(when: passwordState.isEmpty) {
							Text("Kodeord").foregroundColor(.darkGray)
						}
						.padding(20)
						.background(Color(hex: 0xFF5E5E5E, opacity: 0.75))
						.cornerRadius(30.0)
						.focused($focusedInput, equals: 1)
						.submitLabel(.done)
						.id(1)
						.onSubmit {
							focusedInput = nil
						}
					
					HStack {
						NavigationLink(
							destination: ForgotPasswordView(),
							isActive: $shouldShowForgot,
							label: {
								Spacer()
								Text("Glemt kodeord?")
									.font(.footnote)
									.foregroundColor(.textColor)
									.padding(.bottom, 15)
							})
					}
					.padding(.horizontal)
					
					NavigationLink(
						destination: SplashView(),
						isActive: $loginShow,
						label: {
							Button(action: {
								//guard let email = emailState, let password = passwordState else {
								if (emailState.isEmpty || passwordState.isEmpty) {
									showMessagePrompt("Udfyld email og kodeord for at logge ind")
									return
								} else {
									login()
								}
								//loginShow = true
							}) {
								Text("Log in")
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
						})
					VStack {
						NavigationLink(
							destination: SignUpView(),
							isActive: $shouldShowSignUp,
							label: {
								Text("Sign Up")
									.font(.system(size: 24, weight: .semibold))
									.foregroundColor(.lightGray)
							})
					}
				}
				.padding(.horizontal, 30)
			}
			.onAppear {
				AppState.shared.swipeEnabled = false
			}
			.onDisappear {
				AppState.shared.swipeEnabled = true
			}
		}
		.navigationBarHidden(true)
		.navigationBarBackButtonHidden(true)
	}
	
	func login() {
		showSpinner {
			Auth.auth().signIn(withEmail: emailState, password: passwordState) { (result, error) in
				if error != nil {
					hideSpinner {
						print(error?.localizedDescription ?? "Error")
						showMessagePrompt(error?.localizedDescription ?? "Error")
						loginShow = false
					}
				} else {
					hideSpinner {
						print("success")
						userViewModel.getUser()
						loginShow = true
					}
				}
			}
		}
	}
}

struct LoginView_Previews: PreviewProvider {
	static var previews: some View {
		LoginView()
	}
}
