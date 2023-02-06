# About Pathshala

Pathshala (School in Sanskrit) is a app which provides NYC Schools Information. 
The App also provides SAT Scores for the NYC Schools

## Installation

Use Android Studio to build App (Electric Eel Recommended)

## Features
1. Display list of Schools (Supports both landscape & portrait orientation)
2. Save schools in local database
3. Fetch schools incrementally based on offset & limit
4. Retrieve schools from local database if already present
5. Search schools based from local database
6. Display SAT scores for the selected school

## Testing 

1. Refer to both androidTest & unitTest folders for Test Files

## Framework components used

1. Kotlin Programming Language with MVVM design pattern
2. Dagger for Dependency Injection
3. Coroutines for background threads
4. Room Database for local storage
5. ViewModel for caching 
6. LiveData for making view lifecycle aware
7. Retrofit for REST API calls
8. Open source libraries
