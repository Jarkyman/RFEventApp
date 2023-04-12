//
//  CalenderView.swift
//  iosApp
//
//  Created by Jackie Jensen on 22/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct CalendarView: View {
	@EnvironmentObject var viewModel: EventViewModel
	
	var body: some View {
		ZStack {
			Background()
			VStack(alignment: .leading) {
				DateSelector()
				switch viewModel.calenderState {
				case .loaded(let events):
					ScrollView(.vertical) {
						LazyVStack(alignment: .leading ,spacing: 5) {
							ForEach(events, id: \.id) { event in
								EventBox(event: event)
							}
						}
					}
				case .idle:
					Color.clear.onAppear {
						//TODO: Skal ændres til dag når det laves
						let date = Calendar.current.date(from: DateComponents(timeZone: TimeZone(identifier: "Europe/Copenhagen")!, year: 2023, month: 6, day: 24, hour: 0, minute: 0, second: 0))!
						viewModel.loadTodayEvents(date: date)
					}
				case .loading:
					VStack {
						ProgressView()
							.scaleEffect(2)
							.progressViewStyle(CircularProgressViewStyle())
					}
					.frame(maxWidth: .infinity, maxHeight: .infinity)
				case .empty:
					VStack {
						Text("Ingen begivenheder")
							.font(.system(size: 24))
							.foregroundColor(Color.textColor)
						Text("Du har ingen begivenheder i dag.")
							.font(.system(size: 16))
							.foregroundColor(Color.defaultGray)
					}
					.frame(maxWidth: .infinity, maxHeight: .infinity)

				case .failed(_):
					Color.clear
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
}

struct DateSelector: View {
	@EnvironmentObject var viewModel: EventViewModel
	@State var selection: Int = 0

	
	let tabs = ["LØR", "SØN", "MAN", "TIR", "ONS", "TOR", "FRE", "LØR", "SØN"]
	
	let days: [Int: Date] = [
		0: Calendar.current.date(from: DateComponents(timeZone: TimeZone(identifier: "Europe/Copenhagen")!, year: 2023, month: 6, day: 24, hour: 0, minute: 0, second: 0))!,
		1: Calendar.current.date(from: DateComponents(timeZone: TimeZone(identifier: "Europe/Copenhagen")!, year: 2023, month: 6, day: 25, hour: 0, minute: 0, second: 0))!,
		2: Calendar.current.date(from: DateComponents(timeZone: TimeZone(identifier: "Europe/Copenhagen")!, year: 2023, month: 6, day: 26, hour: 0, minute: 0, second: 0))!,
		3: Calendar.current.date(from: DateComponents(timeZone: TimeZone(identifier: "Europe/Copenhagen")!, year: 2023, month: 6, day: 27, hour: 0, minute: 0, second: 0))!,
		4: Calendar.current.date(from: DateComponents(timeZone: TimeZone(identifier: "Europe/Copenhagen")!, year: 2023, month: 6, day: 28, hour: 0, minute: 0, second: 0))!,
		5: Calendar.current.date(from: DateComponents(timeZone: TimeZone(identifier: "Europe/Copenhagen")!, year: 2023, month: 6, day: 29, hour: 0, minute: 0, second: 0))!,
		6: Calendar.current.date(from: DateComponents(timeZone: TimeZone(identifier: "Europe/Copenhagen")!, year: 2023, month: 6, day: 30, hour: 0, minute: 0, second: 0))!,
		7: Calendar.current.date(from: DateComponents(timeZone: TimeZone(identifier: "Europe/Copenhagen")!, year: 2023, month: 7, day: 1, hour: 0, minute: 0, second: 0))!,
		8: Calendar.current.date(from: DateComponents(timeZone: TimeZone(identifier: "Europe/Copenhagen")!, year: 2023, month: 7, day: 2, hour: 0, minute: 0, second: 0))!]
	
	var body: some View {
		ScrollView(.horizontal, showsIndicators: false) {
			ScrollViewReader { proxy in
				LazyHStack(spacing: 10) {
					ForEach(0..<tabs.count) { index in
						Button(action: {
							viewModel.loadTodayEvents(date: days[index])
							selection = index
							withAnimation { proxy.scrollTo(index, anchor: .center) }
						}) {
							Text(tabs[index])
								.id(index)
								.frame(width: 70, height: 80)
								.foregroundColor(selection == index ? Color.darkGray : Color.mainOrange)
								.font(.system(size: 18, weight: .bold))
								.background(selection == index ? Color.mainOrange : Color.darkGray)
								.cornerRadius(10)
						}
					}
				}
				.onAppear {
					//TODO: Scroll to virker ikke on load, virker kun hvis elementet er på skærmen
					withAnimation { proxy.scrollTo(selection, anchor: .center) }
				}
				.padding(10)
				.frame(height: 100)
			}
		}
		HStack {
			Divider()
				.padding(.vertical, 10)
				.padding(.leading, 12)
				.frame(maxWidth: .infinity)
				.frame(height: 1)
				.background(Color.mainOrange)
		}
	}
}

struct EventBox: View {
	let event: Event
	@EnvironmentObject var viewModel: EventViewModel
	@State var isDetailsShow = false
	
	func checkOld() -> Bool {
		let eventDate = Date(timeIntervalSince1970: TimeInterval(event.timestamp / 1000))
		let currentDate = Date()
		
		return eventDate < currentDate
	}
	
	var attributedString: AttributedString {
		let time = event.dayTime.components(separatedBy: " ")[1]
		var result = AttributedString(time)
		if (checkOld()) {
			result.strikethroughStyle = Text.LineStyle(
				pattern: .solid, color: .lightGray)
		}
		return result
	}
	
	var body: some View {
		NavigationLink(
			destination: EventDetails(event: event), isActive: $isDetailsShow
		) {
			VStack(alignment: .leading, spacing: 8) {
				Text(event.title + " ")
					.font(.system(size: 18))
					.fontWeight(.semibold)
					.foregroundColor(Color.textColor)
				+ Text(event.campName)
					.font(.system(size: 14))
					.foregroundColor(Color.orange)
				HStack {
					Text(attributedString)
						.font(.system(size: 16))
						.foregroundColor(checkOld() ? Color.defaultGray : Color.textColor)
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
				isDetailsShow = true
			}
		}
	}
}


struct CalenderView_Previews: PreviewProvider {
	static var previews: some View {
		CalendarView()
	}
}
