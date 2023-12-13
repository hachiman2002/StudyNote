plugins nivel proyecto: id 'com.google.gms.google-services' version '4.4.0' apply false

plugins nivel app: id 'com.google.gms.google-services'

depencias utilizada nivel app: //firebase
    implementation platform('com.google.firebase:firebase-bom:32.6.0')
    implementation 'com.google.firebase:firebase-analytics'
    implementation 'com.firebaseui:firebase-ui-auth:7.2.0'
    implementation("com.google.firebase:firebase-firestore")

     //onsesignal
    implementation 'com.onesignal:OneSignal:[5.0.0, 5.99.99]'
