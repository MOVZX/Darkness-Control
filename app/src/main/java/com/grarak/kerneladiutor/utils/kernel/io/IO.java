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
package com.grarak.kerneladiutor.utils.kernel.io;

import android.content.Context;

import com.grarak.kerneladiutor.fragments.ApplyOnBootFragment;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.Control;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willi on 27.06.16.
 */
public abstract class IO {

    private static final String UFS = "/sys/block/sda/queue";

    private static final String SCHEDULER = "scheduler";
    private static final String IOSCHED = "iosched";
    private static final String READ_AHEAD = "read_ahead_kb";
    private static final String ROTATIONAL = "rotational";
    private static final String IOSTATS = "iostats";
    private static final String ADD_RANDOM = "add_random";
    private static final String RQ_AFFINITY = "rq_affinity";
    private static final String CLK_SCALING = "/sys/class/mmc_host/mmc0/clk_scaling/";
    private static final String SCALE_DOWN_IN_LOW_WR_LOAD = CLK_SCALING + "scale_down_in_low_wr_load";
    private static final String UP_THRESHOLD = CLK_SCALING + "up_threshold";
    private static final String DOWN_THRESHOLD = CLK_SCALING + "down_threshold";
    private static final String POLLING_INTERVAL = CLK_SCALING + "polling_interval";
    private static final String SCSI_MODULE = "/sys/module/scsi_mod/parameters/";
    private static final String SCSI_MULTI_QUEUE = SCSI_MODULE + "use_blk_mq";
    private static final String UFS_PARAMETERS = "/sys/devices/soc/624000.ufshc/";
    private static final String UFS_CLOCK_SCALING = UFS_PARAMETERS + "clkscale_enable";
    private static final String UFS_CLOCK_GATING = UFS_PARAMETERS + "clkgate_enable";
    private static final String UFS_PM_QOS = UFS_PARAMETERS + "624000.ufshc:ufs_variant/pm_qos_enable";

    private static final List<String> sInternal = new ArrayList<>();
    private static final List<String> sExternal = new ArrayList<>();
    private static String INTERNAL;
    private static String EXTERNAL;

    static {
        sInternal.add(UFS);
        sInternal.add("/sys/block/mmcblk0/queue");

        sExternal.add("/sys/block/mmcblk1/queue");
    }

    public static boolean hasExternal() {
        return EXTERNAL != null;
    }

    public static void setRqAffinity(Storage storage, int value, Context context) {
        run(Control.write(String.valueOf(value), getPath(storage, RQ_AFFINITY)),
                getPath(storage, RQ_AFFINITY), context);
    }

    public static int getRqAffinity(Storage storage) {
        return Utils.strToInt(Utils.readFile(getPath(storage, RQ_AFFINITY)));
    }

    public static boolean hasRqAffinity(Storage storage) {
        return Utils.existFile(getPath(storage, RQ_AFFINITY));
    }

