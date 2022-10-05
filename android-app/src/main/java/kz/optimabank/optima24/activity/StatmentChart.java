/*
package kz.optimabank.optima24.activity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.controller.adapter.ChartAdapter;
import kz.optimabank.optima24.model.base.ATFStatement;
import kz.optimabank.optima24.model.base.Category;
import kz.optimabank.optima24.model.base.Chart;
import kz.optimabank.optima24.model.interfaces.Accounts;
import kz.optimabank.optima24.model.interfaces.Categories;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.AccountsImpl;
import kz.optimabank.optima24.model.service.CategoriesImpl;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

*/
/**
 * Created by Max on 27.11.2017.
 *//*


public class StatmentChart extends OptimaActivity implements AccountsImpl.Callback, CategoriesImpl.Callback{

    @BindView(R.id.chart1) PieChart mChart;
    @BindView(R.id.chart2) PieChart mChart2;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tvTitle) TextView tvTitle;

    static ArrayList<Category> categories;
    List<Float> doubleList = new ArrayList<>();
    List<String> stringList = new ArrayList<>();
    List<String> currencyList = new ArrayList<>();
    ArrayList<Chart> chartList = new ArrayList<>();
    static ArrayList<Chart> chartListD = new ArrayList<>();
    static ArrayList<Chart> chartListP = new ArrayList<>();
    static ArrayList<Chart> chartListPP = new ArrayList<>();
    static ArrayList<Integer> colorsAdap = new ArrayList<Integer>();
    static ArrayList<Integer> colorsAdapItog = new ArrayList<Integer>();
    static ArrayList<Integer> colorsAdapFC = new ArrayList<Integer>();
    static ArrayList<Chart> chartListITC = new ArrayList<>();
    ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
    int itog;

    Accounts accounts;
    Categories category;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_list);
        ButterKnife.bind(this);
        initToolbar();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChart.setNoDataText(getResources().getString(R.string.loading));
        mChart.setUsePercentValues(false);
        mChart2.setHoleGradient(getResources().getColor(R.color.white), getResources().getColor(R.color.gray_chart));
        //mChart.setHoleGradient(getResources().getColor(R.color.white),getResources().getColor(R.color.gray_chart));
        mChart2.setUsePercentValues(false);
        mChart2.setNoDataText("");
        if (MenuActivity.screenInches>5) {
            mChart2.setExtraOffsets(21.5f, 21.5f, 21.5f, 21.5f);
        }else{
            mChart2.setExtraOffsets(22, 22, 22, 22);
        }
        //mChart.invalidate();

        if (accounts==null)
            accounts = new AccountsImpl();
        accounts.registerCallBack(this);

        if (category==null)
            category = new CategoriesImpl();
        category.registerCallBack(this);

        if(GeneralManager.getInstance().getCategories().isEmpty()) {
            category.getCategories(this,false);
        }else{
            categories = GeneralManager.getInstance().getCategories();
        }

        if(GeneralManager.getInstance().getStatements().isEmpty()) {
            accounts.getAccountsOperations(this);
        } else {
            initChart();
        }

    }

    private void initChart(){
        final ArrayList<ATFStatement> statementsP = GeneralManager.getInstance().getStatements();
        final ArrayList<ATFStatement> statements = new ArrayList<>();
        //Log.i("initChart","statements = "+statements.size());
        if(!statementsP.isEmpty()) {
            for (ATFStatement statement:statementsP){
                if (statement.baseAmount<0){
                    statements.add(statement);
                }
            }
            Collections.sort(statements, new Comparator<ATFStatement>() {
                @Override
                public int compare(ATFStatement o1, ATFStatement o2) {
                    return String.valueOf(o1.categoryId).compareTo(String.valueOf(o2.categoryId));
                }
            });
            doubleList.clear();
            stringList.clear();
            currencyList.clear();
            float amount = 0;
            String name = "";
            String curr = "";
            */
