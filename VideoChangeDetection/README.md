# Video Change Detection Program

###### Author: Eric Zhewen Li

###### Contact: eric@zhewenli.com

## Update (Feb 15, 2020)

In previous version, images were not converted to floating point after input as `uint8`.  As a result, subtraction of `uint8` images is clipping values that should be negative to zero. Depending on the order of subtraction, objects either darker or brighter than the background will not be detected.

To fix this, `im2double` is now used after calling `imread` function to read in images.

## Introduction

This project was originally written as Project 3 for [CMPEN-454](http://www.cse.psu.edu/~rtc12/CSE486/) of Fall 2019 at Penn State University, taught by [Associate Professor Robert Collins](http://www.cse.psu.edu/~rtc12/).

The major objective of this project is to implement the four basic motion detection functions for a given video, which are: simple background subtraction, simple frame differencing, adaptive background and persistent frame differencing. The basic steps of this project are to read in a set of frames (given datasets), run the MATLAB code to generate the results of those frame, then reconvert them to a video with four different panels that processed by different motion detection algorithms as our expectation. Through this process, we also set up different threshold of the pixel difference in order to generate significant frame difference with the avoidance of random noise. By comparing different results of the four algorithms, we gained a solid understanding of the multiple motion detection functions and their specific outcomes.

Refer to [project report](https://github.com/lizhewen/PSU_Course/blob/master/VideoChangeDetection/ProjectReport.pdf) file for a detailed usage, flowchart, and analysis of the program.

## How to Process Generated Results

To generate a video from all the frames generated from MATLAB, I used and recommended using `ffmpeeg` program. An easy and full installation can be done using `brew` package manager. A sample command to generate the video is:

```bash
ffmpeg -i ArenaA-f%04d.jpg -vcodec libx264 AShipDeck.mp4
```
