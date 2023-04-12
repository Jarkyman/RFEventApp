//
//  SignUpView.swift
//  iosApp
//
//  Created by Jackie Jensen on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import Combine
import shared
import FirebaseAuth
import FirebaseDatabase

struct SignUpView: View {
	let dbref = Database.database().reference()
	@Environment(\.dismiss) private var dismiss
	@EnvironmentObject var userViewModel: UserViewModel
	
	@State private var firstName = ""
	@State private var lastName = ""
	@State private var email = ""
	@State private var campName = ""
	@State private var birthday: Date = (Date.from(year: 1997, month: 04, day: 08) ?? Date())
	@State private var password = ""
	@State private var passwordConfirm = ""
		
	@FocusState var focusedInput: Int?
	
	@State var SignUpShow = false
	
	var body: some View {
		ZStack(alignment: .top) {
			BackgroundWithPicture()
				VStack {
					TextField("", text: $firstName)
						.disableAutocorrection(true)
						.keyboardType(.default)
						.placeholder(when: firstName.isEmpty) {
							Text("Navn").foregroundColor(.darkGray)
						}
						.padding(20)
						.background(Color(hex: 0xFF5E5E5E, opacity: 0.75))
						.foregroundColor(.textColor)
						.frame(height: 60)
						.cornerRadius(30.0)
						.padding(.bottom, 15)
						.submitLabel(.next)
						.focused($focusedInput, equals: 0)
						.id(0)
						.onSubmit {
							focusedInput = 1
						}
					
					
					TextField("", text: $lastName)
						.disableAutocorrection(true)
						.keyboardType(.default)
						.placeholder(when: lastName.isEmpty) {
							Text("Efternavn").foregroundColor(.darkGray)
						}
						.padding(20)
						.background(Color(hex: 0xFF5E5E5E, opacity: 0.75))
						.foregroundColor(.textColor)
						.frame(height: 60)
						.cornerRadius(30.0)
						.padding(.bottom, 15)
						.submitLabel(.next)
						.focused($focusedInput, equals: 1)
						.id(1)
						.onSubmit {
							focusedInput = 2
						}
					
					
					TextField("", text: $email)
						.disableAutocorrection(true)
						.keyboardType(.emailAddress)
						.placeholder(when: email.isEmpty) {
							Text("Email").foregroundColor(.darkGray)
						}
						.padding(20)
						.background(Color(hex: 0xFF5E5E5E, opacity: 0.75))
						.foregroundColor(.textColor)
						.frame(height: 60)
						.cornerRadius(30.0)
						.padding(.bottom, 15)
						.submitLabel(.next)
						.focused($focusedInput, equals: 2)
						.id(2)
						.onSubmit {
							focusedInput = 3
						}
					
					TextField("", text: $campName)
						.disableAutocorrection(true)
						.keyboardType(.default)
						.placeholder(when: campName.isEmpty) {
							Text("Camp navn").foregroundColor(.darkGray)
						}
						.padding(20)
						.background(Color(hex: 0xFF5E5E5E, opacity: 0.75))
						.foregroundColor(.textColor)
						.frame(height: 60)
						.cornerRadius(30.0)
						.padding(.bottom, 15)
						.onReceive(Just(campName)) { _ in
							if (campName.count > 20) {
								campName = String(campName.prefix(20))
							}
						}
						.submitLabel(.next)
						.focused($focusedInput, equals: 3)
						.id(3)
						.onSubmit {
							focusedInput = 4
						}
					
					HStack {
						DatePicker(
							"",
							selection: $birthday,
							displayedComponents: [.date]
						)
						.labelsHidden()
						.datePickerStyle(DefaultDatePickerStyle())
						.padding(20)
						.frame(height: 60)
						Spacer()
					}
					.padding(.horizontal, 0)
					.background(Color(hex: 0xFF5E5E5E, opacity: 0.75))
					.foregroundColor(.textColor)
					.frame(height: 60)
					.cornerRadius(30.0)
					.padding(.bottom, 15)
					.submitLabel(.next)
					.focused($focusedInput, equals: 4)
					.id(4)
					.onSubmit {
						focusedInput = 5
					}
					
					SecureField("", text: $password)
						.disableAutocorrection(true)
						.foregroundColor(.textColor)
						.placeholder(when: password.isEmpty) {
							Text("Kodeord").foregroundColor(.darkGray)
						}
						.padding(20)
						.background(Color(hex: 0xFF5E5E5E, opacity: 0.75))
						.foregroundColor(.textColor)
						.frame(height: 60)
						.cornerRadius(30.0)
						.padding(.bottom, 15)
						.submitLabel(.next)
						.focused($focusedInput, equals: 5)
						.id(5)
						.onSubmit {
							focusedInput = 6
						}
					
					SecureField("", text: $passwordConfirm)
						.disableAutocorrection(true)
						.foregroundColor(.textColor)
						.placeholder(when: passwordConfirm.isEmpty) {
							Text("Bekræft kodeord").foregroundColor(.darkGray)
						}
						.padding(20)
						.background(Color(hex: 0xFF5E5E5E, opacity: 0.75))
						.foregroundColor(.textColor)
						.frame(height: 60)
						.cornerRadius(30.0)
						.padding(.bottom, 15)
						.submitLabel(.done)
						.focused($focusedInput, equals: 6)
						.id(6)
						.onSubmit {
							focusedInput = nil
						}
					
					NavigationLink(
						destination: SplashView(),
						isActive: $SignUpShow,
						label: {
							Button(action: {
								signupUser()
							}) {
								Text("Sign up")
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
			.padding(.horizontal, 20)
		}
		.navigationBarHidden(true)
		.navigationBarBackButtonHidden(true)
	}
	
	func signupUser() {
		let birthdayFormatter = DateFormatter()
		birthdayFormatter.dateFormat = "dd/MM/yyyy"
		let dateFormatter = DateFormatter()
		dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.ms"
		
		if (!firstName.isEmpty && !lastName.isEmpty && !email.isEmpty && !password.isEmpty && !passwordConfirm.isEmpty) {
			if (password == passwordConfirm) {
				showSpinner {
					Auth.auth().createUser(withEmail: email, password: password) { (result, error) in
						if error != nil {
							hideSpinner {
								print(error?.localizedDescription ?? "Error")
								showMessagePrompt(error?.localizedDescription ?? "Error")
							}
						} else {
							let user = User(
								id: (result?.user.uid)!, firstName: firstName, lastName: lastName, email: email, birthday: birthdayFormatter.string(from: birthday), campName: campName, createdAt: dateFormatter.string(from: Date()), updatedAt: dateFormatter.string(from: Date())
							)
							
							let changeRequest = Auth.auth().currentUser?.createProfileChangeRequest()
							changeRequest?.displayName = firstName + " " + lastName
							changeRequest?.commitChanges { error in
								// ...
							}
							
							dbref.child("users").child(user.id).setValue(
								["firstName": user.firstName,
								 "lastName": user.lastName,
								 "email":user.email,
								 "campName":user.campName,
								 "id":user.id,
								 "birthday":user.birthday,
								 "createdAt":user.createdAt,
								 "updatedAt":user.updatedAt]
							) { (error, dbRef) in
								if error != nil {
									hideSpinner {
										print(error?.localizedDescription ?? "Error")
										showMessagePrompt(error?.localizedDescription ?? "Error")
									}
								}else {
									print(dbRef)
									LoggedInUserKt.loggedInUser = user
									userViewModel.getUser()
									hideSpinner {
										print("success")
										SignUpShow = true
									}
								}
							}
						}
					}
				}
			} else {
				showMessagePrompt("Koden matcher ikke")
			}
		} else {
			showMessagePrompt("Der er en eller flere fleter som ikke er udfyldt")
		}
	}
}

struct SignUpView_Previews: PreviewProvider {
	static var previews: some View {
		SignUpView()
	}
}
