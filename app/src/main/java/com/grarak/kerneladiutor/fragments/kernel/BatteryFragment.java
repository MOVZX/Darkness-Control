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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.grarak.kerneladiutor.R;
import com.grarak.kerneladiutor.fragments.ApplyOnBootFragment;
import com.grarak.kerneladiutor.fragments.DescriptionFragment;
import com.grarak.kerneladiutor.fragments.RecyclerViewFragment;
import com.grarak.kerneladiutor.utils.kernel.battery.Battery;
import com.grarak.kerneladiutor.views.recyclerview.CardView;
import com.grarak.kerneladiutor.views.recyclerview.DescriptionView;
import com.grarak.kerneladiutor.views.recyclerview.RecyclerViewItem;
import com.grarak.kerneladiutor.views.recyclerview.SeekBarView;
import com.grarak.kerneladiutor.views.recyclerview.StatsView;
import com.grarak.kerneladiutor.views.recyclerview.SwitchView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willi on 26.06.16.
 */
public class BatteryFragment extends RecyclerViewFragment {

    private static double sBatteryTemperature;
    private static int sBatteryLevel;
    private static int sBatteryVoltage;
    private List<DescriptionView> mInfos = new ArrayList<>();
    private StatsView mTemperature;
    private StatsView mLevel;
    private StatsView mVoltage;
    private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sBatteryTemperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10D;
            sBatteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            sBatteryVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
        }
    };

    @Override
    protected void addItems(List<RecyclerViewItem> items) {
        temperatureInit(items);
        levelInit(items);
        voltageInit(items);
        infoInit(items);
        if (Battery.hasChargingSwitch()) {
            chargingSwitchInit(items);
        }
        if (Battery.hasForceFastCharge()) {
            forceFastChargeInit(items);
        }
        chargeRateInit(items);
        batteryCurrentLimitInit(items);
    }

    @Override
    protected void postInit() {
        super.postInit();

        if (itemsSize() > 2) {
            addViewPagerFragment(ApplyOnBootFragment.newInstance(this));
        }
        if (Battery.hasCapacity(getActivity())) {
            addViewPagerFragment(DescriptionFragment.newInstance(getString(R.string.capacity),
                    Battery.getCapacity(getActivity()) + getString(R.string.mah)));
        }
    }

    private void temperatureInit(List<RecyclerViewItem> items) {
        mTemperature = new StatsView();
        mTemperature.setTitle(getString(R.string.temperature));

        items.add(mTemperature);
    }

    private void levelInit(List<RecyclerViewItem> items) {
        mLevel = new StatsView();
        mLevel.setTitle(getString(R.string.level));

        items.add(mLevel);
    }

    private void voltageInit(List<RecyclerViewItem> items) {
        mVoltage = new StatsView();
        mVoltage.setTitle(getString(R.string.voltage));

        items.add(mVoltage);
    }

    private void chargingSwitchInit(List<RecyclerViewItem> items) {
        SwitchView chargingSwitch = new SwitchView();
        chargingSwitch.setTitle(getString(R.string.charging_switch));
        chargingSwitch.setSummary(getString(R.string.charging_switch_summary));
        chargingSwitch.setChecked(Battery.isChargingSwitch());
        chargingSwitch.addOnSwitchListener(new SwitchView.OnSwitchListener() {
            @Override
            public void onChanged(SwitchView switchView, boolean isChecked) {
                Battery.enableChargingSwitch(isChecked, getActivity());
            }
        });

        items.add(chargingSwitch);
    }

    private void forceFastChargeInit(List<RecyclerViewItem> items) {
        SwitchView forceFastCharge = new SwitchView();
        forceFastCharge.setTitle(getString(R.string.usb_fast_charge));
        forceFastCharge.setSummary(getString(R.string.usb_fast_charge_summary));
        forceFastCharge.setChecked(Battery.isForceFastChargeEnabled());
        forceFastCharge.addOnSwitchListener(new SwitchView.OnSwitchListener() {
            @Override
            public void onChanged(SwitchView switchView, boolean isChecked) {
                Battery.enableForceFastCharge(isChecked, getActivity());
            }
        });

        items.add(forceFastCharge);
    }

    private void batteryCurrentLimitInit(List<RecyclerViewItem> items) {
        CardView batteryCurrentLimitCard = new CardView(getActivity());
        batteryCurrentLimitCard.setTitle(getString(R.string.battery_current_limit));

        if (Battery.hasBatteryCurrentLimit()) {
            SeekBarView batteryCurrentLimit = new SeekBarView();
            batteryCurrentLimit.setTitle(getString(R.string.low_battery_value));
            batteryCurrentLimit.setSummary(getString(R.string.low_battery_value_summary));
            batteryCurrentLimit.setUnit(getString(R.string.pc));
            batteryCurrentLimit.setMax(100);
            batteryCurrentLimit.setMin(1);
            batteryCurrentLimit.setOffset(1);
            batteryCurrentLimit.setProgress(Battery.getBatteryCurrentLimit() / 1 - 1);
            batteryCurrentLimit.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    Battery.setBatteryCurrentLimit((position + 1) * 1, getActivity());
                }

                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }
            });

            batteryCurrentLimitCard.addItem(batteryCurrentLimit);
        }

        if (batteryCurrentLimitCard.size() > 0) {
            items.add(batteryCurrentLimitCard);
        }
    }

    private void chargeRateInit(List<RecyclerViewItem> items) {
        CardView chargeRateCard = new CardView(getActivity());
        chargeRateCard.setTitle(getString(R.string.charge_rate));

        if (Battery.hasChargingCurrent()) {
            SeekBarView chargingCurrent = new SeekBarView();
            chargingCurrent.setTitle(getString(R.string.charging_current));
            chargingCurrent.setSummary(getString(R.string.charging_current_summary));
            chargingCurrent.setUnit(getString(R.string.ma));
            chargingCurrent.setMax(3000);
            chargingCurrent.setMin(100);
            chargingCurrent.setOffset(10);
            chargingCurrent.setProgress(Battery.getChargingCurrent() / 10 - 10);
            chargingCurrent.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    Battery.setChargingCurrent((position + 10) * 10, getActivity());
                }

                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }
            });

            chargeRateCard.addItem(chargingCurrent);
        }

        if (chargeRateCard.size() > 0) {
            items.add(chargeRateCard);
        }
    }

    @Override
    protected void refresh() {
        super.refresh();
        if (mTemperature != null) {
            mTemperature.setStat(sBatteryTemperature + getString(R.string.celsius));
        }
        if (mLevel != null) {
            mLevel.setStat(sBatteryLevel + getString(R.string.pc));
        }
        if (mVoltage != null) {
            mVoltage.setStat(sBatteryVoltage + getString(R.string.mv));
        }
        if (mInfos.size() > 0) {
            for (int i = 0; i < mInfos.size(); i++) {
                mInfos.get(i).setSummary(Battery.getInfo(i));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            getActivity().unregisterReceiver(mBatteryReceiver);
        } catch (IllegalArgumentException ignored) {
        }
    }

    private void infoInit(List<RecyclerViewItem> items) {
        mInfos.clear();
        for (int i = 0; i < Battery.getInfosSize(); i++) {
            if (Battery.hasInfo(i)) {
                DescriptionView info = new DescriptionView();
                info.setTitle(Battery.getInfoText(i, getActivity()));

                items.add(info);
                mInfos.add(info);
            }
        }
    }
}