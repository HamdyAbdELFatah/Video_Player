package com.example.kira.videoplayer2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListVideoes extends AppCompatActivity {
    ArrayList<iteams> mylist;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_videoes);
       mylist=new ArrayList<iteams>();
       RecyclerView r=findViewById(R.id.rec);
       GedtVideos();
        r.setLayoutManager(new LinearLayoutManager(this));
       r.setAdapter(new Myadaptar(mylist, this));


    }

    void GedtVideos(){

          Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
          String selection = MediaStore.Video.Media.DATA + " !=0";
          Cursor cursor = managedQuery(uri, null, selection, null, null);
          if (cursor != null) {
             if (cursor.moveToFirst()) {
                  while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                     String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                     String resalution = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION));
                     String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                     int x = Integer.parseInt(duration) / 1000 / 60;
                     int y = Integer.parseInt(duration) / 1000 % 60;
                      mylist.add(new iteams(resalution, name, path, String.format("%02d", x) + ":" + String.format("%02d", y)));
                  }
            }
          }

}
    class Myadaptar extends RecyclerView.Adapter<Myadaptar.myhold>{
        public Myadaptar(ArrayList<iteams> arr, Context context) {
            this.arr = arr;
            this.context = context;
        }

        ArrayList<iteams> arr;
        Context context;
        @NonNull
        @Override
        public myhold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new myhold(LayoutInflater.from(context).inflate(R.layout.add,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull final myhold holder, final int position) {
holder.myname.setText(arr.get(position).namevideo);
            holder.resulution.setText(arr.get(position).resolution);
            holder.duration.setText(arr.get(position).duration);
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(context,MainActivity.class);
                    intent.putExtra("pass",arr.get(position).path);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arr.size();
        }

        class myhold extends RecyclerView.ViewHolder{
            TextView myname;
            TextView resulution;
            TextView duration;
           LinearLayout linearLayout;
            public myhold(View itemView) {
                super(itemView);
                myname=itemView.findViewById(R.id.name);
                resulution=itemView.findViewById(R.id.resolution);
                duration=itemView.findViewById(R.id.duration);
                linearLayout=itemView.findViewById(R.id.videoClick);
            }
        }

    }
}
