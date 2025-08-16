# Spotify Wrapped Clone

## Team Members
- Bryce Lin
- Nathan Lin 
- Zixiang Lin
- Rishi Jay Manimaran
- Jacoby Melton
- Anshul

## Project Overview

The Spotify Wrapped Clone is an Android application that provides users with personalized music summaries, similar to Spotify's annual Wrapped feature. Users can view weekly, monthly, and yearly summaries, gain insights into their listening habits, and enjoy various interactive features such as song previews, recommendations, and seasonal themes.

## Table of Contents

1. [Project Roles and Team Responsibilities](#project-roles-and-team-responsibilities)
2. [Features](#features)
3. [Tech Stack](#tech-stack)
4. [Setup Instructions](#setup-instructions)
5. [API Usage and Authentication](#api-usage-and-authentication)

## Project Roles and Team Responsibilities

Our team follows clear task ownership to ensure smooth collaboration:

- **Scrum Master**: Nathan, Bryce
- **Product Owner**: Jacoby
- **Developers**: Nathan, Zixiang, Rishi, Jacoby, Bryce
- **Technical Support**: Anshul
- **Documentation**: Bryce

## Features

### Weekly, Monthly, and Yearly Summaries
Users can view personalized music summaries over weekly, monthly, and yearly time spans. This feature provides valuable insights into listening habits, including:
- Top artists
- Most played songs
- Favorite genres
- Listening trends and patterns

### Custom Time Span Control
Users can set specific time frames for Wrapped summaries, choosing how far back the app pulls data. This flexibility allows users to:
- Explore unique listening trends over different periods
- Create personalized summary experiences
- Align with individual music discovery journeys

### Settings Page with Dark Mode and Notifications
A comprehensive settings page allows users to customize their app experience:
- **Dark Mode**: Enhanced viewing experience in low light conditions
- **Notifications**: Stay updated on new Wrapped summaries and relevant alerts
- **Privacy Controls**: Manage data sharing preferences

### Social Sharing
Users can share their Wrapped summaries with friends through:
- Integration with popular social media platforms
- Showcase music tastes and preferences
- Compare summaries with friends
- Built-in social functionality similar to Spotify Wrapped

### Song Previews
Enhance the user experience with:
- Song clips directly within Wrapped summaries
- Ability to relive favorite tracks in-app
- Immersive and engaging summary experience

### Recommended Artists
AI-powered recommendations based on listening history:
- Discover new artists aligned with user tastes
- Personalized song suggestions
- Music discovery insights and analytics

### Export as Image
Users can export their Wrapped summaries as:
- High-quality shareable images
- Easy-to-save visual summaries
- Social media-ready formats

### Themed Visuals for Special Occasions
Seasonal themes and special occasion features:
- **Halloween**: Spooky-themed interface adaptations
- **Christmas**: Holiday-themed visual elements
- **Dynamic Theming**: Interface adapts automatically during special periods
- Enhanced user engagement through festive experiences

### Firebase Multi-Device Access
Secure cloud integration providing:
- Cross-device account synchronization
- Secure user authentication
- Consistent experience across all devices
- Reliable data backup and recovery

## Tech Stack

- **Android Development**: Java, XML, Android Studio
- **Version Control**: Git
- **Database & Authentication**: Firebase
- **API Integrations**: Spotify API, Google Sign-In API

## Setup Instructions

### Prerequisites
- Android Studio (latest version recommended)
- Git
- Firebase account
- Spotify Developer account

### Installation Steps

1. **Clone the Repository**
   ```bash
   git clone [repository-url]
   cd spotify-wrapped-clone
   ```

2. **Configure Firebase**
   - Create a new Firebase project
   - Add your Android app to the Firebase project
   - Download and add `google-services.json` to your app directory
   - Enable Authentication and Firestore Database

3. **Spotify API Setup**
   - Register your app at [Spotify Developer Dashboard](https://developer.spotify.com/)
   - Obtain Client ID and Client Secret
   - Configure redirect URIs for your app

4. **Android Studio Configuration**
   - Open the project in Android Studio
   - Sync project with Gradle files
   - Configure emulator or connect physical device
   - Build and run the application

## API Usage and Authentication

### Spotify API
This application integrates with the Spotify Web API to gather user music data:
- **Authentication**: OAuth 2.0 flow
- **Scopes Required**: `user-read-recently-played`, `user-top-read`, `user-read-playback-state`
- **Rate Limiting**: Implemented to respect API limits
- **Data Access**: User listening history, top tracks, and artist information

### Firebase Authentication
Firebase provides secure user management:
- **Google Sign-In**: Seamless authentication experience
- **User Sessions**: Persistent login across app sessions
- **Security**: Industry-standard security practices
- **Multi-device Support**: Synchronized user data across devices


