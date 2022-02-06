# Offline Country Highlighter android.

## Click any country from map and the country gets highlighted.

### Project Description.

An android tool built with Kotlin language. Pick any country and the country gets highlighted. A toast shows with the Clicked country's Alpha-2 code.

---
## 2 steps
* Reverse geocoding the clicked position to know the clicked country.
* Highlighting the country.

### Reverse geocoding
I used [this great repo for reverse geocoding offline](https://github.com/AReallyGoodName/OfflineReverseGeocode), It is the best that I found. Google maps reverse geocoding often takes time and doesn't work offline.

### Highlighting the country.
* In order to Highlight the country, We need the coordinates of the Country. [Can be fround here.](https://geojson-maps.ash.ms/)
* The downloaded file has all the coordinates of the countries in the same geojson file. which in our case is not what we exactly need. So after downloading the file I sepereted each country into its own geojson file which is named after its Alpha-2 code but in lowercase. [Coordinates of the countries seperted and matched to its Alpha-2 code but in lowercase.](https://github.com/moumen7/Map_picker/tree/master/app/src/main/res/raw)
* Google maps Geojson utility takes the coordinates and draws over the map. [Google maps Geojson utility.](https://developers.google.com/maps/documentation/android-sdk/utility/geojson)

---

### Works Offline after the map gets cached.

---

## Short Demo.
<img src="https://user-images.githubusercontent.com/57041674/152696962-cf5e3eeb-50b4-431d-99f2-193448da56e9.gif" width="250" height="550"/>

---

## Important notes.
* Dominican republic's Alpha-2 code is 'do', Which isn't permitted to be stored as file. So the file's name changed to 'doo'
* [Another version which uses nomination api.](https://github.com/moumen7/MapPicker)
* The results are not always accurate, Since the coordinates downloaded used are mid res.
