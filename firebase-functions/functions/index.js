const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp();

// Take the text parameter passed to this HTTP endpoint and insert it into the
// Realtime Database under the path /messages/:pushId/original
// exports.addMessage = functions.https.onRequest((req, res) => {
//   // Grab the text parameter.
//   const original = req.query.text;
//   // Push the new message into the Realtime Database using the Firebase Admin SDK.
//   return admin.database().ref('/messages').push({original: original}).then((snapshot) => {
//     // Redirect with 303 SEE OTHER to the URL of the pushed object in the Firebase console.
//     return res.redirect(303, snapshot.ref.toString());
//   });
// });

exports.addShop = functions.https.onCall((data, context) => {
    const uid = data.uid;
    const name = data.name;
    const address = data.address;
    const lat = data.lat;
    const lng = data.lng;
    const open_hour = data.open_hour;
    const close_hour = data.close_hour;
    const sid = (new Date()).getTime().toString();
    console.log('uid ' + uid);
    console.log('name ' + name);
    console.log('address ' + address);
    console.log('loc ' + lat + ' ' + lng);
    console.log('open_hour ' + open_hour);
    console.log('close_hour ' + close_hour);
    console.log('sid ' + sid);
    
    return admin.database().ref('/shops').push({uid: uid, name: name, address: address, loc: {lat: lat, lng: lng}, open_hour: open_hour, close_hour: close_hour, sid: sid}).then(() => {
        return {sid: sid};
    });
});
