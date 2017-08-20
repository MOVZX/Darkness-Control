/*
 * Copyright (C) 2015-2016 Willi Ye <williye97@gmail.com>
 *
 * This file is part of Kernel Adiutor.
 *
 * Kernel Adiutor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kernel Adiutor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Kernel Adiutor.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.grarak.kerneladiutor.utils.kernel.battery;

import android.content.Context;

import com.grarak.kerneladiutor.fragments.ApplyOnBootFragment;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.Control;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by willi on 26.06.16.
 */
public class Battery {

    private static final String FORCE_FAST_CHARGE = "/sys/kernel/fast_charge/force_fast_charge";

    private static final String CHARGE_RATE = "/sys/module/qpnp_smbcharger/parameters/";
    private static final String CUSTOM_CURRENT1 = CHARGE_RATE + "default_dcp_icl_ma";
    private static final String CUSTOM_CURRENT2 = CHARGE_RATE + "default_hvdcp_icl_ma";
    private static final String CUSTOM_CURRENT3 = CHARGE_RATE + "default_hvdcp3_icl_ma";
    private static final String CUSTOM_CURRENT4 = CHARGE_RATE + "default_fastchg_current_ma";

    private static final String BATTERY_CURRENT_LIMIT = "/sys/module/battery_current_limit/parameters/low_battery_value";

    private static final List<String> sChargingRate = new ArrayList<>();
    private static Integer sCapacity;
    private static String CUSTOM_CHARGING_RATE;

    static {
        sChargingRate.add(CUSTOM_CURRENT1);
        sChargingRate.add(CUSTOM_CURRENT2);
        sChargingRate.add(CUSTOM_CURRENT3);
        sChargingRate.add(CUSTOM_CURRENT4);
    }

    public static boolean hasChargingCurrent() {
        if (CUSTOM_CHARGING_RATE == null) {
            for (String file : sChargingRate) {
                if (Utils.existFile(file)) {
                    CUSTOM_CHARGING_RATE = file;
                    return true;
                }
            }
        }
        return CUSTOM_CHARGING_RATE != null;
    }

    public static void setChargingCurrent(int value, Context context) {
        run(Control.write(String.valueOf(value), CUSTOM_CURRENT1), CUSTOM_CURRENT1, context);
        run(Control.write(String.valueOf(value), CUSTOM_CURRENT2), CUSTOM_CURRENT2, context);
        run(Control.write(String.valueOf(value), CUSTOM_CURRENT3), CUSTOM_CURRENT3, context);
        run(Control.write(String.valueOf(value), CUSTOM_CURRENT4), CUSTOM_CURRENT4, context);
    }

    public static int getChargingCurrent() {
        return Utils.strToInt(Utils.readFile(CUSTOM_CHARGING_RATE));
    }

    public static void enableForceFastCharge(boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", FORCE_FAST_CHARGE), FORCE_FAST_CHARGE, context);
    }

    public static boolean isForceFastChargeEnabled() {
        return Utils.readFile(FORCE_FAST_CHARGE).equals("1");
    }

    public static boolean hasForceFastCharge() {
        return Utils.existFile(FORCE_FAST_CHARGE);
    }

    public static void setBatteryCurrentLimit(int value, Context context) {
        run(Control.write(String.valueOf(value), BATTERY_CURRENT_LIMIT), BATTERY_CURRENT_LIMIT, context);
    }

    public static int getBatteryCurrentLimit() {
        return Utils.strToInt(Utils.readFile(BATTERY_CURRENT_LIMIT));
    }

    public static boolean hasBatteryCurrentLimit() {
        return Utils.existFile(BATTERY_CURRENT_LIMIT);
    }

    public static int getCapacity(Context context) {
        if (sCapacity == null) {
            try {
                Class<?> powerProfile = Class.forName("com.android.internal.os.PowerProfile");
                Constructor constructor = powerProfile.getDeclaredConstructor(Context.class);
                Object powerProInstance = constructor.newInstance(context);
                Method batteryCap = powerProfile.getMethod("getBatteryCapacity");
                sCapacity = Math.round((long) (double) batteryCap.invoke(powerProInstance));
            } catch (Exception e) {
                e.printStackTrace();
                sCapacity = 0;
            }
        }
        return sCapacity;
    }

    public static boolean hasCapacity(Context context) {
        return getCapacity(context) != 0;
    }

    public static boolean supported(Context context) {
        return hasCapacity(context);
    }

    private static void run(String command, String id, Context context) {
        Control.runSetting(command, ApplyOnBootFragment.BATTERY, id, context);
    }

}