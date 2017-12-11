package com.example.kimdoyeon.daemun.Daemun_main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.kimdoyeon.daemun.Daemun_DB.Events;
import com.example.kimdoyeon.daemun.Daemun_DB.User;
import com.example.kimdoyeon.daemun.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import static android.view.View.GONE;


public class BoardActivity extends AppCompatActivity {

    // 카드뷰
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Events> myDataset;

    // DB에 저장시킬 데이터를 입력받는 EditText
    private EditText editText;

    // 입력받은 데이터를 저장시킬 버튼
    private LinearLayout edittextLayout;
    private Button inputBtn;

    // DB 데이터를 보여줄 ListView
    //private ListView listView; // 메시지 리스트
    //private ListView listView2; // 객체 리스트

   // private ArrayAdapter<String> msgdataAdapter; // 메시지 리스트어댑터
   // private ArrayAdapter<Objects> objectsDataAdapter; // 객체 리스트어댑터

    // 어레이리스트 선언
    private ArrayList<Events> events_arraylist; // 행사들 객체를 저장할 어레이리스트


    // DB 관련 변수
    private FirebaseDatabase database;
    private DatabaseReference myRef; // 메시지 레퍼런스

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        // 변수 초기화.

        editText = (EditText) findViewById(R.id.editText);
        inputBtn = (Button) findViewById(R.id.inputBtn);
        edittextLayout = (LinearLayout) findViewById(R.id.EditTextLayout);
        edittextLayout.setVisibility(GONE); // 텍스트 입력 레이아웃 안보이게.

       // listView = (ListView) findViewById(R.id.listView); // 메시지 리스트

        // 카드뷰---------------------------------------------------
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        myDataset = new ArrayList<>();
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
        // 카드뷰 끝 ------------------------------------------------

        // DB 관련 변수 초기화
        database = FirebaseDatabase.getInstance();

        // message Reference가 없어도 상관 x
        myRef = database.getReference();

        // ListView에 출력할 데이터 초기화
        //msgdataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
       // objectsDataAdapter = new ArrayAdapter<User>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<Objects>());

        // ListView에 Adapter 붙여줌
        //listView.setAdapter(msgdataAdapter);

        // 자신이 얻은 Reference에 이벤트를 붙여줌
        // 데이터의 변화가 있을 때 출력해옴


        myRef.child("Object").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 데이터를 읽어올 때 모든 데이터를 읽어오기때문에 List 를 초기화해주는 작업이 필요하다.
               // msgdataAdapter.clear();
                events_arraylist = new ArrayList<Events>();
                myDataset = new ArrayList<>();

                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    final Events events = messageData.getValue(Events.class);
                    Log.e("TAG", "key="+events.getEvent_key()+" name=" + events.getEvent_name() + ", date=" + events.getEvent_date() + ", place=" + events.getEvent_place()+ ", img=" + events.getEvent_img());
                    String msg = "key="+events.getEvent_key()+" name=" + events.getEvent_name() + ", date=" + events.getEvent_date() + ", place=" + events.getEvent_place()+ ", img=" + events.getEvent_img();

                    Events add_events = new Events(events.getEvent_key(), events.getEvent_name(), events.getEvent_date(), events.getEvent_place(), events.getEvent_img());
                    events_arraylist.add(add_events);
                    myDataset.add(add_events);
                   // msgdataAdapter.add(msg);
                }

                mAdapter = new MyAdapter(myDataset);
                mRecyclerView.setAdapter(mAdapter);

                // notifyDataSetChanged를 안해주면 ListView 갱신이 안됨
               // msgdataAdapter.notifyDataSetChanged();
                // ListView 의 위치를 마지막으로 보내주기 위함
               // listView.setSelection(msgdataAdapter.getCount() - 1);

                for(int i=0 ; i<events_arraylist.size();i++){
                    Log.e("어레이리스트에 있는 객체 정보", "key="+events_arraylist.get(i).getEvent_key()+" name=" +
                            events_arraylist.get(i).getEvent_name() + ", date=" + events_arraylist.get(i).getEvent_date() + ", place=" + events_arraylist.get(i).getEvent_place());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


       /* objectRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 데이터를 읽어올 때 모든 데이터를 읽어오기때문에 List 를 초기화해주는 작업이 필요하다.
                objectsDataAdapter.clear();
                for (DataSnapshot objectsData : dataSnapshot.getChildren()) {
                    String msg = objectsData.getValue().toString();
                    User us = new User();
                    objectsDataAdapter.add(us);
                }
                // notifyDataSetChanged를 안해주면 ListView 갱신이 안됨
                msgdataAdapter.notifyDataSetChanged();
                // ListView 의 위치를 마지막으로 보내주기 위함
                listView.setSelection(msgdataAdapter.getCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


        // 버튼 리스너 정의
        // 클릭시 EditText의 내용이 DB에 저장
        inputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editText.getText().toString().trim();
                // push는 firebase가 임의로 중복되지 않은 키를 생성해서 저장
                // push로 하지 않을 경우 덮어 씌움
                myRef.push().setValue(str);

                // EditText 초기화
                editText.setText("");
            }
        });

    }

}