/*for(int i = 0; i < statements.size(); i++) {
                if(i+1!=statements.size()) {
                    //Log.i("name","name = "+statements.get(i).categoryId);
                    if (!String.valueOf(statements.get(i).categoryId).equals(String.valueOf(statements.get(i + 1).categoryId))) {
                        amount += statements.get(i).baseAmount;
                        doubleList.add(amount);
                        amount = 0;
                        if (!name.equals("")){
                            stringList.add(name);
                            currencyList.add(curr);
                            curr = "";
                            name = "";
                        }else{
                            stringList.add(String.valueOf(statements.get(i).categoryId));
                            name = "";
                            currencyList.add(String.valueOf(statements.get(i).currency));
                            curr = "";
                        }
                        //Log.i("categor","ID = "+statements.get(i).id);
                        //Log.i("categor","categoryId = "+statements.get(i).categoryId);
                    } else {
                        if (name.equals(""))
                            name = String.valueOf(statements.get(i).categoryId);
                        if (curr.equals(""))
                            curr = String.valueOf(statements.get(i).currency);
                        amount += statements.get(i).baseAmount;
                    }
                } else {
                    amount += statements.get(i).baseAmount;
                    doubleList.add(amount);
                    amount = 0;
                    if (!name.equals("")){
                        stringList.add(name);
                        name = "";
                        currencyList.add(curr);
                        curr = "";
                    }else{
                        stringList.add(String.valueOf(statements.get(i).categoryId));
                        name = "";
                        currencyList.add(String.valueOf(statements.get(i).currency));
                        curr = "";
                    }
                    //Log.i("categor","ID = "+statements.get(i).id);
                    //Log.i("categor","categoryId = "+statements.get(i).categoryId);
                }
            }*//*



            for(int i = 0; i < statements.size(); i++) {
                if(i+1!=statements.size()) {
                    if (!String.valueOf(statements.get(i).categoryId).equals(String.valueOf(statements.get(i + 1).categoryId))) {
                        amount += statements.get(i).baseAmount;
                        doubleList.add(amount);
                        amount = 0;
                        stringList.add(String.valueOf(statements.get(i).categoryId));
                        currencyList.add(String.valueOf(statements.get(i).currency));
                    } else {
                        amount += statements.get(i).baseAmount;
                    }
                } else {
                    amount += statements.get(i).baseAmount;
                    doubleList.add(amount);
                    amount = 0;
                    stringList.add(String.valueOf(statements.get(i).categoryId));
                    currencyList.add(String.valueOf(statements.get(i).currency));
                }
            }


                //setChart(doubleList,stringList);//TODO -------------------
            Log.i("categor","stringList.size = "+stringList.size());
            Log.i("categor","doubleList.size = "+doubleList.size());
                chartList = returnItogList(stringList,doubleList,currencyList);
            Log.i("categor","chartList.size = "+chartList.size());
                setChart(chartList);

        } else {
            chartNoDat();
        }
    }

    private void chartNoDat(){
            mChart.invalidate();
            mChart2.invalidate();
            mChart.setNoDataText(getString(R.string.not_data));
            chartList.clear();
            ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
            entries.add(new PieEntry(1));
            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setValueLineVariableLength(false);
            dataSet.setDrawValues(false);
            dataSet.setColors(Color.GRAY);
            PieData data = new PieData(dataSet);
            data.setValueTextSize(0f);
            mChart.setData(data);
            chartList.clear();
            chartList.add(new Chart("",100,0,1,"",null));
            setChart(chartList);
    }

    private void setChart(List<Chart> chartList){//List<Float> doubleList, List<String> stringList
        mChart.getDescription().setEnabled(false);
        mChart.setDrawEntryLabels(false);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.TRANSPARENT);

        mChart2.getDescription().setEnabled(false);
        mChart2.setDrawEntryLabels(false);
        mChart2.setUsePercentValues(false);
        mChart2.setDrawHoleEnabled(true);
        mChart2.setTransparentCircleColor(Color.WHITE);
        mChart2.setTransparentCircleAlpha(110);
        //mChart2.setHoleColor(Color.TRANSPARENT);
        mChart2.setHoleRadius(97f);
        mChart2.setTransparentCircleRadius(0f);

        mChart2.setRotationAngle(0);
        mChart2.setRotationEnabled(true);
        mChart2.setHighlightPerTapEnabled(true);
        mChart2.invalidate();
        mChart2.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        mChart2.getLegend ().setEnabled(false);
        mChart2.setCenterTextSize(15);
        mChart2.setScrollBarSize(20);
        mChart2.setEntryLabelTextSize(15);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(61f);
        mChart.setTransparentCircleRadius(0f);

        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        List<Float> doubleListNew = new LinkedList<>();
        List<String> stringListNew = new LinkedList<>();
        List<Integer> idListNew = new LinkedList<>();
        List<Float> percentListNew = new LinkedList<>();
        //List<String> nameList = new ArrayList<>();
        //nameList.addAll(returnName(stringList));
        itog=0;
        if (chartListPP.size()>0) {
            for (int f = 0; f < chartListPP.size(); f++) {
                if (chartListPP.get(f).viewId == 1) {
                    doubleListNew.add(chartListPP.get(f).amount);
                    itog += chartListPP.get(f).amount;
                    stringListNew.add(chartListPP.get(f).name);
                    idListNew.add(chartListPP.get(f).category.ID);
                    percentListNew.add(chartListPP.get(f).percent);
                }
            }
        }else{
            itog=0;
        }

        setData(doubleListNew, stringListNew, idListNew, percentListNew);

        //for (IDataSet<?> set : mChart.getData().getDataSets())
        //    set.setDrawValues(!set.isDrawValuesEnabled());

        mChart.invalidate();

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        mChart.getLegend ().setEnabled(false);
        mChart.setCenterTextSize(20);
        mChart.setScrollBarSize(20);
        mChart.setEntryLabelTextSize(20);
    }

    private void setIconEntry(int i, int ID){
        //try {
            Log.i("setIconEntry","i = "+i+"   ID = "+ID);
            switch (ID) {
                case 1:
                    entries.get(i).setIcon(getResources().getDrawable(R.drawable.ic_image_white_category_1));
                    break;
                case 2:
                    entries.get(i).setIcon(getResources().getDrawable(R.drawable.ic_image_white_category_2));
                    break;
                case 3:
                    entries.get(i).setIcon(getResources().getDrawable(R.drawable.ic_image_white_category_3));
                    break;
                case 4:
                case 26:
                    entries.get(i).setIcon(getResources().getDrawable(R.drawable.ic_image_white_category_4));
                    break;
                case 5:
                    entries.get(i).setIcon(getResources().getDrawable(R.drawable.ic_image_white_category_5));
                    break;
                case 6:
                    entries.get(i).setIcon(getResources().getDrawable(R.drawable.ic_image_white_category_6));
                    break;
                case 7:
                    entries.get(i).setIcon(getResources().getDrawable(R.drawable.ic_image_white_category_7));
                    break;
                case 8:
                    entries.get(i).setIcon(getResources().getDrawable(R.drawable.ic_image_white_category_8));
                    break;
                case 9:
                    entries.get(i).setIcon(getResources().getDrawable(R.drawable.ic_image_white_category_9));
                    break;
                case 10:
                    entries.get(i).setIcon(getResources().getDrawable(R.drawable.ic_card));
                    break;
                case 11:
                    entries.get(i).setIcon(getResources().getDrawable(R.drawable.ic_image_white_category_11));
                    break;
                case 12:
                    entries.get(i).setIcon(getResources().getDrawable(R.drawable.ic_image_white_category_12));
                    break;
                case 13:
                    entries.get(i).setIcon(getResources().getDrawable(R.drawable.ic_image_white_category_13));
                    break;
                case 14:
                    entries.get(i).setIcon(getResources().getDrawable(R.drawable.ic_image_white_category_14));
                    break;
                case 15:
                    entries.get(i).setIcon(getResources().getDrawable(R.drawable.ic_image_white_category_15));
                    break;
                case 16:
                    entries.get(i).setIcon(getResources().getDrawable(R.drawable.ic_image_white_category_16));
                    break;
                case 17:
                    entries.get(i).setIcon(getResources().getDrawable(R.drawable.ic_image_white_category_17));
                    break;
                case 22:
                    entries.get(i).setIcon(getResources().getDrawable(R.drawable.ic_image_white_category_22));
                    break;

                default:
                    entries.get(i).setIcon(getResources().getDrawable(R.drawable.ic_stars2));
                    break;
            }
        //}catch (Exception ignored){}
    }

    private void setData(List<Float> doubleList, List<String> stringList, List<Integer> idListNew, List<Float> percentListNew) {
        entries.clear();

        ArrayList<PieEntry> entries2 = new ArrayList<PieEntry>();
        entries2.add(new PieEntry(1,"1"));

        for (int i = 0; i < doubleList.size() ; i++) {
            if (percentListNew.get(i)>1) {
                //try {
                    colorsAdapItog.add(colorsAdap.get(i));
                //}catch (Exception ignored){}
                entries.add(new PieEntry(doubleList.get(i)));
                if (percentListNew.get(i)>10)
                    setIconEntry(entries.size()-1,idListNew.get(i));
            }
        }
        //entries.get(0).setIcon(getResources().getDrawable(R.drawable.arrow_left_chart));
        //entries.get(0).setLabel("1");
        Log.i("entries","entries.size = "+ entries.size());
        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(true);
        dataSet.setValueLineVariableLength(true);
        dataSet.setDrawValues(true);
        dataSet.setSliceSpace(2f);

        PieDataSet dataSet2 = new PieDataSet(entries2, "");

        dataSet2.setDrawIcons(false);
        dataSet2.setValueLineVariableLength(false);
        dataSet2.setDrawValues(false);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        //colors.add(ColorTemplate.getHoloBlue());
        //ArrayList<Integer> colorsAdap = new ArrayList<Integer>();
        //for (int i = 0; i<doubleList.size(); i++){
        //    Log.i("COLOR","COLOR "+i);
        //    colorsAdap.add(colors.get(i));
        //}

            Log.i("COLORS", "colorsAdap = "+ colorsAdap);
            Log.i("COLORS", "colorsAdapFC = "+ colorsAdapFC);

        //if (colorsAdap.size()>0)
        //    dataSet.setColors(colorsAdap);
        if (colorsAdapItog.size()>0)
            dataSet.setColors(colorsAdapItog);
        else
            dataSet.setColors(Color.rgb(223,221,224));
        dataSet2.setColor(Color.rgb(244,122,53));

        chartList.add(new Chart(3));
        Log.i("IDaddColor","colorsAdapFC.size setAdapter = "+colorsAdapFC.size());
        setAdapter(chartList,colorsAdapFC);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(0f);
        data.setDrawValues(true);
        mChart.setData(data);
        mChart.getData().setDrawValues(true);
        mChart.setDrawEntryLabels(false);

        mChart.highlightValues(null);

        PieData data2 = new PieData(dataSet2);
        mChart2.setData(data2);
        mChart2.getData().setDrawValues(false);
        mChart2.setDrawEntryLabels(false);
        mChart2.highlightValues(null);
        mChart2.invalidate();

        mChart.invalidate();

        //mChart2.setCenterTextSize(10f);
        if (itog==0){
            mChart2.setCenterText(getString(R.string.costs) + "\n" + itog + " KGS");
            mChart2.setCenterTextColor(Color.rgb(89, 89, 89));
        }else {
            mChart2.setCenterText(getString(R.string.costs) + "\n" + Utilities.AmountFormatter(String.valueOf(itog)) + " KGS");
            mChart2.setCenterTextColor(Color.rgb(89, 89, 89));
        }

        mChart.setTransparentCircleRadius(0f);
        //mChart.setTransparentCircleColor(getResources().getColor(R.color.orange_atf));
        //mChart.setTransparentCircleAlpha(200);
        //mChart.setDrawSecondTransparentCircle(true);
        //mChart.setCenterText(getString(R.string.purchases)+" "+itog+" KZT");
    }

    private void initToolbar() {
        ///setSupportActionBar(toolbar);
        toolbar.setTitle(null);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    */
