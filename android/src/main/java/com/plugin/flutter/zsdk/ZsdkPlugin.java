package com.plugin.flutter.zsdk;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

import com.zebra.sdk.btleComm.BluetoothLeConnection;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.TcpConnection;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** ZsdkPlugin */
public class ZsdkPlugin implements FlutterPlugin, MethodCallHandler {

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    init(
        flutterPluginBinding.getApplicationContext(),
        flutterPluginBinding.getBinaryMessenger()
    );
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    if(channel != null) channel.setMethodCallHandler(null);
  }

  // This static method is only to remain compatible with apps that don’t use the v2 Android embedding.
  @Deprecated()
  @SuppressLint("Registrar")
  public static void registerWith(Registrar registrar)
  {
    new ZsdkPlugin().init(
        registrar.context(),
        registrar.messenger()
    );
  }

  /** Channel */
  static final String _METHOD_CHANNEL = "zsdk";

  /** Methods */
  static final String _PRINT_PDF_FILE_OVER_TCP_IP = "printPdfFileOverTCPIP";
  static final String _PRINT_PDF_DATA_OVER_TCP_IP = "printPdfDataOverTCPIP";
  static final String _PRINT_ZPL_FILE_OVER_TCP_IP = "printZplFileOverTCPIP";
  static final String _PRINT_ZPL_DATA_OVER_TCP_IP = "printZplDataOverTCPIP";
  static final String _CHECK_PRINTER_STATUS_OVER_TCP_IP = "checkPrinterStatusOverTCPIP";
  static final String _GET_PRINTER_SETTINGS_OVER_TCP_IP = "getPrinterSettingsOverTCPIP";
  static final String _SET_PRINTER_SETTINGS_OVER_TCP_IP = "setPrinterSettingsOverTCPIP";
  static final String _DO_MANUAL_CALIBRATION_OVER_TCP_IP = "doManualCalibrationOverTCPIP";
  static final String _PRINT_CONFIGURATION_LABEL_OVER_TCP_IP = "printConfigurationLabelOverTCPIP";
  static final String _REBOOT_PRINTER_OVER_TCP_IP = "rebootPrinterOverTCPIP";
  static final String _FIND_PRINTERS_OVER_TCP_IP = "findPrintersOverTCPIP";

  static final String _PRINT_PDF_FILE_OVER_BLUETOOTH = "printPdfFileOverBluetooth";
  static final String _PRINT_PDF_DATA_OVER_BLUETOOTH = "printPdfDataOverBluetooth";
  static final String _PRINT_ZPL_FILE_OVER_BLUETOOTH = "printZplFileOverBluetooth";
  static final String _PRINT_ZPL_DATA_OVER_BLUETOOTH = "printZplDataOverBluetooth";
  static final String _CHECK_PRINTER_STATUS_OVER_BLUETOOTH = "checkPrinterStatusOverBluetooth";
  static final String _GET_PRINTER_SETTINGS_OVER_BLUETOOTH = "getPrinterSettingsOverBluetooth";
  static final String _SET_PRINTER_SETTINGS_OVER_BLUETOOTH = "setPrinterSettingsOverBluetooth";
  static final String _DO_MANUAL_CALIBRATION_OVER_BLUETOOTH = "doManualCalibrationOverBluetooth";
  static final String _PRINT_CONFIGURATION_LABEL_OVER_BLUETOOTH = "printConfigurationLabelOverBluetooth";
  static final String _REBOOT_PRINTER_OVER_BLUETOOTH = "rebootPrinterOverBluetooth";
  static final String _FIND_PRINTERS_OVER_BLUETOOTH = "findPrintersOverBluetooth";

  /** Properties */
  static final String _filePath = "filePath";
  static final String _data = "data";
  static final String _address = "address";
  static final String _port = "port";
  static final String _cmWidth = "cmWidth";
  static final String _cmHeight = "cmHeight";
  static final String _orientation = "orientation";
  static final String _dpi = "dpi";


  private MethodChannel channel;
  private Context context;

  public ZsdkPlugin() {
  }

  private void init(Context context, BinaryMessenger messenger)
  {
    this.context = context;
    channel = new MethodChannel(messenger, _METHOD_CHANNEL);
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
      try {
          ZPrinter printer = new ZPrinter(
              context,
              channel,
              result,
              new PrinterConf(
                  call.argument(_cmWidth),
                  call.argument(_cmHeight),
                  call.argument(_dpi),
                  Orientation.getValueOfName(call.argument(_orientation))
              )
          );
          switch (call.method) {
              case _DO_MANUAL_CALIBRATION_OVER_TCP_IP:
                  printer.doManualCalibration(
                      newTcpConnection(call)
                  );
                  break;
              case _PRINT_CONFIGURATION_LABEL_OVER_TCP_IP:
                  printer.printConfigurationLabel(
                      newTcpConnection(call)
                  );
                  break;
              case _CHECK_PRINTER_STATUS_OVER_TCP_IP:
                  printer.checkPrinterStatus(
                      newTcpConnection(call)
                  );
                  break;
              case _GET_PRINTER_SETTINGS_OVER_TCP_IP:
                  printer.getPrinterSettings(
                      newTcpConnection(call)
                  );
                  break;
              case _SET_PRINTER_SETTINGS_OVER_TCP_IP:
                  printer.setPrinterSettings(
                      newTcpConnection(call),
                      new PrinterSettings(call.arguments())
                  );
                  break;
              case _PRINT_PDF_FILE_OVER_TCP_IP:
                  printer.printPdfFile(
                      call.argument(_filePath),
                          newTcpConnection(call)
                  );
                  break;
              case _PRINT_ZPL_FILE_OVER_TCP_IP:
                  printer.printZplFile(
                      call.argument(_filePath),
                      newTcpConnection(call)
                  );
                  break;
              case _PRINT_ZPL_DATA_OVER_TCP_IP:
                  printer.printZplData(
                      call.argument(_data),
                      newTcpConnection(call)
                  );
                  break;
              case _REBOOT_PRINTER_OVER_TCP_IP:
                  printer.rebootPrinter(
                      newTcpConnection(call)
                  );
                  break;
              case _PRINT_PDF_DATA_OVER_TCP_IP:
                  printer.printPdfData(
                      call.argument(_data),
                      newTcpConnection(call)
                  );
                  break;
              case _FIND_PRINTERS_OVER_TCP_IP:
                  printer.findPrintersOverTCPIP();
                  break;
              case _DO_MANUAL_CALIBRATION_OVER_BLUETOOTH:
                  printer.doManualCalibration(
                      newBluetoothConnection(call)
                  );
                  break;
              case _PRINT_CONFIGURATION_LABEL_OVER_BLUETOOTH:
                  printer.printConfigurationLabel(
                      newBluetoothConnection(call)
                  );
                  break;
              case _CHECK_PRINTER_STATUS_OVER_BLUETOOTH:
                  printer.checkPrinterStatus(
                      newBluetoothConnection(call)
                  );
                  break;
              case _GET_PRINTER_SETTINGS_OVER_BLUETOOTH:
                  printer.getPrinterSettings(
                      newBluetoothConnection(call)
                  );
                  break;
              case _SET_PRINTER_SETTINGS_OVER_BLUETOOTH:
                  printer.setPrinterSettings(
                      newBluetoothConnection(call),
                      new PrinterSettings(call.arguments())
                  );
                  break;
              case _PRINT_PDF_FILE_OVER_BLUETOOTH:
                  printer.printPdfFile(
                      call.argument(_filePath),
                      newBluetoothConnection(call)
                  );
                  break;
              case _PRINT_ZPL_FILE_OVER_BLUETOOTH:
                  printer.printZplFile(
                      call.argument(_filePath),
                      newBluetoothConnection(call)
                  );
                  break;
              case _PRINT_ZPL_DATA_OVER_BLUETOOTH:
                  printer.printZplData(
                      call.argument(_data),
                      newBluetoothConnection(call)
                  );
                  break;
              case _REBOOT_PRINTER_OVER_BLUETOOTH:
                  printer.rebootPrinter(
                      newBluetoothConnection(call)
                  );
                  break;
              case _FIND_PRINTERS_OVER_BLUETOOTH:
                  printer.findPrintersOverBluetooth();
                  break;
              case _PRINT_PDF_DATA_OVER_BLUETOOTH:
                  printer.printPdfData(
                      call.argument(_data),
                      newBluetoothConnection(call)
                  );
                  break;
              default:
                  result.notImplemented();
          }
      } catch (Exception e) {
          e.printStackTrace();
          result.error(ErrorCode.EXCEPTION.name(), e.getMessage(), null);
      }
  }

    private TcpConnection newTcpConnection(@NonNull MethodCall call) {
        String address = call.argument(_address);
        Integer port = call.argument(_port);
        int MAX_TIME_OUT_FOR_READ = 5000;
        int TIME_TO_WAIT_FOR_MORE_DATA = 0;
        int tcpPort = port != null ? port : TcpConnection.DEFAULT_ZPL_TCP_PORT;

        return new TcpConnection(address, tcpPort, MAX_TIME_OUT_FOR_READ, TIME_TO_WAIT_FOR_MORE_DATA);
    }

    public BluetoothConnection newBluetoothConnection(@NonNull MethodCall call) {
        String address = call.argument(_address);

        return new BluetoothConnection(address);
    }
}
