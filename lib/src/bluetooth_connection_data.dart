import 'dart:convert';

enum PrinterConnectionType { bluetooth, network }

class PrinterConnectionData {
  String address;
  String friendlyName;
  PrinterConnectionType type;

  PrinterConnectionData(
      this.address,
      this.friendlyName,
      this.type
    );

  static PrinterConnectionData fromJson(String jsonString){
    Map<String, dynamic> decoded = jsonDecode(jsonString) as Map<String, dynamic>;
    return PrinterConnectionData(
        decoded['address'],
        decoded['friendlyName'],
        PrinterConnectionType.from
    );
  }
}
