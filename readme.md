you are asked to create an Android project as described below and send us the source code for assessment.



Requirements

The project should be buildable without errors. The app should run on an Android device without crashing using Kotlin.



Tech Stack

Kotlin
MVVM with Clean Architecture
Hilt / Dagger2 / Koin
Retrofit / Ktor
Coroutine



Specification

On the initial screen, fetch the data of 5 cities asynchronously merge the data and display into list. [location.name, temp_f, wind_kph, condition.text ]
Again, fetch the data of 5 cities synchronously and update the existing list.
Use the above tech stack to build the application





Web Service

URL https://api.weatherapi.com/v1/current.jsonRequest method GET



Parameters



key=”520916eb3f46442ca1c12926221402”  , q=”any city name”




Example



https://api.weatherapi.com/v1/current.json?key=520916eb3f46442ca1c12926221402&q=visakhapatnam




Response JSON



{

"location": {

"name": "Visakhapatnam",

"region": "Andhra Pradesh",

"country": "India",

"localtime": "2022-03-25 19:46"

},

"current": {

"last_updated": "2022-03-25 18:30",

"temp_c": 29.1,

"temp_f": 84.4,

"is_day": 0,

"condition": {

     "text": "Clear",

     "icon": "//cdn.weatherapi.com/weather/64x64/night/113.png",

     "code": 1000

},

"wind_mph": 13.6,

"wind_kph": 22.0,

"humidity": 79,

"cloud": 6,

} }



Bonus Points
● Error handling

● Unit test

● Network connection check



has context menu
Compose


1) Call the api in parallel and wit for all and then update ui
2) sequentially call the API and update the UI sequentially.