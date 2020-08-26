# PocketML
Build a dataset for training neural network models in your pocket.

## Installation
Clone this repository and import into **Android Studio**
```bash
git clone git@github.com:laurencepettitt/pocketml.git
```

## Design

The project uses a **Clean Architecture** approach to allow modularisation and testability through separation of concerns. Koin is used to provide dependency injection without reflection.

The app uses a **GraphQL API** to access and update the images in the dataset on the cloud. The Apollo GraphQL server is running as a **Google Cloud Function**. The source code for which can be found here [https://github.com/laurencepettitt/pocketml-backend.git](https://github.com/laurencepettitt/pocketml-backend.git).

**ViewModels** and **LiveData** are used to safely manage data throughout the lifecycle of the Fragments and keep the UI reactive to changes in the state.

Using the **Navigation Component** and **SafeArgs** allows navigating and passing data between Fragments. In addition to this, **Data Binding** brings efficient data flow in and out of Views.

### To do
 - Camera functionality
 - Backend support for training ML models
 - On device inference
