# WhatsApp Web App

This Android application is designed to provide a mobile-friendly interface for accessing WhatsApp Web. The app enables users to use WhatsApp Web seamlessly on their mobile devices with additional features like file downloads, permission management, and more.

## Features

- **Mobile-Friendly WhatsApp Web Interface**: Optimized for mobile use with an intuitive UI.
- **File Upload & Download**: Supports file uploads directly from the mobile device and handles blob URL downloads.
- **Geolocation Support**: Allows the app to access your location for WhatsApp Web features.
- **Permission Management**: Manages camera, audio, and storage permissions to ensure a smooth user experience.
- **Custom User-Agent**: Spoofs the user-agent to mimic a desktop browser for proper functionality.
- **Error Handling**: Handles various WebView errors gracefully and provides appropriate feedback to the user.
- **Activity Lifecycle Management**: WebView state is preserved and managed during activity lifecycle changes.
- **Download Triggering**: Downloads are managed using Android's DownloadManager for robust and background download handling.

## Installation

To build and run the application on your Android device:

1. **Clone the repository**:
    ```bash
    git clone https://github.com/samyak2403/whatsapp-web-app.git
    cd whatsapp-web-app
    ```

2. **Open the project in Android Studio**:
   - Import the project into Android Studio.
   - Ensure all dependencies are installed.

3. **Build the project**:
   - Sync the project with Gradle files.
   - Click on `Build > Make Project` to build the application.

4. **Run the project**:
   - Connect your Android device or use an emulator.
   - Click on `Run > Run 'app'` to install and launch the application on your device.

## Permissions

This app requires the following permissions:

- **Internet Access**: For accessing WhatsApp Web.
- **Storage Access**: For handling file downloads.
- **Camera Access**: For video calls and media sharing.
- **Microphone Access**: For voice recording and video calls.
- **Location Access**: For location sharing features.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request with any enhancements or bug fixes.

## Acknowledgments

- **Android WebView**: The core technology behind rendering web content in the app.
- **WhatsApp Web**: The web-based version of WhatsApp that this app interacts with.

---

Thank you for using the WhatsApp Web App!
