//
//  EventViewModel.swift
//  iosApp
//
//  Created by Jackie Jensen on 29/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared
import FirebaseDatabase
import FirebaseAuth

class EventViewModel: ObservableObject {
	enum State {
		case idle
		case loading
		case failed(Error)
		case loaded([Event])
		case empty
	}
	
	var dbRef = Database.database().reference()
	
	@Published private(set) var popularState = State.idle
	@Published private(set) var upcommingState = State.idle
	@Published private(set) var calenderState = State.idle
	@Published private(set) var myEventsState = State.idle
	@Published private(set) var isSubscribe = false
	
	init() {
		loadPopularEvents()
		loadComingEvents()
	}
	
	func loadPopularEvents(){
		let timestampInMilliseconds = Int(Date().timeIntervalSince1970 * 1000)
		dbRef.child("events").queryOrdered(byChild: "timestamp").queryStarting(atValue: timestampInMilliseconds).observe(.value, with: { snapshot in
			var popularEvents: [Event] = []
			for child in snapshot.children.allObjects as! [DataSnapshot] {
				if let userData = child.value as? [String: Any] {
					let id = userData["id"] as? String ?? ""
					let userId = userData["userId"] as? String ?? ""
					let title = userData["title"] as? String ?? ""
					let description = userData["description"] as? String ?? ""
					let dayTime = userData["dayTime"] as? String ?? ""
					let campName = userData["campName"] as? String ?? ""
					let image = userData["image"] as? String ?? ""
					let location = userData["location"] as? String ?? ""
					let participant = userData["participant"] as? Int32 ?? 999
					let facebookLink = userData["facebookLink"] as? String ?? ""
					let timestamp = userData["timestamp"] as? Int64 ?? 0
					let createdAt = userData["createdAt"] as? String ?? ""
					let updatedAt = userData["updatedAt"] as? String ?? ""
					
					// create new event object
					let event = Event(id: id, userId: userId, title: title, campName: campName, description: description, image: image, dayTime: dayTime, location: location, participant: participant, timestamp: timestamp, createdAt: createdAt, updatedAt: updatedAt, facebookLink: facebookLink)
					
					// use the event object as needed
					popularEvents.append(event)
					print("Event is set: " + event.title + " " + event.dayTime)
				}
			}
			if (!popularEvents.isEmpty) {
				popularEvents = popularEvents.sorted(by: { $0.participant > $1.participant })
				self.popularState = State.loaded(Array(popularEvents.prefix(5)))
			}else {
				self.popularState = State.empty
			}
			//TODO: Error handeling
			print("load done")
		})
	}
	
	func loadComingEvents(){
		//TODO: pagination
		//self.upcommingState = State.loading
		let timestampInMilliseconds = Int(Date().timeIntervalSince1970 * 1000)
		dbRef.child("events").queryOrdered(byChild: "timestamp").queryStarting(atValue: timestampInMilliseconds).observe(.value, with: { snapshot in
			var upComingEvents: [Event] = []
			for child in snapshot.children.allObjects as! [DataSnapshot] {
				if let userData = child.value as? [String: Any] {
					
					let id = userData["id"] as? String ?? ""
					let userId = userData["userId"] as? String ?? ""
					let title = userData["title"] as? String ?? ""
					let description = userData["description"] as? String ?? ""
					let dayTime = userData["dayTime"] as? String ?? ""
					let campName = userData["campName"] as? String ?? ""
					let image = userData["image"] as? String ?? ""
					let location = userData["location"] as? String ?? ""
					let participant = userData["participant"] as? Int32 ?? 999
					let facebookLink = userData["facebookLink"] as? String ?? ""
					let timestamp = userData["timestamp"] as? Int64 ?? 0
					let createdAt = userData["createdAt"] as? String ?? ""
					let updatedAt = userData["updatedAt"] as? String ?? ""
					
					// create new event object
					let event = Event(id: id, userId: userId, title: title, campName: campName, description: description, image: image, dayTime: dayTime, location: location, participant: participant, timestamp: timestamp, createdAt: createdAt, updatedAt: updatedAt, facebookLink: facebookLink)
					
					// use the event object as needed
					upComingEvents.append(event)
					print("Event is set: " + event.title + " " + event.dayTime)
				}
			}
			if (!upComingEvents.isEmpty) {
				self.upcommingState = State.loaded(upComingEvents)
			}else {
				self.upcommingState = State.empty
			}
			print("load done")
		}){ (error) in
			//TODO: Error handeling
			print("Der var en fejl")
			self.upcommingState = State.failed(error)
		}
	}
	
