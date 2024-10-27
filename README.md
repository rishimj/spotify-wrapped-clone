# Spotify Wrapped Clone

**Members**: Rishi Manimaran, Bryce Lin, Nathan Lin, Zixiang Lin, Jacoby Melton, Anshul

## Project Overview

The Spotify Wrapped Clone is an Android application that provides users with personalized music summaries, similar to Spotify’s Wrapped feature. Users can view weekly, monthly, and yearly summaries, gain insights into their listening habits, and enjoy various interactive features such as song previews, recommendations, and seasonal themes.

## Table of Contents

1. [Project Roles and Team Responsibilities](#project-roles-and-team-responsibilities)
2. [Expanded Features](#expanded-features)
3. [Tech Stack](#tech-stack)
4. [Setup Instructions](#setup-instructions)
5. [API Usage and Authentication](#api-usage-and-authentication)
6. [Contribution Guidelines](#contribution-guidelines)
7. [Known Issues and Future Enhancements](#known-issues-and-future-enhancements)

## Project Roles and Team Responsibilities

Our team follows clear task ownership to ensure smooth collaboration:

- **Scrum Master**: Nathan, Bryce
- **Product Owner**: Jacoby
- **Developers**: Rishi, Nathan, Zixiang, Jacoby, Bryce
- **Technical Support**: Anshul
- **Documentation**: Bryce

## Expanded Features

### Weekly, Monthly, and Yearly Summaries
Users can view personalized music summaries over weekly, monthly, and yearly time spans. This feature provides valuable insights into listening habits, such as top artists, songs, and genres. Customizable time spans allow users to tailor their experience and explore trends in their music preferences.

### Custom Time Span Control
Users can set specific time frames for Wrapped summaries, choosing how far back the app pulls data. This flexibility lets users explore unique listening trends over different periods, aligning with the overall personalized theme of the app.

### Settings Page with Dark Mode and Notifications
A settings page allows users to customize their app experience by enabling dark mode for easier viewing in low light and managing notifications to stay updated on new Wrapped summaries and other relevant alerts.

### Social Sharing
Users can share their Wrapped summaries with friends, integrating with popular social media platforms. The feature makes it possible for users to showcase their music tastes and compare summaries, aligning with Spotify Wrapped’s social functionality.

### Song Previews
Enhancing the user experience, the app provides song clips within Wrapped summaries, allowing users to relive favorite tracks directly in the app. This feature creates a more immersive, enjoyable experience.

### Recommended Artists
Based on listening history, the app recommends new artists and songs to help users discover music aligned with their tastes. This feature offers valuable insights into their preferences and aids in music discovery.

### Export as Image
Users can export their Wrapped summaries as shareable images, making it easy to save, view, and share Wrapped details with others who may not use the app.

### Themed Visuals for Special Occasions
The app introduces seasonal themes around specific holidays, like Halloween and Christmas. During these periods, the app’s interface adapts with unique visuals to celebrate the season, enhancing the user experience with a fun, themed Wrapped.

### Firebase for Multi-Device Access
With Firebase integration, users can securely log into their accounts across devices. This feature ensures that Wrapped summaries and preferences are always accessible, creating a consistent experience regardless of device.

## Tech Stack

- **Android Development**: Java, XML, Android Studio
- **Version Control**: Git
- **Database and User Authentication**: Firebase
- **API Integrations**: Spotify API for data access, GoogleSignInActivity for authentication

## Setup Instructions

1. **Clone the Repository**: Clone the project using Git to set up your local environment.
2. **Configure Firebase**: Set up Firebase in the project for user authentication and data management.
3. **Spotify API**: Register your app with Spotify to get access tokens for user data.
4. **Run on Android Studio**: Open the project in Android Studio and configure the emulator to test the app.

## API Usage and Authentication

- **Spotify API**: This application uses the Spotify API to gather user music data. Ensure that you register your application with Spotify to retrieve necessary access tokens for authenticated API calls.
- **Firebase Authentication**: Firebase provides user login and registration functionality. Follow setup instructions in Firebase to enable Google Sign-In and configure it within the app.

## Contribution Guidelines

We welcome contributions to improve the project! Here’s how to get involved:
1. Fork the repository and clone it locally.
2. Create a branch for your changes, following the naming convention `feature/description` or `bugfix/description`.
3. Submit a pull request (PR) with a detailed description of the changes made.

**Coding Standards**: Please follow Java coding standards and Android best practices for consistency.

**Code Review**: All pull requests require approval from at least one team member to ensure code quality.

## Known Issues and Future Enhancements

### Known Issues
- **Spotify API Rate Limits**: Currently, the app may exceed Spotify's rate limits if too many requests are made in a short period. We are working on optimizing API calls and exploring caching options.
- **Login Compatibility**: Some devices may encounter issues with Firebase authentication; further testing is ongoing.

### Future Enhancements
- **Additional Time Frames**: Expanding summaries to include daily or custom time frames.
- **Advanced Customization**: Allow users to personalize the visual appearance of their summaries further.
- **Duo Wrapped**: Introduce features for shared Wrapped experiences among friends.

---

This README provides a detailed overview, setup instructions, and guidance for contributing to the project, making it easier for new users and contributors to get started.
