import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const platform = const MethodChannel('samples.flutter.dev/battery');

  Future<void> _switchGPIO(int value) async {
    // Get battery level.
    print(value.toString());
    if(value == 1){
      print("Switch on");
      try {
        final String result = await platform.invokeMethod('gpioSetStateOn');
      } on PlatformException catch (e) {
        print(e);
      }
    }
    else{
      print("Switch off");
      try {
        final String result = await platform.invokeMethod('gpioSetStateOff');
      } on PlatformException catch (e) {
        print(e);
      }
    }

  }

  @override
  Widget build(BuildContext context) {
    return Material(
      child: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            ElevatedButton(
              child: Text('Switch On'),
                style: ButtonStyle(
                  backgroundColor: MaterialStateProperty.all<Color>(Colors.green),
                ),
                onPressed: () => _switchGPIO(1)
            ),
            ElevatedButton(
              child: Text('Switch Off'),
                style: ButtonStyle(
                  backgroundColor: MaterialStateProperty.all<Color>(Colors.red),
                ),
                onPressed: () => _switchGPIO(0)
            ),
          ],
        ),
      ),
    );
  }
}
