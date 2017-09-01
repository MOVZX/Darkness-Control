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
package com.grarak.kerneladiutor.utils.kernel.sound;

import android.content.Context;

import com.grarak.kerneladiutor.fragments.ApplyOnBootFragment;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.Control;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willi on 25.06.16.
 */
public class Sound {

    private static final String HIGHPERF_MODE_KENZO = "/sys/module/snd_soc_msm8x16_wcd/parameters/high_perf_mode";
    private static final String HIGHPERF_MODE_GEMINI = "/sys/module/snd_soc_wcd9330/parameters/high_perf_mode";
    private static final String CODEC_POWER_GATING_KENZO = "/sys/module/snd_soc_msm8x16_wcd/parameters/dig_core_collapse_enable";
    private static final String CODEC_POWER_GATING_GEMINI = "/sys/module/snd_soc_wcd9335/parameters/dig_core_collapse_enable";

    /* FKSC: Start */
    private static final String FKSC_HEADPHONE_GAIN = "/sys/devices/virtual/misc/soundcontrol/volume_boost";
    private static final String FKSC_SPEAKER_GAIN = "/sys/devices/virtual/misc/soundcontrol/speaker_boost";
    private static final String FKSC_EARPIECE_GAIN = "/sys/devices/virtual/misc/soundcontrol/earpiece_boost";
    private static final String FKSC_MICROPHONE_GAIN = "/sys/devices/virtual/misc/soundcontrol/mic_boost";
    /* FKSC: End */

    /* EXSC: Start */
    private static final String EXSC_HEADPHONE_GAIN = "/sys/kernel/sound_control/headphone_gain";
    private static final String EXSC_SPEAKER_GAIN = "/sys/kernel/sound_control/speaker_gain";
    private static final String EXSC_EARPIECE_GAIN = "/sys/kernel/sound_control/earpiece_gain";
    private static final String EXSC_MICROPHONE_GAIN = "/sys/kernel/sound_control/mic_gain";
    /* EXSC: End */

    private static final List<String> sHighPerfFiles = new ArrayList<>();
    private static final List<String> sPowerGating = new ArrayList<>();
    private static final List<String> sSpeakerGainFiles = new ArrayList<>();
    private static final List<String> sFKSCLimits = new ArrayList<>();
    private static final List<String> sEXSC1Limits = new ArrayList<>();
    private static final List<String> sEXSC2Limits = new ArrayList<>();
    private static String HIGHPERF_MODE;
    private static String CODEC_POWER_GATING;
    private static String SPEAKER_GAIN_FILE;

    static {
        sHighPerfFiles.add(HIGHPERF_MODE_KENZO);
        sHighPerfFiles.add(HIGHPERF_MODE_GEMINI);
    }

    static {
        sPowerGating.add(CODEC_POWER_GATING_KENZO);
        sPowerGating.add(CODEC_POWER_GATING_GEMINI);
    }

    static {
        sSpeakerGainFiles.add(FKSC_SPEAKER_GAIN);
        sSpeakerGainFiles.add(EXSC_SPEAKER_GAIN);
    }

    static {
        for (int i = -20; i < 21; i++) {
            sFKSCLimits.add(String.valueOf(i));
        }
        for (int i = -10; i < 21; i++) {
            sEXSC1Limits.add(String.valueOf(i));
        }
        for (int i = -84; i < 21; i++) {
            sEXSC2Limits.add(String.valueOf(i));
        }
    }

    /* Headset High Performance Mode */
    public static void enableHighPerfMode(boolean enable, Context context) {
        switch (HIGHPERF_MODE) {
            case HIGHPERF_MODE_KENZO:
                run(Control.write(enable ? "1" : "0", HIGHPERF_MODE_KENZO), HIGHPERF_MODE_KENZO, context);
                break;
            case HIGHPERF_MODE_GEMINI:
                run(Control.write(enable ? "1" : "0", HIGHPERF_MODE_GEMINI), HIGHPERF_MODE_GEMINI, context);
                break;
        }
    }

