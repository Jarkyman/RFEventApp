//
//  CreateEventView.swift
//  iosApp
//
//  Created by Jackie Jensen on 30/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import Combine
import shared
import FirebaseAuth
import FirebaseStorage
import FirebaseDatabase

struct CreateEventView: View {
	let dbRef = Database.database().reference()
	let storageRef = Storage.storage().reference()
	
	@Environment(\.dismiss) private var dismiss
	@EnvironmentObject var eventViewModel: EventViewModel
	
	var isEdit: Bool = false
	@State var imageChanged: Bool = false
	var event: Event?
	
	@State var descriptionLetters: Int = 0
	
	@State private var image: UIImage = UIImage()
	@State private var isImageSelected: Bool = false
	@State private var showSheet = false
	
	@State private var title: String = ""
	@State private var campName = ""
	@State private var location = ""
	@State private var description = ""
	
	
	var thirtyDays: ClosedRange<Date> {
		var start = Calendar.current.date(from: DateComponents(timeZone: TimeZone(identifier: "Europe/Copenhagen")!, year: 2023, month: 6, day: 24, hour: 0, minute: 0, second: 0))!
		let end = Calendar.current.date(from: DateComponents(timeZone: TimeZone(identifier: "Europe/Copenhagen")!, year: 2023, month: 7, day: 2, hour: 0, minute: 0, second: 0))!
		
		let today = Date()
		if start < today  {
			if end > today {
				start = today
			}else {
				start = end
			}
		}
		return start...end
	}
	@State var currentDate = Calendar.current.date(from: DateComponents(timeZone: TimeZone(identifier: "Europe/Copenhagen")!, year: 2023, month: 6, day: 24, hour: 10, minute: 30, second: 0))!
	
	init(event: Event?) {
		if (event != nil) {
			self.event = event
			self.isEdit = true
			_title = State(initialValue: event!.title)
			_campName = State(initialValue: event!.campName)
			_location = State(initialValue: event!.location)
			_description = State(initialValue: event!.description_)
			_descriptionLetters = State(initialValue: event!.description_.count)
			
			let timestamp = event!.timestamp
			_currentDate = State(initialValue: Date(timeIntervalSince1970: TimeInterval(timestamp / 1000)))
			
			
		}else {
		}
		UITextView.appearance().backgroundColor = .clear
	}
	
