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
package com.grarak.kerneladiutor.fragments.kernel;

import com.grarak.kerneladiutor.R;
import com.grarak.kerneladiutor.fragments.ApplyOnBootFragment;
import com.grarak.kerneladiutor.fragments.RecyclerViewFragment;
import com.grarak.kerneladiutor.utils.kernel.sound.Sound;
import com.grarak.kerneladiutor.views.recyclerview.RecyclerViewItem;
import com.grarak.kerneladiutor.views.recyclerview.SeekBarView;
import com.grarak.kerneladiutor.views.recyclerview.SwitchView;
import com.grarak.kerneladiutor.views.recyclerview.TitleView;

import java.util.List;

/**
 * Created by willi on 26.06.16.
 */
public class SoundFragment extends RecyclerViewFragment {

    @Override
    protected void init() {
        super.init();

        addViewPagerFragment(ApplyOnBootFragment.newInstance(this));
    }

    @Override
    protected void addItems(List<RecyclerViewItem> items) {
        /* Headset High Performance Mode */
        if (Sound.hasHighPerfMode()) {
            highPerfModeEnableInit(items);
        }

        /* Audio Codec Power Gating */
        if (Sound.hasCodecPowerGating()) {
            codecPowerGatingEnableInit(items);
        }

        if (Sound.hasSpeakerGain()) {
            speakerGainInit(items);
        }

        /* FKSC: Start */
        if (Sound.hasFKSCVolumeGain()) {
            fkscvolumeGainInit(items);
        }
        if (Sound.hasFKSCEarpieceGain()) {
            fkscearpieceGainInit(items);
        }
        if (Sound.hasFKSCMicrophoneGain()) {
            fkscmicrophoneGainInit(items);
        }
        /* FKSC: End */

        /* EXSC: Start */
        if (Sound.hasEXSCVolumeGain()) {
            exscvolumeGainInit(items);
        }
        if (Sound.hasEXSCEarpieceGain()) {
            exscearpieceGainInit(items);
        }
        if (Sound.hasEXSCMicrophoneGain()) {
            exscmicrophoneGainInit(items);
        }
        /* EXSC: End */
    }

    /* Headset High Performance Mode */
    private void highPerfModeEnableInit(List<RecyclerViewItem> items) {
        SwitchView highPerfMode = new SwitchView();
        highPerfMode.setSummary(getString(R.string.headset_highperf_mode));
        highPerfMode.setChecked(Sound.isHighPerfMode());
        highPerfMode.addOnSwitchListener(new SwitchView.OnSwitchListener() {
            @Override
            public void onChanged(SwitchView switchView, boolean isChecked) {
                Sound.enableHighPerfMode(isChecked, getActivity());
            }
        });

        items.add(highPerfMode);
    }

    /* Audio Codec Power Gating */
    private void codecPowerGatingEnableInit(List<RecyclerViewItem> items) {
        SwitchView codecPowerGating = new SwitchView();
        codecPowerGating.setSummary(getString(R.string.codec_power_gating));
        codecPowerGating.setChecked(Sound.isCodecPowerGating());
        codecPowerGating.addOnSwitchListener(new SwitchView.OnSwitchListener() {
            @Override
            public void onChanged(SwitchView switchView, boolean isChecked) {
                Sound.enableCodecPowerGating(isChecked, getActivity());
            }
        });

        items.add(codecPowerGating);
    }

