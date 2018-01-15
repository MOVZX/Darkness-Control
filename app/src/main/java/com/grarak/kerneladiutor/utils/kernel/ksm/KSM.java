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
package com.grarak.kerneladiutor.utils.kernel.ksm;

import android.content.Context;

import com.grarak.kerneladiutor.R;
import com.grarak.kerneladiutor.fragments.ApplyOnBootFragment;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.Control;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by willi on 28.06.16.
 */
public class KSM {

    private static final String KSM = "/sys/kernel/mm/ksm";
    private static final String UKSM = "/sys/kernel/mm/uksm";
    private static final String FULL_SCANS = "/full_scans";
    private static final String PAGES_SCANNED = "/pages_scanned";
    private static final String PAGES_SHARED = "/pages_shared";
    private static final String PAGES_SHARING = "/pages_sharing";
    private static final String PAGES_UNSHARED = "/pages_unshared";
    private static final String PAGES_VOLATILE = "/pages_volatile";
    private static final String RUN = "/run";
    private static final String RUN_CHARGING = "/run_charging";
    private static final String DEFERRED_TIMER = "/deferred_timer";
    private static final String PAGES_TO_SCAN = "/pages_to_scan";
    private static final String SLEEP_MILLISECONDS = "/sleep_millisecs";
    private static final String MAX_CPU_PERCENTAGE = "/max_cpu_percentage";
    private static final String CPU_GOVERNOR = "/cpu_governor";

    private static final List<String> sParent = new ArrayList<>();
    private static final LinkedHashMap<String, Integer> sInfos = new LinkedHashMap<>();
    private static String PARENT;

    static {
        sParent.add(UKSM);
        sParent.add(KSM);

        sInfos.put(FULL_SCANS, R.string.full_scans);
        sInfos.put(PAGES_SCANNED, R.string.pages_scanned);
        sInfos.put(PAGES_SHARED, R.string.pages_shared);
        sInfos.put(PAGES_SHARING, R.string.pages_sharing);
        sInfos.put(PAGES_UNSHARED, R.string.pages_unshared);
        sInfos.put(PAGES_VOLATILE, R.string.pages_volatile);
    }

    public static void setMaxCpuPercentage(int value, Context context) {
        run(Control.write(String.valueOf(value), PARENT + MAX_CPU_PERCENTAGE), MAX_CPU_PERCENTAGE, context);
    }

    public static int getMaxCpuPercentage() {
        return Utils.strToInt(Utils.readFile(PARENT + MAX_CPU_PERCENTAGE));
    }

    public static boolean hasMaxCpuPercentage() {
        return Utils.existFile(PARENT + MAX_CPU_PERCENTAGE);
    }

    public static void setSleepMilliseconds(int ms, Context context) {
        run(Control.write(String.valueOf(ms), PARENT + SLEEP_MILLISECONDS), PARENT + SLEEP_MILLISECONDS, context);
    }

    public static int getSleepMilliseconds() {
        return Utils.strToInt(Utils.readFile(PARENT + SLEEP_MILLISECONDS));
    }

    public static boolean hasSleepMilliseconds() {
        return Utils.existFile(PARENT + SLEEP_MILLISECONDS);
    }

    public static void setPagesToScan(int pages, Context context) {
        run(Control.write(String.valueOf(pages), PARENT + PAGES_TO_SCAN), PARENT + PAGES_TO_SCAN, context);
    }

    public static int getPagesToScan() {
        return Utils.strToInt(Utils.readFile(PARENT + PAGES_TO_SCAN));
    }

    public static boolean hasPagesToScan() {
        return Utils.existFile(PARENT + PAGES_TO_SCAN);
    }

    public static void enableDeferredTimer(boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", PARENT + DEFERRED_TIMER), PARENT + DEFERRED_TIMER, context);
    }

    public static boolean isDeferredTimerEnabled() {
        return Utils.readFile(PARENT + DEFERRED_TIMER).equals("1");
    }

    public static boolean hasDeferredTimer() {
        return Utils.existFile(PARENT + DEFERRED_TIMER);
    }

    public static void enableKsm(boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", PARENT + RUN), PARENT + RUN, context);
    }

    public static boolean isEnabled() {
        return Utils.readFile(PARENT + RUN).equals("1");
    }

    public static boolean hasEnable() {
        return Utils.existFile(PARENT + RUN);
    }

    public static void enableCharging(boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", PARENT + RUN_CHARGING), PARENT + RUN_CHARGING, context);
    }

    public static boolean isChargingEnabled() {
        return Utils.readFile(PARENT + RUN_CHARGING).equals("1");
    }

    public static boolean hasChargingEnable() {
        return Utils.existFile(PARENT + RUN_CHARGING);
    }

    public static String getInfo(int position) {
        return Utils.readFile(PARENT + sInfos.keySet().toArray(new String[sInfos.size()])[position]);
    }

    public static boolean hasInfo(int position) {
        return Utils.existFile(PARENT + sInfos.keySet().toArray(new String[sInfos.size()])[position]);
    }

    public static String getInfoText(int position, Context context) {
        return context.getString(sInfos.get(sInfos.keySet().toArray(new String[sInfos.size()])[position]));
    }

    public static int getInfosSize() {
        return sInfos.size();
    }

    public static boolean supported() {
        if (PARENT == null) {
            for (String file : sParent) {
                if (Utils.existFile(file)) {
                    PARENT = file;
                    return true;
                }
            }
        }
        return PARENT != null;
    }

    public static void setCPUGovernor(String value, Context context) {
        run(Control.write(value, PARENT + CPU_GOVERNOR), CPU_GOVERNOR, context);
    }

    public static String getCPUGovernor() {
        return getCPUGovernor(PARENT + CPU_GOVERNOR);
    }

    public static List<String> getCPUGovernors() {
        return getCPUGovernors(PARENT + CPU_GOVERNOR);
    }

    public static boolean hasCPUGovernor() {
        return Utils.existFile(PARENT + CPU_GOVERNOR);
    }

    private static String getCPUGovernor(String path) {
        String[] governors = Utils.readFile(path).split(" ");
        for (String governor : governors) {
            if (governor.startsWith("[") && governor.endsWith("]")) {
                return governor.replace("[", "").replace("]", "");
            }
        }
        return "";
    }

    private static List<String> getCPUGovernors(String path) {
        String[] governors = Utils.readFile(path).split(" ");
        List<String> list = new ArrayList<>();
        for (String governor : governors) {
            list.add(governor.replace("[", "").replace("]", ""));
        }
        return list;
    }

    private static void run(String command, String id, Context context) {
        Control.runSetting(command, ApplyOnBootFragment.KSM, id, context);
    }

}