/*public static List<String> returnName  (List<String> stringList){
        List<String> NameList = new LinkedList<>();
        for (String string : stringList){
            for (Category category : categories){
                if (string.equals(String.valueOf(Double.valueOf(category.ID)))){
                    NameList.add(category.getName());
                }
            }
        }
        return NameList;
    }*//*


   private static void addColor(int a,int b){
       if (b==0) {
           Utilities.setColorsForChart(a,colorsAdapFC);
           Log.i("IDaddColor","ID = "+a);
           */
/*switch (a) {
               case 1:
                   colorsAdapFC.add(Color.rgb(171, 203, 226));
                   //colorsAdap.add(Color.rgb(153,50,204));
                   break;
               case 2:
                   colorsAdapFC.add(Color.rgb(213, 190, 219));//214,229,76
                   //colorsAdap.add(Color.rgb(240,128,128));
                   break;
               case 3:
                   colorsAdapFC.add(Color.rgb(204, 162, 138));
                   //colorsAdap.add(Color.rgb(103,58,183));
                   break;
               case 4:
               case 26:
                   colorsAdapFC.add(Color.rgb(184, 245, 143));
                   //colorsAdap.add(Color.rgb(246,76,97));
                   break;
               case 5:
                   colorsAdapFC.add(Color.rgb(244, 134, 165));
                   //colorsAdap.add(Color.rgb(127,255,0));
                   break;
               case 6:
                   colorsAdapFC.add(Color.rgb(204, 168, 240));
                   //colorsAdap.add(Color.rgb(255,105,180));
                   break;
               case 7:
                   colorsAdapFC.add(Color.rgb(243, 168, 124));
                   //colorsAdap.add(Color.rgb(21,179,209));
                   break;
               case 8:
                   colorsAdapFC.add(Color.rgb(217, 155, 233));
                   //colorsAdap.add(Color.rgb(0,181,255));
                   break;
               case 9:
                   colorsAdapFC.add(Color.rgb(217, 155, 233));
                   //colorsAdap.add(Color.rgb(0,139,139));
                   break;
               case 10:
                   colorsAdapFC.add(Color.rgb(168, 240, 239));
                   //colorsAdap.add(Color.rgb(0,255,127));
                   break;
               case 11:
                   colorsAdapFC.add(Color.rgb(180, 216, 202));
                   //colorsAdap.add(Color.rgb(41,231,242));
                   break;
               case 12:
                   colorsAdapFC.add(Color.rgb(207, 147, 219));
                   //colorsAdap.add(Color.rgb(255,74,172));
                   break;
               case 14:
                   colorsAdapFC.add(Color.rgb(224, 199, 184));
                   //colorsAdap.add(Color.rgb(160,82,45));
                   break;
               case 15:
                   colorsAdapFC.add(Color.rgb(168, 240, 194));
                   //colorsAdap.add(Color.rgb(0,255,255));
                   break;
               case 16:
                   colorsAdapFC.add(Color.rgb(240, 209, 168));
                   //colorsAdap.add(Color.rgb(17,74,119));
                   break;
               case 17:
                   colorsAdapFC.add(Color.rgb(201, 206, 197));
                   //colorsAdap.add(Color.rgb(42,223,133));
                   break;
               case 22:
                   colorsAdapFC.add(Color.rgb(87, 93, 161));
                   //colorsAdap.add(Color.rgb(240,128,128));
                   break;
               default:
                   colorsAdapFC.add(Color.rgb(253, 175, 155));
                   break;
           }*//*

       }
       if (b!=0)
           Utilities.setColorsForChart(a,colorsAdap);
       */
