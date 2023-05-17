//
//  EventSearchView.swift
//  iosApp
//
//  Created by Jackie Jensen on 19/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct EventSearchView: View {
	@EnvironmentObject var viewModel: EventViewModel
	@State private var searchText = ""
	@State private var isEditing = false
	
	init() {
		UITextField.appearance(whenContainedInInstancesOf: [UISearchBar.self]).backgroundColor = .white
		UITextField.appearance(whenContainedInInstancesOf: [UISearchBar.self]).tintColor = .black
		UINavigationBar.appearance().barTintColor = .clear
		UINavigationBar.appearance().setBackgroundImage(UIImage(), for: .default)
	}
	
	var body: some View {
		NavigationView {
			ZStack(alignment: .top) {
				Background()
				if (searchText.isEmpty) {
					VStack {
						Spacer()
						Text("Søg efter events")
							.font(.system(size: 24))
							.foregroundColor(Color.textColor)
							.multilineTextAlignment(.center)
						Text("Her kan du søge efter den event du leder efter")
							.font(.system(size: 16))
							.foregroundColor(Color.gray)
							.multilineTextAlignment(.center)
						Spacer()
					}
				} else {
					ScrollView {
						switch viewModel.eventSearchState {
						case .idle:
							Color.clear.onAppear()
						case .loading:
							VStack {
								ProgressView()
									.scaleEffect(2)
									.progressViewStyle(CircularProgressViewStyle())
							}
							.frame(maxWidth: .infinity, maxHeight: .infinity)
						case .failed(let error):
							VStack {
								Spacer()
								Text("No events")
									.font(.system(size: 24))
									.foregroundColor(Color.textColor)
									.multilineTextAlignment(.center)
								Text("Der gik noget galt, prøv igen senere")
									.font(.system(size: 16))
									.foregroundColor(Color.gray)
									.multilineTextAlignment(.center)
								Spacer()
							}
						case .loaded(let events):
							LazyVStack(alignment: .leading ,spacing: 20) {
								ForEach(events, id: \.id) { event in
									EventCardBig(event: event)
								}
							}
						case .empty:
							VStack {
								Spacer()
								Text("Ingen begivenheder")
									.font(.system(size: 24))
									.foregroundColor(Color.textColor)
								Text("Der er ikke nogle events")
									.font(.system(size: 16))
									.foregroundColor(Color.defaultGray)
								Spacer()
							}
							.frame(maxWidth: .infinity, maxHeight: .infinity)
						}
					}
				}
			}
			.navigationTitle(searchText)
			.searchable(text: $searchText, placement: .navigationBarDrawer(displayMode: .always), prompt: "Søg efter evnets...")
			.onChange(of: searchText) { newValue in
				viewModel.searchEvents(keyword: newValue)
			}
			
		}
		.navigationBarHidden(true)
		.navigationBarBackButtonHidden(true)
	}
}

struct EventCardBig: View {
	let event: Event
	
	@EnvironmentObject var viewModel: EventViewModel
	@State var detailsShow = false
	
	var body: some View {
		let encodedImg = event.image.addingPercentEncoding(withAllowedCharacters: .urlPathAllowed)!
		let imgUrl: String = event.image.isEmpty ? "" : "https://firebasestorage.googleapis.com/v0/b/rf-app-6c0f6.appspot.com/o/images%2Fevents%2F" + String(encodedImg.components(separatedBy: "&")[0]) + "?alt=media&token=" + String(encodedImg.components(separatedBy: "&")[1])
		
		NavigationLink(
			destination: EventDetails(event: event),
			isActive: $detailsShow) {
				ZStack(alignment: .bottom) {
					AsyncImage(
						url: URL(string: imgUrl),
						content: { image in
							image
								.resizable()
								.aspectRatio(contentMode: .fill)
								.frame(width: UIScreen.main.bounds.size.width - 40, height: (UIScreen.main.bounds.size.width - 40) * 0.8)
								.cornerRadius(40, corners: [.bottomRight, .bottomLeft, .topRight, .topLeft])
						},
						placeholder: {
							ZStack {
								Rectangle()
									.foregroundColor(Color.backgroundColor)
									.frame(width: UIScreen.main.bounds.size.width - 40, height: (UIScreen.main.bounds.size.width - 40) * 0.8)
									.cornerRadius(40, corners: [.bottomRight, .bottomLeft, .topRight, .topLeft])
								ProgressView()
									.scaleEffect(2)
									.progressViewStyle(CircularProgressViewStyle(tint: .mainOrange))
								
							}
						}
					)
					.frame(width: UIScreen.main.bounds.size.width - 40, height: (UIScreen.main.bounds.size.width - 40) * 0.8)
					.cornerRadius(40, corners: [.bottomRight, .bottomLeft, .topRight, .topLeft])
					
					Rectangle()
						.foregroundColor(Color.darkGray).opacity(0.85)
						.frame(height: (UIScreen.main.bounds.height / 3) / 3)
						.padding(.bottom, 0)
						.cornerRadius(40, corners: [.bottomRight, .bottomLeft])
						.overlay {
							VStack(alignment: .leading, spacing: 2) {
								Text(event.title + " ")
									.font(.system(size: 20, weight: .bold))
									.foregroundColor(Color.textColor)
								+ Text(event.campName)
									.font(.system(size: 14, weight: .bold))
									.foregroundColor(Color.mainOrange)
								
								HStack(alignment: .top) {
									VStack(alignment: .leading, spacing: 2) {
										Text(event.dayTime)
											.font(.system(size: 12))
											.foregroundColor(Color.textColor)
										
										HStack {
											Image(systemName: "person.3.fill")
												.resizable()
												.aspectRatio(contentMode: .fit)
												.frame(width: 20, height: 20)
												.foregroundColor(Color.textColor)
											
											Text("\(event.participant)")
												.font(.system(size: 12))
												.foregroundColor(Color.textColor)
										}
									}
									
									Divider()
										.frame(width: 2, height: .infinity)
										.background(Color.lightGray)
									
									
									Text(event.description_)
										.font(.system(size: 12))
										.foregroundColor(Color.textColor)
									Spacer()
								}
								.frame(maxHeight: .infinity)
							}
							.padding(16)
						}
				}
				.padding(20)
				.onTapGesture {
					detailsShow = true
				}
			}
	}
}


struct EventSearchView_Previews: PreviewProvider {
	static var previews: some View {
		EventSearchView()
			.environmentObject(EventViewModel())
	}
}

