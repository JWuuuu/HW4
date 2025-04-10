# HW4

This repository contains the implementation of the Staff Module for CSE 360 Homework 4. The module allows staff members to manage questions, view activity logs, and interact with the system. It includes features such as adding, deleting, and updating questions, logging user activities, and testing functionalities using JUnit.

Features Implemented
Staff Role Functionalities

Staff can manage questions (add, delete, update).

Staff can view activity logs of user actions.

Staff can log activities when performing actions on the system.

Staff can change roles (admin, user, etc.).

Activity Logging

Activities such as managing questions are logged and stored in the database.

The activity log can be viewed by staff.

Testing

JUnit tests are implemented for validating the functionality of question management and activity logging.

The tests check if questions are correctly added, deleted, and updated, and if the activity logs are stored and retrieved properly.

Installation
To get started, clone the repository:
git clone https://github.com/JWuuuu/HW4.git
You will need to set up the H2 database and JavaFX to run this project. Instructions for setting up the development environment are as follows:

Install Java 8 or higher: Make sure that Java is installed on your machine.

Download and Set Up JavaFX: You will need JavaFX SDK 11 or higher. Follow the installation guide from the official JavaFX website.

Database Setup: This project uses an H2 database, which is configured to run locally. Ensure that the DatabaseHelper class is set up properly to connect to the database.

Usage
Running the Application:

To run the application, execute the StartCSE360.java class from your IDE or command line.

The application will prompt you to log in, and depending on your role, you will be redirected to either the staff or user interface.

Testing:

To run the JUnit tests, right-click on the StaffFunctionsTest.java file and run the tests from your IDE.

The tests verify that the question management system and activity logging are working as expected.

Screencast
Here is the link to the screencast explaining the implementation:

Screencast Link
Passcode: DT6fX=Zy

The screencast covers:

The implementation of the staff role user stories and how they work.

The implementation of JUnit tests and how they work.

The results of running the JUnit tests and the output produced using Javadoc.

The code in the screencast is readable, and the explanations are clear and audible.

Repository Links
GitHub Repository: HW4 GitHub Repository

Screencast:[ HW4 Screencast](https://asu.zoom.us/rec/share/BBgLj-rncmQbq4QrBbyEhqll-9kwGNIN7fJadiXVSQonTOCvVE3oif2t2cIyL3Yq.KIiChFKeHIMl9yC6 
Passcode: DT6fX=Zy)
