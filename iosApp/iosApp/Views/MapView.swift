//
//  MapView.swift
//  iosApp
//
//  Created by Jackie Jensen on 22/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import PDFKit

struct MapView: View {
	let pdfDoc: PDFDocument
	
	@State var isExpanded = false
	@State var subviewHeight : CGFloat = 500
	
	init() {
		let url = Bundle.main.url(forResource: "rf_map_2022", withExtension: "pdf")!
		pdfDoc = PDFDocument(url: url)!
	}
	
	var body: some View {
		ZStack(alignment: .top) {
			PDFKitView(showing: pdfDoc)
				.ignoresSafeArea()
			ZStack {
				Rectangle()
					.fill(Color.black)
					.frame(maxWidth: .infinity, maxHeight: isExpanded ? subviewHeight : 50)
					.cornerRadius(10)
					.overlay(
						VStack(alignment: .center) {
							HStack {
								Text("Map")
									.font(.title)
									.foregroundColor(.white)
									.padding(.leading, 10)
								Spacer()
								Image(systemName: isExpanded ? "chevron.up" : "chevron.down")
									.foregroundColor(.orange)
									.padding(.trailing, 10)
							}
							if (isExpanded) {
								MapInfoExpand()
							}
						}
					)
					.overlay(
						RoundedRectangle(cornerRadius: 10)
							.stroke(Color.orange, lineWidth: 1)
					)
					.padding(.top, 20)
					.onTapGesture {
						//Expand
						withAnimation(.easeIn(duration: 1.0)) {
							isExpanded.toggle()
						}
					}
				
			}
			.padding(.horizontal, 15)
			
		}
		.onAppear {
			AppState.shared.swipeEnabled = false
		}
		.onDisappear {
			AppState.shared.swipeEnabled = true
		}
	}
	
	struct MapDetails: View {
		var body: some View {
			VStack {
				HStack(spacing: 8) {
					HexFigur(color: Color(hex: 0xFF005D94, opacity: 1), borderColor: .white, text: "Gate")
						.frame(maxWidth: .infinity, maxHeight: 80)
						.layoutPriority(1)
					BoxShape(color: Color(hex: 0xFFA6D155, opacity: 1), borderColor: Color.orange, text: "Festival Site")
						.frame(maxWidth: .infinity, maxHeight: 80)
						.layoutPriority(1)
					
					BoxShape(color: Color(hex: 0xFF33B754, opacity: 1), borderColor: .clear, text: "Regular Camping")
						.frame(maxWidth: .infinity, maxHeight: 80)
						.layoutPriority(1)
				}
				.padding(.horizontal, 8)
				
				HStack(spacing: 8) {
					BoxShape(color: Color(hex: 0xFF1B8329, opacity: 1), borderColor: .clear, text: "Community Special Camp")
						.frame(maxWidth: .infinity, maxHeight: 80)
						.layoutPriority(1)
					
					BoxShape(color: Color(hex: 0xFFC3BB90, opacity: 1), borderColor: .white, text: "Closed area")
						.frame(maxWidth: .infinity, maxHeight: 80)
						.layoutPriority(1)
					
					BoxShape(color: Color(hex: 0xFF80A15A, opacity: 1), borderColor: .white, text: "Parking")
						.frame(maxWidth: .infinity, maxHeight: 80)
						.layoutPriority(1)
				}
				.padding(.horizontal, 8)
			}
		}
	}
	
	struct BoxShape: View {
		let color: Color
		let borderColor: Color
		let text: String
		
		var body: some View {
			let hex = RoundedRectangle(cornerRadius: 12)
			
			VStack(alignment: .center, spacing: 4) {
				ZStack {
					hex.fill(color)
						.frame(width: 40, height: 40)
						.overlay(hex.stroke(borderColor, lineWidth: 1))
						.padding(EdgeInsets(top: 0, leading: 0, bottom: 0, trailing: 0))
					
				}
				.shadow(radius: 0)
				
				Text(text)
					.font(.system(size: 12))
					.foregroundColor(Color.gray)
					.multilineTextAlignment(.center)
					.lineLimit(2)
			}
			.frame(height: 80)
		}
	}
	
	struct HexFigur: View {
		var color: Color
		let borderColor: Color
		let text: String
		
		var body: some View {
			let hex = HexShape()
			
			VStack(alignment: .center, spacing: 4) {
				ZStack {
					hex.fill(color)
						.frame(width: 40, height: 40)
						.overlay(hex.stroke(borderColor, lineWidth: 1))
						.padding(EdgeInsets(top: 0, leading: 0, bottom: 0, trailing: 0))
					
				}
				.shadow(radius: 0)
				
				Text(text)
					.font(.system(size: 12))
					.foregroundColor(Color.gray)
					.multilineTextAlignment(.center)
					.lineLimit(2)
			}
			.frame(height: 80)
		}
	}

	struct HexShape: Shape {
		func path(in rect: CGRect) -> Path {
			var path = Path()
			let center = CGPoint(x: rect.midX, y: rect.midY)
			let radius = min(rect.size.height, rect.size.width) / 2
			let corners = corners(center: center, radius: radius)
			path.move(to: corners[0])
			corners[1...5].forEach() { point in
				path.addLine(to: point)
			}
			path.closeSubpath()
			return path
		}
		
