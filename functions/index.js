// const functions = require('firebase-functions');
// const admin = require('firebase-admin');
// admin.initializeApp(functions.config().firebase);

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });


// exports.sendNotification = functions.database.ref('/requests/{blood_group}/{phone_number}')
//     .onWrite(event => {

//         console.log("Inside cloud function");

//         // !event.data.val() is a deleted event
//         // event.data.previous.val() is a modified event
//         if (!event.data.val() || event.data.previous.val()) {
//             console.log("not a new write event");
//             return;
//         }

//         const newRequest = event.data.val();

//         console.log(event.params.blood_group + " " + event.params.phone_number);

//         console.log(newRequest);



//     });

// function sendNotification(){

// }


//import firebase functions modules
const functions = require('firebase-functions');
//import admin module
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


// Listens for new messages added to messages/:pushId
exports.sendNotification = functions.database.ref('/requests/{blood_group}/{phone_number}').onWrite(event => {

    console.log('Push notification event triggered');


    var blood_group = event.params.blood_group;
    var requester_phone = event.params.phone_number;


    console.log("Blood Group: " + blood_group);
    console.log("Phone number " + requester_phone);




    // !event.data.val() is a deleted event
    //         // event.data.previous.val() is a modified event
    if (!event.data.val() || event.data.previous.val()) {
        if (!event.data.val()) {
            console.log("Delete Event");
        }
        if (event.data.previous.val()) {
            console.log("Modify Event:(Prev value): " + event.data.previous.val());
            console.log("New value: " + event.data.val);
        }
        console.log("not a new write event");
        return;
    }

    //  Grab the current value of what was written to the Realtime Database.
    var newObject = event.data.val();


    var groupRef = '/user_tokens' ;
    // var query  =  admin.database().ref(groupRef).orderByKey();
    // query.once("value")
    //     .then(function(snapshot) {


    //         if(snapshot.hasChild(blood_group)){
                const donorRef = groupRef + '/' + blood_group;
                const donorQuery = admin.database().ref(donorRef).orderByKey();
                donorQuery.once('value') 
                    .then(function(donorsSnapshot) {
                        donorsSnapshot.forEach(function(singleDonorSnapshot) {
                            console.log("Token for " + singleDonorSnapshot.key + " Token is : " + singleDonorSnapshot.val());
                            const tokens = singleDonorSnapshot.val();

                            const payload = {
                                notification: {
                                    title:"New request for " + blood_group + " blood!",
                                    body: "Request from " + requester_phone,
                                    sound: "default",
                                    // click_action: "io.github.utshaw.blooddonor_v3.FAQActivity"
                                },
                            };


                            const options = {
                                priority: "high",
                                timeToLive: 60 * 60 * 24
                            };
                            var go = admin.messaging().sendToDevice(tokens, payload);
                            // var go = admin.messaging().sendToTopic("pushNotifications", payload, options);
                        });
                    });
                    return; 
                

    //             }else{
    //                 console.log("No token for " + blood_group);
    //             }
    //         }
    //     });

    // });

















                // const bloodGroupQuery = admin.database().ref('/user_tokens/' + blood_group + '/' + childNumber).once('value');
                // bloodGroupQuery.once("value")
                //     .then(function())
                // const idQuery = admin.database().ref('/user_tokens/' + blood_group + '/' + childNumber).once('value');
                // idQuery.once("value")
                //     .then(function(idSnapshot) {
                //         if(idSnapshot.exists()){
                //             console.log("NULL for " + childNumber);
                //         }else{
                //             var childdata = childSnapshot.val();
                //             console.log(childdata.activeDonor);
                //             if(childdata.activeDonor == 'true'){
                //                 console.log(childNumber + " is active");
                //             }else{
                //                 console.log(childNumber + " is inactive");
                //             }
                //         }

                //     });



                

                
            // });
        // });

    // if(user_number.activeDonor === 'active'){
    //     console.log("Active: "  + user_number.mUsername);
    // }else{
    //     console.log(user_number.mUsername);
    // }

    // console.log(valueObject);



    /*
  // Create a notification
    const payload = {
        notification: {
            title:valueObject.name,
            body: valueObject.text || valueObject.photoUrl,
            sound: "default"
        },
    };

  //Create an options object that contains the time to live for the notification and the priority
    const options = {
        priority: "high",
        timeToLive: 60 * 60 * 24
    };


    return admin.messaging().sendToTopic("pushNotifications", payload, options);

    */
});