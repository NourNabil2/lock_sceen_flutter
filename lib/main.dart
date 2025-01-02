import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // TRY THIS: Try running your application with "flutter run". You'll see
        // the application has a purple toolbar. Then, without quitting the app,
        // try changing the seedColor in the colorScheme below to Colors.green
        // and then invoke "hot reload" (save your changes or press the "hot
        // reload" button in a Flutter-supported IDE, or press "r" if you used
        // the command line to start the app).
        //
        // Notice that the counter didn't reset back to zero; the application
        // state is not lost during the reload. To reset the state, use hot
        // restart instead.
        //
        // This works for code too, not just values: Most code changes can be
        // tested with just a hot reload.
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: LockScreenButton(),
    );
  }
}

class LockScreenButton extends StatelessWidget {
  static const _channel = MethodChannel('com.example.deletethis/lockscreen');

  static Future<void> lockScreen() async {
    try {
      // Try to lock the screen
      await _channel.invokeMethod('lockScreen');
      log('Locking screen...');
    } catch (e) {
      log('Error locking screen: $e');
      // Handle admin permissions if screen locking fails
      if (e.toString().contains('Device admin permissions not enabled')) {
        // If the admin permissions are not enabled, prompt user to enable them
        requestDeviceAdmin();
      }
    }
  }

  static Future<void> requestDeviceAdmin() async {
    try {
      await _channel.invokeMethod('requestDeviceAdmin');
      log('Requesting device admin permission...');
    } catch (e) {
      log('Error requesting device admin: $e');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Lock Screen Example")),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            ElevatedButton(
              onPressed: lockScreen,
              child: Text("Lock Screen"),
            ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: requestDeviceAdmin,
              child: Text("Request Admin Permission"),
            ),
          ],
        ),
      ),
    );
  }
}


