package com.tyl.base_lib.provider;

import java.util.HashMap;

class ProviderRegisterManager {

    public static HashMap<String, String> map = new HashMap<>();

    public static boolean isInitByPlugin = false;

    public static void loadRegister() {
        // generate code by plugin
        // like this :
        // loadClassMapByRegister(OneProviderRegister)
        // loadClassMapByRegister(TowProviderRegister)
        // loadClassMapByRegister(ThreeProviderRegister)
        // ...
    }

    public static void loadClassMapByRegister(String register) {
        try {
            Class<?> registerClass = Class.forName(register);
            IProviderRegister instance = (IProviderRegister) registerClass.newInstance();
            instance.register(map);
            if (!isInitByPlugin)
                isInitByPlugin = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HashMap<Class<?>, Class<?>> getClassMap() {
        HashMap<Class<?>, Class<?>> classMap = new HashMap<>();
        try {
            for (String key : map.keySet()) {
                String value = map.get(key);
                if (key != null && value != null)
                    classMap.put(Class.forName(key), Class.forName(value));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classMap;
    }
}
