//
//  ImagePicker.swift
//  iosApp
//
//  Created by Jackie Jensen on 31/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct ImagePicker: UIViewControllerRepresentable {
	@Environment(\.presentationMode) private var presentationMode
	var sourceType: UIImagePickerController.SourceType = .photoLibrary
	@Binding var selectedImage: UIImage
	@Binding var isSelected: Bool

	func makeUIViewController(context: UIViewControllerRepresentableContext<ImagePicker>) -> UIImagePickerController {

		let imagePicker = UIImagePickerController()
		imagePicker.allowsEditing = false
		imagePicker.sourceType = sourceType
		imagePicker.delegate = context.coordinator

		return imagePicker
	}

	func updateUIViewController(_ uiViewController: UIImagePickerController, context: UIViewControllerRepresentableContext<ImagePicker>) {

	}

	func makeCoordinator() -> Coordinator {
		Coordinator(self)
	}

	final class Coordinator: NSObject, UIImagePickerControllerDelegate, UINavigationControllerDelegate {

		var parent: ImagePicker

		init(_ parent: ImagePicker) {
			self.parent = parent
		}

		func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {

			if let image = info[UIImagePickerController.InfoKey.originalImage] as? UIImage {
				parent.selectedImage = image
				parent.isSelected = true
			}else {
				parent.isSelected = false
			}
			
			parent.presentationMode.wrappedValue.dismiss()
		}

	}
}
