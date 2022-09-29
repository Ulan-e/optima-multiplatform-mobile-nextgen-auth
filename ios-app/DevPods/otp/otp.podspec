#
# Be sure to run `pod lib lint otp.podspec' to ensure this is a
# valid spec before submitting.
#
# Any lines starting with a # are optional, but their use is encouraged
# To learn more about a Podspec see https://guides.cocoapods.org/syntax/podspec.html
#

Pod::Spec.new do |s|
  s.name             = 'otp'
  s.version          = '0.1.0'
  s.summary          = 'A short description of otp.'

# This description is used to generate tags and improve search results.
#   * Think: What does it do? Why did you write it? What is the focus?
#   * Try to keep it short, snappy and to the point.
#   * Write the description between the DESC delimiters below.
#   * Finally, don't worry about the indent, CocoaPods strips it!

  s.description      = <<-DESC
TODO: Add long description of the pod here.
                       DESC

  s.homepage         = 'https://github.com/Uzumaki/otp'
  # s.screenshots     = 'www.example.com/screenshots_1', 'www.example.com/screenshots_2'
  s.license          = { :type => 'MIT', :file => 'LICENSE' }
  s.author           = { 'Uzumaki' => '123kanatai@gmail.com' }
  s.source           = { :git => 'https://github.com/Uzumaki/otp.git', :tag => s.version.to_s }
  # s.social_media_url = 'https://twitter.com/<TWITTER_USERNAME>'

  s.ios.deployment_target = '14.1'

  # s.source_files = 'otp/Classes/**/*'
  s.source_files = 'otp/Features/**/*.{swift}'
  s.resources = "otp/Features/**/*.{xcassets,json,storyboard,xib,xcdatamodeld}"
  
  # s.resource_bundles = {
  #   'otp' => ['otp/Assets/*.png']
  # }

  # s.public_header_files = 'Pod/Classes/**/*.h'
  # s.frameworks = 'UIKit', 'MapKit'
  s.dependency 'biometric'
  s.dependency 'design'
  
end
