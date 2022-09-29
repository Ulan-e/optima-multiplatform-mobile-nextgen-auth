//
//  WebView.swift
//  registration
//
//  Created by Kanatbek Torogeldiev on 20/9/22.
//

import SwiftUI
import WebKit

struct WebView: UIViewRepresentable {
    let url: URL?
    
    func makeUIView(context: Context) -> WKWebView {
        let prefs = WKWebpagePreferences()
        prefs.allowsContentJavaScript = true
        let config = WKWebViewConfiguration()
        config.defaultWebpagePreferences = prefs
        return WKWebView(frame: .zero, configuration: config)
    }
    
    func updateUIView(_ uiView: UIViewType, context: Context) {
        guard let myURL = url else { return }
        
        let request = URLRequest(url: myURL)
        uiView.load(request)
    }
}
