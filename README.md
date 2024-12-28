# TravelAssistant
This app is designed for a user base of people who are looking to travel internationally.
There are many things to note before visiting another country and TravelAssitant will help travellers decide where to go.

TravelAssistant consists of three pages: the homepage, the destination page, and the information page

### The Homepage
This page welcomes users and asks the user for their residence. This residence can either be a country or a dependency of another country. For example, both the United States and Guam are viable/separate input for the app.
This input is collected as a String in a text field. This string is put through a normalizer which fixes any reasonable misspells as well as non-official names for country. This input is saved for the information page.

### The Destination Page
This page collects the user input for their potential destination. This destination can either be a country or a dependency of another country.
Users can provide this input by clicking on an interactive map. The x and y values of the "click" within the map is translated to latitude and longitude to find the destination that the user desires.
After clicking on the map, the app will pull up the information for the information page. This can take up to five seconds.

### The Information Page
This page provides the user with the information about their destination. Firstly, it will give a travel advisory message about the destination. After this, the app will ask the user how much money they plan to bring in their residence's currency. As the user gives this information, TravelAssitant will calculate a converted amount of money in the destination's currency. If the user changes the input, so will the output change.