/*switch (a){
           case 1:
               colorsAdap.add(Color.rgb(171, 203, 226));
               //colorsAdap.add(Color.rgb(153,50,204));
               break;
           case 2:
               colorsAdap.add(Color.rgb(213, 190, 219));//214,229,76
               //colorsAdap.add(Color.rgb(240,128,128));
               break;
           case 3:
               colorsAdap.add(Color.rgb(204, 162, 138));
               //colorsAdap.add(Color.rgb(103,58,183));
               break;
           case 4:
           case 26:
               colorsAdap.add(Color.rgb(184, 245, 143));
               //colorsAdap.add(Color.rgb(246,76,97));
               break;
           case 5:
               colorsAdap.add(Color.rgb(244, 134, 165));
               //colorsAdap.add(Color.rgb(127,255,0));
               break;
           case 6:
               colorsAdap.add(Color.rgb(204, 168, 240));
               //colorsAdap.add(Color.rgb(255,105,180));
               break;
           case 7:
               colorsAdap.add(Color.rgb(243, 168, 124));
               //colorsAdap.add(Color.rgb(21,179,209));
               break;
           case 8:
               colorsAdap.add(Color.rgb(217, 155, 233));
               //colorsAdap.add(Color.rgb(0,181,255));
               break;
           case 9:
               colorsAdap.add(Color.rgb(217, 155, 233));
               //colorsAdap.add(Color.rgb(0,139,139));
               break;
           case 10:
               colorsAdap.add(Color.rgb(168, 240, 239));
               //colorsAdap.add(Color.rgb(0,255,127));
               break;
           case 11:
               colorsAdap.add(Color.rgb(180, 216, 202));
               //colorsAdap.add(Color.rgb(41,231,242));
               break;
           case 12:
               colorsAdap.add(Color.rgb(207, 147, 219));
               //colorsAdap.add(Color.rgb(255,74,172));
               break;
           case 13:
               colorsAdap.add(Color.rgb(177, 191, 200));
               //did'nt have default color
               break;
           case 14:
               colorsAdap.add(Color.rgb(224, 199, 184));
               //colorsAdap.add(Color.rgb(160,82,45));
               break;
           case 15:
               colorsAdap.add(Color.rgb(168, 240, 194));
               //colorsAdap.add(Color.rgb(0,255,255));
               break;
           case 16:
               colorsAdap.add(Color.rgb(240, 209, 168));
               //colorsAdap.add(Color.rgb(17,74,119));
               break;
           case 17:
               colorsAdap.add(Color.rgb(201, 206, 197));
               //colorsAdap.add(Color.rgb(42,223,133));
               break;
           case 22:
               colorsAdap.add(Color.rgb(87, 93, 161));
               //colorsAdap.add(Color.rgb(240,128,128));
               break;
           default:
               colorsAdap.add(Color.rgb(253, 175, 155));
               break;
       }*//*

   }

    public static ArrayList<Chart> returnItogList  (List<String> stringList, List<Float> doubleList, List<String> currencyList){

        float allPerc = 0;
        for (float f : doubleList){
            allPerc += Math.abs(f);
        }
        allPerc = allPerc/100;

        ArrayList<Chart> chartList = new ArrayList<>();
        //chartList.add(new Chart((Constants.HEADER_SPACING)));
        int i = 0;
        int l = 0;

        colorsAdap.clear();
        colorsAdapFC.clear();

        */