	var body: some View {
		//NavigationView {
		ZStack(alignment: .top) {
			Background()
			ScrollView {
				VStack(alignment: .center) {
					
					Rectangle()
						.foregroundColor(Color.defaultGray).opacity(0.75)
						.frame(width: 200, height: 200)
						.cornerRadius(70)
						.overlay {
							ZStack {
								VStack {
									Image(systemName: "plus")
										.resizable()
										.frame(maxWidth: 40, maxHeight: 40)
										.foregroundColor(Color.mainOrange)
									Text("Tilføj billede")
										.foregroundColor(Color.mainOrange)
								}
								if(isEdit && !isImageSelected) {
									let encodedImg = event!.image.addingPercentEncoding(withAllowedCharacters: .urlPathAllowed)!
									let imgUrl: String = event!.image.isEmpty ? "" : "https://firebasestorage.googleapis.com/v0/b/rf-app-6c0f6.appspot.com/o/images%2Fevents%2F" + String(encodedImg.components(separatedBy: "&")[0]) + "?alt=media&token=" + String(encodedImg.components(separatedBy: "&")[1])
									
									AsyncImage(
										url: URL(string: imgUrl),
										content: { image in
											image
												.resizable()
												.background(Color.clear)
												.frame(width: 200, height: 200)
												.cornerRadius(70)
												.aspectRatio(contentMode: .fill)
										},
										placeholder: {
											ProgressView()
										}
									)
								}else {
									Image(uiImage: self.image)
										.resizable()
										.background(Color.clear)
										.frame(width: 200, height: 200)
										.cornerRadius(70)
										.aspectRatio(contentMode: .fill)
								}
								RoundedRectangle(cornerRadius: 70)
									.stroke(Color.mainOrange, lineWidth: 1)
							}
						}
						.padding(20)
						.onTapGesture {
							showSheet = true
						}
						.sheet(isPresented: $showSheet, onDismiss: {showSheet = false}) {
							ImagePicker(sourceType: .photoLibrary, selectedImage: self.$image, isSelected: self.$isImageSelected)
						}
					
					TextField("", text: $title)
						.disableAutocorrection(true)
						.keyboardType(.default)
						.placeholder(when: title.isEmpty) {
							Text("Navn på begivenhed").foregroundColor(.darkGray)
						}
						.padding(20)
						.background(Color.defaultGray).opacity(0.75)
						.foregroundColor(.textColor)
						.frame(height: 60)
						.cornerRadius(30.0)
						.padding(.bottom, 15)
						.onReceive(Just(title)) { _ in
							if (title.count > 20) {
								title = String(title.prefix(20))
							}
						}
					
					TextField("", text: $campName)
						.disableAutocorrection(true)
						.keyboardType(.default)
						.placeholder(when: campName.isEmpty) {
							Text("Camp navn").foregroundColor(.darkGray)
						}
						.padding(20)
						.background(Color.defaultGray).opacity(0.75)
						.foregroundColor(.textColor)
						.frame(height: 60)
						.cornerRadius(30.0)
						.padding(.bottom, 15)
						.onReceive(Just(campName)) { _ in
							if (campName.count > 20) {
								campName = String(campName.prefix(20))
							}
						}
					
					Rectangle()
						.padding(20)
						.background(Color.defaultGray).opacity(0.75)
						.foregroundColor(.clear)
						.frame(height: 60)
						.cornerRadius(30.0)
						.overlay {
							HStack {
								DatePicker("Vælg dag", selection: $currentDate, in: thirtyDays, displayedComponents: [.date, .hourAndMinute])
							}
							.padding(20)
						}
						.padding(.bottom, 15)
					
					TextField("", text: $location)
						.disableAutocorrection(true)
						.keyboardType(.default)
						.placeholder(when: location.isEmpty) {
							Text("Lokation").foregroundColor(.darkGray)
						}
						.padding(20)
						.background(Color.defaultGray).opacity(0.75)
						.foregroundColor(.textColor)
						.frame(height: 60)
						.cornerRadius(30.0)
						.padding(.bottom, 15)
						.onReceive(Just(location)) { _ in
							if (location.count > 5) {
								location = String(location.prefix(5))
							}
						}
					
					ZStack(alignment: .topTrailing) {
						if #available(iOS 16.0, *) {
							TextEditor(text: $description)
								.scrollContentBackground(.hidden)
								.foregroundColor(.textColor)
								.frame(minHeight: 200)
								.placeholder(when: description.isEmpty, alignment: .topLeading) {
									Text("Beskrivelse").foregroundColor(.darkGray)
								}
								.padding(20)
								.background(Color.defaultGray).opacity(0.75)
								.cornerRadius(30.0)
								.frame(maxHeight: 350)
								.padding(.bottom, 15)
								.onReceive(Just(description)) { _ in
									if (description.count > 1200) {
										description = String(description.prefix(1200))
									}
								}
						} else {
							TextEditor(text: $description)
								.foregroundColor(.textColor)
								.frame(minHeight: 200)
								.placeholder(when: description.isEmpty, alignment: .topLeading) {
									Text("Beskrivelse").foregroundColor(.darkGray)
								}
								.padding(20)
								.background(Color.defaultGray).opacity(0.75)
								.cornerRadius(30.0)
								.frame(maxHeight: 350)
								.padding(.bottom, 15)
								.onReceive(Just(description)) { _ in
									if (description.count > 1200) {
										description = String(description.prefix(1200))
									}
								}
						}
						LetterCounter(text: $description)
							.padding(.trailing, 20)
							.padding(.top, 10)
					}
					
					if (!title.isEmpty && !description.isEmpty && !campName.isEmpty && (isImageSelected || isEdit)) {
						Button(action: {
							//guard let email = emailState, let password = passwordState else {
							if (isEdit) {
								updateImage()
							}else {
								createEvent()
							}
							//loginShow = true
						}) {
							Text(isEdit ? "GEM" : "OPRET")
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
					
					Spacer()
				}
				.padding(20)
			}
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
				if (self.event != nil) {
					Button(action: {
						deleteEvent()
					}) {
						Image(systemName: "trash")
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
		.navigationBarHidden(true)
		.navigationBarBackButtonHidden(true)
		
		//}
	}
	
	
	func createEvent() {
		showSpinner {
			let dateFormatter = DateFormatter()
			dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.ms"
			
			let dayTimeFormatter = DateFormatter()
			dayTimeFormatter.locale = Locale(identifier: "da_DK")
			dayTimeFormatter.dateFormat = "E HH:mm"
			
			let timestamp = Int64(currentDate.timeIntervalSince1970 * 1000)
			
			let imageName: String = "\(title)\(Int64(Date().timeIntervalSince1970 * 1000))"
			
			let imgRef = storageRef.child("images/events/\(imageName)")
			
			guard let data: Data = image.jpegData(compressionQuality: 0.5) else { return }
			
			let md = StorageMetadata()
			md.contentType = "image/jpeg"
			
			let uploadTask = imgRef.putData(data, metadata: md) { (metadata, error) in
				guard let metadata = metadata else {
					// Uh-oh, an error occurred!
					return
				}
				// Metadata contains file metadata such as size, content-type.
				let size = metadata.size
				// You can also access to download URL after upload.
				imgRef.downloadURL { (url, error) in
					guard let downloadURL = url else {
						// Uh-oh, an error occurred!
						return
					}
					let token = String("\(downloadURL)").components(separatedBy: "token=")[1]
					let newEvent = dbRef.child("events").childByAutoId()
					
					let event: Event = Event(id: newEvent.key!, userId: Auth.auth().currentUser!.uid, title: title, campName: campName, description: description, image: imageName + "&" + token, dayTime: dayTimeFormatter.string(from: currentDate).replacingOccurrences(of: ".", with: "").capitalized, location: location, participant: 1, timestamp: timestamp, createdAt: dateFormatter.string(from: Date()), updatedAt: dateFormatter.string(from: Date()), facebookLink: "")
					
					newEvent.setValue(
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
						 "updatedAt": event.updatedAt]
					)
					
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
					
					hideSpinner {
						//GO BACK
						dismiss()
					}
				}
			}
			
		}
		
	}
	
	func updateImage() {
		showSpinner {
			
			if (isImageSelected) {
				print("Update med billede")
				let imageName: String = "\(title)\(Int64(Date().timeIntervalSince1970 * 1000))"
				let imgRef = storageRef.child("images/events/\(imageName)")
				
				guard let data: Data = image.jpegData(compressionQuality: 0.5) else { return }
				
				let md = StorageMetadata()
				md.contentType = "image/jpeg"
				
				let uploadTask = imgRef.putData(data, metadata: md) { (metadata, error) in
					guard let metadata = metadata else {
						// Uh-oh, an error occurred!
						return
					}
					// Metadata contains file metadata such as size, content-type.
					let size = metadata.size
					// You can also access to download URL after upload.
					imgRef.downloadURL { (url, error) in
						guard let downloadURL = url else {
							// Uh-oh, an error occurred!
							return
						}
						let token = String("\(downloadURL)").components(separatedBy: "token=")[1]
						
						//TODO: Delete old picture
						// Create a reference to the file to delete
						let oldImgRef = storageRef.child("images/events/\(event!.image.components(separatedBy: "&")[0])")
						
						// Delete the file
						oldImgRef.delete { error in
							if let error = error {
								// Uh-oh, an error occurred!
							} else {
								// File deleted successfully
								print("Billede slettet")
							}
						}
						
						updateEvent(imageName: imageName + "&" + token)
						
					}
				}
			}else {
				print("Update uden billede")
				updateEvent(imageName: "")
				
			}
		}
	}
	
	func updateEvent(imageName: String) {
		print("imageName = \(imageName)")
		let newEvent = dbRef.child("events").child(self.event!.id)
		
		let dateFormatter = DateFormatter()
		dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.ms"
		
		let timestamp = Int64(currentDate.timeIntervalSince1970 * 1000)
		
		let dayTimeFormatter = DateFormatter()
		dayTimeFormatter.locale = Locale(identifier: "da_DK")
		dayTimeFormatter.dateFormat = "E HH:mm"
		
		newEvent.setValue(
			["title": title,
			 "description": description,
			 "userId": self.event!.userId,
			 "campName": campName,
			 "id": self.event!.id,
			 "dayTime": dayTimeFormatter.string(from: currentDate).replacingOccurrences(of: ".", with: "").capitalized,
			 "image": imageName.isEmpty ? self.event!.image : imageName,
			 "facebookLink": self.event!.facebookLink, //TODO: Facebooklink update
			 "location": location,
			 "participant": self.event!.participant,
			 "timestamp": timestamp,
			 "createdAt": self.event!.createdAt,
			 "updatedAt": dateFormatter.string(from: Date())]
		)
		
		dbRef.child("users").child(Auth.auth().currentUser!.uid).child("events").child(self.event!.id).setValue(
			["title": title,
			 "description": description,
			 "userId": self.event!.userId,
			 "campName": campName,
			 "id": self.event!.id,
			 "dayTime": dayTimeFormatter.string(from: currentDate).replacingOccurrences(of: ".", with: "").capitalized,
			 "image": imageName.isEmpty ? self.event!.image : imageName,
			 "facebookLink": self.event!.facebookLink, //TODO: Facebooklink update
			 "location": location,
			 "participant": self.event!.participant,
			 "timestamp": timestamp,
			 "createdAt": self.event!.createdAt,
			 "updatedAt": dateFormatter.string(from: Date())])
		
		hideSpinner {
			//GO BACK
			dismiss()
		}
	}
	
	func deleteEvent() {
		guard let event = self.event else { return }
		
		showSpinner {
			// Delete event data from Realtime Database
			let eventRef = dbRef.child("events").child(event.id)
			eventRef.removeValue()
			
			// Delete event image from Storage
			let storageRef = storageRef.child("images/events").child(event.image.components(separatedBy: "&")[0])
			storageRef.delete { error in
				if let error = error {
					print("Error deleting event image: \(error.localizedDescription)")
				} else {
					print("Event image deleted successfully")
				}
			}
			
			// Dismiss the current view controller
			hideSpinner {
				dismiss()
			}
		}
		
	}
	
	
}

struct LetterCounter: View {
	@Binding var text: String
	var counter: Int = 0
	
	init(text: Binding<String>) {
		self._text = text
		self.counter = self._text.wrappedValue.count
	}
	
	var body: some View {
		Text("\(counter)/1200")
	}
}

struct CreateEventView_Previews: PreviewProvider {
	static var previews: some View {
		CreateEventView(event: nil)
	}
}
