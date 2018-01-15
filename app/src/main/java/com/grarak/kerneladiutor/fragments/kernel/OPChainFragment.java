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
import com.grarak.kerneladiutor.utils.kernel.opchain.OPChain;
import com.grarak.kerneladiutor.views.recyclerview.CardView;
import com.grarak.kerneladiutor.views.recyclerview.RecyclerViewItem;
import com.grarak.kerneladiutor.views.recyclerview.SeekBarView;
import com.grarak.kerneladiutor.views.recyclerview.SwitchView;

import java.util.List;

/**
 * Created by willi on 26.06.16.
 */
public class OPChainFragment extends RecyclerViewFragment {

    @Override
    protected void init() {
        super.init();

        addViewPagerFragment(ApplyOnBootFragment.newInstance(this));
    }

    @Override
    protected void addItems(List<RecyclerViewItem> items) {
        if (OPChain.hasChainOn()) {
            chainOnInit(items);
        }
        if (OPChain.hasBoost()) {
            boostInit(items);
        }
        if (OPChain.hasBoostTL() || OPChain.hasBoostST()) {
            boostParameterInit(items);
        }
    }

    private void chainOnInit(List<RecyclerViewItem> items) {
        SwitchView chainOn = new SwitchView();
        chainOn.setTitle(getString(R.string.opchain_chain_on));
        chainOn.setSummary(getString(R.string.opchain_chain_on_summary));
        chainOn.setChecked(OPChain.isChainOnEnabled());
        chainOn.addOnSwitchListener(new SwitchView.OnSwitchListener() {
            @Override
            public void onChanged(SwitchView switchView, boolean isChecked) {
                OPChain.enableChainOn(isChecked, getActivity());
            }
        });

        items.add(chainOn);
    }

    private void boostInit(List<RecyclerViewItem> items) {
        SwitchView boost = new SwitchView();
        boost.setTitle(getString(R.string.opchain_boost));
        boost.setSummary(getString(R.string.opchain_boost_summary));
        boost.setChecked(OPChain.isBoostEnabled());
        boost.addOnSwitchListener(new SwitchView.OnSwitchListener() {
            @Override
            public void onChanged(SwitchView switchView, boolean isChecked) {
                OPChain.enableBoost(isChecked, getActivity());
            }
        });

        items.add(boost);
    }

    private void boostParameterInit(List<RecyclerViewItem> items) {
        CardView boostParameterCard = new CardView(getActivity());
        boostParameterCard.setTitle(getString(R.string.opchain_boost_parameter));

        if (OPChain.hasBoostTL()) {
            SeekBarView boostTL = new SeekBarView();
            boostTL.setTitle(getString(R.string.opchain_boost_tl));
            boostTL.setSummary(getString(R.string.opchain_boost_tl_summary));
            boostTL.setUnit(getString(R.string.pc));
            boostTL.setMax(100);
            boostTL.setMin(1);
            boostTL.setOffset(1);
            boostTL.setProgress(OPChain.getBoostTL() / 1 - 1);
            boostTL.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    OPChain.setBoostTL((position + 1) * 1, getActivity());
                }

                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }
            });

            boostParameterCard.addItem(boostTL);
        }

        if (OPChain.hasBoostST()) {
            SeekBarView boostST = new SeekBarView();
            boostST.setTitle(getString(R.string.opchain_boost_st));
            boostST.setSummary(getString(R.string.opchain_boost_st_summary));
            boostST.setUnit(getString(R.string.ms));
            boostST.setMax(50000);
            boostST.setMin(10000);
            boostST.setOffset(100);
            boostST.setProgress(OPChain.getBoostST() / 100 - 100);
            boostST.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    OPChain.setBoostST((position + 100) * 100, getActivity());
                }

                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }
            });

            boostParameterCard.addItem(boostST);
        }

        if (boostParameterCard.size() > 0) {
            items.add(boostParameterCard);
        }
    }

}