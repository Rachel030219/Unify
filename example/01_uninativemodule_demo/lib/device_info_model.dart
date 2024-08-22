// =================================================
// Autogenerated from Unify 3.0.0, do not edit directly.
// =================================================


///  设备信息 实体类
class DeviceInfoModel {
    String? osVersion; /// 系统版本
    String? memory; /// 内存信息
    String? plaform; /// 手机型号

    Object encode() {
        final Map<String, dynamic> ret = <String, dynamic>{};
        ret['osVersion'] = osVersion as String?;
        ret['memory'] = memory as String?;
        ret['plaform'] = plaform as String?;
        return ret;
    }

    static DeviceInfoModel decode(Object message) {
        final Map<dynamic, dynamic> rawMap = message as Map<dynamic, dynamic>;
        final Map<String, dynamic> map = Map.from(rawMap);
        return DeviceInfoModel()
            ..osVersion = map['osVersion'] == null
                ? ''
                : map['osVersion'] as String?
            ..memory = map['memory'] == null
                ? ''
                : map['memory'] as String?
            ..plaform = map['plaform'] == null
                ? ''
                : map['plaform'] as String?
            ;
    }
}
