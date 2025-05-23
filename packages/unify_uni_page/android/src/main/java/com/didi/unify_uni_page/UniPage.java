package com.didi.unify_uni_page;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;

public abstract class UniPage implements PlatformView {
    private View view;
    private Context context;

    private String viewType;

    private int viewId;
    private Map<String, Object> creationParams;

    private MethodChannel channel;

    private AbsUniPageFactoryListener uniPageFactoryListener;

    public UniPage() {
    }

    /**********************************************
     *  UniPage 生命周期
     **********************************************/

    /**
     * 嵌原生页面创建
     *
     * @return 嵌原生页面的根视图
     */
    public abstract View onCreate();

    /**
     * 嵌原生页面释放
     * 释放页面所依赖的资源，避免内存泄漏
     */
    public abstract void onDispose();

    /**
     * 嵌原生视图创建完成，被添加到布局树
     */
    public void postCreate() {

    }

    /**
     * 嵌原生页面进入前台；
     * 该方法 <bold>不保证</bold> 在首次显示时调用，仅响应 Activity 的 onStart 事件。
     */
    public void onForeground() {

    }

    /**
     * 嵌原生页面进入后台；
     * 该方法 <bold>不保证</bold> 在销毁前调用，仅响应 Activity 的 onStop 事件。
     */
    public void onBackground() {

    }

    /**********************************************
     *  资源获取
     **********************************************/

    public Context getContext() {
        return context;
    }

    public int getViewId() {
        return viewId;
    }

    public Map<String, Object> getCreationParams() {
        return creationParams;
    }

    /**********************************************
     *  Flutter Navigator 路由
     **********************************************/

    public void pushNamed(String routePath, Map<String, Object> params) {
        pushNamed(routePath, params, null);
    }

    public void pushNamed(String routePath, Map<String, Object> params, MethodChannel.Result callback) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(Constants.UNI_PAGE_CHANNEL_PARAMS_PATH, routePath);
        paramsMap.put(Constants.UNI_PAGE_CHANNEL_PARAMS_PARAMS, params);
        channel.invokeMethod(Constants.UNI_PAGE_ROUTE_PUSH_NAMED, paramsMap, callback);
    }

    public void pop(Object result) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(Constants.UNI_PAGE_CHANNEL_PARAMS_PARAMS, result);
        channel.invokeMethod(Constants.UNI_PAGE_ROUTE_POP, paramsMap);
    }

    /**********************************************
     *  Invoke 调用 Flutter 方法
     **********************************************/
    public void invoke(String methodName, Map<String, Object> params) {
        invoke(methodName, params, null);
    }

    public void invoke(String methodName, Map<String, Object> params, MethodChannel.Result callback) {
        Log.i("UniPage", "invoking Flutter method " + methodName);
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put(Constants.UNI_PAGE_CHANNEL_VIEW_TYPE, viewType);
        arguments.put(Constants.UNI_PAGE_CHANNEL_VIEW_ID, viewId);
        arguments.put(Constants.UNI_PAGE_CHANNEL_METHOD_NAME, methodName);
        arguments.put(Constants.UNI_PAGE_CHANNEL_PARAMS_PARAMS, params);
        channel.invokeMethod(Constants.UNI_PAGE_CHANNEL_INVOKE, arguments, callback);
    }

    public void onMethodCall(String methodName, Map<String, Object> params, MethodChannel.Result result) {
    }

    /**********************************************
     *  嵌原生接口
     **********************************************/
    public void init(@NonNull Context context, String viewType, int id, BinaryMessenger binaryMessenger, @Nullable Map<String, Object> creationParams) {
        this.context = context;
        this.viewType = viewType;
        this.viewId = id;
        this.creationParams = creationParams;
        this.channel = new MethodChannel(
                binaryMessenger,
                Constants.createChannelName(viewType, viewId));

        channel.setMethodCallHandler((call, result) -> {
            Map<String, Object> arguments = (Map<String, Object>) call.arguments;
            if (call.method.equals(Constants.UNI_PAGE_CHANNEL_INVOKE)) {
                String methodName = (String) arguments.get(Constants.UNI_PAGE_CHANNEL_METHOD_NAME);
                Map<String, Object> methodParams = (Map<String, Object>) arguments.get(Constants.UNI_PAGE_CHANNEL_PARAMS_PARAMS);
                onMethodCall(methodName, methodParams, result);
            }
        });

        UniPageLifecycleHolder.getInstance().bindUniPageToCurrentActivity(this);
    }

    @Nullable
    @Override
    public View getView() {
        if (view != null) {
            return view;
        }
        view = onCreate();
        if (view != null) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    postCreate();
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
        return view;
    }

    @Override
    public void dispose() {
        onDispose();
        this.channel.setMethodCallHandler(null);

        UniPageLifecycleHolder.getInstance().unbindUniPageFromActivity(this);

        if (uniPageFactoryListener != null) {
            uniPageFactoryListener.onPlatformViewDispose(
                    getContext(),
                    getViewId());
            uniPageFactoryListener = null;
        }

        this.context = null;
    }

    public void setUniPageFactoryListener(AbsUniPageFactoryListener uniPageFactoryListener) {
        this.uniPageFactoryListener = uniPageFactoryListener;
    }
}
