import 'package:flutter/material.dart';

void main() => runApp(const MyApp());

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    const name = String.fromEnvironment("clientName", defaultValue: "Default name");
    const icon = String.fromEnvironment("app_icon", defaultValue: "");
    return MaterialApp(
      title: 'Material App',
      home: Scaffold(
        appBar: AppBar(
          title: const Text('$name App Bar'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Image.asset(icon,height: 200,),
              Text('path: $icon')
            ],
          ),
        ),
      ),
    );
  }
}