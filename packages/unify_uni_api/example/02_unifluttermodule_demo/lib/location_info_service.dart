// =================================================
// Autogenerated from Unify 3.0.2, do not edit directly.
// =================================================

import 'package:flutter/services.dart';

import 'location_info_model.dart';

/// Call flow direction : native -> dart
abstract class LocationInfoService {
    static Map<K, dynamic> mapClone<K, V>(Map<K, V> map) {
      Map<K, dynamic> newMap = <K, dynamic>{};

      map.forEach((key, value) {
        newMap[key] = 
          (value is Map ? mapClone(value) : 
          value is List ? listClone(value): 
          value is LocationInfoModel ? value.encode() : 
          value);
      });

      return newMap;
    }
    
    static List<T> listClone<T>(List list) {
      List<T> newList = <T>[];
      for (var value in list) {
        newList.add(
          value is Map ? mapClone(value) :
          value is List ? listClone(value) :
          value is LocationInfoModel ? value.encode() : 
          value);
      }

      return newList;
    }
    

    void updateLocationInfo(LocationInfoModel model);

    static void setup(LocationInfoService impl) {
        {
            const BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>('com.didi.flutter.uni_api.LocationInfoService.updateLocationInfo', StandardMessageCodec());
            channel.setMessageHandler((Object? message) async {
                if (message == null) return null;
                Map<Object?, Object?> wrapped = message as Map<Object?, Object?>;
                impl.updateLocationInfo(LocationInfoModel.decode(wrapped['model']!));
            });
        }
    }
}