    /* FKSC: Start */
    private void fkscvolumeGainInit(List<RecyclerViewItem> items) {
        TitleView fksc_title = new TitleView();
        fksc_title.setText(getString(R.string.fksc_title));

        SeekBarView fkscvolumeGain = new SeekBarView();
        fkscvolumeGain.setTitle(getString(R.string.headphone_gain));
        fkscvolumeGain.setItems(Sound.getFKSCVolumeGainLimits());
        fkscvolumeGain.setProgress(Sound.getFKSCVolumeGainLimits().indexOf(Sound.getFKSCVolumeGain()));
        fkscvolumeGain.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
            @Override
            public void onStop(SeekBarView seekBarView, int position, String value) {
                Sound.setFKSCVolumeGain(value, getActivity());
            }

            @Override
            public void onMove(SeekBarView seekBarView, int position, String value) {
            }
        });

        items.add(fksc_title);
        items.add(fkscvolumeGain);
    }

    private void fkscearpieceGainInit(List<RecyclerViewItem> items) {
        SeekBarView fkscearpieceGain = new SeekBarView();
        fkscearpieceGain.setTitle(getString(R.string.earpiece_gain));
        fkscearpieceGain.setItems(Sound.getFKSCEarpieceGainLimits());
        fkscearpieceGain.setProgress(Sound.getFKSCEarpieceGainLimits().indexOf(Sound.getFKSCEarpieceGain()));
        fkscearpieceGain.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
            @Override
            public void onStop(SeekBarView seekBarView, int position, String value) {
                Sound.setFKSCEarpieceGain(value, getActivity());
            }

            @Override
            public void onMove(SeekBarView seekBarView, int position, String value) {
            }
        });

        items.add(fkscearpieceGain);
    }

    private void fkscmicrophoneGainInit(List<RecyclerViewItem> items) {
        SeekBarView fkscmicrophoneGain = new SeekBarView();
        fkscmicrophoneGain.setTitle(getString(R.string.microphone_gain));
        fkscmicrophoneGain.setItems(Sound.getFKSCMicrophoneGainLimits());
        fkscmicrophoneGain.setProgress(Sound.getFKSCMicrophoneGainLimits().indexOf(Sound.getFKSCMicrophoneGain()));
        fkscmicrophoneGain.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
            @Override
            public void onStop(SeekBarView seekBarView, int position, String value) {
                Sound.setFKSCMicrophoneGain(value, getActivity());
            }

            @Override
            public void onMove(SeekBarView seekBarView, int position, String value) {
            }
        });

        items.add(fkscmicrophoneGain);
    }
    /* FKSC: End */

    /* EXSC: Start */
    private void exscvolumeGainInit(List<RecyclerViewItem> items) {
        TitleView exsc_title = new TitleView();
        exsc_title.setText(getString(R.string.exsc_title));

        SeekBarView exscvolumeGain = new SeekBarView();
        exscvolumeGain.setTitle(getString(R.string.headphone_gain));
        exscvolumeGain.setItems(Sound.getEXSCVolumeGainLimits());
        exscvolumeGain.setProgress(Sound.getEXSCVolumeGainLimits().indexOf(Sound.getEXSCVolumeGain()));
        exscvolumeGain.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
            @Override
            public void onStop(SeekBarView seekBarView, int position, String value) {
                Sound.setEXSCVolumeGain(value, getActivity());
            }

            @Override
            public void onMove(SeekBarView seekBarView, int position, String value) {
            }
        });

        items.add(exsc_title);
        items.add(exscvolumeGain);
    }

    private void exscearpieceGainInit(List<RecyclerViewItem> items) {
        SeekBarView exscearpieceGain = new SeekBarView();
        exscearpieceGain.setTitle(getString(R.string.earpiece_gain));
        exscearpieceGain.setItems(Sound.getEXSCEarpieceGainLimits());
        exscearpieceGain.setProgress(Sound.getEXSCEarpieceGainLimits().indexOf(Sound.getEXSCEarpieceGain()));
        exscearpieceGain.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
            @Override
            public void onStop(SeekBarView seekBarView, int position, String value) {
                Sound.setEXSCEarpieceGain(value, getActivity());
            }

            @Override
            public void onMove(SeekBarView seekBarView, int position, String value) {
            }
        });

        items.add(exscearpieceGain);
    }

    private void exscmicrophoneGainInit(List<RecyclerViewItem> items) {
        SeekBarView exscmicrophoneGain = new SeekBarView();
        exscmicrophoneGain.setTitle(getString(R.string.microphone_gain));
        exscmicrophoneGain.setItems(Sound.getEXSCMicrophoneGainLimits());
        exscmicrophoneGain.setProgress(Sound.getEXSCMicrophoneGainLimits().indexOf(Sound.getEXSCMicrophoneGain()));
        exscmicrophoneGain.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
            @Override
            public void onStop(SeekBarView seekBarView, int position, String value) {
                Sound.setEXSCMicrophoneGain(value, getActivity());
            }

            @Override
            public void onMove(SeekBarView seekBarView, int position, String value) {
            }
        });

        items.add(exscmicrophoneGain);
    }
    /* EXSC: End */

    private void speakerGainInit(List<RecyclerViewItem> items) {
        SeekBarView speakerGain = new SeekBarView();
        speakerGain.setTitle(getString(R.string.speaker_gain));
        speakerGain.setItems(Sound.getSpeakerGainLimits());
        speakerGain.setProgress(Sound.getSpeakerGainLimits().indexOf(Sound.getSpeakerGain()));
        speakerGain.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
            @Override
            public void onStop(SeekBarView seekBarView, int position, String value) {
                Sound.setSpeakerGain(value, getActivity());
            }

            @Override
            public void onMove(SeekBarView seekBarView, int position, String value) {
            }
        });

        items.add(speakerGain);
    }
}