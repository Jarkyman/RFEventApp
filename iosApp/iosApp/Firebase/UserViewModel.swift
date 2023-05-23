//
//  FirebaseUser.swift
//  iosApp
//
//  Created by Jackie Jensen on 23/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import FirebaseDatabase
import FirebaseAuth

class UserViewModel: ObservableObject {
	let dbRef = Database.database().reference()
	
	@Published private(set) var loggedInUser: shared.User? = nil
	
	init() {
		getUser()
	}
	
	func getUser() {
		guard let userID = Auth.auth().currentUser?.uid else {
				print("User is not authenticated")
				return
			}
		dbRef.child("users").child(userID).observe(.value) {snapshot in
			if let userData = snapshot.value as? [String: Any] {
				let id = userData["id"] as? String ?? ""
				let firstName = userData["firstName"] as? String ?? ""
				let lastName = userData["lastName"] as? String ?? ""
				let email = userData["email"] as? String ?? ""
				let campName = userData["campName"] as? String ?? ""
				let birthday = userData["birthday"] as? String ?? ""
				let createdAt = userData["createdAt"] as? String ?? ""
				let updatedAt = userData["updatedAt"] as? String ?? ""
				
				// create new user object
				let user = User(id: id, firstName: firstName, lastName: lastName, email: email, birthday: birthday, campName: campName, createdAt: createdAt, updatedAt: updatedAt)
				
				// use the user object as needed
				self.loggedInUser = user
				LoggedInUserKt.loggedInUser = user
				print("User is set" + user.firstName + " " + user.lastName)
			}
		}
	}
	
	func deleteUser(id: String, completion: @escaping (Error?) -> Void) {
		let oldUser = dbRef.child("users").child(id)
		
		oldUser.observeSingleEvent(of: .value, with: { snapshot in
			guard snapshot.exists() else {
				// Håndter fejl
				let error = NSError(domain: "UserNotFound", code: 0, userInfo: [NSLocalizedDescriptionKey: "Brugeren blev ikke fundet"])
				completion(error)
				return
			}
			
			let deletedData = snapshot.value as? [String: Any]
			
			let deletedUser = self.dbRef.child("users").child("Deleted\(id)")
			deletedUser.setValue(deletedData) { error, _ in
				if let error = error {
					// Håndter fejl
					print("FEJL SKER HER")
					completion(error)
					return
				}
				oldUser.removeValue { _, _ in
					// Håndter fejl
					completion(nil)
				}
			}
		})
	}

}
