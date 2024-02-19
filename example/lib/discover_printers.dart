import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:zsdk/zsdk.dart';

class DiscoverPrinters extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _DiscoverPrintersState();
}

class _DiscoverPrintersState extends State<DiscoverPrinters>
{
  bool stillLookingForPrinters = true;
  late ZSDK _zebraLinkosSdkPlugin;
  List<PrinterConnectionData> printerList = [];

  @override
  void initState() {
    super.initState();
    _zebraLinkosSdkPlugin = ZSDK(printerFound: _onPrinterFound);
    WidgetsBinding.instance.addPostFrameCallback((_) => _onAfterFirstBuild());
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Printers Found'),
      ),
      body:
      Column(
        children: [
          Expanded(
              flex: 1,
              child: stillLookingForPrinters ? const Text('Looking for printers...') : const Text('Search complete')
          ),
          Expanded(
              flex: 6,
              child: ListView.separated(
                padding: const EdgeInsets.all(10),
                itemCount: printerList.length,
                itemBuilder: (context, index) {
                  final PrinterConnectionData printer = printerList[index];

                  return ListTile(
                    title: Text('Friendly name: ${printer.friendlyName}'),
                    subtitle: Text('Address: ${printer.address}'),
                    onTap: () {
                      Navigator.pop(context, printer);
                    }
                  );
                }, separatorBuilder: (BuildContext context, int index) => const Divider(),
              )
          ),
        ],
      ),
    );
  }

  _onAfterFirstBuild() async {
    PermissionStatus permissionRequestResult = await Permission.bluetooth.request();
    PermissionStatus permissionScanRequestResult = await Permission.bluetoothScan.request();
    PermissionStatus permissionConnectRequestResult = await Permission.bluetoothConnect.request();
    PermissionStatus locationRequestResult = await Permission.location.request();

    if(locationRequestResult != PermissionStatus.granted || permissionRequestResult != PermissionStatus.granted || permissionScanRequestResult != PermissionStatus.granted || permissionConnectRequestResult != PermissionStatus.granted){
      Navigator.pop(context, null);
      return;
    }
    await _zebraLinkosSdkPlugin.findPrintersOverBluetooth();
    if(mounted){
      setState(() {
        stillLookingForPrinters = false;
      });
    }
  }

  _onPrinterFound(PrinterConnectionData printerConnectionData) {
    setState(() {
      printerList.add(printerConnectionData);
    });
  }
}