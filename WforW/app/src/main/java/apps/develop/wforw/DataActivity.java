package apps.develop.wforw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import java.util.ArrayList;

import apps.develop.wforw.databinding.ActivityDataBinding;

public class DataActivity extends AppCompatActivity {

    ActivityDataBinding binding;

    ArrayList<DataModel> list = new ArrayList<>();
    DataAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerView.setHasFixedSize(true);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        LinearLayoutManager manager = new LinearLayoutManager(DataActivity.this);
        binding.recyclerView.setLayoutManager(manager);

        adapter = new DataAdapter(DataActivity.this,list);
        binding.recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        setData();

        binding.inputCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0){
                    binding.imgClear.setVisibility(View.VISIBLE);
                    filter(s.toString().toLowerCase());

                }else {
                    binding.imgClear.setVisibility(View.GONE);
                    setData();
                }




            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.imgClear.setOnClickListener(v -> {
            binding.inputCity.setText("");

        });


    }
    private void setData(){
        list.add(new DataModel("Seven Handloomers","Door No: 1-104a,Kothapet,Mangalagiri","sles@glcottons.com"
        ,"7036777767","₹200 to ₹6000",16.436555042000407,80.56802109961728));

        list.add(new DataModel("Gayathri Kalamkari","Dugullapally,Machilipatnam","mgayathriworks333@gmail.com"
                ,"08048372357","₹200 to ₹5000",16.17725564375055,81.03358566417329));

        list.add(new DataModel("Venkatagiri Handloomer Cloths","Dugullapally,Machilipatnam",
                "venkatagiriworks@gmail.com"
                ,"08068970147","₹100 to ₹9000",16.17725564375055,81.03358566417329));


        list.add(new DataModel("Omkar Handloom Factory","Keshava Nagar,Siva Nagar,Dharmavaram","N/A"
                ,"97004 00400","₹400 to ₹3000",14.419805693609588,77.72727224764739));

        list.add(new DataModel("Pochampally Ikkath Cloths","Markondaih temple,Pochamplly,Kakinada"
                ,"sarode1@gmail.com"
                ,"9441905005","₹200 to ₹5000",17.349943563974602, 78.82047502838275));

        list.add(new DataModel("Chenetha Vastralayam","Sangareddy sales emphorium,Muncipal Complex,Mehboob nagar","chenatha@gmail.com"
                ,"9133496726","₹200 to ₹5000",17.62105640992, 77.99394536647873));

        list.add(new DataModel("Narasimha Stores","Mehaboobnagar Handlooms House Muncipal Complex,Mehaboobnagar"
                ,"narasimha@gmail.com"
                ,"9493602706","₹200 to ₹5000",16.74003810400083, 77.98399880273905));

        list.add(new DataModel("Texttile Platofrm","Karimnagar Handloom House,Dhanvantri Complex,Karimnagar","texttileplot@gmail.com"
                ,"9948502679","₹200 to ₹5000",18.433406738328294, 79.13376069290541));

        list.add(new DataModel("Khammam Sales Emphorium","Station road,Khammam","emphorium@gmail.com"
                ,"9030448969","₹200 to ₹5000",17.248944490250693, 80.13800179530843));

        list.add(new DataModel("Waver's service centre","Gorkhabasti (Opp: Tripura Housing Board)," +
                "P.O: Kthal Bagan, Agarthala","wscagt@gmail.com"
                ,"3812325255","₹200 to ₹5000",23.85482653532552, 91.28248945493662));

        list.add(new DataModel("Waver's service centre","Bunkara Bhavan, Plot No: A/407,Shahid Nagar,Maharshi Collehge" +
                " Raod,Bhubaneswar-751007","bbsrwsc@yahoo.com"
                ,"6742549859","₹200 to ₹5000",20.28627257645199, 85.84969359720145));


        list.add(new DataModel("Waver's service centre","15 - A, Mama Paramand Marg, Opera House, Mumbai - 400004.","dirwzwscmum@@gmail.com"
                ,"2223610923","₹200 to ₹5000",18.955516386627625, 72.8166552278605));


     ///
        list.add(new DataModel("Waver's service centre","Wavers Service Centre, F-4, Industrial Estates, Polo Ground, Indore -452015.","wscindore@hotmailgmail.in"
                ,"7312420545","₹200 to ₹5000",22.734798247068913, 75.85430289724495));

        list.add(new DataModel("Devanhalli weaver sub centre","Devanhalli","Devanhalli12K@gmail.com"
                ,"7321891929","₹300 to ₹5000",13.24115315400281, 77.71499055069498));

        list.add(new DataModel("Mandapa Waevers association","Weavers' Service centre, No.2&4, IInd Main Road,Okalipuram," +
                "Beside R.R.R Kalyna Mandapa,Banglore-560021","dirwzwscmum@@gmail.in"
                ,"8023121662","₹200 to ₹5000",12.982575259143108, 77.5649007795059));

        list.add(new DataModel("Atchuthananda textile and handloom industry","Nelamangala","weaver376@@gmail.in"
                ,"9743444211","₹200 to ₹5000",13.097300602374803, 77.38538226391553));


        list.add(new DataModel("Sri K.Nagaraju Vivekaananda Abhyudaya hwcs","Devangapuri-523165,Chrala mandal,Prakasam District"
                ,"nagaraju@@gmail.in"
                ,"9398265856","₹500 to ₹5000",15.807999388215233, 80.3339557265443));

        list.add(new DataModel("Weaver Sub Centre","Doddabalupura","waevresubcentre@@gmail.in"
                ,"9643127654","₹700 to ₹5000",13.29180551333794, 77.52932835924291));

        list.add(new DataModel("Weaver Sub Centre","Hoskote","Hoskoteweav@@gmail.in"
                ,"8324567123","₹100 to ₹5000",13.070051232840557, 77.79024151140176));

        list.add(new DataModel("Weaver's Service Centre","Madana Complex,1st and 2nd floormsouth bazaar,Kannur -670002","wsckannur@@gmail.in"
                ,"4972761937","₹200 to ₹5000",11.874444318670996, 75.36650633792897));



        list.add(new DataModel("Nagendra Kanaka Durga HWCS","Venkatagiri town,Nellore District-524132","waevresubcentre@@gmail.in"
                ,"9398265856","₹700 to ₹5000",13.954213627317301, 79.57677645932496));






    }
    void filter(String text){
        ArrayList<DataModel> temp = new ArrayList();
        for(DataModel d: list){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getName().contains(text)){
                temp.add(d);
            }
        }
        //update recyclerview
        adapter = new DataAdapter(DataActivity.this,temp);
        binding.recyclerView.setAdapter(adapter);
    }
}