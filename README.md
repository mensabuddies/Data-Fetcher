# Data Fetcher

This code runs as a Cloud Function on Google Cloud Platform. It is triggered regularly and fetches the newest data of all canteens, cafeterias and meals from the Website of the Studentenwerk (unfortunately, there is no public API, so we just parse the HTML). The fetched data is then stored in Cloud Firestore, from where the iOS and Android app read it.
