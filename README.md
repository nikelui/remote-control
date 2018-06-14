# Android Remote Control

## Scenario

Our customer has a video screen at an exhibition that can be remotely controlled on a local network. In front of the video screen is an Android device that visitors at the exhibition use to control whats playing.
The customer needs an Android application that can run on a phone or tablet and control the machine. They are in a fix and need the app as soon as possible.

## Specifications

Write a simple Android application, running on 4.1 or later. The exhibition visitor should be able to activate three different video sequences through the app.

For each video sequence an administrator using the app should be able to customize what is being sent to to the video screen device.
The protocol is based on sequences of UDP packages with customizable text payloads, i.e. commands, and customizable time delay between each message.

The administrator should also be able to change the target host and port. 

The message format should be a plain text string terminated with \n .

Here is an example for what might be sent if the exhibition visitor initiates a video sequence.

    start video1
    a 500 ms pause
    stop video1
    start video2
    a 1200 ms pause
    start fade
    a 200 ms pause￼
    stop fade
    stop video2

## Optional Requirements

* Broadcasted UDP messages option. 
* A unit test or two.

## Delivery

A complete Android Studio project pushed to this repository.

## Time

The app does not have to be perfect nor production ready. Treat it as a first iteration.

With an already set up development environment, the assignment should not take more than 4 hours. Note that this is just an approximation.

## Hints

You can verify the sequence of messages by starting netcat

    nc -kluvw 0 10002
￼￼￼