    public static boolean isHighPerfMode() {
        switch (HIGHPERF_MODE) {
            case HIGHPERF_MODE_KENZO:
                return Utils.readFile(HIGHPERF_MODE_KENZO).equals("1");
            case HIGHPERF_MODE_GEMINI:
                return Utils.readFile(HIGHPERF_MODE_GEMINI).equals("1");
        }
        return false;
    }

    public static boolean hasHighPerfMode() {
        if (HIGHPERF_MODE == null) {
            for (String file : sHighPerfFiles)
                if (Utils.existFile(file)) {
                    HIGHPERF_MODE = file;
                    return true;
                }
        }
        return HIGHPERF_MODE != null;
    }

    /* Audio Codec Power Gating */
    public static void enableCodecPowerGating(boolean enable, Context context) {
        switch (CODEC_POWER_GATING) {
            case CODEC_POWER_GATING_KENZO:
                run(Control.write(enable ? "1" : "0", CODEC_POWER_GATING_KENZO), CODEC_POWER_GATING_KENZO, context);
                break;
            case CODEC_POWER_GATING_GEMINI:
                run(Control.write(enable ? "1" : "0", CODEC_POWER_GATING_GEMINI), CODEC_POWER_GATING_GEMINI, context);
                break;
        }
    }

    public static boolean isCodecPowerGating() {
        switch (CODEC_POWER_GATING) {
            case CODEC_POWER_GATING_KENZO:
                return Utils.readFile(CODEC_POWER_GATING_KENZO).equals("1");
            case CODEC_POWER_GATING_GEMINI:
                return Utils.readFile(CODEC_POWER_GATING_GEMINI).equals("1");
        }
        return false;
    }

    public static boolean hasCodecPowerGating() {
        if (CODEC_POWER_GATING == null) {
            for (String file : sPowerGating)
                if (Utils.existFile(file)) {
                    CODEC_POWER_GATING = file;
                    return true;
                }
        }
        return CODEC_POWER_GATING != null;
    }

    /* FKSC: Start */
    public static void setFKSCVolumeGain(String value, Context context) {
        run(Control.write(value, FKSC_HEADPHONE_GAIN), FKSC_HEADPHONE_GAIN, context);
    }

    public static String getFKSCVolumeGain() {
        return Utils.readFile(FKSC_HEADPHONE_GAIN);
    }

    public static List<String> getFKSCVolumeGainLimits() {
        return sFKSCLimits;
    }

    public static boolean hasFKSCVolumeGain() {
        return Utils.existFile(FKSC_HEADPHONE_GAIN);
    }

    public static void setFKSCEarpieceGain(String value, Context context) {
        run(Control.write(value, FKSC_EARPIECE_GAIN), FKSC_EARPIECE_GAIN, context);
    }

    public static String getFKSCEarpieceGain() {
        return Utils.readFile(FKSC_EARPIECE_GAIN);
    }

    public static List<String> getFKSCEarpieceGainLimits() {
        return sFKSCLimits;
    }

    public static boolean hasFKSCEarpieceGain() {
        return Utils.existFile(FKSC_EARPIECE_GAIN);
    }

    public static void setFKSCMicrophoneGain(String value, Context context) {
        run(Control.write(value, FKSC_MICROPHONE_GAIN), FKSC_MICROPHONE_GAIN, context);
    }

    public static String getFKSCMicrophoneGain() {
        return Utils.readFile(FKSC_MICROPHONE_GAIN);
    }

    public static List<String> getFKSCMicrophoneGainLimits() {
        return sFKSCLimits;
    }

    public static boolean hasFKSCMicrophoneGain() {
        return Utils.existFile(FKSC_MICROPHONE_GAIN);
    }
    /* FKSC: End */

    /* EXSC: Start */
    public static void setEXSCVolumeGain(String value, Context context) {
        int newGain = Utils.strToInt(value);
        if (newGain >= -84 && newGain <= 20) {
            run(Control.write(value + " " + value, EXSC_HEADPHONE_GAIN), EXSC_HEADPHONE_GAIN, context);
        }
    }

