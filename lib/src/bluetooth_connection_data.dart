import 'dart:convert';

enum PrinterConnectionType { bluetooth, bluetooth_low_energy, network }

class PrinterConnectionData {
  String address;
  String? friendlyName;
  PrinterConnectionType type;
  bool? supportsPDF;
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
      (){
        switch(decoded['supportsPDF'] ?? null) {
          case 'true': return true;
          case 'false': return false;
          case null: return null;
        }
      }(),
      decoded['dpi'] ?? null
    );
  }

  String toJson() {
    return jsonEncode({
      'address': address,
      'friendlyName': friendlyName,
      'type': type.name,
      'supportsPDF': (){
        switch(supportsPDF) {
          case true: return 'true';
          case false: return 'false';
          case null: return null;
        }
      }(),
      'dpi': dpi
    });
  }
}