		func corners(center: CGPoint, radius: CGFloat) -> [CGPoint] {
			var points: [CGPoint] = []
			for i in (0...5) {
				let angle = CGFloat.pi / 3 * CGFloat(i)
				let point = CGPoint(
					x: center.x + radius * cos(angle),
					y: center.y + radius * sin(angle)
				)
				points.append(point)
			}
			return points
		}
	}


	
	struct MapInfoExpand: View {
		var body: some View {
			ScrollView {
				LazyVStack(spacing: 8) {
					MapDetails()
					HStack {
						InfoIcon(text: "Agora", icon: "icon_agora")
							.frame(maxWidth: .infinity * 0.45, alignment: .leading)
						InfoIcon(text: "Gear rental", icon: "icon_gear_rental")
							.frame(maxWidth: .infinity, alignment: .leading)
					}
					HStack {
						InfoIcon(text: "Recycling station", icon: "icon_recycling_station")
							.frame(maxWidth: .infinity * 0.45, alignment: .leading)
						InfoIcon(text: "Supermarket", icon: "icon_supermarket")
							.frame(maxWidth: .infinity, alignment: .leading)
					}
					HStack {
						InfoIcon(text: "Refund station", icon: "icon_refund_station")
							.frame(maxWidth: .infinity * 0.45, alignment: .leading)
						InfoIcon(text: "Merchandise", icon: "icon_merchandise")
							.frame(maxWidth: .infinity, alignment: .leading)
					}
					HStack {
						InfoIcon(text: "Disabled toilets", icon: "icon_disabled_toilets")
							.frame(maxWidth: .infinity * 0.45, alignment: .leading)
						InfoIcon(text: "Lost & found", icon: "icon_lost_and_found")
							.frame(maxWidth: .infinity, alignment: .leading)
					}
					HStack {
						InfoIcon(text: "Showers", icon: "icon_showers")
							.frame(maxWidth: .infinity * 0.45, alignment: .leading)
						InfoIcon(text: "Information", icon: "icon_information")
							.frame(maxWidth: .infinity, alignment: .leading)
					}
					HStack {
						InfoIcon(text: "First aid", icon: "icon_first_aid")
							.frame(maxWidth: .infinity * 0.45, alignment: .leading)
						InfoIcon(text: "Laundromat", icon: "icon_laundromat")
							.frame(maxWidth: .infinity, alignment: .leading)
					}
					HStack {
						InfoIcon(text: "Pharmacy", icon: "icon_pharmacy")
							.frame(maxWidth: .infinity * 0.45, alignment: .leading)
						InfoIcon(text: "Service", icon: "icon_service")
							.frame(maxWidth: .infinity, alignment: .leading)
					}
					InfoIcon(text: "Chip wristband support", icon: "icon_chip_wristband_support")
						.frame(maxWidth: .infinity, alignment: .leading)
					VStack {
						InfoIconLong(text: "Parking (Regular, bicycles, disabled)", iconList: ["icon_parking_regular", "icon_parking_bicycles", "icon_parking_disabled"])
							.frame(maxWidth: .infinity, alignment: .leading)
						InfoIconLong(text: "Train/Shuttle Bus/Taxi", iconList: ["icon_bus", "icon_train", "icon_taxi"])
							.frame(maxWidth: .infinity, alignment: .leading)
					}
				}
				.padding(8)
			}
		}
	}
	
	struct InfoIcon: View {
		let text: String
		let icon: String
		
		var body: some View {
			HStack(spacing: 4) {
				Image(icon)
					.resizable()
					.frame(width: 32, height: 32)
				Text(text)
					.font(.body)
					.foregroundColor(Color.gray)
					.lineLimit(2)
					.padding(5)
			}
			.padding(.horizontal, 8)
			.cornerRadius(8)
			.shadow(color: Color.gray.opacity(0.4), radius: 2, x: 1, y: 1)
			.frame(height: 54)
		}
	}
	
	struct InfoIconLong: View {
		let text: String
		let iconList: [String]
		
		var body: some View {
			HStack(spacing: 4) {
				ForEach(iconList, id: \.self) { icon in
					Image(icon)
						.resizable()
						.frame(width: 32, height: 32)
				}
				Text(text)
					.font(.body)
					.foregroundColor(Color.gray)
					.lineLimit(2)
					.padding(5)
				Spacer()
			}
			.padding(.vertical, 4)
			.padding(.horizontal, 8)
			.cornerRadius(8)
			.shadow(color: Color.gray.opacity(0.4), radius: 2, x: 1, y: 1)
		}
	}
}

struct PDFKitView: UIViewRepresentable {
	
	let pdfDocument: PDFDocument
	
	init(showing pdfDoc: PDFDocument) {
		self.pdfDocument = pdfDoc
	}
	
	func makeUIView(context: Context) -> PDFView {
		let pdfView = PDFView()
		pdfView.document = pdfDocument
		pdfView.backgroundColor = UIColor(Color.backgroundColor)
		pdfView.autoScales = true
		pdfView.minScaleFactor = 1.5
		pdfView.maxScaleFactor = pdfView.scaleFactorForSizeToFit
		pdfView.displaysPageBreaks = true
		pdfView.displayDirection = .horizontal
		pdfView.displayMode = .singlePageContinuous
		pdfView.interpolationQuality = .high
		pdfView.isUserInteractionEnabled = true
		pdfView.enableDataDetectors = false
		pdfView.usePageViewController(true)
		return pdfView
	}
	
	func updateUIView(_ pdfView: PDFView, context: Context) {
		pdfView.document = pdfDocument
	}
	
}

struct MapView_Previews: PreviewProvider {
	static var previews: some View {
		MapView()
	}
}
