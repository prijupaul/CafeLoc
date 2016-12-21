Readme:

These are the different modules in the this project.

- UI module. At this project does not require much UI, I have just used activities and have not used fragments. 
- UI consists of Recycler view and card views.
- Integrated with Maps and click on maps icon show a marker on the location
- Shows the cafe ratings as well.
- Supports only portrait mode due to time contraint. 
- Networking module. Used retrofit and Gson to parse the response
- Location module. Have used both Google location api and also Googles play store service location api. This was used because, 
  Android location api is not accurate but is fast, but Google play store services location api is accurate but not fast.

Scenarios tested:

1. Positive testing. Launch the app
2. Check whether location is updated every set time and able to get latest ui. During this, dont block the UI but make the change
   in background

