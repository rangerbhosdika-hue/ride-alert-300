RIDE ALERT 300 — SOURCE PROJECT

Yeh Android source project hai, ready APK nahi.

Kaam:
- Uber Driver app (package: com.ubercab.driver) ke visible offer text ko Accessibility Service se read karta hai.
- Text mein ₹300 ya usse zyada ka rupee amount mile to Android notification deta hai.
- Ride ko accept/reject/match nahi karta.
- Koi data server par upload nahi karta.

Build:
1. Android Studio mein is folder ko Open karo.
2. Gradle sync hone do.
3. Android phone connect karke Run karo.
4. App kholo aur notification permission allow karo.
5. "Enable fare monitoring" dabao.
6. Android Settings > Accessibility > Downloaded apps/Installed apps > Uber ride fare alert > ON.
7. Uber Driver par wapas jao.

Important:
- Accessibility permission powerful hoti hai. Is project ka service package filter com.ubercab.driver hai aur text local device par hi process hota hai.
- Uber app kuch versions mein screen text Accessibility API ko expose na kare; us case mein alert trigger nahi hoga.
- App ko Android background restrictions se bachane ke liye battery settings mein unrestricted/background allowed rakhna pad sakta hai.
- ₹300 threshold abhi fixed hai.
- Uber ki policies aur local law follow karein. Uber account safety ki guarantee nahi di ja sakti.
- Test pehle kisi non-critical situation mein karein. Fare parser offer card par visible ₹ amount ko detect karta hai; false positive/negative possible hain.