/*for (String string : stringList) {
                for (Category category : categories) {
                    //Log.i("categor","category.ID = "+category.ID+";;;;category.ParentId = "+category.ParentId);
                    if (string.equals(String.valueOf(Double.valueOf(category.ID)))) {
                        if (category.ParentId==0) {

                            Log.i("statements","category.Name = "+category.Name);
                            try {
                                Log.i("statements", "category.name = " + category.name);
                            }catch (Exception ignore){}
                            Log.i("statements","category.code = "+category.code);
                            Log.i("statements","category.ID = "+category.ID);
                            try{
                                chartList.add(new Chart(category.getName(), Math.abs(doubleList.get(i)) / allPerc,
                                        Math.abs(doubleList.get(i)), 1, currencyList.get(i), category.ID));
                            }catch (Exception ignore){}
                            Log.i("RODCAT", "category.ID SC = "+category.ID);
                            addColor(category.ID,0);
                            addColor(category.ID,1);

                            for (String stringInside : stringList) {
                                for (Category categoryInside : categories) {
                                    if (stringInside.equals(String.valueOf(Double.valueOf(categoryInside.ID))) && categoryInside.ParentId == category.ID) {
                                        try{
                                            chartList.add(new Chart(categoryInside.getName(), Math.abs(doubleList.get(l)) / allPerc,
                                                    Math.abs(doubleList.get(l)), 2, currencyList.get(l), category.ID));
                                        }catch (Exception ignore){}
                                        addColor(categoryInside.ParentId,0);
                                        Log.i("RODCAT", "categoryInside.ParentId SC= "+categoryInside.ParentId);
                                    }
                                }
                                l++;
                            }
                            l=0;
                        }*//*
*/
/*else{
                            for (String str : stringList){
                                if (!str.equals(category.ParentId)){
                                    stringList.add(String.valueOf(category.ParentId));
                                    doubleList.add(doubleList.get(i));

                                }
                            }
                        }*//*
*/
/*
                    }

                }
            //}
            i++;
        }*//*



        chartListP.clear();
        chartListPP.clear();
        chartListD.clear();
        chartListITC.clear();

        i = 0;
        l = 0;
        for (String string : stringList) {
            if (categories != null)
                for (Category category : categories) {
                    if (string.equals(String.valueOf(Double.valueOf(category.ID)))) {
                        if (category.ParentId == 0) {
                            try {
                                chartListP.add(new Chart(category.getName(), Math.abs(doubleList.get(i)) / allPerc,
                                        Math.abs(doubleList.get(i)), 1, currencyList.get(i), category));

                                //addColor(category.ID,0);
                                //addColor(category.ID,1);

                            } catch (Exception ignore) {}
                        }else{
                            try {
                                chartListD.add(new Chart(category.getName(), Math.abs(doubleList.get(i)) / allPerc,
                                        Math.abs(doubleList.get(i)), 2, currencyList.get(i), category));

                                //addColor(category.ID,0);

                            }catch (Exception ignore){}
                        }
                    }


                }
            i++;
        }

        Log.i("CHART","doubleList.size = "+doubleList.size());
        Log.i("CHART","stringList.size = "+stringList.size());
        Log.i("CHART","chartListD.size = "+chartListD.size());
        Log.i("CHART","chartListP.size = "+chartListP.size());

        boolean isHave = false;
        Category categor = null;
        for (Chart chart:chartListD){
            for (Chart chartIn:chartListP){
                if(chart.category.ParentId==chartIn.category.ID  && !isHave){
                    isHave=true;
                }
            }
            if (!isHave)
                for (Category cat : categories){
                    if (chart.category.ParentId==cat.ID){
                        categor=cat;
                    }
                }
            if (!isHave){
                if (categor!=null)
                    chartListP.add(new Chart(categor.getName(), 0,
                            0, 1, "", categor));
                //addColor(categor.ID,0);
                //addColor(categor.ID,1);
            }
            categor = null;
            isHave=false;
        }

        Log.i("CHART","-----------------------------------");
        Log.i("CHART","doubleList.size = "+doubleList.size());
        Log.i("CHART","stringList.size = "+stringList.size());
        Log.i("CHART","chartListD.size = "+chartListD.size());
        Log.i("CHART","chartListP.size = "+chartListP.size());

        ArrayList<Chart> chartListIT = new ArrayList<>();
        //chartListIT.add(new Chart((Constants.HEADER_SPACING)));

        float allAmoun;
        float allPercent;

        for(Chart chart:chartListP){
            allAmoun = 0;
            allPercent = 0;
            allAmoun+=chart.amount;
            allPercent+=chart.percent;
            for (Chart chartD:chartListD){
                if (chart.category.ID==chartD.category.ParentId){
                    allAmoun+=chartD.amount;
                    allPercent+=chartD.percent;
                }
            }
            chartListPP.add(new Chart(chart.name,allPercent,allAmoun,chart.viewId,"",chart.IDcategory,chart.category));
        }

        Log.i("CHART","-----------------------------------");
        Log.i("CHART","chartListP.size = "+chartListP.size());
        Log.i("CHART","chartListPP.size = "+chartListPP.size());

        for(Chart chart:chartListPP){
            chartListIT.add(chart);
            for (Chart chartD:chartListD){
                if (chart.category.ID==chartD.category.ParentId){
                    chartListIT.add(chartD);
                }
            }
        }
        colorsAdapFC.clear();
        colorsAdap.clear();
        for(Chart chart:chartListP){
            chartListITC.add(chart);
            addColor(chart.category.ID,1);
            addColor(chart.category.ID,0);
            for (Chart chartD:chartListD){
                if (chart.category.ID==chartD.category.ParentId){
                    chartListITC.add(chartD);
                    addColor(chartD.category.ParentId,0);
                }
            }
        }

        Log.i("CHART","-----------------------------------");
        Log.i("CHART","chartListIT.size = "+chartListIT.size());
        Log.i("IDaddColor","colorsAdapFC.size = "+colorsAdapFC.size());
        return chartListIT;//chartList
    }

    private void setApplicationLanguage() {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(OptimaBank.getContext());
        String languageToLoad = preference.getString(Constants.APP_LANGUAGE, "ru");
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    public void jsonAccountsResponse(int statusCode, String errorMessage) {

    }

    @Override
    public void jsonAccountsOperationsResponse(int statusCode, String errorMessage) {
        if(statusCode==0) {
            initChart();
        } else {
            chartNoDat();
        }
    }

    @Override
    public void jsonCategoriesResponse(int statusCode, String errorMessage) {
        if (statusCode==0) {
            categories = new ArrayList<>();
            categories.addAll(GeneralManager.getInstance().getCategories());
        }
    }

    public void setAdapter(ArrayList<Chart> list,  ArrayList<Integer> colors) {
        if (!list.isEmpty()&&list!=null){
            ChartAdapter chartAdapter = new ChartAdapter(StatmentChart.this,list,colors);
            recyclerView.setAdapter(chartAdapter);
        }
    }
}
*/
