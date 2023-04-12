//
//  EventDetails.swift
//  iosApp
//
//  Created by Jackie Jensen on 29/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct EventDetails: View {
	let event: Event
	@EnvironmentObject var eventViewModel: EventViewModel
	@Environment(\.dismiss) private var dismiss
	
	var body: some View {
		let encodedImg = event.image.addingPercentEncoding(withAllowedCharacters: .urlPathAllowed)!
		let imgUrl: String = event.image.isEmpty ? "" : "https://firebasestorage.googleapis.com/v0/b/rf-app-6c0f6.appspot.com/o/images%2Fevents%2F" + String(encodedImg.components(separatedBy: "&")[0]) + "?alt=media&token=" + String(encodedImg.components(separatedBy: "&")[1])
		ZStack(alignment: .top) {
			ZStack(alignment: .bottom) {
				VStack {
					AsyncImage(
						url: URL(string: imgUrl),
						content: { image in
							image
								.resizable()
								.aspectRatio(contentMode: .fill)
						},
						placeholder: {
							ZStack {
								Rectangle()
									.foregroundColor(Color.backgroundColor)
								ProgressView()
									.scaleEffect(2)
									.progressViewStyle(CircularProgressViewStyle(tint: .mainOrange))
									
							}
						}
					)
					.frame(width: UIScreen.main.bounds.size.width , height: UIScreen.main.bounds.size.height / 3, alignment: .top)
					Spacer()
				}
				Rectangle()
					.frame(width: UIScreen.main.bounds.size.width , height: UIScreen.main.bounds.size.height * 0.7)
					.overlay {
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
						.overlay {
							VStack(alignment: .leading) {
								Text(event.title)
									.font(.system(size: 24, weight: .bold))
									.foregroundColor(Color.textColor)
								Text(event.campName)
									.font(.system(size: 16))
									.foregroundColor(Color.mainOrange)
								ScrollView(.vertical) {
									VStack {
										HStack {
											if !event.dayTime.isEmpty {
												IconField(icon: "calendar", text: event.dayTime.components(separatedBy: " ")[0])
												Spacer()
												IconField(icon: "clock", text: event.dayTime.components(separatedBy: " ")[1])
												Spacer()
											}
											if !event.location.isEmpty {
												IconField(icon: "mappin.and.ellipse", text: event.location)
												Spacer()
											}
											IconField(icon: "person.3.fill", text: "\(event.participant)")
										}
										.padding(.bottom, 10)
										.padding(.horizontal ,16)
										Text(event.description_)
											.foregroundColor(Color.textColor)
											.multilineTextAlignment(.leading)
											.frame(maxWidth: .infinity, alignment: .leading)
											.padding(.bottom, 80)
									}
									.frame(maxWidth: UIScreen.main.bounds.size.width ,maxHeight: .infinity)
								}
							}
							.padding(20)
						}
					}
					.cornerRadius(20, corners: [.topLeft, .topRight])
				Button(action: {
					//TODO: Den går tilbage når man kliker..
					eventViewModel.objectWillChange.send()
					eventViewModel.suscribeToEvent(event: event)
				}) {
					Text(eventViewModel.isSubscribe ? "Deltag ikke" : "Deltag")
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
				.padding(.horizontal, 20)
				.padding(.bottom, 20)
			}
			.onAppear {
				eventViewModel.checkIfSub(event: event)
			}
			.frame(height: .infinity)
			.ignoresSafeArea()
			.navigationBarHidden(true)
			.navigationBarBackButtonHidden(true)
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
				if (!event.facebookLink.isEmpty) {
					Button(action: {
						print("Facebook link")
					}) {
						Image(systemName: "facebook") //TODO: Add facebook logo
							.frame(width: 50, height: 50)
							.font(.system(size: 26))
							.foregroundColor(Color.backgroundColor)
							.background(Color.defaultGray).opacity(0.75)
							.clipShape(Circle())
					}
				}
			}
			.padding(.horizontal, 10)
		}
	}
	
	struct IconField: View {
		let icon: String
		let text: String
		
		var body: some View {
			VStack(spacing: 4) {
				Image(systemName: icon)
					.font(.system(size: 30))
					.frame(minWidth: 32, minHeight: 32)
					.frame(maxWidth: 32, maxHeight: 32)
					.foregroundColor(Color.textColor)
				Text(text)
					.font(.system(size: 14, weight: .bold))
					.foregroundColor(Color.textColor)
					.lineLimit(1)
			}
		}
	}
}

struct EventDetails_Previews: PreviewProvider {
	static var previews: some View {
		let desc: String = """
  A key confusion people have is that they think .frame(...) changes the size of the View it's attached to. That's not correct at all. As before, a ZStack is always the size of its contents. .frame() creates a completely new view of the requested size. It then positions the wrapped view inside itself according to the frame's alignment. So in this example it works like this:
  
  Top-level - Background - Frame - ZStack - { Image Text }
  
  The top-level view offers all its space to the Background. Backgrounds are the size of what they contain, so it offers all of that space to the Frame. The Frame is flexible in both directions (due to the max fields), and so it ignores its child's size and chooses to be the size it was offered.
  
  The Frame then offers all that space to the ZStack. The ZStack lays out its children, and returns its size as exactly the size that contains them.
  
  The Frame then places the ZStack according to the Frame's alignment (.center, since that's the default). If you'd set the Frame's alignment to .top, then the ZStack would have been placed at the top of the frame (but the text would be centered in the ZStack not in the Frame).
  """
		EventDetails(event: Event(id: "1234", userId: "1234", title: "Event title", campName: "Camp name", description: desc, image: "En ny event1680084265&525ce1c2-dfb9-4c4d-9d82-5d8f69906d79", dayTime: "Tir 10:00", location: "F40", participant: 10, timestamp: 5216356235, createdAt: "", updatedAt: "", facebookLink: ""))
		
	}
	
}
