# Weight-Tracking App

This repository contains the complete code for the Weight-Tracking App, developed as part of the CS 360 Mobile App Development course. The app was designed to help users track their daily weight, set and reach weight goals, and monitor progress over time. This project demonstrates user-centered design and app development best practices, from initial planning and UI design to coding and testing for a functional mobile application.

## Project Summary

**Requirements and Goals:**  
The Weight-Tracking App was created to address user needs around tracking weight-related goals. The app allows users to log their weight daily, set a specific weight target, and receive notifications upon reaching their goal. This helps users stay motivated and engaged in their fitness or health journey by simplifying weight management and progress tracking.

**Key User Needs Addressed:**
- Easy weight entry and display of historical weight data.
- Visual tracking of weight trends to monitor progress.
- Motivation through goal setting and achievement notifications.

## UI Design: Screens and Features

**User Interface Design:**  
The UI was designed to be intuitive, with a clean and organized layout that ensures ease of use. Key screens include:
- **Login Screen:** Allows users to sign in or create a new account.
- **Goal Weight Screen:** Enables users to set a target weight.
- **Daily Weight Entry Screen:** Simplifies daily weight logging.
- **Progress Tracking Screen:** Displays weight history in a grid format, allowing users to view trends over time.

**Design Philosophy:**  
Each screen and feature was crafted with the user experience in mind. The UI design follows Android design guidelines, focusing on a minimalistic and accessible interface. The clear layout and straightforward navigation keep users focused on their primary goal—tracking and achieving weight targets. These designs are successful as they minimize complexity, allowing users to focus on their weight-tracking objectives without unnecessary distractions.

## Approach to Coding

**Coding Techniques and Strategies:**  
I structured the app code using modular design principles, ensuring a clear separation between UI components and backend logic. The app leverages the Android SensorManager to prompt SMS notifications, SQLite for data persistence, and efficient handling of user interactions. To enhance readability and maintainability, I used descriptive naming conventions and inline comments throughout the code.

**Future Application:**  
These techniques will be valuable in future projects by enabling clear, maintainable, and scalable code structures, especially in collaborative environments. The modular design approach also provides flexibility for adding features and adapting the app to evolving user requirements.

## Testing Process

**Testing Strategy:**  
The app was thoroughly tested using the Android Emulator to ensure functionality across different scenarios. I implemented breakpoints to monitor data flow and confirmed that user interactions with the database, SMS permissions, and navigation worked as expected. 

**Importance of Testing:**  
Testing was crucial for validating that each feature met user needs and functioned as intended. This process revealed minor logic adjustments needed to improve data handling and user feedback, resulting in a smoother user experience.

## Reflections on the Development Process

**Challenges and Innovations:**  
Throughout the development process, one challenge was ensuring efficient and reliable data handling with the SQLite database. To overcome this, I implemented additional logic checks and user feedback mechanisms to enhance error handling. Another challenge was dynamically enabling/disabling the SMS notification feature based on user permissions, which required a tailored approach to ensure app continuity without notifications.

**Demonstrated Skills and Experience:**  
The Progress Tracking Screen, which displays the user’s historical weight data in an organized, user-friendly grid, best showcases my skills in combining UI design and database integration. This component allowed me to demonstrate knowledge in both front-end and back-end development, providing a clear, accessible view of weight trends.
