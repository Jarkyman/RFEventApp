//
//  MyEventsView.swift
//  iosApp
//
//  Created by Jackie Jensen on 30/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct MyEventsView: View {
	@EnvironmentObject var viewModel: EventViewModel
	@Environment(\.dismiss) private var dismiss
	
    var body: some View {
		ZStack(alignment: .top) {
			Background()
			ScrollView(.vertical) {
				switch viewModel.myEventsState {
				case .idle:
					Color.clear.onAppear(perform: viewModel.loadMyEventsEvents)
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
					LazyVStack(alignment: .leading ,spacing: 5) {
						ForEach(events, id: \.id) { event in
							EventBoxEdit(event: event)
								.padding(.horizontal, 20)
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
			}
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
		.navigationBarHidden(true)
		.navigationBarBackButtonHidden(true)
    }
}

struct EventBoxEdit: View {
	let event: Event
	@EnvironmentObject var viewModel: EventViewModel
	@State var isEditShow = false
	
	func checkOld() -> Bool {
		let eventDate = Date(timeIntervalSince1970: TimeInterval(event.timestamp / 1000))
		let currentDate = Date()
		
		return eventDate < currentDate
	}
	
	var body: some View {
		NavigationLink(
			destination: CreateEventView(event: event), isActive: $isEditShow
		) {
			VStack(alignment: .leading, spacing: 8) {
				//TODO: Hvis teksten er for lang og rammer næste linje, så starter den i midten...
				Text(event.title + " ")
					.font(.system(size: 18))
					.fontWeight(.semibold)
					.foregroundColor(Color.textColor)
				+ Text(event.campName)
					.font(.system(size: 14))
					.foregroundColor(Color.orange)
				HStack {
					Text(event.dayTime)
						.font(.system(size: 16))
						.foregroundColor(Color.textColor)
						.multilineTextAlignment(.trailing)
						.lineLimit(1)
					Spacer()
				}
				.frame(width: .infinity)
			}
			.padding(16)
			.background(Color.darkGray)
			.cornerRadius(10)
			.padding(EdgeInsets(top: 16, leading: 16, bottom: 0, trailing: 16))
			.onTapGesture {
				isEditShow = true
			}
		}
	}
}

struct MyEventsView_Previews: PreviewProvider {
    static var previews: some View {
        MyEventsView()
			.environmentObject(EventViewModel())
    }
}
