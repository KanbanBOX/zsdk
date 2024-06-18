import 'dart:convert';

enum PrinterConnectionType { bluetooth, network }

class PrinterConnectionData {
  String address;
  String? friendlyName;
  PrinterConnectionType type;
  bool supportsPDF;
  String? dpi;

  PrinterConnectionData(
      this.address,
      this.friendlyName,
      this.type,
      this.supportsPDF,
      this.dpi
    );

  static PrinterConnectionData fromJson(String jsonString){
    Map<String, dynamic> decoded = jsonDecode(jsonString) as Map<String, dynamic>;
    return PrinterConnectionData(
      decoded['address'],
      decoded['friendlyName'],
      PrinterConnectionType.values.byName(decoded['type'] as String),
      decoded['supportsPDF'] == 'true',
      decoded['dpi']
    );
  }

  String toJson() {
    return jsonEncode({
      'address': address,
      'friendlyName': friendlyName,
      'type': type.name,
      'supportsPDF': supportsPDF ? 'true' : 'false',
      'dpi': dpi
    });
  }
}
