//
//  HomeView.swift
//  iosApp
//
//  Created by Jackie Jensen on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared
import FirebaseDatabase

struct HomeView: View {
	var dbRef = Database.database().reference()
	@EnvironmentObject var viewModel: EventViewModel
	
	var body: some View {
			ZStack {
				Background()
				ScrollView(.vertical) {
					VStack(alignment: .leading) {
						switch viewModel.popularState {
						case .loaded(let events):
							Text("Populær")
								.foregroundColor(Color.textColor)
								.fontWeight(.bold)
								.font(.system(size: 24))
								.padding(.leading, 16)
							ScrollView(.horizontal) {
								LazyHStack(alignment: .top ,spacing: 20) {
									ForEach(events, id: \.id) { event in
										EventCardSmall(event: event)
									}
								}
								.padding(20)
							}
							HStack {
								Divider()
									.padding(.vertical, 10)
									.padding(.leading, 12)
									.frame(maxWidth: .infinity)
									.frame(height: 1)
									.background(Color.mainOrange)
							}
							.padding(.leading)
						case .idle:
							Color.clear.onAppear(perform: viewModel.loadPopularEvents)
						case .loading:
							Color.clear
						case .failed(_):
							Color.clear
						case .empty:
							Color.clear
						}
						Text("Kommende")
							.foregroundColor(Color.textColor)
							.fontWeight(.bold)
							.font(.system(size: 24))
							.padding(.leading, 16)
						
						switch viewModel.upcommingState {
						case .idle:
							Color.clear.onAppear(perform: viewModel.loadComingEvents)
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
								Text("No events")
									.font(.system(size: 24))
									.foregroundColor(Color.textColor)
								Text("Der er ikke nogle events")
									.font(.system(size: 16))
									.foregroundColor(Color.defaultGray)
							}
							.frame(maxWidth: .infinity, maxHeight: .infinity)
						}
						Spacer()
					}
				}
			}
			.onAppear {
				AppState.shared.swipeEnabled = false
			}
			.onDisappear {
				AppState.shared.swipeEnabled = true
			}
		
	}
	
	
	struct EventCardSmall: View {
		let event: Event
		
		@EnvironmentObject var viewModel: EventViewModel
		@State var detailsShow = false
		
		var body: some View {
			let encodedImg = event.image.addingPercentEncoding(withAllowedCharacters: .urlPathAllowed)!
			let imgUrl: String = event.image.isEmpty ? "" : "https://firebasestorage.googleapis.com/v0/b/rf-app-6c0f6.appspot.com/o/images%2Fevents%2F" + String(encodedImg.components(separatedBy: "&")[0]) + "?alt=media&token=" + String(encodedImg.components(separatedBy: "&")[1])
			
			NavigationLink(
				destination: EventDetails(event: event),
				isActive: $detailsShow) {
					HStack(spacing: 1) {
						AsyncImage(
							url: URL(string: imgUrl),
							content: { image in
								image
									.resizable()
									.aspectRatio(contentMode: .fill)
									.frame(width: 140, height: 140)
							},
							placeholder: {
								ZStack {
									Rectangle()
										.foregroundColor(Color.backgroundColor)
										.frame(width: 140, height: 140)
									ProgressView()
										.scaleEffect(2)
										.progressViewStyle(CircularProgressViewStyle(tint: .mainOrange))
										
								}
							}
						)
						//.resizable()
						.frame(width: 140, height: 140)
						.clipShape(RoundedRectangle(cornerRadius: 10))
						.overlay(
							RoundedRectangle(cornerRadius: 10)
								.stroke(Color.mainOrange, lineWidth: 1)
						)
						
						VStack(alignment: .leading, spacing: 5) {
							Text(event.title)
								.font(.system(size: 18, weight: .bold))
								.foregroundColor(Color.textColor)
								.lineLimit(2)
							HStack(spacing: 5) {
								Image(systemName: "person.3.fill")
									.resizable()
									.frame(width: 30, height: 20)
									.foregroundColor(Color.mainOrange)
								Text(String(event.participant))
									.foregroundColor(Color.defaultGray)
									.font(.system(size: 12))
								Spacer()
								Text(event.dayTime)
									.foregroundColor(Color.defaultGray)
									.font(.system(size: 12))
								Image(systemName: "calendar")
									.resizable()
									.frame(width: 20, height: 20)
									.foregroundColor(Color.mainOrange)
							}
							Text(event.description_)
								.foregroundColor(Color.defaultGray)
								.font(.system(size: 14))
							Spacer()
						}
						.padding(10)
						.frame(width: 180, height: 125)
						.background(Color.darkGray)
						.cornerRadius(20, corners: .topRight)
						.cornerRadius(20, corners: .bottomRight)
						.foregroundColor(.white)
					}
					.frame(maxWidth: .infinity)
					.onTapGesture {
						detailsShow = true
					}
				}
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
	
}

struct HomeView_Previews: PreviewProvider {
	static var previews: some View {
		HomeView()
	}
}
