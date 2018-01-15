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
package com.grarak.kerneladiutor.utils.kernel.opchain;

import android.content.Context;

import com.grarak.kerneladiutor.fragments.ApplyOnBootFragment;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.Control;

/**
 * Created by willi on 26.06.16.
 */
public class OPChain {

    private static final String OPCHAIN = "/sys/module/opchain/parameters/";
    private static final String CHAIN_ON = OPCHAIN + "chain_on";
    private static final String BOOST = OPCHAIN + "boost";
    private static final String BOOST_TL = OPCHAIN + "boost_tl";
    private static final String BOOST_ST = OPCHAIN + "boost_sample_time";

    /* Chain On/Off */
    public static boolean hasChainOn() {
        return Utils.existFile(CHAIN_ON);
    }

    public static void enableChainOn(boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", CHAIN_ON), CHAIN_ON, context);
    }

    public static boolean isChainOnEnabled() {
        return Utils.readFile(CHAIN_ON).equals("1");
    }

    /* Boost On/Off */
    public static boolean hasBoost() {
        return Utils.existFile(BOOST);
    }

    public static void enableBoost(boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", BOOST), BOOST, context);
    }

    public static boolean isBoostEnabled() {
        return Utils.readFile(BOOST).equals("1");
    }

    /* Boost Target Load */
    public static boolean hasBoostTL() {
        return Utils.existFile(BOOST_TL);
    }

    public static void setBoostTL(int pages, Context context) {
        run(Control.write(String.valueOf(pages), BOOST_TL), BOOST_TL, context);
    }

    public static int getBoostTL() {
        return Utils.strToInt(Utils.readFile(BOOST_TL));
    }

    /* Boost Sample Time */
    public static boolean hasBoostST() {
        return Utils.existFile(BOOST_ST);
    }

    public static void setBoostST(int pages, Context context) {
        run(Control.write(String.valueOf(pages), BOOST_ST), BOOST_ST, context);
    }

    public static int getBoostST() {
        return Utils.strToInt(Utils.readFile(BOOST_ST));
    }

    public static boolean supported() {
        return hasChainOn() || hasBoost() || hasBoostTL() || hasBoostST();
    }

    private static void run(String command, String id, Context context) {
        Control.runSetting(command, ApplyOnBootFragment.OPCHAIN, id, context);
    }

}