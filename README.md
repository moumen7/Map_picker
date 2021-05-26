# Offline Map picker.

## Click any country from map and the country gets highlighted.

### Project Description.

An android application built with Kotlin language and using a stylized Google maps. It allows the user to pick any country and the country gets highlighted or dehighlighted. A toast shows with the Clicked country's Alpha-2 code.

### Stylizing the map.
* [Map-styling Document.](https://developers.google.com/maps/documentation/android-sdk/styling)
* [Json file used for Styling](https://github.com/moumen7/Map_picker/blob/master/app/src/main/res/raw/mapstyle.json)

### Highlighting the country.
* In order to Highlight the country, We need the coordinates of the Country. [Coordinates of countries were downloaded from here.](https://geojson-maps.ash.ms/), Low Res but takes less space and time to process.
* The downloaded file has all the coordinates of the countries in the same geojson file. which in ourcase is not what we exactly need. So after downloading the file I sepereted each country into its own geojson file which is named after its Alpha-2 code but in lowercase. [Coordinates of the countries seperted and matched to its Alpha-2 code but in lowercase..](https://github.com/moumen7/Map_picker/tree/master/app/src/main/res/raw)
* Google maps Geojson utility takes the coordinates and draws over the map. [Google maps Geojson utility](https://developers.google.com/maps/documentation/android-sdk/utility/geojson)

### Works Offline after the map gets cached.
In order to make it work offline and take alot less time. The downloaded country coordinates are used to track the user's click. Instead of using google's reverse geocoding which needs Internet access.

## Short Demo
<img src="https://user-images.githubusercontent.com/57041674/119538478-6eaf0600-bd8b-11eb-9cc0-d17827de4c8d.gif" width="250" height="550"/>

## Important notes
* Dominican republic's Alpha-2 code is 'do', Which isn't permitted to be stored as file. So the file's name changed to 'doo'
* [Another version which uses nomination api.](https://github.com/moumen7/MapPicker)
* The results are not always accurate, Since the coordinates downloaded used are low res.
