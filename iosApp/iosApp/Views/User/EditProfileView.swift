//
//  EditProfileView.swift
//  iosApp
//
//  Created by Jackie Jensen on 23/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import Combine
import Firebase
import FirebaseAuth
import FirebaseDatabase
import shared

struct EditProfileView: View {
	let dbref = Database.database().reference()
	@Environment(\.dismiss) private var dismiss
	
	@EnvironmentObject var userViewModel: UserViewModel
	
	@State private var firstName = ""
	@State private var lastName = ""
	@State private var email = ""
	@State private var campName = ""
	@State private var birthday: Date = Date()
	
	@State var SaveChanges = false
	
	var body: some View {
		NavigationView {
			ZStack(alignment: .top) {
				Background()
				VStack {
					TextField("", text: $firstName) //TODO: Fix farven på label
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
					
					TextField("", text: $lastName) //TODO: Fix farven på label
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
					TextField("", text: $email) //TODO: Fix farven på label
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
					
					TextField("", text: $campName) //TODO: Fix farven på label
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
					HStack { //TODO: Textcolor white Label med farve
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
					Button(action: {
						updateProfile()
					}) {
						Text("Gem")
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
			.onAppear {
				firstName = userViewModel.loggedInUser?.firstName ?? ""
				lastName = userViewModel.loggedInUser?.lastName ?? ""
				email = userViewModel.loggedInUser?.email ?? ""
				campName = userViewModel.loggedInUser?.campName ?? ""
				birthday = (Date.from(year: Int(userViewModel.loggedInUser!.birthday.split(separator: "/")[2]) ?? 1997, month: Int(userViewModel.loggedInUser!.birthday.split(separator: "/")[1]) ?? 04, day: Int(userViewModel.loggedInUser!.birthday.split(separator: "/")[0]) ?? 08) ?? Date())
			}
		}
		.navigationBarHidden(true)
		.navigationBarBackButtonHidden(true)
	}
	
	func updateProfile() {
		if (firstName.isEmpty || lastName.isEmpty || email.isEmpty) {
			showMessagePrompt("Udfyld alle felter")
		} else {
			part1()
		}
		
	}
	
	func part1() {
		if (userViewModel.loggedInUser!.email != email) {
			// Opret en UIAlertController
			let alertController = UIAlertController(title: "Log ind", message: nil, preferredStyle: .alert)
			
			// Tilføj to tekstfelter for e-mail og kodeord
			alertController.addTextField { (textField) in
				textField.placeholder = "E-mail"
			}
			alertController.addTextField { (textField) in
				textField.placeholder = "Kodeord"
				textField.isSecureTextEntry = true
			}
			
			// Opret handling for "Log ind" knappen
			let loginAction = UIAlertAction(title: "Log ind", style: .default) { (_) in
				// Hent tekst fra tekstfelterne
				guard let email = alertController.textFields?[0].text,
					  let password = alertController.textFields?[1].text else {
					return
				}
				
				// Opret credentials
				let credential = EmailAuthProvider.credential(withEmail: email, password: password)
				
				// Hent nuværende bruger
				let user = Auth.auth().currentUser
				// Re-authenticate brugeren med de givne credentials
				showSpinner {
					user?.reauthenticate(with: credential, completion: { (authResult, error) in
						if let error = error {
							hideSpinner {
								showMessagePrompt("Fejl: " + error.localizedDescription)
							}
							return
						}
						user?.updateEmail(to: self.email, completion: { (error) in
							if let error = error {
								hideSpinner {
									showMessagePrompt("Fejl: " + error.localizedDescription)
								}
								return
							}
							//Email opdatert
							dbref.child("users").child(userViewModel.loggedInUser!.id).child("email")
								.setValue(self.email) { error, dbref in
									if error != nil {
										hideSpinner {
											showMessagePrompt("Error: " + (error?.localizedDescription ?? "Error"))
										}
										return
									}
									hideSpinner {
										part2()
									}
								}
						})
					})
				}
				
			}
			
			// Tilføj handling til alert controlleren
			alertController.addAction(loginAction)
			
			// Vis alert controlleren
			if let vc = UIApplication.shared.windows.first?.rootViewController {
				vc.present(alertController, animated: true, completion: nil)
			}
			
		} else {
			part2()
		}
	}
	
	func part2() {
		let birthdayFormatter = DateFormatter()
		birthdayFormatter.dateFormat = "dd/MM/yyyy"
		let dateFormatter = DateFormatter()
		dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.ms"
		showSpinner {
			print("Ny spinner")
			if (userViewModel.loggedInUser!.firstName != firstName || userViewModel.loggedInUser!.lastName != lastName) {
				let changeRequest = Auth.auth().currentUser?.createProfileChangeRequest()
				changeRequest?.displayName = firstName + " " + lastName
				changeRequest?.commitChanges { error in
					if error != nil {
						hideSpinner {
							showMessagePrompt("Error: " + (error?.localizedDescription ?? "Error"))
						}
						return;
					}
				}
			}
			
			let user = User(id: userViewModel.loggedInUser!.id, firstName: firstName, lastName: lastName, email: email, birthday: birthdayFormatter.string(from: birthday), campName: campName, createdAt: userViewModel.loggedInUser!.createdAt, updatedAt: dateFormatter.string(from: Date()))
			
			if (user != userViewModel.loggedInUser!) {
				
				let updates = [
					"firstName": user.firstName,
					"lastName": user.lastName,
					"campName": user.campName,
					"birthday": user.birthday,
					"updatedAt": user.updatedAt
				]

				dbref.child("users").child(userViewModel.loggedInUser!.id).updateChildValues(updates) { (error, dbref) in
					if let error = error {
						hideSpinner {
							showMessagePrompt("Error: " + error.localizedDescription)
						}
						return
					}
					hideSpinner {
						self.dismiss()
					}
				}
			}
		}
	}
}

struct EditProfileView_Previews: PreviewProvider {
	static var previews: some View {
		EditProfileView()
	}
}