	func loadTodayEvents(date: Date?) {
		//self.calenderState = State.loading
		dbRef.child("events").queryOrdered(byChild: "timestamp").observe(.value, with: { snapshot in
			var todayEvents: [Event] = []
			for child in snapshot.children.allObjects as! [DataSnapshot] {
				if let userData = child.value as? [String: Any] {
					let myEvents = self.dbRef.child("users/\(Auth.auth().currentUser!.uid)/events").observe(.value, with: { snapshot in
						for child in snapshot.children.allObjects as! [DataSnapshot] {
							if let userEventData = child.value as? [String: Any] {
								let userEventId = userEventData["id"] as? String ?? "None"
								let eventId = userData["id"] as? String ?? ""
								if (userEventId == eventId) {
									let timestamp = userData["timestamp"] as? Int64 ?? 0 // timestamp i millisekunder
									let dateFromTimestamp = Date(timeIntervalSince1970: Double(timestamp) / 1000.0)
									
									let calendar = Calendar.current
									let dayOfMonthTimestamp = calendar.component(.day, from: dateFromTimestamp)
									let dayOfMonthSelected = calendar.component(.day, from: date!)
									
									print("\(dayOfMonthTimestamp) / \(dayOfMonthSelected)")
									
									if (dayOfMonthSelected == dayOfMonthTimestamp) {
										let id = userData["id"] as? String ?? ""
										let userId = userData["userId"] as? String ?? ""
										let title = userData["title"] as? String ?? ""
										let description = userData["description"] as? String ?? ""
										let dayTime = userData["dayTime"] as? String ?? ""
										let campName = userData["campName"] as? String ?? ""
										let image = userData["image"] as? String ?? ""
										let location = userData["location"] as? String ?? ""
										let participant = userData["participant"] as? Int32 ?? 999
										let facebookLink = userData["facebookLink"] as? String ?? ""
										let createdAt = userData["createdAt"] as? String ?? ""
										let updatedAt = userData["updatedAt"] as? String ?? ""
										
										let event = Event(id: id, userId: userId, title: title, campName: campName, description: description, image: image, dayTime: dayTime, location: location, participant: participant, timestamp: timestamp, createdAt: createdAt, updatedAt: updatedAt, facebookLink: facebookLink)
										
										todayEvents.append(event)
									}
								}
							}
						}
						if (!todayEvents.isEmpty) {
							self.calenderState = State.loaded(todayEvents)
						}else {
							print("Empty list")
							self.calenderState = State.empty
						}
					})
				}
				//TODO: Error handeling
				
				print("load done")
			}
		})
	}
	
	func loadMyEventsEvents(){
		//TODO: pagination
		//self.myEventsState = State.loading
		dbRef.child("events").queryOrdered(byChild: "timestamp").observe(.value, with: { snapshot in
			var myEvents: [Event] = []
			for child in snapshot.children.allObjects as! [DataSnapshot] {
				if let userData = child.value as? [String: Any] {
					
					let id = userData["id"] as? String ?? ""
					let userId = userData["userId"] as? String ?? ""
					let title = userData["title"] as? String ?? ""
					let description = userData["description"] as? String ?? ""
					let dayTime = userData["dayTime"] as? String ?? ""
					let campName = userData["campName"] as? String ?? ""
					let image = userData["image"] as? String ?? ""
					let location = userData["location"] as? String ?? ""
					let participant = userData["participant"] as? Int32 ?? 999
					let facebookLink = userData["facebookLink"] as? String ?? ""
					let timestamp = userData["timestamp"] as? Int64 ?? 0
					let createdAt = userData["createdAt"] as? String ?? ""
					let updatedAt = userData["updatedAt"] as? String ?? ""
					
					// create new event object
					let event = Event(id: id, userId: userId, title: title, campName: campName, description: description, image: image, dayTime: dayTime, location: location, participant: participant, timestamp: timestamp, createdAt: createdAt, updatedAt: updatedAt, facebookLink: facebookLink)
					
					// use the event object as needed
					if(event.userId == Auth.auth().currentUser!.uid) {
						myEvents.append(event)
					}
					print("Event is set: " + event.title + " " + event.dayTime)
				}
			}
			if (!myEvents.isEmpty) {
				self.myEventsState = State.loaded(myEvents)
			}else {
				self.myEventsState = State.empty
			}
			print("load done")
		}){ (error) in
			//TODO: Error handeling
			print("Der var en fejl")
			self.myEventsState = State.failed(error)
		}
	}
	
	func checkIfSub(event: Event) {
		print(event.id)
		dbRef.child("users/\(Auth.auth().currentUser!.uid)/events/\(event.id)").getData(completion: { error, snapshot in
			for child in snapshot?.children.allObjects as! [DataSnapshot] {
				if let userData = child.value as? [String: Any] {
					self.isSubscribe = userData.keys.contains(event.id)
				}
			}
		})
	}
	
	func suscribeToEvent(event: Event) {
		var participant = event.participant
		if (isSubscribe) {
			participant = participant - 1
			dbRef.child("events").child(event.id).updateChildValues(["participant": participant])
			dbRef.child("users").child(Auth.auth().currentUser!.uid).child("events").child(event.id).removeValue()
			isSubscribe = !isSubscribe
		}else {
			participant = participant + 1
			dbRef.child("events").child(event.id).updateChildValues(["participant": participant])
			dbRef.child("users").child(Auth.auth().currentUser!.uid).child("events").child(event.id).setValue(
				["title": event.title,
				 "description": event.description_,
				 "userId": event.userId,
				 "campName": event.campName,
				 "id": event.id,
				 "dayTime": event.dayTime,
				 "image": event.image,
				 "facebookLink": event.facebookLink,
				 "location": event.location,
				 "participant": event.participant,
				 "timestamp": event.timestamp,
				 "createdAt": event.createdAt,
				 "updatedAt": event.updatedAt])
			isSubscribe = !isSubscribe
		}
	}
	
}
