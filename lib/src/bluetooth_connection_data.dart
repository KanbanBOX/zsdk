import 'dart:convert';

enum PrinterConnectionType { bluetooth, network }

class PrinterConnectionData {
  String address;
  String? friendlyName;
  PrinterConnectionType type;
  bool supportsPDF;

  PrinterConnectionData(
      this.address,
      this.friendlyName,
      this.type,
      this.supportsPDF
    );

  static PrinterConnectionData fromJson(String jsonString){
    Map<String, dynamic> decoded = jsonDecode(jsonString) as Map<String, dynamic>;
    return PrinterConnectionData(
        decoded['address'],
        decoded['friendlyName'],
        PrinterConnectionType.values.byName(decoded['type'] as String),
        decoded['supportsPDF']
    );
  }

  String toJson() {
    return jsonEncode({
      'address': address,
      'friendlyName': friendlyName,
      'type': type.name,
      'supportsPDF': supportsPDF
    });
  }
}
