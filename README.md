# Node JS Android Chat Application Using Node Bluetooth

**Node JS on Android**

There are many ways to build mobile application besides using native programming language. One of the way to build it is by using Node.js. Node.js can be run using application named Termux and since Node.js uses Google’s V8 JavaScript engine (The V8 engine compiles JavaScript to a native code before executing it, a process known as Just-In-Time (JIT)) therefore it can run in Android smoothly. 

Making mobile application using Node.js also has several benefits since Node js currently is one of the fastest growing ecosystem with well over 110,000 free JavaScript modules. This abundant modules will expedite development process and make it more efficient. Also most of the time mobile app has to have browser based application for desktop access. Having one platform and one codebase for both the browser and mobile applications can make the development process far more efficient.

**Node Bluetooth**

By utilising Node.js it will open access to millions of library available in Node.js ecosystem. One of it is node-bluetooth (https://github.com/song940/node-bluetooth). Using node-bluetooth, the application can perform bluetooth access functionality (find device, find serial port, connect, and write). Developer also can contribute to improve the module since it’s opened in GitHub

**Node JS Bluetooth Chatting Application**

In this project, the application will do chat function. It will establish a two-way chat over Bluetooth between devices. This application will perform basic bluetooth communication like: 
Scanning for other Bluetooth devices 
Querying the local Bluetooth adapter for paired Bluetooth devices 
Establishing RFCOMM channels/sockets 
Connecting to a remote device 
Transfering data over Bluetooth

## Bluetooth API on Termux

**How to Install & Use the API**

Unfortunately, you have to build everything from termux app, termux api, and termux api script packages. Here's the guideline to navigate: 

- build termux-app, make sure to clean project if there's installation problem especially if you have existing termux version
- build termux-api, remove default activity from buid configuration
- install git : pkg install git
- install libllvm : pkg install libllvm
- install make : pkg install make
- install clang	: apt install clang
- clone this repository
- navigate to folder termux-api-package
- execute 'make install'
- execute 'termux-bluetooth-status'

Script to debug termux api: 

```
logcat -s termux:* termux-api:*
```


## Progress: 

- Installing Termux
- Running node.js in Android
- Running web application in Android
- Running node-bluetooth in Android - Failed
- Forking node-bluetooth - Find problem that it needs implementation for Android. All hardcoded
- Installing Termux API - no bluetooth support
- Forking Termux API 
- Rebuilding Termux app + Termux API
- Creating Bluetooth support API - in progress


## Problem: 

- Hardcoded implementation: https://github.com/song940/node-bluetooth
- No bluetooth access: https://github.com/termux/termux-api/issues/88


## Sources: 

- Native Bluetooth Android samples: https://github.com/googlesamples/android-BluetoothChat
- Installing node js on Termux: https://medium.freecodecamp.org/building-a-node-js-application-on-android-part-1-termux-vim-and-node-js-dfa90c28958f
- Nodejs application example on Android: https://medium.freecodecamp.org/building-a-node-js-application-on-android-part-2-express-and-nedb-ced04caea7bb
- Termux API : https://termux.com/add-on-api.html
- Building multiple repositories : https://stackoverflow.com/questions/28101444/how-to-include-multiple-repositories-in-build-gradle


  

