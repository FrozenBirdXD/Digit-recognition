# Project Digit Recognition
I'm training a simple neural network to recognize handwritten digits. You can execute the program and draw your own digit. The network makes a prediction and tells you 
what digit you drew. I implemented a neural network with one hidden layer and a variable amount of input and 
output neurons. The network has an accuracy of 93.4% on the mnist test data, but the accuracy is lower with the GUI (will be fixed in the future).

## Features
- `GUI` that promts the user to draw a digits on a canvas
- `Data Loader` that loads and handles the mnist data
- `Network class` which creates a neural network
- `Matrix class` with matrix calculations
- `Graph class` that creates graphs for the training process of the network (work in progress)
- `Mnist` training data is included

## Technologies Used
- `JavaFX` + `FXML`
- `Java` language
- `Mnist` dataset
- `Maven` build automation tool
- `VSCode` editor

## Getting Started
### Prerequisites
- [Java](https://www.java.com/en/download/) 8 or higher
- [JavaFX](https://openjfx.io/)
- [Maven](https://maven.apache.org/download.cgi)
- [VSCode](https://code.visualstudio.com/Download) (or any other code editor)
- (Git version control)

### Installation
1. Clone the repository (or download the zip from github):
```
git clone https://github.com/FrozenBirdXD/digitRecognition.git 
```
2. Change the file location of `weights-biases.txt` in the `SimpleNetwork` class to the relative file location e.g.: `digitRecognition\\app\\src\\weights-biases.txt`
3. Navigate to the project directory with the `pom.xml` file:
```
e.g.: cd digitRecognition/app
```
4. Build the project and install the package files:
```
mvn install
```
5. Run the 'main' method of the class called App to start the program/GUI.

## Usage
To use the program run the 'main' method to start. 
<video src='https://user-images.githubusercontent.com/118717731/226986129-82beb471-878e-4b65-94d1-6e8c0289d05b.gif' width=180/>
[![Demo Doccou alpha](http://share.gifyoutube.com/KzB6Gb.gif)](https://www.youtube.com/watch?v=ek1j272iAmc)

## Future Plans
- fix accuracy problem with GUI
- implement a convolutional neural network (CNN)
- recognize whole number not just digits
- add help section with relevant equations for machine learning as lookup

## Great literature and references

The following list contains links to great articles and other helpful content I used for this project:
- [Neural networks and deep learning by Michael Nielsen](http://neuralnetworksanddeeplearning.com/index.html)
- [The MNIST Database by Yann LeCun, Corinna Cortes, Christopher J.C. Burges](http://yann.lecun.com/exdb/mnist/)
- [Deep learning by Ian Goodfellow and Yoshua Bengio and Aaron Courville](https://www.deeplearningbook.org/)
- [Performance of Matrix multiplication in Python, Java and C++ by Martin Thoma](https://martin-thoma.com/matrix-multiplication-python-java-cpp/)

This project is still work in progress but a "pre-release" will be created in the near future.