    public static String getEXSCVolumeGain() {
        String value = Utils.readFile(EXSC_HEADPHONE_GAIN);
        int gain = Utils.strToInt(value.contains(" ") ? value.split(" ")[0] : value);
        if (gain >= 0 && gain <= 20) {
            return String.valueOf(gain);
        } else if (gain >= 216 && gain <= 255) {
            return String.valueOf(gain - 256);
        }
        return "";
    }

    public static List<String> getEXSCVolumeGainLimits() {
        return sEXSC2Limits;
    }

    public static boolean hasEXSCVolumeGain() {
        return Utils.existFile(EXSC_HEADPHONE_GAIN);
    }

    public static void setEXSCEarpieceGain(String value, Context context) {
        run(Control.write(value, EXSC_EARPIECE_GAIN), EXSC_EARPIECE_GAIN, context);
    }

    public static String getEXSCEarpieceGain() {
        return Utils.readFile(EXSC_EARPIECE_GAIN);
    }

    public static List<String> getEXSCEarpieceGainLimits() {
        return sEXSC1Limits;
    }

    public static boolean hasEXSCEarpieceGain() {
        return Utils.existFile(EXSC_EARPIECE_GAIN);
    }

    public static void setEXSCMicrophoneGain(String value, Context context) {
        run(Control.write(value, EXSC_MICROPHONE_GAIN), EXSC_MICROPHONE_GAIN, context);
    }

    public static String getEXSCMicrophoneGain() {
        return Utils.readFile(EXSC_MICROPHONE_GAIN);
    }

    public static List<String> getEXSCMicrophoneGainLimits() {
        return sEXSC1Limits;
    }

    public static boolean hasEXSCMicrophoneGain() {
        return Utils.existFile(EXSC_MICROPHONE_GAIN);
    }
    /* EXSC: End */

    public static void setSpeakerGain(String value, Context context) {
        switch (SPEAKER_GAIN_FILE) {
            case FKSC_SPEAKER_GAIN:
                run(Control.write(value, FKSC_SPEAKER_GAIN), FKSC_SPEAKER_GAIN, context);
                break;
            case EXSC_SPEAKER_GAIN:
                run(Control.write(value, EXSC_SPEAKER_GAIN), EXSC_SPEAKER_GAIN, context);
                break;
        }
    }

    public static String getSpeakerGain() {
        switch (SPEAKER_GAIN_FILE) {
            case FKSC_SPEAKER_GAIN:
                return Utils.readFile(FKSC_SPEAKER_GAIN);
            case EXSC_SPEAKER_GAIN:
                return Utils.readFile(EXSC_SPEAKER_GAIN);
        }
        return "";
    }

    public static List<String> getSpeakerGainLimits() {
        switch (SPEAKER_GAIN_FILE) {
            case FKSC_SPEAKER_GAIN:
                return sFKSCLimits;
            case EXSC_SPEAKER_GAIN:
                return sEXSC1Limits;
        }
        return new ArrayList<>();
    }

    public static boolean hasSpeakerGain() {
        if (SPEAKER_GAIN_FILE == null) {
            for (String file : sSpeakerGainFiles)
                if (Utils.existFile(file)) {
                    SPEAKER_GAIN_FILE = file;
                    return true;
                }
        }
        return SPEAKER_GAIN_FILE != null;
    }

    public static boolean supported() {
        return hasHighPerfMode() ||
                hasCodecPowerGating() ||

                /* FKSC: Start */
                hasFKSCVolumeGain() ||
                hasFKSCEarpieceGain() ||
                hasFKSCMicrophoneGain() ||
                /* FKSC: End */

                /* EXSC: Start */
                hasEXSCVolumeGain() ||
                hasEXSCEarpieceGain() ||
                hasEXSCMicrophoneGain() ||
                /* EXSC: End */

                hasSpeakerGain();
    }

    private static void run(String command, String id, Context context) {
        Control.runSetting(command, ApplyOnBootFragment.SOUND, id, context);
    }

}