# mb_foursquare_api_sample
This project is a sample of the Foursquare API use in order to show places inside a particular range and for a given search term according to the user's current position. 

The app checks if the user's position is available or asks him to turn on the GPS, otherwise. As soon as he enters a range in meters (max. 100.000) and he is successfully located, he can press the "Go to the map" button to start the search. By default, the app looks for the gyms inside the given range but the user has the freedom to enter any different search term.

After pressing the "Go to the map" button, the next screen shows a map centered on the user's current position, the search range as a circle around it and and any number of markers (whose icon is the Foursquare logo) indicating where the searched places are located. When a marker is pressed, the name of the place as well as the distance in meters from the user's position to it are shown in a bubble.

If no results are found using the Foursquare API, an error message is displayed to the user. He can go back and change the range or the search term, everytime he wants, to start another search.

> Take into account that a stable connection to the Internet is needed in order to be able to use the Foursquare API.

### References
* [Foursquare for Developers] - API Endpoints (Visited on September 2016).
* [Java Code Geeks] - Android Foursquare API Example (Visited on September 2016).


[//]: # (These are reference links used in the body of this note)
   [Foursquare for Developers]: <https://developer.foursquare.com/docs/>
   [Java Code Geeks]: <https://examples.javacodegeeks.com/android/android-foursquare-api-example/>
