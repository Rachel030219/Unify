// =================================================
// Autogenerated from Unify 3.0.0, do not edit directly.
// =================================================

#import <Foundation/Foundation.h>
NS_ASSUME_NONNULL_BEGIN

#ifndef UNI_EXPORT
#define UNI_EXPORT(className) \
__attribute__((used, section("__DATA , ud_uni_api"))) \
static char *__uni_export_class_##className##__ = ""#className"";
#endif

@interface UDUniAPI : NSObject

/// 加载导出类
+ (void)loadExportClass;

/// 获取协议的遵守者
+ (id)get:(NSString *)className;

@end

NS_ASSUME_NONNULL_END
