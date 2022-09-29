//
//  WebViewContent.swift
//  registration
//
//  Created by Kanatbek Torogeldiev on 20/9/22.
//

import SwiftUI
import design

struct WebViewContent: View {
    
    var urlLink: URL
    @Binding var show: Bool
    @Binding var isShare: Bool
    
    var body: some View {
        NavigationView {
            WebView(url: urlLink)
                .toolbar {
                    ToolbarItem(placement: .navigationBarLeading) {
                        Button {
                            self.show.toggle()
                        } label: {
                            AppImages.getImage(.close)
                                .foregroundColor(AppColors.getColor(.secondarySystemBlue))
                        }
                    }
                    ToolbarItem(placement: .navigationBarTrailing) {
                        Button {
                            self.isShare.toggle()
                        } label: {
                            AppImages.getImage(.upload)
                                .foregroundColor(AppColors.getColor(.secondarySystemBlue))
                        }
                        .background(SharingViewController(isPresenting: $isShare) {
                            let av = UIActivityViewController(activityItems: [urlLink], applicationActivities: nil)
                            av.completionWithItemsHandler = { _, _, _, _ in
                                isShare = false
                            }
                            return av
                        })
                }
            }
        }
    }
}

//struct WebViewContent_Previews: PreviewProvider {
//    static var previews: some View {
//        WebView()
//    }
//}
