package kz.optimabank.optima24.fragment.service_point;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.optimabank.optima24.R;
import kz.optimabank.optima24.activity.ServicePointDetails;
import kz.optimabank.optima24.controller.adapter.ATMListAdapter;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.Terminal;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.manager.GeneralManager;
import static kz.optimabank.optima24.utility.Utilities.getPreferences;

public class ServicePointList extends ATFFragment implements TextWatcher, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.lin_search) LinearLayout lin_search;
    public @BindView(R.id.edSearch) EditText edSearch;
    @BindView(R.id.clearSearch) ImageView clearSearch;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;

    ArrayList<Terminal> branches;
    ArrayList<Terminal> terminals;
    ArrayList<Terminal> atms;

    ArrayList<Terminal> items;
    ArrayList<Terminal> allTerminal = new ArrayList<>();
    ATMListAdapter adapter;
    SharedPreferences mSharedPreferences;
    ArrayList<Terminal> terminalsPod;
    public static boolean M500 = true,Mm1000 = true,M1000 = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.service_point_list, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        edSearch.addTextChangedListener(this);
        mSharedPreferences = getPreferences(getActivity());

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_color));
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edSearch.setText("");
            }
        });
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG,"setUserVisibleHint");
        setAdapter();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        if(isAdded()&& adapter!=null) {
            if (editable.length() == 0) {
                //adapter.updateList(allTerminal);
                if (items != null) {
                    items.clear();
                }
                setAdapter();
            } else {
                filter(editable.toString());
            }
        }
    }

    void filter(String text) {
        items = new ArrayList<>();
        for(Terminal terminal : allTerminal) {
            if(terminal.getAddress().toLowerCase().contains(text) || (terminal.getNote()!= null && terminal.getNote().toLowerCase().contains(text))
                || (terminal.getName() != null && terminal.getName().toLowerCase().contains(text) || (terminal.getWorkTime() != null && terminal.getWorkTime().toLowerCase().contains(text)))) {
                items.add(terminal);
            }
        }
        setAdapter();
    }

    private void setAdapter() {
        ServicePointList.M500 = true;
        ServicePointList.M1000 = true;
        ServicePointList.Mm1000 = true;

        Log.d(TAG,"setAdapter");
        if(isAdded()) {
            ArrayList<Terminal> terminalList;
            if (items!=null&&items.size()>0){
                terminalList = items;
                Log.d(TAG," terminals = items");
            }else {
                terminalList = getAllTerminal();
            }

            Collections.sort(terminalList, new Comparator<Terminal>() {
                public int compare(Terminal o1, Terminal o2) {
                    return o1.compareTo(o2);
                }
            });

            terminalsPod = new ArrayList<>();

            for (Terminal term : terminalList){

                if(term.getDistance()<=500){
                    Log.i("getDistance","getDistance = "+term.getDistance());
                    Log.i("M500","M500 = "+M500);
                    if (M500){
                        Terminal vrem = null;
                        try {
                            vrem = term.clone();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        vrem.setDistance(500123321);
                        terminalsPod.add(vrem);
                        M500 = false;
                    }
                    terminalsPod.add(term);
                }
                if(term.getDistance()<1000&&term.getDistance()>500){
                    if (Mm1000){
                        Log.i("term.getDistance","term.getDistance = "+term.getDistance());
                        Log.i("Mm1000","Mm1000 = "+Mm1000);
                        Terminal vrem = null;
                        try {
                            vrem = term.clone();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        vrem.setDistance(1100123321);
                        terminalsPod.add(vrem);
                        Mm1000 = false;
                    }
                    terminalsPod.add(term);
                }
                if(term.getDistance()>=1000){
                    if (ServicePointList.M1000){
                        Log.i("term.getDistance()","term.getDistance() = "+term.getDistance());
                        Log.i("M1000","M1000 = "+M1000);
                        Terminal vrem = null;
                        try {
                            vrem = term.clone();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        vrem.setDistance(1000123321);
                        terminalsPod.add(vrem);
                        M1000 = false;
                    }
                    terminalsPod.add(term);
                }

            }

            if(adapter == null) {
                adapter = new ATMListAdapter(getActivity(), terminalsPod, new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Terminal terminal;
                        /*if (items != null && !items.isEmpty()) {
                            terminal = items.get(position-1);
                            if (items.get(position-1).getDistance()==(terminal.getDistance())) {
                                terminal = items.get(position-1);
                            }else if (items.get(position-2).getDistance()==(terminal.getDistance())){
                                terminal = items.get(position-2);
                            } else {
                                terminal = items.get(position-3);
                            }
                        } else {*/
                            terminal = terminalsPod.get(position);
                            /*if (terminalsPod.get(position-1).getDistance()==(terminal.getDistance())) {
                                terminal = terminalsPod.get(position-1);
                            }else if (terminalsPod.get(position-2).getDistance()==(terminal.getDistance())){
                                terminal = terminalsPod.get(position-2);
                            } else {
                                terminal = terminalsPod.get(position-3);
                            }*/
                        //}
                        Intent intent = new Intent(getActivity(), ServicePointDetails.class);
                        intent.putExtra("isDetails", true);
                        intent.putExtra("terminal", terminal);
                        getActivity().startActivity(intent);
                    }
                });
                recyclerView.setAdapter(adapter);
            } else {
                Log.d(TAG,"updateList");
                adapter.updateList(terminalsPod);
            }
        }
    }

    private ArrayList<Terminal> getAllTerminal() {
        boolean isTerminalChecked = GeneralManager.isTerminalChecked();
        boolean isBranchesChecked =  GeneralManager.isBranchesChecked();
        boolean isAtmChecked = GeneralManager.isAtmChecked();
        ArrayList<Terminal> emptyList = new ArrayList<>();

        if(isTerminalChecked) {
            terminals = GeneralManager.getInstance().getTerminals();
        } else
            terminals = emptyList;
        if(isBranchesChecked) {
            branches = GeneralManager.getInstance().getBranches();
        } else
            branches = emptyList;
        if(isAtmChecked) {
            atms = GeneralManager.getInstance().getAtms();
        } else
            atms = emptyList;

        allTerminal.clear();

        if(terminals!=null && !terminals.isEmpty()) {
            allTerminal.addAll(terminals);
        }
        if(atms!=null && !atms.isEmpty()) {
            allTerminal.addAll(atms);
        }
        if(branches!=null && !branches.isEmpty()) {
            allTerminal.addAll(branches);
        }

        return allTerminal;
    }

    public void notifyDataChanged(){
        items = null;
        setAdapter();
    }

    @Override
    public void onRefresh() {
        edSearch.getText().clear();
        setAdapter();
        swipeRefreshLayout.setRefreshing(false);
    }
}