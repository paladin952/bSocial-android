package com.clpstudio.bsocial.presentation.general.mvp;

import android.support.annotation.NonNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public abstract class BaseMvpPresenter<V extends IBaseMvpPresenter.View> {
    private V view;

    public void bindView(@NonNull V view) {
        this.view = view;
    }

    public void unbindView() {
        //We reset our view with a fake one using a proxy. When the proxy is called it does nothing
        //This way we don;t have to handle null views if we unbind it onPause/onStop/onDestroy
        resetView();
    }

    protected V view() {
        return view;
    }

    protected boolean setupDone() {
        return view() != null;
    }

    /**
     * Changes the current view instance with a dynamic proxy to avoid real UI updates.
     */
    private void resetView() {
        this.view = getFakeViewProxy();
    }

    /**
     * Create a fake proxy of V that does nothing
     *
     * @return a proxy of V
     */
    private V getFakeViewProxy() {
        final Class<?> viewClass = getViewInterfaceClass();
        InvocationHandler emptyHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) {
                return null;
            }
        };
        ClassLoader classLoader = viewClass.getClassLoader();
        Class[] interfaces = new Class[1];
        interfaces[0] = viewClass;
        return (V) Proxy.newProxyInstance(classLoader, interfaces, emptyHandler);
    }

    private Class<?> getViewInterfaceClass() {
        Class<?> interfaceClass = null;
        Class<?>[] interfaces = this.view.getClass().getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            Class<?> interfaceCandidate = interfaces[i];
            if (IBaseMvpPresenter.View.class.isAssignableFrom(interfaceCandidate)) {
                interfaceClass = interfaceCandidate;
            }
        }
        return interfaceClass;
    }
}