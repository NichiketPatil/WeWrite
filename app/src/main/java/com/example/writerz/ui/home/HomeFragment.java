package com.example.writerz.ui.home;

import android.location.Location;
import android.nfc.cardemulation.CardEmulation;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.writerz.CustomListAdapter;
import com.example.writerz.LocationActivity;
import com.example.writerz.Model.Writer;
import com.example.writerz.R;
import com.example.writerz.card;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference reference;
    DatabaseReference myReference;
    FirebaseUser fuser;
    Location location;
    List<Location> locationList;
    List<Location> newLocationList;
    Double myLatitude = 0.0;
    Double myLongitude = 0.0;
    List<Writer> writerList;
    ArrayList<card> list;
    Location myLocation;
    CustomListAdapter adapter;


    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView =  root.findViewById(R.id.recycler_view);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        locationList = new ArrayList<>();
        newLocationList = new ArrayList<>();
        writerList = new ArrayList<>();
        if (!fuser.isAnonymous()) {
            myReference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getPhoneNumber());
            myReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Writer writer = dataSnapshot.getValue(Writer.class);
                    myLatitude = Double.parseDouble(writer.getLat());
                    myLongitude = Double.parseDouble(writer.getLon());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        list = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    location = new Location("providerNA");
                    Writer writer = snapshot.getValue(Writer.class);
                    assert writer!=null;
                    assert  fuser!=null;
                    if (writer.getUt().equals("w")){
                        location.setLatitude(Double.parseDouble(writer.getLat()));
                        location.setLongitude(Double.parseDouble(writer.getLon()));
                        locationList.add(location);
                        writerList.add(writer);}
                }
                myLocation = new Location("providerNA");
                myLocation.setLongitude(myLongitude);
                myLocation.setLatitude(myLatitude);
                newLocationList = sortLocations(locationList,myLocation);

                for (int j = 0; j < writerList.size(); j++) {
                    for (int i = 0; i < newLocationList.size(); i++) {
                        if (newLocationList.get(j) == locationList.get(i)){
                            list.add(new card(writerList.get(i).getH1URL(), writerList.get(i).getLoc(getContext()), writerList.get(i).getpURL()));
                            break;
                        }
                    }
                }
                if (getContext()!=null){
                    adapter = new CustomListAdapter(getContext(), R.layout.card_layout, list);
                    LinearLayoutManager llm = new LinearLayoutManager(getContext());
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(llm);
                    recyclerView.setAdapter(adapter);}


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        return root;
    }


    private   List<Location> sortLocations(List<Location> locations, final Location myLocation) {
        if (!fuser.isAnonymous()) {

            Comparator comp = new Comparator<Location>() {
                @Override
                public int compare(Location o1, Location o2) {
//                    float[] result1 = new float[3];
//                    android.location.Location.distanceBetween(myLatitude, myLongitude, o.getLatitude(), o.getLongitude(), result1);
//                    Float distance1 = result1[0];
//
//                    float[] result2 = new float[3];
//                    android.location.Location.distanceBetween(myLatitude, myLongitude, o2.getLatitude(), o2.getLongitude(), result2);
//                    Float distance2 = result2[0];

                    Float dist1 = o1.distanceTo(myLocation);
                    Float dist2 = o2.distanceTo(myLocation);

                    return dist1.compareTo(dist2);
                }
            };

            Collections.sort(locations, comp);
        }
        return locations;
    }


}