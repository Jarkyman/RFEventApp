//
//  ProfileView.swift
//  iosApp
//
//  Created by Jackie Jensen on 22/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import FirebaseAuth
import shared

struct ProfileView: View {
	@EnvironmentObject var userViewModel: UserViewModel
	
	var body: some View {
		//TODO: De er noget med den loggedInUser der ikke virker..
			ZStack {
				Background()
				VStack() {
					WelcomeMsg(name: userViewModel.loggedInUser == nil ? "" : userViewModel.loggedInUser!.firstName + " " + userViewModel.loggedInUser!.lastName)
					InfoField(description: "Email", info: userViewModel.loggedInUser?.email ?? "Error")
					InfoField(description: "Camp", info: userViewModel.loggedInUser == nil ? "No camp" : userViewModel.loggedInUser!.campName)
					HStack {
						Divider()
							.padding(.vertical, 10)
							.padding(.leading, 12)
							.frame(maxWidth: .infinity)
							.frame(height: 1)
							.background(Color.mainOrange)
					}
					.padding(.leading)
					EventField()
					HStack {
						Divider()
							.padding(.vertical, 10)
							.padding(.leading, 12)
							.frame(maxWidth: .infinity)
							.frame(height: 1)
							.background(Color.mainOrange)
					}
					.padding(.leading)
					SettingsField()
					Spacer()
				}
		}
			.onAppear {
				AppState.shared.swipeEnabled = false
			}
			.onDisappear {
				AppState.shared.swipeEnabled = true
			}
	}
	
	struct WelcomeMsg: View {
		let name: String
		
		var body: some View {
			VStack(spacing: 0) {
				Text("Hej")
					.font(.system(size: 24, weight: .bold))
					.foregroundColor(Color.textColor)
				
				Text(name)
					.font(.system(size: 24, weight: .bold))
					.foregroundColor(Color.textColor)
					.lineLimit(1)
			}
			.frame(maxWidth: .infinity)
			.frame(height: UIScreen.main.bounds.height / 4)
			.padding(.horizontal, 16)
		}
	}
	
	struct InfoField: View {
		let description: String
		let info: String
		
		var body: some View {
			VStack(alignment: .leading, spacing: 4) {
				Text(description)
					.foregroundColor(Color.defaultGray)
					.font(.system(size: 14))
				Text(info)
					.foregroundColor(Color.textColor)
					.fontWeight(.bold)
					.font(.system(size: 16))
			}
			.padding(.horizontal, 16)
			.padding(.top, 2)
			.frame(maxWidth: .infinity, alignment: .leading)
		}
	}
	
	struct EventField: View {
		@State var createShow = false
		@State var myEventsShow = false
		var body: some View {
			VStack(alignment: .leading, spacing: 4) {
				Text("Begivenheder")
					.foregroundColor(Color.defaultGray)
					.font(.system(size: 14))
				VStack(alignment: .leading, spacing: 16) {
					NavigationLink(
						destination: CreateEventView(event: nil),
					isActive: $createShow) {
						Text("Opret begivenhed")
							.foregroundColor(Color.textColor)
							.font(.system(size: 16))
							.fontWeight(.bold)
							.onTapGesture {
								createShow = true
							}
						}
					NavigationLink(
						destination: MyEventsView(),
					isActive: $myEventsShow) {
						Text("Mine begivenheder")
							.foregroundColor(Color.textColor)
							.font(.system(size: 16))
							.fontWeight(.bold)
							.onTapGesture {
								myEventsShow = true
							}
						}
				}
			}
			
			.padding(.horizontal, 16)
			.padding(.top, 8)
			.frame(maxWidth: .infinity, alignment: .leading)
			
		}
	}
	
	struct SettingsField: View {
		@EnvironmentObject var userViewModel: UserViewModel
		
		@State var editShow = false
		@State var changePassShow = false
		@State var loginShow = false
		var body: some View {
				VStack(alignment: .leading, spacing: 4) {
					Text("Indstillinger")
						.foregroundColor(Color.defaultGray)
						.font(.system(size: 14))
					VStack(alignment: .leading, spacing: 16) {
						NavigationLink(
							destination: EditProfileView(),
						isActive: $editShow) {
							Text("Rediger Profil")
								.foregroundColor(Color.textColor)
								.font(.system(size: 16))
								.fontWeight(.bold)
								.onTapGesture(perform: {
									editShow = true
								})
							}
						NavigationLink(
							destination: ChangePasswordView(),
						isActive: $changePassShow) {
							Text("Skift kode")
								.foregroundColor(Color.textColor)
								.font(.system(size: 16))
								.fontWeight(.bold)
								.onTapGesture(perform: {
									changePassShow = true
								})
							}
						/*Text("Notifikationer")
							.foregroundColor(Color.textColor)
							.font(.system(size: 16))
							.fontWeight(.bold)
							.onTapGesture(perform: {
								showMessagePrompt("Notifikationer")
							})*/
						/*Text("Sprog")
							.foregroundColor(Color.textColor)
							.font(.system(size: 16))
							.fontWeight(.bold)
							.onTapGesture(perform: {
								showMessagePrompt("Sprog")
							})*/
						NavigationLink(
							destination: SplashView(),
						isActive: $loginShow) {
								Text("Log ud")
									.foregroundColor(Color.textColor)
									.font(.system(size: 16))
									.fontWeight(.bold)
									.onTapGesture(perform: {
										let firebaseAuth = Auth.auth()
										do {
											try firebaseAuth.signOut()
											loginShow = true
										} catch let signOutError as NSError {
											showMessagePrompt("Error signing out: %@" + signOutError.localizedDescription)
										}
									})
							}
					}
				}
				.padding(.horizontal, 16)
				.padding(.top, 8)
				.frame(maxWidth: .infinity, alignment: .leading)
		}
	}
	
	struct PopupNotification: View {
		@State private var notificationReminder = false
		@State private var notificationNews = false
		@State private var notificationChanges = false
		
		var body: some View {
			VStack(alignment: .leading) {
				HStack {
					Text("Påmindelser")
						.fontWeight(.bold)
						.foregroundColor(Color.textColor)
					Spacer()
					Toggle(
						"",
						isOn: $notificationReminder
					)
				}.padding(.vertical, 8)
				
				HStack {
					Text("Nyheder")
						.fontWeight(.bold)
						.foregroundColor(Color.textColor)
					Spacer()
					Toggle(
						"",
						isOn: $notificationNews
					)
				}.padding(.vertical, 8)
				
				HStack {
					Text("Ændringer")
						.fontWeight(.bold)
						.foregroundColor(Color.textColor)
					Spacer()
					Toggle(
						"",
						isOn: $notificationChanges
					)
				}.padding(.vertical, 8)
			}
		}
	}
	
	struct PopupLangurage: View {
		@State private var notificationReminder = false
		@State private var notificationNews = false
		
		var body: some View {
			HStack(alignment: .top) {
				HStack {
					Image("dk")
						.frame(width: 50, height: 50)
				}.padding(.vertical, 8)
				
				HStack {
					Image("en")
						.frame(width: 50, height: 50)
				}.padding(.vertical, 8)
			}
		}
	}
}

struct ProfileView_Previews: PreviewProvider {
	static var previews: some View {
		ProfileView()
	}
}
