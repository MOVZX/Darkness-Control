package com.grarak.kerneladiutor.utils.kernel.gpu;

import android.content.Context;

import com.grarak.kerneladiutor.fragments.ApplyOnBootFragment;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.Control;

/**
 * Created by didntread on 6/20/2017.
 */

public class AdrenoBoost {
    private static final String AdrenoBoost = "/sys/class/devfreq/1c00000.qcom,kgsl-3d0/adrenoboost";

    public static void setAdrenoBoost(int value, Context context) {
        run(Control.write(String.valueOf(value), AdrenoBoost), AdrenoBoost, context);
    }

    public static int getAdrenoBoost() {
        return Utils.strToInt(Utils.readFile(AdrenoBoost));
    }

    public static boolean supported() {
        return Utils.existFile(AdrenoBoost);
    }

    private static void run(String command, String id, Context context) {
        Control.runSetting(command, ApplyOnBootFragment.GPU, id, context);
    }
}