    public static void enableAddRandom(Storage storage, boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", getPath(storage, ADD_RANDOM)), getPath(storage, ADD_RANDOM),
                context);
    }

    public static boolean isAddRandomEnabled(Storage storage) {
        return Utils.readFile(getPath(storage, ADD_RANDOM)).equals("1");
    }

    public static boolean hasAddRandom(Storage storage) {
        return Utils.existFile(getPath(storage, ADD_RANDOM));
    }

    public static void enableIOstats(Storage storage, boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", getPath(storage, IOSTATS)), getPath(storage, IOSTATS),
                context);
    }

    public static boolean isIOStatsEnabled(Storage storage) {
        return Utils.readFile(getPath(storage, IOSTATS)).equals("1");
    }

    public static boolean hasIOStats(Storage storage) {
        return Utils.existFile(getPath(storage, IOSTATS));
    }

    public static void enableRotational(Storage storage, boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", getPath(storage, ROTATIONAL)),
                getPath(storage, ROTATIONAL), context);
    }

    public static boolean isRotationalEnabled(Storage storage) {
        return Utils.readFile(getPath(storage, ROTATIONAL)).equals("1");
    }

    public static boolean hasRotational(Storage storage) {
        return Utils.existFile(getPath(storage, ROTATIONAL));
    }

    /* Clock Scaling: Start */
    public static boolean hasScaleDownInLowWrLoad() {
        return Utils.existFile(SCALE_DOWN_IN_LOW_WR_LOAD);
    }

    public static boolean isScaleDownInLowWrLoadEnabled() {
        return Utils.readFile(SCALE_DOWN_IN_LOW_WR_LOAD).equals("1");
    }

    public static void enableScaleDownInLowWrLoad(boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", SCALE_DOWN_IN_LOW_WR_LOAD), SCALE_DOWN_IN_LOW_WR_LOAD, context);
    }

    /* SCSI */
    public static boolean hasSCSIMultiQueue() {
        return Utils.existFile(SCSI_MULTI_QUEUE);
    }

    public static boolean isSCSIMultiQueueEnabled() {
        return Utils.readFile(SCSI_MULTI_QUEUE).equals("Y");
    }

    public static void enableSCSIMultiQueue(boolean enable, Context context) {
        run(Control.write(enable ? "Y" : "N", SCSI_MULTI_QUEUE), SCSI_MULTI_QUEUE, context);
    }

    public static boolean hasUFSClockScaling() {
        return Utils.existFile(UFS_CLOCK_SCALING);
    }

    public static boolean isUFSClockScalingEnabled() {
        return Utils.readFile(UFS_CLOCK_SCALING).equals("1");
    }

    public static void enableUFSClockScaling(boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", UFS_CLOCK_SCALING), UFS_CLOCK_SCALING, context);
    }

    public static boolean hasUFSClockGating() {
        return Utils.existFile(UFS_CLOCK_GATING);
    }

    public static boolean isUFSClockGatingEnabled() {
        return Utils.readFile(UFS_CLOCK_GATING).equals("1");
    }

    public static void enableUFSClockGating(boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", UFS_CLOCK_GATING), UFS_CLOCK_GATING, context);
    }

    public static boolean hasUFSPMQOS() {
        return Utils.existFile(UFS_PM_QOS);
    }

    public static boolean isUFSPMQOSEnabled() {
        return Utils.readFile(UFS_PM_QOS).equals("1");
    }

    public static void enableUFSPMQOS(boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", UFS_PM_QOS), UFS_PM_QOS, context);
    }
    /* SCSI */

    public static boolean hasUpThreshold() {
        return Utils.existFile(UP_THRESHOLD);
    }

    public static int getUpThreshold() {
        return Utils.strToInt(Utils.readFile(UP_THRESHOLD));
    }

    public static void setUpThreshold(int value, Context context) {
        run(Control.write(String.valueOf(value), UP_THRESHOLD), UP_THRESHOLD, context);
    }

    public static boolean hasDownThreshold() {
        return Utils.existFile(DOWN_THRESHOLD);
    }

    public static int getDownThreshold() {
        return Utils.strToInt(Utils.readFile(DOWN_THRESHOLD));
    }

    public static void setDownThreshold(int value, Context context) {
        run(Control.write(String.valueOf(value), DOWN_THRESHOLD), DOWN_THRESHOLD, context);
    }

    public static boolean hasPollingInterval() {
        return Utils.existFile(POLLING_INTERVAL);
    }

    public static int getPollingInterval() {
        return Utils.strToInt(Utils.readFile(POLLING_INTERVAL));
    }

    public static void setPollingInterval(int value, Context context) {
        run(Control.write(String.valueOf(value), POLLING_INTERVAL), POLLING_INTERVAL, context);
    }
    /* Clock Scaling: End */

    public static void setReadahead(Storage storage, int value, Context context) {
        run(Control.write(String.valueOf(value), getPath(storage, READ_AHEAD)),
                getPath(storage, READ_AHEAD), context);
    }

    public static int getReadahead(Storage storage) {
        return getReadahead(getPath(storage, READ_AHEAD));
    }

    public static boolean hasReadahead(Storage storage) {
        return Utils.existFile(getPath(storage, READ_AHEAD));
    }

    public static String getIOSched(Storage storage) {
        return getPath(storage, IOSCHED);
    }

    public static void setScheduler(Storage storage, String value, Context context) {
        run(Control.write(value, getPath(storage, SCHEDULER)), getPath(storage, SCHEDULER), context);
    }

    public static String getScheduler(Storage storage) {
        return getScheduler(getPath(storage, SCHEDULER));
    }

    public static List<String> getSchedulers(Storage storage) {
        return getSchedulers(getPath(storage, SCHEDULER));
    }

    public static boolean hasScheduler(Storage storage) {
        return Utils.existFile(getPath(storage, SCHEDULER));
    }

    public static boolean supported() {
        if (INTERNAL == null) {
            INTERNAL = exists(sInternal);
        }
        if (EXTERNAL == null) {
            if (isUFS()) {
                sExternal.add("/sys/block/mmcblk0/queue");
            }
            EXTERNAL = exists(sExternal);
            if (EXTERNAL != null && EXTERNAL.equals(INTERNAL)) {
                EXTERNAL = null;
            }
        }
        return INTERNAL != null;
    }

    private static boolean isUFS() {
        return Utils.existFile(UFS);
    }

    private static String getPath(Storage storage, String file) {
        return storage == Storage.Internal ? INTERNAL + "/" + file : EXTERNAL + "/" + file;
    }

    private static int getReadahead(String path) {
        return Utils.strToInt(Utils.readFile(path));
    }

    private static String getScheduler(String path) {
        String[] schedulers = Utils.readFile(path).split(" ");
        for (String scheduler : schedulers) {
            if (scheduler.startsWith("[") && scheduler.endsWith("]")) {
                return scheduler.replace("[", "").replace("]", "");
            }
        }
        return "";
    }

    private static List<String> getSchedulers(String path) {
        String[] schedulers = Utils.readFile(path).split(" ");
        List<String> list = new ArrayList<>();
        for (String scheduler : schedulers) {
            list.add(scheduler.replace("[", "").replace("]", ""));
        }
        return list;
    }

    private static String exists(List<String> files) {
        for (String file : files) {
            if (Utils.existFile(file)) {
                return file;
            }
        }
        return null;
    }

    private static void run(String command, String id, Context context) {
        Control.runSetting(command, ApplyOnBootFragment.IO, id, context);
    }

    public enum Storage {
        Internal,
        External
    }

}
